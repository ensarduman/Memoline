using System;
using System.Collections.Generic;
using System.Text;

namespace ensarduman.Utils
{
    public enum UserType
    {
        Admin = 1,
        User = 2
    }

    public enum ResponseStatus
    {
        Success = 1,
        Error = 2,
        LoginError = 3,
        UserIsNotValid = 4,
        AuthorizationError = 5,
        NotFoundError = 6,
        UserAlreadyExists = 7,
    }

    public enum EnumNoteType
    {
        Text = 1,
        Image = 2,
        Video = 3,
        GPS = 4
    }
}
