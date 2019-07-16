using System;
using memoline.Server.Models;
using memoline.Business;
using ensarduman.Utils;
using memoline.DTO;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;

namespace memoline.WebApi.Controllers
{
    [Route("api/[controller]/[Action]")]
    [ApiController]
    public class AuthenticationController : MemolineControllerBase
    {
        public AuthenticationController(IConfiguration configuration) : base(configuration)
        {
        }


        /// <summary>
        /// Mevcut kullanıcıyı verir
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public ResponseModel GetCurrentUser()
        {
            AuthenticationService servsice = new AuthenticationService(this.ConnectionString);
            var user = CurrentUser;

            ResponseModel res;
            if (user == null)
            {
                res = SendResponse(null, ResponseStatus.LoginError);
            }
            else
            {
                res = SendResponse(user, ResponseStatus.Success);
            }

            return res;
        }

        /// <summary>
        /// Yeni kullanıcı yaratıp kullanıcıyı döner
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPost]
        public ResponseModel NewUser(UserDTO user,string password)
        {
            ResponseModel res;

            AuthenticationService authenticationService = new AuthenticationService(this.ConnectionString);

            UserDTO newUser = authenticationService.GetUserByEmail(user.Email);
            if (newUser == null)
            {
                res = SendResponse(authenticationService.CreateNewUser(user, password), ResponseStatus.Success);
            }
            else
            {
                res = SendResponse(null, ResponseStatus.UserAlreadyExists);
            }

            return res;
        }

        /// <summary>
        /// Kullanıcıyı güncelleyip sonucu döner
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPost]
        public ResponseModel UpdateUser(UserDTO user)
        {
            ResponseModel res;
            if (IsLoggedIn && CurrentUser.UserID == user.UserID)
            {
                AuthenticationService authenticationService = new AuthenticationService(this.ConnectionString);
                res = SendResponse(authenticationService.UpdateUser(user), ResponseStatus.Success);
            }
            else
            {
                res = SendResponse(null, ResponseStatus.AuthorizationError);
            }

            return res;
        }

        /// <summary>
        /// Login işlemini yapıp access key döner
        /// </summary>
        /// <param name="email"></param>
        /// <param name="password"></param>
        /// <returns></returns>kes
        [HttpGet]
        public ResponseModel Login(string email, string password)
        {
            AuthenticationService service = new AuthenticationService(this.ConnectionString);
            var accessKey = service.GetAccessKey(email, password);

            ResponseModel res;
            if (String.IsNullOrEmpty(accessKey))
            {
                res = SendResponse(null, ResponseStatus.LoginError);
            }
            else
            {
                res = SendResponse(accessKey, ResponseStatus.Success);
            }

            return res;
        }
        
        /// <summary>
        /// Facebook ile Login işlemini yapıp access key döner
        /// </summary>
        /// <param name="email"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        [HttpGet]
        public ResponseModel FacebookLogin(string facebookUserId, string accessToken)
        {
            AuthenticationService service = new AuthenticationService(this.ConnectionString);
            var accessKey = service.GetAccessKeyByFacebook(facebookUserId, accessToken);

            ResponseModel res;
            if (String.IsNullOrEmpty(accessKey))
            {
                res = SendResponse(null, ResponseStatus.LoginError);
            }
            else
            {
                res = SendResponse(accessKey, ResponseStatus.Success);
            }

            return res;
        }

        /// <summary>
        /// Kullanıcının accessKey'ini expired ederek logout eder
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public ResponseModel Logout()
        {
            AuthenticationService service = new AuthenticationService(this.ConnectionString);
            service.SetExpiredAccessKey(this.CurrentAccessKey);

            var res = SendResponse(null, ResponseStatus.Success);

            return res;
        }

        [HttpGet]
        public ResponseModel IsExists(string email)
        {
            AuthenticationService service = new AuthenticationService(this.ConnectionString);
            var user = service.GetUserByEmail(email);

            var res = SendResponse((user != null), ResponseStatus.Success);

            return res;
        }

        /// <summary>
        /// Validasyon maili gönderir
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public ResponseModel SendValidationEmail()
        {
            var res = SendResponse(null, ResponseStatus.Success);

            return res;
        }

        /// <summary>
        /// validasyon kodu ile email adresini onaylar
        /// </summary>
        /// <param name="validationcode"></param>
        /// <returns></returns>
        [HttpGet]
        public ResponseModel ValidateEmail(string validationcode)
        {
            var res = SendResponse(null, ResponseStatus.Success);

            return res;
        }
    }
}