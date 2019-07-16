package com.ensarduman.memoline.Data.ServerData.Interfaces;

import com.ensarduman.memoline.DTO.ResponseDTO;
import com.ensarduman.memoline.DTO.UserDTO;

/**
 * Created by duman on 08/03/2018.
 */

public interface IAuthenticationServer {
    ResponseDTO<UserDTO> GetCurrentUser();
    ResponseDTO<UserDTO> NewUser(UserDTO user, String password);
    ResponseDTO UpdateUser(UserDTO user);
    ResponseDTO<String> Login(String email, String password);
    ResponseDTO<String> FacebookLogin(String facebookUserId, String accessToken);
    ResponseDTO Logout();
    ResponseDTO<Boolean> IsExists(String email);
    ResponseDTO SendValidationEmail();
    ResponseDTO ValidateEmail(String validationcode);
}
