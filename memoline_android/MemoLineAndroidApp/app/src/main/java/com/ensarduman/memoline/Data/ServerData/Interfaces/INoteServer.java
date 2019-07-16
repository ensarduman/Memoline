package com.ensarduman.memoline.Data.ServerData.Interfaces;

import com.ensarduman.memoline.DTO.NoteDTO;
import com.ensarduman.memoline.DTO.ResponseDTO;

/**
 * Created by duman on 08/03/2018.
 */

public interface INoteServer {
    ResponseDTO AddNote(NoteDTO dto);
    ResponseDTO DeleteNote(String noteUniqueId);
    ResponseDTO CheckNote(String noteUniqueId, boolean isChecked);
    ResponseDTO GetUpdates(long lastupdatedate);
}
