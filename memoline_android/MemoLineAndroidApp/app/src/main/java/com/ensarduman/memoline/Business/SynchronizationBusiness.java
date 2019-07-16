package com.ensarduman.memoline.Business;


import android.content.Context;

import com.ensarduman.memoline.DTO.NoteDTO;
import com.ensarduman.memoline.DTO.ResponseDTO;
import com.ensarduman.memoline.Data.LocalData.ILocalData;
import com.ensarduman.memoline.Data.LocalData.LocalDataFactory;
import com.ensarduman.memoline.Data.ServerData.Interfaces.IAuthenticationServer;
import com.ensarduman.memoline.Data.ServerData.Interfaces.INoteServer;
import com.ensarduman.memoline.Data.ServerData.ServerFactory;
import com.ensarduman.memoline.Model.NoteModel;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.Util.DateHelper;
import com.ensarduman.memoline.Util.EnumLocalDataType;
import com.ensarduman.memoline.Util.EnumResponseStatus;
import com.ensarduman.memoline.Util.EnumServerDataType;
import com.ensarduman.memoline.Util.GuidHelper;

import java.util.Date;

/**
 * Created by duman on 12/02/2018.
 */

public class SynchronizationBusiness extends BusinessBase{

    UserModel currentUser = null;
    Context context;
    EnumServerDataType serverDataType = EnumServerDataType.WebApi;

    public SynchronizationBusiness(Context context) {
        this.context = context;

        AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(context);
        currentUser = authenticationBusiness.GetLocalCurrentUser();
    }

    private IAuthenticationServer GetAuthenticationServer(){
        String accessKey = null;
        if(currentUser != null)
        {
            accessKey = currentUser.getAccessKey();
        }

        return new ServerFactory(context).GetAuthenticationServer(serverDataType, accessKey);
    }

    private INoteServer GetNoteServer(){
        String accessKey = null;
        if(currentUser != null)
        {
            accessKey = currentUser.getAccessKey();
        }

        return new ServerFactory(context).GetNoteServer(serverDataType, accessKey);
    }

    private ILocalData GetLocalData()
    {
        LocalDataFactory localData = new LocalDataFactory(this.context);
        return localData.GetDao(EnumLocalDataType.SQLite);
    }

    public boolean SendNote(NoteModel note){
        boolean rv = false;
        if(currentUser != null) {
            if (note.getNoteUniqueID() == "" || note.getNoteUniqueID() == null) {
                note.setNoteUniqueID(GuidHelper.GetRandomGuid());
            }

            NoteDTO dto = new NoteDTO(note);
            INoteServer noteServer = GetNoteServer();
            ResponseDTO responseDTO = noteServer.AddNote(dto);

            if(isResponseSuccess(responseDTO)) {
                ILocalData iLocalData = GetLocalData();
                iLocalData.SetOnServer(note.getNoteID(), true);
                iLocalData.SetNoteUniqueID(note.getNoteID(), note.getNoteUniqueID());
                rv = true;
            }
        }

        return rv;
    }

    /**
     * Sunucudan notları alır, veritabanına yazar
     * Yeni oluşturulmuş notları döner
     * */
    public boolean GetUpdates(){
        boolean returnValue = false;

        if(currentUser != null) {

            INoteServer noteServer = GetNoteServer();
            ILocalData localData = GetLocalData();

            /*
            * Burada NoteBusiness üzerinden lokale kaydetmeyi yaptım
            * çünkü not kaydedildikten sonra hashtag kaydetme konusu için önemli
            * bir algoritma orada mevcut onun tekrar yazılması doğru olmazdı
            * */
            NoteBusiness noteBusiness = new NoteBusiness(context);

            try {
                //Şu anki tarih alınıyor. update başarılı olursa kullanıcıya atanacak
                Date newLastSyncDate = DateHelper.GetUTCNow();

                long lastSyncDate = 1;
                if(currentUser != null)
                {
                    lastSyncDate = currentUser.getLastSyncDate().getTime();
                }

                ResponseDTO<NoteDTO[]> responseDTO = noteServer.GetUpdates(lastSyncDate);

                if(isResponseSuccess(responseDTO)) {

                    NoteDTO[] notes = responseDTO.getData();

                    NoteModel currentNoteModel;
                    if (notes != null) {
                        for (int i = 0; i < responseDTO.getData().length; i++) {
                            currentNoteModel = new NoteModel(notes[i]);
                            currentNoteModel.setOnServer(true);
                            currentNoteModel.setNoteID(noteBusiness.SaveNote(currentNoteModel));
                        }
                    }

                    returnValue = notes.length > 0;

                    //son güncelleme tarihi güncelleniyor
                    localData.SetUserLastSyncDate(currentUser.getUserID(), newLastSyncDate);
                }
            }
            finally {

            }
        }

        return returnValue;
    }

    public boolean CheckNote(int noteID, boolean isChecked){
        boolean rv = false;
        if(currentUser != null) {
            INoteServer noteServer = GetNoteServer();
            ILocalData iLocalData = GetLocalData();

            NoteModel noteModel = iLocalData.GetNote(noteID);

            ResponseDTO responseDTO = noteServer.CheckNote(noteModel.getNoteUniqueID(), isChecked);

            if(isResponseSuccess(responseDTO)) {
                iLocalData.SetCheckedOnServer(noteID, isChecked);
                rv = true;
            }
        }
        return rv;
    }

    public boolean DeleteNote(int noteID){
        boolean rv = false;
        if(currentUser != null) {
            INoteServer noteServer = GetNoteServer();
            ILocalData iLocalData = GetLocalData();

            NoteModel noteModel = iLocalData.GetNote(noteID);

            ResponseDTO responseDTO = noteServer.DeleteNote(noteModel.getNoteUniqueID());

            if(isResponseSuccess(responseDTO)) {
                iLocalData.SetDeletedOnServer(noteID, true);
                rv = true;
            }
        }

        return rv;
    }
}
