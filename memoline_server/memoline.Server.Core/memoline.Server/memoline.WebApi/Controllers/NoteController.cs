using System;
using memoline.DTO;
using memoline.Business;
using memoline.Server.Models;
using ensarduman.Utils;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;

namespace memoline.WebApi.Controllers
{
    [Route("api/[controller]/[Action]")]
    [ApiController]
    public class NoteController : MemolineControllerBase
    {
        public NoteController(IConfiguration configuration) : base(configuration)
        {
        }

        /// <summary>
        /// Not ekler
        /// </summary>
        /// <param name="dto"></param>
        /// <returns></returns>
        [HttpPost]
        public ResponseModel AddNote(NoteDTO dto)
        {
            ResponseModel rv;
            if (IsLoggedIn)
            {
                try
                {
                    NoteService noteService = new NoteService(this.ConnectionString, CurrentUser.UserID);
                    rv = SendResponse(noteService.AddNote(dto), ResponseStatus.Success);
                }
                catch (UnauthorizedAccessException)
                {
                    rv = SendResponse(null, ResponseStatus.AuthorizationError);
                }
                catch(Exception ee)
                {
                    rv = SendExceptionResponse(ee);
                }
            }
            else
            {
                rv = SendResponse(null, ResponseStatus.AuthorizationError);
            }

            return rv;
        }

        /// <summary>
        /// Notu siler
        /// </summary>
        /// <param name="noteUniqueId"></param>
        [HttpGet]
        public ResponseModel DeleteNote(string noteUniqueId)
        {
            ResponseModel rv;
            if (IsLoggedIn)
            {
                try
                {
                    NoteService noteService = new NoteService(this.ConnectionString, CurrentUser.UserID);
                    rv = SendResponse(noteService.DeleteNote(noteUniqueId), ResponseStatus.Success);
                }
                catch (UnauthorizedAccessException)
                {
                    rv = SendResponse(null, ResponseStatus.AuthorizationError);
                }
                catch (Exception ee)
                {
                    rv = SendExceptionResponse(ee);
                }
            }
            else
            {
                rv = SendResponse(null, ResponseStatus.AuthorizationError);
            }

            return rv;
        }

        /// <summary>
        /// Notu işaretler
        /// </summary>
        /// <param name="noteId"></param>
        [HttpGet]
        public ResponseModel CheckNote(string noteUniqueId, bool isChecked)
        {
            ResponseModel rv;
            if (IsLoggedIn)
            {
                try
                {
                    NoteService noteService = new NoteService(this.ConnectionString, CurrentUser.UserID);
                    rv = SendResponse(noteService.CheckNote(noteUniqueId, isChecked), ResponseStatus.Success);
                }
                catch (UnauthorizedAccessException)
                {
                    rv = SendResponse(null, ResponseStatus.AuthorizationError);
                }
                catch (Exception ee)
                {
                    rv = SendExceptionResponse(ee);
                }
            }
            else
            {
                rv = SendResponse(null, ResponseStatus.AuthorizationError);
            }

            return rv;
        }

        /// <summary>
        /// Son güncel notları alır
        /// </summary>
        /// <param name="lastupdatedate"></param>
        /// <returns></returns>
        [HttpGet]
        public ResponseModel GetUpdates(long lastupdatedate)
        {
            ResponseModel rv;

            if (IsLoggedIn)
            {
                try
                {
                    NoteService noteService = new NoteService(this.ConnectionString, CurrentUser.UserID);
                    rv = SendResponse(noteService.GetUpdates(lastupdatedate.ToDateTime()), ResponseStatus.Success);
                }
                catch (UnauthorizedAccessException)
                {
                    rv = SendResponse(null, ResponseStatus.AuthorizationError);
                }
                catch (Exception ee)
                {
                    rv = SendExceptionResponse(ee);
                }
            }
            else
            {
                rv = SendResponse(null, ResponseStatus.AuthorizationError);
            }

            return rv;
        }
    }
}