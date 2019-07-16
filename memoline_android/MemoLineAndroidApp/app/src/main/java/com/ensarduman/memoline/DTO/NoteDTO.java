package com.ensarduman.memoline.DTO;

import android.provider.ContactsContract;

import com.ensarduman.memoline.Model.HashtagModel;
import com.ensarduman.memoline.Model.NoteModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by duman on 07/03/2018.
 */

public class NoteDTO implements IDTO<NoteDTO> {
    String NoteUniqueID;
    String ContentText;
    String ContentURL;
    int NoteType;
    long NoteDate;
    boolean IsChecked;
    boolean IsDeleted;
    String[] NoteHashtags;

    public NoteDTO() {
    }

    public NoteDTO(NoteModel noteModel) {
        setNoteUniqueID(noteModel.getNoteUniqueID());
        setContentText(noteModel.getContentText());
        setContentURL(noteModel.getContentURL());
        setChecked(noteModel.getIsChecked());
        setDeleted(noteModel.isDeleted());
        setNoteDate(noteModel.getNoteDate().getTime());
        setNoteType(noteModel.getNoteType().getValue());

        List<HashtagModel> modelHashtags = noteModel.getHashtags();
        String[] hashtags = new String[modelHashtags.size()];

        for(int i = 0; i<modelHashtags.size(); i++)
        {
            hashtags[i] = modelHashtags.get(i).getHashtagValue();
        }

        setNoteHashtags(hashtags);
    }

    public String getNoteUniqueID() {
        return NoteUniqueID;
    }

    public void setNoteUniqueID(String noteUniqueID) {
        NoteUniqueID = noteUniqueID;
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

    public int getNoteType() {
        return NoteType;
    }

    public void setNoteType(int noteType) {
        NoteType = noteType;
    }

    public long getNoteDate() {
        return NoteDate;
    }

    public void setNoteDate(long noteDate) {
        NoteDate = noteDate;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public String[] getNoteHashtags() {
        return NoteHashtags;
    }

    public void setNoteHashtags(String[] noteHashtags) {
        NoteHashtags = noteHashtags;
    }

    @Override
    public NoteDTO fromJSON(String json) {
        NoteDTO dto = null;

        if(json != null) {
            Gson gson = new GsonBuilder().create();
            dto = gson.fromJson(json, NoteDTO.class);
        }

        return  dto;
    }

    @Override
    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this);
        return  json;
    }
}
