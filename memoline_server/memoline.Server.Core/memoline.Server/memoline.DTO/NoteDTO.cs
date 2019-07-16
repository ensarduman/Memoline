using ensarduman.Utils;
using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.DTO
{
    public class NoteDTO
    {
        public NoteDTO()
        {
            this.NoteDate = DateTime.UtcNow.ToBinary();
            this.NoteType = (int)EnumNoteType.Text;
        }

        public string NoteUniqueID { get; set; }

        public string ContentText { get; set; }

        public string ContentURL { get; set; }

        public int NoteType { get; set; }

        public long NoteDate { get; set; }

        public bool IsChecked { get; set; }

        public bool IsDeleted { get; set; }

        public string[] NoteHashtags { get; set; }
    }
}
