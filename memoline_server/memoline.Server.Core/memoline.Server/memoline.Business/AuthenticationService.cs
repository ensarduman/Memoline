using ensarduman.Utils;
using memoline.Model;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using memoline.DTO;
using System.Net.Http;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Net;
using System.Security.Cryptography.X509Certificates;
using System.Net.Security;

namespace memoline.Business
{
    public class AuthenticationService:BusinessBase
    {
        public AuthenticationService(string connectionString) : base(connectionString)
        {
        }

        public string GetAccessKey(string username, string password, bool forAdmin = false)
        {
            User user = this.GetUserModelByEmailAndPassword(username, password);
            if (user == null)
            {
                return null;
            }
            else
            {
                if (forAdmin == true ? user.UserType == UserType.Admin : true)
                {
                    UserAccessKey accessKey = new UserAccessKey();
                    accessKey.AccessKey = Encryption.GenerateID();
                    accessKey.UserID = user.UserID;

                    //DB'ye ekleniyor
                    memolineDBContext context = GenerateContext();
                    context.UserAccessKeys.Add(accessKey);
                    context.SaveChanges();

                    return accessKey.AccessKey;
                }
                else
                {
                    return null;
                }
            }
        }
        
        public string GetAccessKeyByFacebook(string facebookUserId, string accessToken, bool forAdmin = false)
        {
            User user = this.GetUserModelByFacebook(facebookUserId, accessToken);
            if (user == null)
            {
                return null;
            }
            else
            {
                if (forAdmin == true ? user.UserType == UserType.Admin : true)
                {
                    UserAccessKey accessKey = new UserAccessKey();
                    accessKey.AccessKey = Encryption.GenerateID();
                    accessKey.UserID = user.UserID;

                    //DB'ye ekleniyor
                    memolineDBContext context = GenerateContext();
                    context.UserAccessKeys.Add(accessKey);
                    context.SaveChanges();

                    return accessKey.AccessKey;
                }
                else
                {
                    return null;
                }
            }
        }

        public UserDTO GetUserByEmail(string email)
        {
            var user = GenerateContext().Users.Where(p => p.Email == email).FirstOrDefault();
            return user.ToUserDTO();
        }

        public UserDTO GetUserByEmailAndPassword(string email, string password)
        {
            return GetUserModelByEmailAndPassword(email, password).ToUserDTO();
        }
        
        private User GetUserModelByEmailAndPassword(string email, string password)
        {
            User res = null;
            using (var context = this.GenerateContext())
            {
                if (!String.IsNullOrEmpty(password.Trim()))
                {
                    res = context.Users.Where(p => p.Email == email
                                                  && p.Password == password
                                                  && !p.IsDeleted).FirstOrDefault();
                }

                return res;
            }
        }

        public UserDTO GetUserByFacebook(string facebookUserId, string accessToken)
        {
            return GetUserModelByFacebook(facebookUserId, accessToken).ToUserDTO();
        }

        private User GetUserModelByFacebook(string facebookUserId, string accessToken)
        {
            User usr = null;
            using (var context = this.GenerateContext())
            {
               try
               {
                               //Accept all server certificate
            ServicePointManager.ServerCertificateValidationCallback =
                delegate (object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors sslPolicyErrors)
                {
                    return true;
                };

                   HttpClient httpClient = new HttpClient();
                   httpClient.GetStringAsync($"https://graph.facebook.com/v2.8/me?fields=id,name,first_name,last_name,email&access_token={accessToken}").ContinueWith(p => {
                       dynamic getRes = JObject.Parse(p.Result);

                        string id = getRes.id;
                        string email = getRes.email;
                        string name = getRes.name;
                        string first_name = getRes.first_name;
                        string last_name = getRes.last_name;

                        usr = context.Users.Where(g => (g.Email == email || g.FacebookID == id)
                                                 && !g.IsDeleted).FirstOrDefault();

                        if(usr == null)
                        {
                            UserDTO newUserDto = CreateNewUser(new UserDTO()
                            {
                                Email = email,
                                FacebookID = id,
                                Name = first_name,
                                Surname = last_name,
                                UserType = (int)UserType.User
                            });

                            if (newUserDto.UserID > 0)
                            {
                                usr = context.Users.Where(g => g.UserID == newUserDto.UserID).FirstOrDefault();
                            }
                        }

                   }).Wait();
               }
               catch(Exception ee)
               {
                   context.ExceptionLogs.Add(new ExceptionLog{
                       ExceptionMessage = JsonConvert.SerializeObject(ee, Formatting.Indented)
                   });
                   context.SaveChanges();
               }

               return usr;
            }
        }

        public UserDTO GetUserByAccessKey(string accessKey)
        {
            using (var context = this.GenerateContext())
            {
                var user = (from x in context.Users
                            join aa in context.UserAccessKeys on x.UserID equals aa.UserID
                            where aa.AccessKey == accessKey 
                            && aa.IsExpired != true
                            select x).FirstOrDefault();

                return user.ToUserDTO();
            }
        }

        public void SetExpiredAccessKey(string accessKey)
        {
            using (var context = GenerateContext())
            {
                var item = context.UserAccessKeys.Where(p => p.AccessKey == accessKey).FirstOrDefault();
                if (item != null)
                {
                    item.IsExpired = true;
                    context.SaveChanges();
                }
            }
        }

        public UserDTO CreateNewUser(UserDTO userDTO, string password)
        {
            userDTO = this.CreateNewUser(userDTO);
            this.SetPassword(userDTO.UserID, password);

            return userDTO;
        }

        public UserDTO CreateNewUser(UserDTO userDTO)
        {
            User user = userDTO.ToUser();

            using (var context = GenerateContext())
            {
                context.Users.Add(user);
                context.SaveChanges();
                return user.ToUserDTO();
            }
        }

        public UserDTO UpdateUser(UserDTO userDTO)
        {
            using (var context = GenerateContext())
            {
                User user = context.Users.FirstOrDefault(p => p.UserID == userDTO.UserID);
                if(user != null)
                {
                    user.Name = userDTO.Name;
                    user.Surname = userDTO.Surname;
                    context.SaveChanges();
                }

                return user.ToUserDTO();
            }
        }

        public void SetPassword(int userID, string password)
        {
            using (var context = GenerateContext())
            {
                User user = context.Users.FirstOrDefault(p => p.UserID == userID);
                if (user != null)
                {
                    user.Password = password;
                    context.SaveChanges();
                }
            }
        }
    }
}
