package com.ensarduman.memoline.Business;

import android.content.Context;

import com.ensarduman.memoline.Data.LocalData.ILocalData;
import com.ensarduman.memoline.Data.LocalData.LocalDataFactory;
import com.ensarduman.memoline.Model.HashtagModel;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.Util.DateHelper;
import com.ensarduman.memoline.Util.EnumLocalDataType;
import com.ensarduman.memoline.Util.GuidHelper;

import java.util.List;

/**
 * Created by duman on 12/02/2018.
 */

public class NoteBusiness extends BusinessBase {

    UserModel currentUser = null;
    Context context;

    public NoteBusiness(Context context) {
        this.context = context;

        AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(context);
        currentUser = authenticationBusiness.GetLocalCurrentUser();
    }

    private ILocalData GetLocalData()
    {
        LocalDataFactory localData = new LocalDataFactory(this.context);
        return localData.GetDao(EnumLocalDataType.SQLite);
    }

    /*
    * NoteUniqueID boş ise doldurur
    * dolu ise aynısı mevcutmu diye bakar
    * yenisini yaratır ve kaydeder
    * varsa günceller.
    * Ayrıca kaydedilen notun OnServer
    * özelliğini false yaparak
    * sunucuya değişikliklerin iletilmesini
    * sağlar.
    * */
    public int SaveNote(NoteModel note)
    {
        LocalDataFactory localData = new LocalDataFactory(this.context);
        ILocalData localDao = localData.GetDao(EnumLocalDataType.SQLite);

        if(note.getNoteUniqueID() == "" || note.getNoteUniqueID() == null)
        {
            note.setNoteUniqueID(GuidHelper.GetRandomGuid());
        }

        if(currentUser != null)
        {
            note.setUserID(currentUser.getUserID());
        }

        //Not kaydediliyor
        int noteID = localDao.SaveNote(note);

        List<HashtagModel> hashtagsModels = note.getHashtags();

        int currentHashtagID;
        for(int i = 0; i < hashtagsModels.size(); i++)
        {
            //Tüm hashtag'lerin son kullanıldığı tarih şu an olarak değiştiriliyor
            hashtagsModels.get(i).setLastUseDate(DateHelper.GetUTCNow());

            //Hashtag varsa db'deki listedekine set ediliyor.
            //Hashtag yoksa db'ye eklenip ID'si set ediliyor.
            hashtagsModels.get(i).setHashtagID(localDao.CreateOrUpdateHashtag(hashtagsModels.get(i)));

            //Hashtag'in not ile ilişkisi db'ye ekleniyor.
            localDao.AddNoteHashtagRelation(noteID, hashtagsModels.get(i).getHashtagID());
        }

        return noteID;
    }

    public List<NoteModel> GetAll(List<HashtagModel> filterHashtagModels, Boolean isChecked)
    {
        LocalDataFactory localData = new LocalDataFactory(this.context);
        ILocalData localDao = localData.GetDao(EnumLocalDataType.SQLite);

        //Eğer login olmuş bi kullanıcı yoksa kullanıcısı olmayan notlar alınıyor
        //Eğer login olunmuşsa o kullanıcının notları getiriliyor
        Integer currentUserID = null;
        if(currentUser != null)
        {
            currentUserID = currentUser.getUserID();
        }

        //Notlar alınıyor
        List<NoteModel> notes = null;
        if(filterHashtagModels != null && filterHashtagModels.size() > 0) {
            notes = localDao.GetAllNotes(currentUserID, filterHashtagModels, isChecked);
        }
        else{
            notes = localDao.GetAllNotes(currentUserID, isChecked);
        }

        //Notların hashtag'leri alınıyor
        for(int i = 0; i < notes.size(); i++)
        {
            notes.get(i).setHashtags(localDao.GetNoteHashtags(notes.get(i).getNoteID()));
        }

        return notes;
    }

    public NoteModel Get(int noteID)
    {
        LocalDataFactory localData = new LocalDataFactory(this.context);
        ILocalData localDao = localData.GetDao(EnumLocalDataType.SQLite);

        NoteModel noteModel = localDao.GetNote(noteID);

        return noteModel;
    }

    public void DeleteNote(int noteID)
    {
        ILocalData localDao = GetLocalData();
        localDao.DeleteNote(noteID);
    }

    public void CheckNote(int noteID)
    {
        ILocalData localDao = GetLocalData();
        localDao.CheckNote(noteID);
    }

    public void UnCheckNote(int noteID)
    {
        ILocalData localDao = GetLocalData();
        localDao.UnCheckNote(noteID);
    }

    //*
    // En son kullanılan hashtag'leri döner
    // */
    public List<HashtagModel> GetLastHashTags(int maxCount)
    {
        ILocalData localDao = GetLocalData();
        List<HashtagModel> rv;

        if(currentUser != null)
        {
            rv = localDao.GetLastHashtags(maxCount, currentUser.getUserID());
        }
        else
        {
            rv = localDao.GetLastHashtags(maxCount, null);
        }

        return rv;
    }

    public List<HashtagModel> SearchHashTags(String keyword, int maxCount)
    {
        ILocalData localDao = GetLocalData();
        List<HashtagModel> rv;

        if(currentUser != null)
        {
            rv = localDao.SearchHashtags(keyword, maxCount, currentUser.getUserID());
        }
        else
        {
            rv = localDao.SearchHashtags(keyword, maxCount, null);
        }

        return rv;
    }

    public int CreateOrUpdateHashtag(HashtagModel hashtag)
    {
        ILocalData localDao = GetLocalData();
        return localDao.CreateOrUpdateHashtag(hashtag);
    }

    public void SetOnServer(int noteID, boolean onServer)
    {
        ILocalData localDao = GetLocalData();
        localDao.SetOnServer(noteID, onServer);
    }

    public void SetDeletedOnServer(int noteID, boolean deleted)
    {
        ILocalData localDao = GetLocalData();
        localDao.SetDeletedOnServer(noteID, deleted);
    }

    public void SetCheckedOnServer(int noteID, boolean checked)
    {
        ILocalData localDao = GetLocalData();
        localDao.SetCheckedOnServer(noteID, checked);
    }

    /**
     * Silinmiş fakat silinme durumu
     * henüz sunucuya iletilmemiş notları
     * döner
     * */
    public List<NoteModel> GetNotesForSendDeletedOnServer()
    {
        ILocalData localDao = GetLocalData();
        return localDao.GetNotesForSendDeletedOnServer();
    }

}
