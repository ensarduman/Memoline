using ensarduman.Utils;
using System;
using System.Collections.Generic;

namespace memoline.Model
{
    public class Note : BaseModel
    {
        public Note()
        {
            this.NoteDate = DateTime.UtcNow;
            this.NoteType = EnumNoteType.Text;
        }

        public int NoteID { get; set; }

        public string NoteUniqueID { get; set; }

        public int UserID { get; set; }

        public string ContentText { get; set; }

        public string ContentURL { get; set; }

        public EnumNoteType NoteType { get; set; }

        public DateTime NoteDate { get; set; }

        public bool IsChecked { get; set; }

        public virtual IList<NoteHashtag> NoteHashtags { get; set; }
    }
}
