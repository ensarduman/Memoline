using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.Model
{
    public class UserAccessKey:BaseModel
    {
        public UserAccessKey()
        {
            this.IsExpired = false;
        }

        public int UserAccessKeyID
        {
            get;
            set;
        }

        public int UserID
        {
            get;
            set;
        }

        public virtual User User
        {
            get;
            set;
        }

        public string AccessKey
        {
            get;
            set;
        }

        public bool IsExpired
        {
            get;
            set;
        }

    }
}
