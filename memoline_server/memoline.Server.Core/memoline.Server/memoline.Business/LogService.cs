using memoline.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace memoline.Business
{
    public class LogService:BusinessBase
    {
        public LogService(string connectionString) : base(connectionString)
        {
        }

        //ExceptionLog tablosuna hata logunu ekler
        public void AddExceptionLog(string request, string exceptionMessage, int? userId)
        {
            using (var context = this.GenerateContext())
            {
                ExceptionLog exceptionLog = new ExceptionLog()
                {
                    Request = request,
                    ExceptionMessage = exceptionMessage,
                    UserId = userId
                };

                context.ExceptionLogs.Add(exceptionLog);
                context.SaveChanges();
            }
        }
    }
}
