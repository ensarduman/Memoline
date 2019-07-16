package com.ensarduman.memoline.Data.LocalData;
import com.ensarduman.memoline.Model.HashtagModel;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.Model.UserModel;

import java.util.Date;
import java.util.List;

/**
 * Created by duman on 12/02/2018.
 */

public interface ILocalData {

    //Notlar
    int SaveNote(NoteModel noteModel);
    NoteModel GetNote(int noteID);
    List<NoteModel> GetAllNotes(Integer userID, Boolean isChecked);
    List<NoteModel> GetAllNotes(Integer userID, List<HashtagModel> filterHashtagModels, Boolean isChecked);
    void CheckNote(int noteID);
    void UnCheckNote(int noteID);
    void DeleteNote(int noteID);
    void SetDeletedOnServer(int noteID, boolean deleted);
    void SetCheckedOnServer(int noteID, boolean checked);
    void SetOnServer(int noteID, boolean onServer);
    void SetNoteUniqueID(int noteID, String noteUniqueID);
    List<NoteModel> GetNotesForSendDeletedOnServer();

    //Hashtag'ler
    HashtagModel GetHashtag(String hashtagValue);
    int CreateOrUpdateHashtag(HashtagModel hashtagModel);
    void AddNoteHashtagRelation(int noteID, int hashtagID);
    List<HashtagModel> GetNoteHashtags(int noteID);
    List<HashtagModel> GetLastHashtags(int maxCount, Integer userID);
    List<HashtagModel> SearchHashtags(String keyword, int maxCount, Integer userID);

    //Kullanıcılar
    UserModel GetCurrentUser();
    void SetUserLastSyncDate(int userID, Date lastSyncDate);
    UserModel GetUserByEmail(String email);
    int CreateOrUpdateUser(UserModel userModel);
    void SetValidUser(int userID);
    void SetUnvalidAllUsers();
    void SetUnOwnedNotesToOwned(int userID);
}
