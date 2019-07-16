package com.ensarduman.memoline.Model;

import com.ensarduman.memoline.DTO.NoteDTO;
import com.ensarduman.memoline.Util.DateHelper;
import com.ensarduman.memoline.Util.EnumNoteType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by duman on 12/02/2018.
 */

public class NoteModel {
    int NoteID;
    Integer UserID;
    String NoteUniqueID;
    String ContentText;
    String ContentURL;
    EnumNoteType NoteType;
    Date NoteDate;
    boolean IsChecked;
    boolean IsCheckedOnServer;
    boolean IsDeleted;
    boolean IsDeletedOnServer;
    boolean IsOnServer;
    List<HashtagModel> Hashtags;

    public NoteModel() {
        Hashtags = new ArrayList<>();
        NoteDate = DateHelper.GetUTCNow();
    }

    public NoteModel(NoteDTO noteDTO) {
        this.setChecked(noteDTO.isChecked());
        this.setCheckedOnServer(noteDTO.isChecked());
        this.setDeleted(noteDTO.isDeleted());
        this.setDeletedOnServer(noteDTO.isDeleted());
        this.setContentText(noteDTO.getContentText());
        this.setContentURL(noteDTO.getContentURL());
        this.setNoteDate(new Date(noteDTO.getNoteDate()));
        this.setNoteType(EnumNoteType.fromInt(noteDTO.getNoteType()));
        this.setNoteUniqueID(noteDTO.getNoteUniqueID());

        List<HashtagModel> hashtagModels = new ArrayList<>();

        String[] noteHashtags = noteDTO.getNoteHashtags();
        HashtagModel currentHashtagModel;
        if(noteHashtags != null)
        {
            for(int i = 0; i<noteHashtags.length; i++)
            {
                currentHashtagModel = new HashtagModel();
                currentHashtagModel.setHashtagValue(noteHashtags[i]);
                hashtagModels.add(currentHashtagModel);
            }
        }

        this.setHashtags(hashtagModels);

    }

    public int getNoteID() {
        return NoteID;
    }

    public void setNoteID(int noteID) {
        NoteID = noteID;
    }

    public Integer getUserID() {
        return UserID;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
    }

    public String getContentText() {
        return ContentText;
    }

    public void setContentText(String contentText) {
        ContentText = contentText;
    }

    public String getContentURL() {
        return ContentURL;
    }

    public void setContentURL(String contentURL) {
        ContentURL = contentURL;
    }

    public EnumNoteType getNoteType() {
        return NoteType;
    }

    public void setNoteType(EnumNoteType noteType) {
        NoteType = noteType;
    }

    public Date getNoteDate() {
        return NoteDate;
    }

    public void setNoteDate(Date noteDate) {
        NoteDate = noteDate;
    }

    public boolean getIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(boolean checked) {
        IsChecked = checked;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }

    public List<HashtagModel> getHashtags() {
        return Hashtags;
    }

    public void setHashtags(List<HashtagModel> hashtags) {
        Hashtags = hashtags;
    }

    public boolean isCheckedOnServer() {
        return IsCheckedOnServer;
    }

    public void setCheckedOnServer(boolean checkedOnServer) {
        IsCheckedOnServer = checkedOnServer;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public boolean isDeletedOnServer() {
        return IsDeletedOnServer;
    }

    public void setDeletedOnServer(boolean deletedOnServer) {
        IsDeletedOnServer = deletedOnServer;
    }

    public String getNoteUniqueID() {
        return NoteUniqueID;
    }

    public void setNoteUniqueID(String noteUniqueID) {
        NoteUniqueID = noteUniqueID;
    }

    public boolean isOnServer() {
        return IsOnServer;
    }

    public void setOnServer(boolean onServer) {
        IsOnServer = onServer;
    }
}
