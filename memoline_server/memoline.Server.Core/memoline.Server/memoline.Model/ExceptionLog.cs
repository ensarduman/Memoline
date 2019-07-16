using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace memoline.Model
{
    public class ExceptionLog
    {
        public ExceptionLog()
        {
            CreateDate = DateTime.UtcNow;
        }

        public int ExceptionLogID { get; set; }
        public int? UserId { get; set; }
        public string Request { get; set; }
        public string ExceptionMessage { get; set; }
        public DateTime CreateDate { get; set; }
    }
}
