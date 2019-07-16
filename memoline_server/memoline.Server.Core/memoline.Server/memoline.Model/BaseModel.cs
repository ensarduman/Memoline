using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.Model
{
    public class BaseModel
    {
        public BaseModel()
        {
            this.CreateDate = DateTime.UtcNow;
            this.UpdateDate = DateTime.UtcNow;
            this.IsDeleted = false;
        }

        public DateTime CreateDate
        {
            get;
            set;
        }

        public DateTime UpdateDate
        {
            get;
            set;
        }

        public bool IsDeleted
        {
            get;
            set;
        }
    }
}
