package com.ensarduman.memoline.Data.ServerData.WebApi;

import android.content.Context;

import com.ensarduman.memoline.DTO.NoteDTO;
import com.ensarduman.memoline.DTO.ResponseDTO;
import com.ensarduman.memoline.Data.ServerData.Interfaces.INoteServer;

/**
 * Created by duman on 08/03/2018.
 */

public class WebApiNoteServer implements INoteServer {

    Context context;
    String accessKey;

    public WebApiNoteServer(Context context, String accessKey) {
        this.context = context;
        this.accessKey = accessKey;
    }

    @Override
    public ResponseDTO<Integer> AddNote(NoteDTO noteDTO) {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);
        String res = webApiHelper.PostRequest("note/addnote", noteDTO.toJSON());

        ResponseDTO<Integer> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, Integer.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO DeleteNote(String noteUniqueId) {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);
        String res = webApiHelper.GetRequest("note/DeleteNote?noteUniqueId="+noteUniqueId);

        ResponseDTO<String> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, String.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO CheckNote(String noteUniqueId, boolean isChecked) {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);

        String strChecked = "false";

        if(isChecked)
        {
            strChecked = "true";
        }

        String res = webApiHelper.GetRequest("note/CheckNote?noteUniqueId="+noteUniqueId + "&isChecked=" + strChecked );

        ResponseDTO<String> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, String.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO GetUpdates(long lastupdatedate) {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);
        String res = webApiHelper.GetRequest("note/getupdates?lastupdatedate="+lastupdatedate);

        ResponseDTO<NoteDTO[]> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, NoteDTO[].class);
        }

        return dto;
    }
}
