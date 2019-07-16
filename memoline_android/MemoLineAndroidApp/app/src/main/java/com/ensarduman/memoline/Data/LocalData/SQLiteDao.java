package com.ensarduman.memoline.Data.LocalData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ensarduman.memoline.Data.LocalData.SQLiteBusiness.SQLiteDatabaseSource;
import com.ensarduman.memoline.Model.HashtagModel;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.Util.DateHelper;
import com.ensarduman.memoline.Util.EnumNoteType;
import com.ensarduman.memoline.Util.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by duman on 13/02/2018.
 */

final class SQLiteDao implements ILocalData {

    SQLiteDatabase database;
    SQLiteDatabaseSource sqLiteDatabaseSource;

    public SQLiteDao(Context c) {
        this.sqLiteDatabaseSource = new SQLiteDatabaseSource(c);
    }

    private void OpenConnection()
    {
        database = sqLiteDatabaseSource.getWritableDatabase();
    }

    private void CloseConnection()
    {
        sqLiteDatabaseSource.close();
    }

    //NOT İŞLEMLERİ

    private NoteModel GetNoteFromCursor(Cursor cursor)
    {
        NoteModel model = new NoteModel();
        model.setNoteID(cursor.getInt(0));

        //String serverNoteID = cursor.getString(1);

        model.setContentText(cursor.getString(2));
        model.setContentURL(cursor.getString(3));
        model.setNoteType(EnumNoteType.fromInt(cursor.getInt(4)));
        model.setNoteDate(new Date(cursor.getLong(5)));

        int isDeleted = cursor.getInt(7);
        model.setDeleted(isDeleted <= 0 ? false:true);

        int isChecked = cursor.getInt(8);
        model.setIsChecked(isChecked <= 0 ? false:true);

        int isDeletedOnServer = cursor.getInt(9);
        model.setDeletedOnServer(isDeletedOnServer <= 0 ? false:true);

        int isCheckedOnServer = cursor.getInt(10);
        model.setCheckedOnServer(isCheckedOnServer <= 0 ? false:true);

        model.setNoteUniqueID(cursor.getString(11));

        int isOnServer = cursor.getInt(12);
        model.setOnServer(isOnServer <= 0 ? false:true);

        String strUserID = cursor.getString(13);
        if(strUserID != null) {
            model.setUserID(Integer.valueOf(strUserID));
        }

        return  model;
    }

    /**
     * Unique ID boş değilse
     * Unique ID'sine bakarak aynısı var ise günceller
     * Bu ID server'da yok ise
     * Yeni not olarak kaydedilir
     * */
    @Override
    public int SaveNote(NoteModel noteModel) {
        ContentValues values = new ContentValues();
        values.put("ContentText", noteModel.getContentText());
        values.put("ContentURL", noteModel.getContentURL());
        values.put("ContentType", noteModel.getNoteType().getValue());
        values.put("IsChecked", noteModel.getIsChecked()?1:0);
        values.put("NoteUniqueID", noteModel.getNoteUniqueID());
        values.put("IsCheckedOnServer", noteModel.isCheckedOnServer()?1:0);
        values.put("IsDeleted", noteModel.isDeleted()?1:0);
        values.put("IsDeletedOnServer", noteModel.isDeletedOnServer()?1:0);
        values.put("IsOnServer", noteModel.isOnServer()?1:0);

        boolean isExists = false;

        if(noteModel.getNoteUniqueID() != null && noteModel.getNoteUniqueID() != "") {
            NoteModel currentModel = GetNoteByUniqueID(noteModel.getNoteUniqueID());
            if(currentModel != null)
            {
                isExists = true;
                UpdateNote(currentModel.getNoteID(), values);
                noteModel.setNoteID(currentModel.getNoteID());
            }
            else
            {
                noteModel.setNoteID(CreateNote(noteModel, values));
            }
        }

        return noteModel.getNoteID();
    }

    private int CreateNote(NoteModel noteModel, ContentValues values){
        OpenConnection();
        values.put("UserID", noteModel.getUserID());
        values.put("CreateDate", DateHelper.GetUTCNow().getTime());
        values.put("NoteDate", noteModel.getNoteDate().getTime());

        noteModel.setNoteID((int)database.insert("Tbl_Note", null, values));
        CloseConnection();

        return noteModel.getNoteID();
    }

    private void UpdateNote(int noteID, ContentValues values){
        OpenConnection();
        database.update("Tbl_Note", values, "NoteID=?", new String[]{String.valueOf(noteID)});
        CloseConnection();
    }

    public NoteModel GetNote(int noteID){
        NoteModel returnValue = null;
        List<String> selectionArgs = new ArrayList<>();


        String query = "SELECT * FROM Tbl_Note WHERE NoteID = ?";

        selectionArgs.add(String.valueOf(noteID));

        OpenConnection();

        Cursor cursor = database.rawQuery(query, StringHelper.StringListToArray(selectionArgs));

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            returnValue = GetNoteFromCursor(cursor);
        }

        CloseConnection();

        return returnValue;
    }

    public NoteModel GetNoteByUniqueID(String noteUniqueID){
        NoteModel returnValue = null;

        String query = "SELECT * FROM Tbl_Note WHERE NoteUniqueID = ?";

        OpenConnection();

        Cursor cursor = database.rawQuery(query, new String[]{noteUniqueID});

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            returnValue = GetNoteFromCursor(cursor);
        }

        CloseConnection();

        return returnValue;
    }

    @Override
    public List<NoteModel> GetAllNotes(Integer userID, Boolean isChecked) {
        List<NoteModel> returnValue = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<String>();

        String selection = "SELECT * FROM Tbl_Note WHERE ";

        if(isChecked != null) {
            if (isChecked == true) {
                selection += "IsChecked = ? ";
                selectionArgs.add("1");
            } else {
                selection += "(IsChecked = ?" ;
                selectionArgs.add("0");
                selection += " OR";
                selection += " IsChecked IS NULL)";
            }
                selection += " AND ";
        }

        if(userID != null) {
            selection += " UserID = ? AND ";
            selectionArgs.add(userID.toString());
        }
        else
        {
            selection += " UserID IS NULL AND ";
        }

        selection += " IsDeleted = 0 ORDER BY NoteDate";

        OpenConnection();
        Cursor cursor = database.rawQuery(selection, StringHelper.StringListToArray(selectionArgs));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            returnValue.add(GetNoteFromCursor(cursor));

            cursor.moveToNext();
        }

        CloseConnection();

        return returnValue;
    }

    public List<NoteModel> GetAllNotes(Integer userID, List<HashtagModel> filterHashtagModels, Boolean isChecked){
        List<NoteModel> returnValue = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();


        String query = "SELECT * FROM Tbl_Note WHERE IsDeleted = 0 AND NoteID IN (SELECT NoteID FROM" +
                " (SELECT Tbl_Note.*, COUNT(Tbl_Hashtag.HashtagID) as NoteCount FROM Tbl_Note"+
                " INNER JOIN Tbl_Note_Hashtag ON Tbl_Note_Hashtag.NoteID = Tbl_Note.NoteID"+
                " INNER JOIN Tbl_Hashtag ON Tbl_Hashtag.HashtagID = Tbl_Note_Hashtag.HashtagID"+
                " WHERE ";

        if(isChecked != null) {
            if (isChecked == true) {
                query += " IsChecked = ?";
                selectionArgs.add("1");
            } else {
                query += " (IsChecked = ?";
                selectionArgs.add("0");
                query += " OR";
                query += " IsChecked IS NULL)";
            }

            if (filterHashtagModels.size() > 0) {
                query += " AND ";
            }
        }

        if(userID != null) {
            query += " UserID = ? AND ";
            selectionArgs.add(userID.toString());
        }
        else
        {
            query += " UserID IS NULL AND ";
        }

        String values = "";
        for(int i = 0; i<filterHashtagModels.size(); i++)
        {
            values += " Tbl_Hashtag.HashtagValue LIKE ?";
            //values += "'";
            //values += filterHashtagModels.get(i).getHashtagValue();
            //values += "%'";
            selectionArgs.add(filterHashtagModels.get(i).getHashtagValue() + "%");

            if(i + 1 != filterHashtagModels.size())
            {
                values += " OR ";
            }
        }
        query += values;
        query += " GROUP BY Tbl_Note.NoteID) tbl";
        query += " WHERE tbl.NoteCount >= " + filterHashtagModels.size();
        query += ") ORDER BY NoteDate";


        OpenConnection();

        Cursor cursor = database.rawQuery(query, StringHelper.StringListToArray(selectionArgs));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            returnValue.add(GetNoteFromCursor(cursor));

            cursor.moveToNext();
        }

        CloseConnection();

        return returnValue;
    }

    @Override
    public void CheckNote(int noteID) {
        CheckNote(noteID, true);
    }

    @Override
    public void UnCheckNote(int noteID) {
        CheckNote(noteID, false);
    }

    private void CheckNote(int noteID, boolean IsChecked) {
        OpenConnection();

        ContentValues values = new ContentValues();
        values.put("IsChecked", IsChecked?1:0);
        database.update("Tbl_Note", values, "NoteID=?", new String[]{String.valueOf(noteID)});

        CloseConnection();
    }

    @Override
    public void DeleteNote(int noteID){
        OpenConnection();

        ContentValues values = new ContentValues();
        values.put("IsDeleted", 1);
        //database.delete("Tbl_Note", "NoteID=" + noteModel.NoteID, null);
        database.update("Tbl_Note", values, "NoteID=?", new String[]{String.valueOf(noteID)});

        CloseConnection();
    }

    @Override
    public void SetDeletedOnServer(int noteID, boolean deleted) {
        OpenConnection();

        ContentValues values = new ContentValues();
        values.put("IsDeletedOnServer", deleted);
        //database.delete("Tbl_Note", "NoteID=" + noteModel.NoteID, null);
        database.update("Tbl_Note", values, "NoteID=?", new String[]{String.valueOf(noteID)});

        CloseConnection();
    }

    @Override
    public void SetCheckedOnServer(int noteID, boolean checked) {
        OpenConnection();

        ContentValues values = new ContentValues();
        values.put("IsCheckedOnServer", checked);
        //database.delete("Tbl_Note", "NoteID=" + noteModel.NoteID, null);
        database.update("Tbl_Note", values, "NoteID=?", new String[]{String.valueOf(noteID)});

        CloseConnection();
    }

    @Override
    public void SetOnServer(int noteID, boolean onServer) {
        OpenConnection();

        ContentValues values = new ContentValues();
        values.put("IsOnServer", onServer);
        //database.delete("Tbl_Note", "NoteID=" + noteModel.NoteID, null);
        database.update("Tbl_Note", values, "NoteID=?", new String[]{String.valueOf(noteID)});

        CloseConnection();
    }

    @Override
    public void SetNoteUniqueID(int noteID, String noteUniqueID) {
        OpenConnection();

        ContentValues values = new ContentValues();
        values.put("NoteUniqueID", noteUniqueID);
        //database.delete("Tbl_Note", "NoteID=" + noteModel.NoteID, null);
        database.update("Tbl_Note", values, "NoteID=?", new String[]{String.valueOf(noteID)});

        CloseConnection();
    }

    @Override
    /**
     * Silinmiş fakat silinme durumu
     * henüz sunucuya iletilmemiş notları
     * döner
     * */
    public List<NoteModel> GetNotesForSendDeletedOnServer() {
        List<NoteModel> returnValue = new ArrayList<>();

        OpenConnection();

        Cursor cursor = database.query("Tbl_Note", null, "IsDeleted = 1 AND IsDeletedOnServer = 0",null,null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            returnValue.add(GetNoteFromCursor(cursor));

            cursor.moveToNext();
        }

        CloseConnection();

        return returnValue;
    }

    //HASHTAG İŞLEMLERİ

    private HashtagModel GetHashtagFromCursor(Cursor cursor)
    {
        HashtagModel model = new HashtagModel();
        model.setHashtagID(cursor.getInt(0));
        model.setServerHashtagID(cursor.getInt(1));
        model.setHashtagValue(cursor.getString(2));
        model.setLastUseDate(new Date(cursor.getLong(3)));

        return  model;
    }

    @Override
    public HashtagModel GetHashtag(String hashtagValue) {

        HashtagModel returnValue = null;

        OpenConnection();

        String[] selectionArgs = new String[]{hashtagValue.toLowerCase()};

        Cursor cursor = database.query("Tbl_Hashtag", null, "HashtagValue = ?",selectionArgs,null, null, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            returnValue = GetHashtagFromCursor(cursor);
        }

        CloseConnection();

        return returnValue;
    }

    @Override
    public int CreateOrUpdateHashtag(HashtagModel hashtagModel) {
        HashtagModel hashtagModelFromDB = GetHashtag(hashtagModel.getHashtagValue());

        if(hashtagModelFromDB == null)
        {
            //Eğer DB'de yoksa yenisi yaratılıyor
            hashtagModel.setHashtagID(AddHashtag(hashtagModel));
        }
        else
        {
            //Eğer DB'de varsa ID'si alınıyor
            hashtagModel.setHashtagID(hashtagModelFromDB.getHashtagID());

            //LastUseDate update ediliyor
            UpdateHashtagLastUserDate(hashtagModel.getHashtagID(), hashtagModel.getLastUseDate());
        }
        return hashtagModel.getHashtagID();
    }

    private int AddHashtag(HashtagModel hashtagModel)
    {
        ContentValues values = new ContentValues();
        values.put("ServerHashtagID", hashtagModel.getServerHashtagID());
        values.put("HashtagValue", hashtagModel.getHashtagValue().toLowerCase());
        values.put("LastUseDate", hashtagModel.getLastUseDate().getTime());

        OpenConnection();
        hashtagModel.setHashtagID((int)database.insert("Tbl_Hashtag", null, values));
        CloseConnection();

        return hashtagModel.getHashtagID();
    }

    private void UpdateHashtagLastUserDate(int hashtagID, Date lastUseDate)
    {
        String[] selectionArgs = new String[]{String.valueOf(hashtagID)};
        ContentValues values = new ContentValues();
        values.put("LastUseDate", lastUseDate.getTime());

        OpenConnection();
        database.update("Tbl_Hashtag",values, "HashtagID=?", selectionArgs);
        CloseConnection();
    }

    /*
    * Not ve tag ilişkisi daha önce var mıymış diye bakar.
    * Var ise eklemez
    * */
    @Override
    public void AddNoteHashtagRelation(int noteID, int hashtagID) {
        //Eğer böyle bir ilişki daha önce yok ise kontrolü
        if(!IsNoteHashtagRelationExists(noteID, hashtagID)) {
            ContentValues values = new ContentValues();
            values.put("NoteID", noteID);
            values.put("HashtagID", hashtagID);

            OpenConnection();
            database.insert("Tbl_Note_Hashtag", null, values);
            CloseConnection();
        }
    }

    /*
    * Bu değerlerde bir ilişki mevcut mu?
    * */
    private boolean IsNoteHashtagRelationExists(int noteID, int hashtagID)
    {
        boolean returnValue = false;

        String query ="SELECT * FROM Tbl_Note_Hashtag WHERE NoteID=? AND HashtagID=?";
        OpenConnection();
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(noteID), String.valueOf(hashtagID)});

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            returnValue = true;
        }

        CloseConnection();

        return returnValue;
    }

    @Override
    public List<HashtagModel> GetNoteHashtags(int noteID) {

        List<HashtagModel> returnValue = new ArrayList<>();

        String query =
                "SELECT Tbl_Hashtag.HashtagID, Tbl_Hashtag.ServerHashtagID, Tbl_Hashtag.HashtagValue, Tbl_Hashtag.LastUseDate FROM Tbl_Hashtag " +
                "INNER JOIN Tbl_Note_Hashtag ON Tbl_Note_Hashtag.HashtagID = Tbl_Hashtag.HashtagID " +
                "WHERE Tbl_Note_Hashtag.NoteID = ?";
        OpenConnection();
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(noteID)});

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            returnValue.add(GetHashtagFromCursor(cursor));

            cursor.moveToNext();
        }

        CloseConnection();

        return returnValue;
    }

    @Override
    public List<HashtagModel> GetLastHashtags(int maxCount, Integer userID) {
        List<HashtagModel> returnValue = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();

        String query =
                "SELECT DISTINCT Tbl_Hashtag.* FROM Tbl_Hashtag " +
                        "LEFT JOIN Tbl_Note_Hashtag ON Tbl_Note_Hashtag.HashtagID = Tbl_Hashtag.HashtagID " +
                        "LEFT JOIN Tbl_Note ON Tbl_Note.NoteID = Tbl_Note_Hashtag.NoteID " +
                        "WHERE (Tbl_Note.UserID IS NULL OR Tbl_Note.IsDeleted = 0) ";

        if(userID != null)
        {
            query += "AND (Tbl_Note.UserID IS NULL OR Tbl_Note.UserID = ?) ";
            selectionArgs.add(Integer.toString(userID));
        }
        else
        {
            query += "AND Tbl_Note.UserID IS NULL ";
        }


        query += "ORDER BY LastUseDate DESC LIMIT ? ";
        selectionArgs.add(Integer.toString(maxCount));

        OpenConnection();
        Cursor cursor = database.rawQuery(query, StringHelper.StringListToArray(selectionArgs));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            returnValue.add(GetHashtagFromCursor(cursor));

            cursor.moveToNext();
        }

        CloseConnection();

        return returnValue;
    }

    @Override
    public List<HashtagModel> SearchHashtags(String keyword, int maxCount, Integer userID) {
        List<HashtagModel> returnValue = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();

        String query =
                "SELECT DISTINCT Tbl_Hashtag.* FROM Tbl_Hashtag " +
                        "LEFT JOIN Tbl_Note_Hashtag ON Tbl_Note_Hashtag.HashtagID = Tbl_Hashtag.HashtagID " +
                        "LEFT JOIN Tbl_Note ON Tbl_Note.NoteID = Tbl_Note_Hashtag.NoteID " +
                        "WHERE (Tbl_Note.UserID IS NULL OR Tbl_Note.IsDeleted = 0) AND Tbl_Hashtag.HashtagValue LIKE ? ";

        selectionArgs.add(keyword + "%");

        if(userID != null)
        {
            query += "AND (Tbl_Note.UserID IS NULL OR Tbl_Note.UserID = ?) ";
            selectionArgs.add(Integer.toString(userID));
        }
        else
        {
            query += "AND Tbl_Note.UserID IS NULL ";
        }

        query += "ORDER BY Tbl_Hashtag.LastUseDate DESC LIMIT ? ";
        selectionArgs.add(Integer.toString(maxCount));

        OpenConnection();
        Cursor cursor = database.rawQuery(query, StringHelper.StringListToArray(selectionArgs));

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            returnValue.add(GetHashtagFromCursor(cursor));

            cursor.moveToNext();
        }

        CloseConnection();

        return returnValue;
    }


    //USER İŞLEMLERİ

    private UserModel GetUserFromCursor(Cursor cursor)
    {
        UserModel model = new UserModel();
        model.setUserID(cursor.getInt(0));
        model.setEmail(cursor.getString(1));
        model.setAccessKey(cursor.getString(2));
        model.setName(cursor.getString(3));
        model.setSurname(cursor.getString(4));
        model.setUserType(cursor.getInt(5));
        model.setDeleted(cursor.getInt(7)==0 ?false:true);
        model.setValid(cursor.getInt(8)==0 ?false:true);
        model.setLastSyncDate(new Date(cursor.getLong(9)));

        return  model;
    }

    /*
    * Şu anda valid olan kullanıcıyı döner
    * */
    public UserModel GetCurrentUser()
    {
        UserModel returnValue = null;
        String query = "SELECT * FROM Tbl_User WHERE IsDeleted = 0 AND IsValid = 1 ORDER BY UserID DESC LIMIT 1";
        OpenConnection();
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            returnValue = GetUserFromCursor(cursor);
            if(returnValue.isDeleted())
            {
                returnValue = null;
            }
        }

        CloseConnection();

        return returnValue;
    }

    /*
    * Kullanıcının son sync tarihini değiştirir
    * */
    @Override
    public void SetUserLastSyncDate(int userID, Date lastSyncDate) {
        String[] selectionArgs = new String[]{String.valueOf(userID)};
        ContentValues values = new ContentValues();
        values.put("LastSyncDate", lastSyncDate.getTime());

        OpenConnection();
        database.update("Tbl_User",values, "UserID=?", selectionArgs);
        CloseConnection();
    }

    /*
    * Email adresine göre kullanıcı bulur
    * Bu motod kullanıcıların çoklanmasını önlerken
    * kullanılıyor
    * */
    @Override
    public UserModel GetUserByEmail(String email) {
        String[] selectionArgs = new String[]{email};
        UserModel returnValue = null;
        String query = "SELECT * FROM Tbl_User WHERE Email = ?";
        OpenConnection();
        Cursor cursor = database.rawQuery(query, selectionArgs);

        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            returnValue = GetUserFromCursor(cursor);
            if(returnValue.isDeleted())
            {
                returnValue = null;
            }
        }

        CloseConnection();

        return returnValue;
    }

    private int CreateUser(UserModel userModel, ContentValues contentValues)
    {
        int rv = 0;

        contentValues.put("Email", userModel.getEmail());
        contentValues.put("CreateDate", DateHelper.GetUTCNow().getTime());

        OpenConnection();
        userModel.setUserID((int)database.insert("Tbl_User", null, contentValues));
        rv = userModel.getUserID();
        CloseConnection();

        return rv;
    }

    private int UpdateUser(UserModel userModel, ContentValues values)
    {
        int rv = 0;

        OpenConnection();
        database.update("Tbl_User", values, "UserID=?", new String[]{String.valueOf(userModel.getUserID())});
        rv = userModel.getUserID();
        CloseConnection();

        return rv;
    }


    /*
    * Yeni bir kullanıcı yaratır veya aynı email'den varsa günceller
    * Bunun için yukarıdaki private olan CreateUser ve UpdateUser metodlarını kullanır
    * */
    @Override
    public int CreateOrUpdateUser(UserModel userModel)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("AccessKey", userModel.getAccessKey());
        contentValues.put("Name", userModel.getName());
        contentValues.put("Surname", userModel.getSurname());
        contentValues.put("UserType", userModel.getUserType());

        UserModel currentUser = GetUserByEmail(userModel.getEmail());

        if(currentUser == null) {
            return CreateUser(userModel, contentValues);
        }
        else
        {
            return UpdateUser(currentUser, contentValues);
        }
    }

    /*
    * Tüm kullanıcıların IsValid değerini false yaparken
    * ID'si verilen kullanıcınınkini true yapar
    * aynı zamanda bu kullanıcı silinmiş ise
    * Silinme işlemini de geri alır
    * */
    @Override
    public void SetValidUser(int userID) {

        SetUnvalidAllUsers();

        OpenConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IsValid", 1);
        contentValues.put("IsDeleted", 0);
        database.update("Tbl_User", contentValues, "UserID=?", new String[]{String.valueOf(userID)});
        CloseConnection();
    }

    /*
    * Tüm kullanıcıların IsValid değerini false yapar
    * */
    @Override
    public void SetUnvalidAllUsers() {
        OpenConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IsValid", 0);
        contentValues.put("IsDeleted", 0);
        database.update("Tbl_User", contentValues, null, null);
        CloseConnection();
    }

    /*
    * UserID'si boş olan tüm notları ID'si verilen kullanıcıya atar
    * */
    @Override
    public void SetUnOwnedNotesToOwned(int userID) {
        OpenConnection();
        database.execSQL ("UPDATE Tbl_Note SET UserID = "+ userID +", IsOnServer = 0 WHERE UserID IS NULL");
        CloseConnection();
    }
}
