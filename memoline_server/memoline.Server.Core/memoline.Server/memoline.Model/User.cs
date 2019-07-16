using ensarduman.Utils;
using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.Model
{
    public class User : BaseModel
    {
        public User()
        {
            UserType = UserType.User;
            LastSyncDate = DateTime.UtcNow;
            IsValid = false;
        }
        
        public int UserID
        {
            get;
            set;
        }

        public UserType UserType
        {
            get;
            set;
        }

        public string Email
        {
            get;
            set;
        }

        public string FacebookID
        {
            get;
            set;
        }

        public string Name
        {
            get;
            set;
        }

        public string Surname
        {
            get;
            set;
        }

        public string Password
        {
            get;
            set;
        }

        public bool IsValid
        {
            get;
            set;
        }

        public DateTime LastSyncDate { get; set; }

    }
}
