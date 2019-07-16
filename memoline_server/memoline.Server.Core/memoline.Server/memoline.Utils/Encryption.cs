using System;
using System.Security.Cryptography;
using System.Text;

namespace ensarduman.Utils
{
    public class Encryption
    {
        public static string MD5Hash(string input)
        {
            using (var md5 = MD5.Create())
            {
                var result = md5.ComputeHash(Encoding.ASCII.GetBytes(input));
                return Encoding.ASCII.GetString(result);
            }
        }

        public static string GenerateID()
        {
            return Guid.NewGuid().ToString();
        }
    }
}
