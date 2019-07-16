package com.ensarduman.memoline.DTO;

import com.ensarduman.memoline.Model.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by duman on 07/03/2018.
 */

public class UserDTO implements IDTO<UserDTO> {
    String Email;
    String Name;
    String Surname;
    int UserType;

    public UserDTO() {
    }

    public UserDTO(UserModel userModel) {
        this.setEmail(userModel.getEmail());
        this.setName(userModel.getName());
        this.setSurname(userModel.getSurname());
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    @Override
    public UserDTO fromJSON(String json) {
        UserDTO dto = null;

        if(json != null) {
            Gson gson = new GsonBuilder().create();
            dto = gson.fromJson(json, UserDTO.class);
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
