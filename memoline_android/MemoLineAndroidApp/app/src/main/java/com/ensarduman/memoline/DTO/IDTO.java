package com.ensarduman.memoline.DTO;

/**
 * Created by duman on 08/03/2018.
 */

public interface IDTO<T> {
    T fromJSON(String json);
    String toJSON();
}
