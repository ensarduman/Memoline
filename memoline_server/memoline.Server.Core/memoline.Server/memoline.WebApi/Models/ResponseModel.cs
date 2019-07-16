using ensarduman.Utils;
using System;

namespace memoline.Server.Models
{
    public class ResponseModel
    {
        public ResponseModel(object data, ResponseStatus status)
        {
            this.Data = data;
            this.StatusCode = (int)status;
            this.StatusText = status.ToString();
        }

        public object Data
        {
            get;
            private set;
        }

        public int StatusCode
        {
            get;
            private set;
        }

        public string StatusText
        {
            get;
            private set;
        }

    }
}
