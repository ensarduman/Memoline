package com.ensarduman.memoline.Util;

import com.ensarduman.memoline.Model.HashtagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duman on 17/02/2018.
 */

public class StringHelper {
    public static List<HashtagModel> GetHashtagListFromSstring(String value)
    {
        List<HashtagModel> hastags = new ArrayList<>();

        value = ClearHashtagWord(value);

        String hashtagValues[] = value.split(" ");



        HashtagModel currentModel;
        String currentValue;
        for(int i = 0; i < hashtagValues.length; i++)
        {
            currentValue = hashtagValues[i];
            currentValue = currentValue.trim();

            if(!new String("").equals(currentValue)) {
                currentModel = new HashtagModel();
                currentModel.setHashtagValue(currentValue);
                hastags.add(currentModel);
            }
        }

        return  hastags;
    }

    public static String GetHashtagStringFromList(List<HashtagModel> hastags, int tagType)
    {
        String returnValue = "";

        for(int i = 0; i < hastags.size(); i++)
        {
            if(tagType == 2 || i == 0) {
                returnValue += " #";
            }
            returnValue += hastags.get(i).getHashtagValue();
        }

        returnValue = returnValue.trim();

        return returnValue;
    }

    public static String[] StringListToArray(List<String> list)
    {
        String[] rv = new String[list.size()];

        for(int i = 0; i < list.size(); i++)
        {
            rv[i] = list.get(i).toString();
        }

        return rv;
    }

    public static String ClearHashtagWord(String value)
    {
        value = value.replace("\n", " ");
        value = value.replace("\t", " ");
        value = value.replace("\t", " ");
        value = value.replace("#", "");
        return value;
    }


}
