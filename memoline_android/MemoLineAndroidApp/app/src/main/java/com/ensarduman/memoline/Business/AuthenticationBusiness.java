package com.ensarduman.memoline.Business;

import android.content.Context;

import com.ensarduman.memoline.DTO.ResponseDTO;
import com.ensarduman.memoline.DTO.UserDTO;
import com.ensarduman.memoline.Data.LocalData.ILocalData;
import com.ensarduman.memoline.Data.LocalData.LocalDataFactory;
import com.ensarduman.memoline.Data.ServerData.Interfaces.IAuthenticationServer;
import com.ensarduman.memoline.Data.ServerData.ServerFactory;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.Util.EnumLocalDataType;
import com.ensarduman.memoline.Util.EnumLoginType;
import com.ensarduman.memoline.Util.EnumResponseStatus;
import com.ensarduman.memoline.Util.EnumServerDataType;

import java.util.Date;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class AuthenticationBusiness extends BusinessBase {
    Context context;
    EnumServerDataType serverDataType = EnumServerDataType.WebApi;

    public AuthenticationBusiness(Context context) {
        this.context = context;
    }

    private IAuthenticationServer GetAuthenticationServer(String accessKey){
        return new ServerFactory(context).GetAuthenticationServer(serverDataType, accessKey);
    }

    private IAuthenticationServer GetAuthenticationServer(){
        return new ServerFactory(context).GetAuthenticationServer(serverDataType);
    }

    private ILocalData GetLocalData()
    {
        LocalDataFactory localData = new LocalDataFactory(this.context);
        return localData.GetDao(EnumLocalDataType.SQLite);
    }

    /**
     * Lokalde en son giriş yapan kullanıcıyı döner
     */
    public UserModel GetLocalCurrentUser()
    {
        ILocalData localData = GetLocalData();
        UserModel localCurrentUser = localData.GetCurrentUser();
        return localCurrentUser;
    }

    /**
     * Giriş yapılan kullanıcı server da valid mi?
     * Valid değilse lokaldekileri de unvalid yapar
     */
    public boolean ControlValidCurrentUser()
    {
        UserModel currentUser = GetLocalCurrentUser();
        boolean rv = false;

        if(currentUser != null) {
            IAuthenticationServer authenticationServer = GetAuthenticationServer(currentUser.getAccessKey());
            ResponseDTO<UserDTO> responseDTO = authenticationServer.GetCurrentUser();
            rv = responseDTO.getData() != null;

            if(!rv)
            {
                ILocalData localData = GetLocalData();
                localData.SetUnvalidAllUsers();
            }
        }

        return rv;
    }



    public boolean IsCurrentUserLoggedIn()
    {
        boolean returnValue = false;

        ILocalData localData = GetLocalData();
        UserModel localCurrentUser = localData.GetCurrentUser();

        //user mevcutsa, silinmemişse ve valid ise login kontrolü yapılmaya değerdir
        if(localCurrentUser != null) {
            if(localCurrentUser.isValid() && !localCurrentUser.isDeleted()) {
                IAuthenticationServer authenticationServer = GetAuthenticationServer(localCurrentUser.getAccessKey());
                ResponseDTO<UserDTO> userDTOResponseDTO = authenticationServer.GetCurrentUser();
                if (userDTOResponseDTO.getData() != null) {
                    returnValue = true;
                }
            }
        }

        return returnValue;
    }

    public UserModel CreateUser(UserDTO userDTO, String password)
    {
        UserModel rv = null;

        IAuthenticationServer  authenticationServer = GetAuthenticationServer();
        ILocalData localData = GetLocalData();

        ResponseDTO<UserDTO> userDTOResponseDTO = authenticationServer.NewUser(userDTO, password);

        if(isResponseSuccess(userDTOResponseDTO)) {
            if (userDTOResponseDTO.getStatusCode() == EnumResponseStatus.Success.getValue()) {
                rv = Login(userDTO.getEmail(), password);
            }
        }

        return rv;
    }

    public UserModel Login(String email, String password)
    {
        UserModel rv = null;
        ILocalData localData = GetLocalData();
        IAuthenticationServer  authenticationServer = GetAuthenticationServer();
        ResponseDTO<String> loginDto = authenticationServer.Login(email, password);

        return  GetUserModelFromResponse(loginDto);
    }


    public UserModel FacebookLogin(String facebookUserId, String accessToken)
    {
        UserModel rv = null;
        ILocalData localData = GetLocalData();
        IAuthenticationServer  authenticationServer = GetAuthenticationServer();
        ResponseDTO<String> loginDto = authenticationServer.FacebookLogin(facebookUserId, accessToken);

        return  GetUserModelFromResponse(loginDto);
    }

    private UserModel GetUserModelFromResponse(ResponseDTO<String> loginDto)
    {
        UserModel rv = null;
        if(isResponseSuccess(loginDto)) {

            ILocalData localData = GetLocalData();
            IAuthenticationServer  authenticationServer = GetAuthenticationServer(loginDto.getData());
            ResponseDTO<UserDTO> userDTOResponseDTO = authenticationServer.GetCurrentUser();

            if(isResponseSuccess(userDTOResponseDTO)) {

                //Server'dan alınan user
                UserModel userOnServer = new UserModel(userDTOResponseDTO.getData());
                userOnServer.setAccessKey(loginDto.getData());

                userOnServer.setUserID(localData.CreateOrUpdateUser(userOnServer));
                rv = userOnServer;

                localData.SetValidUser(rv.getUserID());
                localData.SetUserLastSyncDate(rv.getUserID(), new Date(1));
                localData.SetUnOwnedNotesToOwned(rv.getUserID());
            }
        }

        return rv;
    }

    public Boolean IsUserExists(String email)
    {
        Boolean rv = null;

        IAuthenticationServer authenticationServer = GetAuthenticationServer();
        ResponseDTO<Boolean> responseDTO = authenticationServer.IsExists(email);

        if(isResponseSuccess(responseDTO)) {
            rv = responseDTO.getData();
        }

        return rv;
    }

    public void LogoutUser()
    {
        UserModel currentUser = GetLocalCurrentUser();
        if(currentUser != null) {
            IAuthenticationServer authenticationServer = GetAuthenticationServer(currentUser.getAccessKey());
            ResponseDTO responseDTO = authenticationServer.Logout();

            if(isResponseSuccess(responseDTO)) {
                ILocalData localData = GetLocalData();
                localData.SetUnvalidAllUsers();
            }
        }
    }
}
