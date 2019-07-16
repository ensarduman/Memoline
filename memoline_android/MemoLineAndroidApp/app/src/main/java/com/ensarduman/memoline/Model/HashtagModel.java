package com.ensarduman.memoline.Model;

import com.ensarduman.memoline.Util.DateHelper;

import java.util.Date;

/**
 * Created by duman on 17/02/2018.
 */

public class HashtagModel {
    int HashtagID;
    Integer ServerHashtagID;
    String HashtagValue;
    Date LastUseDate;

    public HashtagModel() {
    }

    public HashtagModel(String hashtagValue) {
        HashtagValue = hashtagValue;
        LastUseDate = DateHelper.GetUTCNow();
    }

    public HashtagModel(String hashtagValue, Date lastUseDate) {
        HashtagValue = hashtagValue;
        LastUseDate = lastUseDate;
    }

    public int getHashtagID() {
        return HashtagID;
    }

    public void setHashtagID(int hashtagID) {
        HashtagID = hashtagID;
    }

    public Integer getServerHashtagID() {
        return ServerHashtagID;
    }

    public void setServerHashtagID(Integer serverHashtagID) {
        ServerHashtagID = serverHashtagID;
    }

    public String getHashtagValue() {
        return HashtagValue;
    }

    public void setHashtagValue(String hashtagValue) {
        HashtagValue = hashtagValue;
    }

    public Date getLastUseDate() {
        return LastUseDate;
    }

    public void setLastUseDate(Date lastUseDate) {
        LastUseDate = lastUseDate;
    }
}
