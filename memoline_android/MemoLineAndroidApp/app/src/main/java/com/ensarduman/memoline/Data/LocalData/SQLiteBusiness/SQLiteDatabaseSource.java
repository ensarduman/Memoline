package com.ensarduman.memoline.Data.LocalData.SQLiteBusiness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by duman on 13/02/2018.
 */

public class SQLiteDatabaseSource  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Db_NoteLine";
    private static final int DATABASE_VERSION = 12;

    public SQLiteDatabaseSource(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        CreateTables(db);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        //RemoveTables(db);
        //CreateTables(db);

        //1'den 2'ye IsChecked kolonu ekleniyor
        if(oldVersion <= 1 && newVersion >= 2)
        {
            db.execSQL("ALTER TABLE Tbl_Note ADD IsChecked TINYINT NOT NULL DEFAULT 0");
        }

        //1'den 2'ye IsChecked kolonu ekleniyor
        if(oldVersion <= 3 && newVersion >= 4)
        {
            CreateHashtagTable(db);
            CreateNoteHashtagTable(db);
        }

        //1'den 2'ye IsChecked kolonu ekleniyor
        if(oldVersion <= 4 && newVersion >= 5)
        {
            RemoveTable(db, "Tbl_Note_Hashtag");
            CreateNoteHashtagTable(db);
        }

        //5'den 6'ya Bazı veriler temizleniyor
        //if(oldVersion <= 5 && newVersion >= 6)
        //{
        //    String updateString = "UPDATE Tbl_Note_Hashtag \n" +
        //            "SET \n" +
        //            "HashtagID = \n" +
        //            "(SELECT MAX(HashtagID) FROM Tbl_Hashtag tht2 WHERE tht2.HashtagValue = \n" +
        //            "(SELECT HashtagValue FROM Tbl_Hashtag tht WHERE tht.HashtagID = HashtagID LIMIT 1)) " +
        //            " WHERE NoteID = Tbl_Note_Hashtag.NoteID";
//
//
        //    String deleteString = "DELETE FROM Tbl_Hashtag\n" +
        //            "WHERE HashtagID IN (\n" +
        //            "SELECT ht.HashtagID FROM Tbl_Hashtag ht\n" +
        //            "WHERE 0 = (SELECT COUNT(*) FROM Tbl_Note_Hashtag tnht WHERE tnht.HashtagID = ht.HashtagID  )\n" +
        //            ")";
//
        //    db.execSQL(updateString);
        //    db.execSQL(deleteString);
        //}

        //6'den 7'ye IsDeletedOnServer ve IsCheckedOnServer alanları yaratılıyor
        if(oldVersion <= 6 && newVersion >= 7)
        {
            db.execSQL("ALTER TABLE Tbl_Note ADD IsDeletedOnServer TINYINT NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE Tbl_Note ADD IsCheckedOnServer TINYINT NOT NULL DEFAULT 0");
        }

        //7'den 8'e NoteUniqueID ekleniyor
        if(oldVersion <= 7 && newVersion >= 8)
        {
            db.execSQL("ALTER TABLE Tbl_Note ADD NoteUniqueID TEXT NULL");
        }

        //7'den 8'e NoteUniqueID ekleniyor
        if(oldVersion <= 8 && newVersion >= 9)
        {
            db.execSQL("ALTER TABLE Tbl_Note ADD IsOnServer TINYINT NOT NULL DEFAULT 0");
        }

        if(oldVersion <= 9 && newVersion >= 10)
        {
            db.execSQL("ALTER TABLE Tbl_Note ADD UserID INT NULL");
            CreateUserTable(db);
        }

        if(oldVersion == 10 && newVersion >= 11)
        {
            db.execSQL("ALTER TABLE Tbl_User ADD LastSyncDate BIGINT NOT NULL DEFAULT 1");
        }

        if(oldVersion <= 11 && newVersion >= 12)
        {
            db.execSQL("UPDATE Tbl_Note SET IsOnServer = 0");
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    private void CreateTables(SQLiteDatabase db){
        CreateNoteTable(db);
        CreateHashtagTable(db);
        CreateNoteHashtagTable(db);
        CreateUserTable(db);
    }

    private void RemoveTables(SQLiteDatabase db){
        //Tbl_Note tablosu kaldırılıyor
        db.execSQL("DROP TABLE IF EXISTS Tbl_Note");
    }

    private void RemoveTable(SQLiteDatabase db, String tableName){
        //Tbl_Note tablosu kaldırılıyor
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    private void CreateNoteTable(SQLiteDatabase db)
    {
        //Tbl_Note tablosu yaratılıyor
        String queryNote = "CREATE TABLE Tbl_Note" +
                "(" +
                "NoteID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                "ServerNoteID INTEGER NULL,"+
                "ContentText TEXT NULL,"+
                "ContentURL TEXT NULL,"+
                "ContentType INTEGER NOT NULL,"+
                "NoteDate BIGINT NOT NULL,"+
                "CreateDate BIGINT NOT NULL,"+
                "IsDeleted TINYINT NOT NULL DEFAULT 0,"+
                "IsChecked TINYINT NOT NULL DEFAULT 0,"+
                "IsDeletedOnServer  TINYINT NOT NULL DEFAULT 0,"+
                "IsCheckedOnServer TINYINT NOT NULL DEFAULT 0,"+
                "NoteUniqueID TEXT NULL,"+
                "IsOnServer TINYINT NOT NULL DEFAULT 0,"+
                "UserID INT NULL" +
                ")";
        db.execSQL(queryNote);
    }

    private void CreateHashtagTable(SQLiteDatabase db)
    {
        //Tbl_Note tablosu yaratılıyor
        String queryNote = "CREATE TABLE Tbl_Hashtag" +
                "(" +
                "HashtagID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                "ServerHashtagID INTEGER NULL,"+
                "HashtagValue TEXT NOT NULL,"+
                "LastUseDate BIGINT NOT NULL"+
                ")";
        db.execSQL(queryNote);
    }

    private void CreateNoteHashtagTable(SQLiteDatabase db)
    {
        //Tbl_Note_Hashtag tablosu yaratılıyor
        String queryNote = "CREATE TABLE Tbl_Note_Hashtag" +
                "(" +
                "HashtagID INTEGER NOT NULL,"+
                "NoteID INTEGER NOT NULL"+
                ")";
        db.execSQL(queryNote);
    }

    private void CreateUserTable(SQLiteDatabase db)
    {
        //Tbl_User tablosu yaratılıyor
        String queryNote = "CREATE TABLE Tbl_User" +
                "(" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                "Email TEXT NOT NULL,"+
                "AccessKey TEXT NULL,"+
                "Name TEXT NULL,"+
                "Surname TEXT NULL,"+
                "UserType INT NOT NULL DEFAULT 2,"+
                "CreateDate BIGINT NOT NULL,"+
                "IsDeleted TINYINT NOT NULL DEFAULT 0,"+
                "IsValid TINYINT NOT NULL DEFAULT 1,"+
                "LastSyncDate BIGINT NOT NULL DEFAULT 1"+
                ")";
        db.execSQL(queryNote);
    }
}
