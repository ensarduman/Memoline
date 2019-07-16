using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.Model
{
    public class Hashtag : BaseModel
    {
        public int HashtagID { get; set; }
        public string HashtagValue { get; set; }

        public virtual IList<NoteHashtag> NoteHashtags { get; set; }
    }
}
