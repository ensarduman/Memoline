using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text;

namespace ensarduman.Utils
{
    public static class DateHelper
    {
        public static String DateToString(this DateTime date)
        {

            return date.ToString("yyyy-MM-dd HH:mm");
        }

        public static DateTime StringToDate(this string datestr)
        {
            return DateTime.ParseExact(datestr, "yyyy-MM-dd HH:mm", CultureInfo.InvariantCulture);
        }

        public static DateTime ToDateTime(this long milliSeconds)
        {
            return new DateTime(1970, 1, 1).AddMilliseconds(milliSeconds);
        }

        public static long ToMilliSeconds(this DateTime dateTime)
        {
            DateTime dt1970 = new DateTime(1970, 1, 1);
            TimeSpan span = dateTime - dt1970;
            return Convert.ToInt64(span.TotalMilliseconds);
        }
    }
}
