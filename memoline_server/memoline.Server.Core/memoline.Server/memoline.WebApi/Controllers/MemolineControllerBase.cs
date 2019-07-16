using System;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http;
using System.Linq;
using System.Collections;
using memoline.Server.Models;
using ensarduman.Utils;
using memoline.DTO;
using memoline.Business;
using System.Text;
using System.Collections.Generic;
using Microsoft.Extensions.Configuration;

namespace memoline.WebApi.Controllers
{
    public class MemolineControllerBase: ControllerBase
    {
        public IConfiguration Configuration;
        public string ConnectionString;

        public MemolineControllerBase(IConfiguration configuration)
        {
            this.Configuration = configuration;
            this.ConnectionString = configuration.GetConnectionString("MemolineServerDB");
        }

        public ResponseModel SendResponse(object data, ResponseStatus status)
        {
            return new ResponseModel(data, status);
        }

        /// <summary>
        /// Hata mesajını db'ye yazdıktan sonra hata response'u oluşturur
        /// </summary>
        /// <param name="data"></param>
        /// <param name="ee"></param>
        /// <returns></returns>
        public ResponseModel SendExceptionResponse(Exception ee)
        {
            LogService logService = new LogService(this.ConnectionString);

            StringBuilder sb = new StringBuilder();

            sb.AppendLine(Request.ToString());

            sb.AppendLine("");

            sb.AppendLine("##Headers##");
            if (Request.Headers != null)
            {
                foreach (var header in Request.Headers)
                {
                    sb.AppendLine("Key:" + header.Key);
                    sb.AppendLine("Values:");
                    header.Value.ToList().ForEach(p =>
                    {
                        sb.AppendLine(p);
                        sb.AppendLine("");
                    });
                    sb.AppendLine("-----------------");
                }
            }

            sb.AppendLine("");

            sb.AppendLine("##Content Headers##");
            if (HttpContext.Request.Headers != null)
            {
                foreach (var header in HttpContext.Request.Headers)
                {
                    sb.AppendLine("Key:" + header.Key);
                    sb.AppendLine("Values:");
                    header.Value.ToList().ForEach(p =>
                    {
                        sb.AppendLine(p);
                        sb.AppendLine("");
                    });
                    sb.AppendLine("-----------------");
                }
            }

            logService.AddExceptionLog(sb.ToString(), ee.Message, CurrentUser == null ? (int?)null : CurrentUser.UserID);

            return new ResponseModel(null, ResponseStatus.Error);
        }

        public UserDTO CurrentUser
        {
            get
            {
                AuthenticationService service = new AuthenticationService(this.ConnectionString);
                var user = service.GetUserByAccessKey(this.CurrentAccessKey);
                return user;
            }
        }

        public string CurrentAccessKey
        {
            get
            {
                string rv = null;
                IEnumerable<string> values;

                if(HttpContext.Request.Headers.ContainsKey("access_key"))
                {
                    rv = HttpContext.Request.Headers["access_key"];
                }

                return rv;
            }
        }

        public bool IsLoggedIn
        {
            get
            {
                return CurrentUser != null;
            }
        }

        public bool IsAdmin
        {
            get
            {
                return CurrentUser != null && CurrentUser.UserType == (int)UserType.Admin;
            }
        }
    }
}