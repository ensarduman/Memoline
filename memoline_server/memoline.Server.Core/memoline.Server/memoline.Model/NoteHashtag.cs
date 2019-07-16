using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.Model
{
    public class NoteHashtag
    {
        public int NoteID { get; set; }
        public virtual Note Note { get; set; }
        public int HashtagID { get; set; }
        public virtual Hashtag Hashtag { get; set; }
    }
}
