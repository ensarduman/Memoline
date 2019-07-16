using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.Business
{
    public class BusinessBase
    {
        string connectionString;

        public BusinessBase(string connectionString)
        {
            this.connectionString = connectionString;
        }

        internal memolineDBContext GenerateContext()
        {
            return new memolineDBContext(this.connectionString);
        }
    }
}
