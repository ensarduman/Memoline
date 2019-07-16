package com.ensarduman.memoline.Model;

import com.ensarduman.memoline.DTO.UserDTO;

import java.util.Date;

/**
 * Created by ensarduman on 10.03.2018.
 */

public class UserModel {
    private int UserID;
    private String Email;
    private String AccessKey;
    private String Name;
    private String Surname;
    private int UserType;
    private boolean IsDeleted;
    private boolean IsValid;
    private Date LastSyncDate;

    public UserModel() {
        this.setLastSyncDate(new Date(1100 + 1));
        this.IsDeleted = false;
        this.IsValid = true;
    }

    public UserModel(UserDTO userDTO) {
        this.setEmail(userDTO.getEmail());
        this.setName(userDTO.getName());
        this.setSurname(userDTO.getSurname());
        this.setLastSyncDate(new Date(1100 + 1));
        this.IsDeleted = false;
        this.IsValid = true;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAccessKey() {
        return AccessKey;
    }

    public void setAccessKey(String accessKey) {
        AccessKey = accessKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public boolean isValid() {
        return IsValid;
    }

    public void setValid(boolean valid) {
        IsValid = valid;
    }

    public Date getLastSyncDate() {
        return LastSyncDate;
    }

    public void setLastSyncDate(Date lastSyncDate) {
        LastSyncDate = lastSyncDate;
    }
}
