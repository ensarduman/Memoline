using ensarduman.Utils;
using memoline.DTO;
using memoline.Model;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

namespace memoline.Business
{
    internal static class ModelConverter
    {
        #region User

        public static User ToUser(this UserDTO dto)
        {
            User user = null;

            if (dto != null)
            {
                user = new User();
                user.UserID = dto.UserID;
                user.Email = dto.Email;
                user.Name = dto.Name;
                user.Surname = dto.Surname;
                user.UserType = (UserType)dto.UserType;
            }

            return user;
        }

        public static UserDTO ToUserDTO(this User user)
        {
            UserDTO userDTO = null;

            if (user != null)
            {
                userDTO = new UserDTO();
                userDTO.UserID = user.UserID;
                userDTO.Email = user.Email;
                userDTO.Name = user.Name;
                userDTO.Surname = user.Surname;
                userDTO.UserType = (int)user.UserType;
            }

            return userDTO;
        }

        #endregion User

        #region Note

        public static Note ToNote(this NoteDTO dto)
        {
            Note note = null;

            if (dto != null)
            {
                IList<NoteHashtag> hashtags = null;
                if (dto.NoteHashtags != null)
                {
                    hashtags = dto.NoteHashtags.Select(p => new NoteHashtag
                    {
                        Hashtag = new Hashtag() { HashtagValue = p }
                    }).ToList();
                }

                note = new Note()
                {
                    ContentText = dto.ContentText,
                    ContentURL = dto.ContentURL,
                    NoteDate = dto.NoteDate.ToDateTime(),
                    NoteHashtags = hashtags,
                    NoteType = (EnumNoteType)dto.NoteType,
                    NoteUniqueID = dto.NoteUniqueID
                };
            }

            return note;
        }

        /// <summary>
        /// Bu metod güncelleme işlemi için kullanma amacıyla geliştirildi
        /// Eğer db'den alınmış bir not nesnesi varsa onun instence'ının adresini
        /// değiştirmeden üzerine dto'den gelen bilgilerin
        /// ContentText, ContentURL ve NoteHashtags property'lerine
        /// yazılması sağlandı.
        /// Çünkü diğer özelliklerin değiştirilmesine gerek yok.
        /// Silinme veya check etme durumları ise başka senaryolarla 
        /// güncelleniyor.
        /// </summary>
        /// <param name="dto"></param>
        /// <param name="note"></param>
        public static void ToNote(this NoteDTO dto, ref Note note)
        {
            if (dto != null)
            {
                IList<NoteHashtag> hashtags = null;
                if (dto.NoteHashtags != null)
                {
                    hashtags = dto.NoteHashtags.Select(p => new NoteHashtag
                    {
                        Hashtag = new Hashtag() { HashtagValue = p }
                    }).ToList();
                }

                note.ContentText = dto.ContentText;
                note.ContentURL = dto.ContentURL;
                note.NoteHashtags = hashtags;
            }
        }

        public static NoteDTO ToNoteDTO(this Note note)
        {
            NoteDTO noteDto = null;

            if (note != null)
            {
                string[] hashtags = null;

                if (note.NoteHashtags != null)
                {
                    hashtags = note.NoteHashtags.Where(p => p.Hashtag != null).Select(p => p.Hashtag.HashtagValue).ToArray();
                }

                noteDto = new NoteDTO()
                {
                    ContentText = note.ContentText,
                    ContentURL = note.ContentURL,
                    IsChecked = note.IsChecked,
                    NoteDate = note.NoteDate.ToMilliSeconds(),
                    NoteHashtags = hashtags,
                    NoteType = (int)note.NoteType,
                    NoteUniqueID = note.NoteUniqueID,
                    IsDeleted = note.IsDeleted,
                };
            }

            return noteDto;
        }

        #endregion Note
    }
}
