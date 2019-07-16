package com.ensarduman.memoline.Data.ServerData.WebApi;

import android.content.Context;

import com.ensarduman.memoline.DTO.ResponseDTO;
import com.ensarduman.memoline.DTO.UserDTO;
import com.ensarduman.memoline.Data.ServerData.Interfaces.IAuthenticationServer;

import org.json.JSONObject;

/**
 * Created by duman on 08/03/2018.
 */

public class WebApiAuthenticationServer implements IAuthenticationServer {

    Context context;
    String accessKey;

    public WebApiAuthenticationServer(Context context) {
        this.context = context;
        this.accessKey = null;
    }

    public WebApiAuthenticationServer(Context context, String accessKey) {
        this.context = context;
        this.accessKey = accessKey;
    }

    @Override
    public ResponseDTO GetCurrentUser() {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);
        String res = webApiHelper.GetRequest("authentication/getcurrentuser");
        ResponseDTO<UserDTO> dto = new ResponseDTO(res, UserDTO.class);
        return dto;
    }

    @Override
    public ResponseDTO NewUser(UserDTO user, String password) {
        WebApiHelper webApiHelper = new WebApiHelper(context, null);
        String res = webApiHelper.PostRequest("authentication/newuser?password=" + password, user.toJSON());

        ResponseDTO<UserDTO> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, UserDTO.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO UpdateUser(UserDTO user) {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);
        String res = webApiHelper.PostRequest("authentication/updateuser", user.toJSON());
        ResponseDTO<UserDTO> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, UserDTO.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO Login(String email, String password) {
        WebApiHelper webApiHelper = new WebApiHelper(context, null);
        String res = webApiHelper.GetRequest("authentication/login?email=" + email + "&password=" + password);

        ResponseDTO<String> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, String.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO<String> FacebookLogin(String facebookUserId, String accessToken) {
        WebApiHelper webApiHelper = new WebApiHelper(context, null);
        String res = webApiHelper.GetRequest("authentication/facebooklogin?facebookUserId=" + facebookUserId + "&accessToken=" + accessToken);

        ResponseDTO<String> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, String.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO Logout() {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);
        String res = webApiHelper.GetRequest("authentication/logout");

        ResponseDTO<String> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, String.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO<Boolean> IsExists(String email) {
        WebApiHelper webApiHelper = new WebApiHelper(context, accessKey);
        String res = webApiHelper.GetRequest("authentication/isexists?email=" + email);

        ResponseDTO<Boolean> dto = null;
        if(res != null) {
            dto = new ResponseDTO(res, Boolean.class);
        }

        return dto;
    }

    @Override
    public ResponseDTO SendValidationEmail() {
        return null;
    }

    @Override
    public ResponseDTO ValidateEmail(String validationcode) {
        return null;
    }
}
