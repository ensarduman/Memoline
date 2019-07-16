using memoline.DTO;
using System.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using memoline.Model;

namespace memoline.Business
{
    public class NoteService : BusinessBase
    {
        int currentUserID;

        public NoteService(string connectionString, int currentUserID):base(connectionString)
        {
            this.currentUserID = currentUserID;
        }

        /// <summary>
        /// DB'ye not ekler veya günceller.
        /// Notun olup olmadığını NoteUniqueID ile kontrol eder
        /// Eğer not zaten var ise güncelleyerek ID'sini döner.
        /// </summary>
        /// <param name="dto"></param>
        /// <returns></returns>
        public int AddNote(NoteDTO dto)
        {
            List<NoteHashtag> hashtags;
            Note note;
            using (var context = this.GenerateContext())
            {

                //DB'de daha önce aynı not kaydedilmiş mi?
                note = context.Notes.Where(p => p.NoteUniqueID.Trim() == dto.NoteUniqueID.Trim()).FirstOrDefault();


                //Kaydedilmemişse ekliyor
                if (note == null)
                {
                    note = dto.ToNote();
                    note.UserID = currentUserID;

                    //Notun hashtagleri daha sonra kaydedilmek için alınıyor
                    hashtags = note.NoteHashtags.ToList();

                    //Notun tag'leri ayrıca yönetileceği için burada boş gönderiliyor.
                    note.NoteHashtags = new List<NoteHashtag>();

                    context.Notes.Add(note);
                }
                else
                {
                    ControlUser(note.UserID);


                    /*
                    Eğer güncelleme yapılıyorsa silinen hashtag'lerin
                    db'de silinebilmesi için önce burada tamamı siliniyor
                    ve aşağıdaki satırlarda yeni hashtag'ler tekrar ekleniyor
                    */
                    //note.NoteHashtags.ToList().ForEach(p => context.NoteHashtags.Remove(p));

                    /*
                     DTO'dan gelen datalar db'den alınan note'un
                     referansına yazılıyor
                     */
                    dto.ToNote(ref note);

                    //Notun dto'dan gelen yeni hashtagleri daha sonra kaydedilmek için alınıyor
                    hashtags = note.NoteHashtags.ToList();

                    //Notun tag'leri ayrıca yönetileceği için burada boş gönderiliyor.
                    note.NoteHashtags = new List<NoteHashtag>();

                    /*Notun güncelleme tarihi de dğiştirilerek
                     Güncellenen notlar istendiğinde gelmesi (GetUpdates)
                     sağlanıyor
                     */
                    note.UpdateDate = DateTime.UtcNow;
                }

                //Değişiklikler db'ye kaydediliyor
                context.SaveChanges();
            }

            //Notun hashtag'leri db'ye yazılıyor
            foreach (var hashtag in hashtags)
            {
                if (hashtag.Hashtag != null)
                {
                    AddHashtag(note.NoteID, hashtag.Hashtag.HashtagValue);
                }
            }

            return note.NoteID;
        }

        /// <summary>
        /// Hashtag'i nota ekler
        /// </summary>
        /// <param name="noteId"></param>
        /// <param name="hashtagValue"></param>
        private void AddHashtag(int noteId, string hashtagValue)
        {
            using (var context = this.GenerateContext())
            {
                hashtagValue = hashtagValue.ToLower().Trim();

                //Bu değerde hashtag varsa alıyor
                var hashtag = context.Hashtags.Where(p => p.HashtagValue.Trim().ToLower() == hashtagValue.Trim().ToLower()).FirstOrDefault();

                //Eğer hashtag yok ise yenisini yaratıyor
                if (hashtag == null)
                {
                    hashtag = new Hashtag
                    {
                        HashtagValue = hashtagValue
                    };

                    context.Hashtags.Add(hashtag);
                    context.SaveChanges();
                }

                //Bu hashtag daha önce bu nota eklenmiş mi?
                var notehashtag = context.NoteHashtags.Where(p => p.HashtagID == hashtag.HashtagID && p.NoteID == noteId).FirstOrDefault();

                //Eklenmemişse ekleniyor
                if (notehashtag == null)
                {
                    context.NoteHashtags.Add(new NoteHashtag
                    {
                        HashtagID = hashtag.HashtagID,
                        NoteID = noteId
                    });
                    context.SaveChanges();
                }
            }
        }

        /// <summary>
        /// Notu siler
        /// </summary>
        /// <param name="noteUniqueId"></param>
        /// <returns></returns>
        public bool DeleteNote(string noteUniqueId)
        {
            var rv = false;

            using (var context = this.GenerateContext())
            {
                var note = context.Notes.Where(p => p.NoteUniqueID == noteUniqueId).FirstOrDefault();

                ControlUser(note.UserID);

                //Eğer notr var ise işlem yapılıyor
                if (note != null)
                {
                    note.IsDeleted = true;
                    context.SaveChanges();

                    SetNoteLastUpdateDate(note.NoteID);

                    rv = true;
                }
            }

            return rv;
        }

        /// <summary>
        /// Notun tikleme durumunu değiştirir
        /// </summary>
        /// <param name="noteUniqueId"></param>
        /// <returns></returns>
        public bool CheckNote(string noteUniqueId, bool isChecked)
        {
            var rv = false;

            using (var context = this.GenerateContext())
            {
                var note = context.Notes.Where(p => p.NoteUniqueID == noteUniqueId).FirstOrDefault();

                ControlUser(note.UserID);

                //Eğer not var ise işlem yapılıyor
                if (note != null)
                {
                    note.IsChecked = isChecked;
                    context.SaveChanges();

                    SetNoteLastUpdateDate(note.NoteID);

                    rv = true;
                }
            }

            return rv;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="lastupdatedate"></param>
        /// <returns></returns>
        public NoteDTO[] GetUpdates(DateTime lastUpdateDate)
        {
            List<NoteDTO> rv = new List<NoteDTO>();

            using (var context = this.GenerateContext())
            {
                var notes = context.Notes.Where(p => p.UpdateDate > lastUpdateDate && p.UserID == currentUserID).ToList();
                rv = notes.Select(p => p.ToNoteDTO()).ToList();
            }

            return rv.ToArray();
        }

        /// <summary>
        /// Notun son güncelleme tarihini değiştirir
        /// </summary>
        /// <param name="noteID"></param>
        /// <param name="lastupdatedate"></param>
        private void SetNoteLastUpdateDate(int noteID)
        {
            using (var context = this.GenerateContext())
            {
                var note = context.Notes.Where(p => p.NoteID == noteID).FirstOrDefault();

                ControlUser(note.UserID);

                //Eğer not var ise işlem yapılıyor
                if (note != null)
                {
                    note.UpdateDate = DateTime.UtcNow;
                    context.SaveChanges();
                }
            }
        }

        /// <summary>
        /// Verilen ID login olan kullanıcının ID'si değilse hata fırlatır
        /// </summary>
        /// <param name="userId"></param>
        private void ControlUser(int userId)
        {
            if (userId != currentUserID)
            {
                throw new UnauthorizedAccessException();
            }
        }
    }
}