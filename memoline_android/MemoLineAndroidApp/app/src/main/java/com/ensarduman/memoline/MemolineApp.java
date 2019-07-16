package com.ensarduman.memoline;

import android.app.Application;
import android.content.Context;

/**
 * Created by duman on 17/02/2018.
 */

public class MemolineApp extends Application {
    public static MemolineApp GetInstance(Context applicationContext) {
        return (MemolineApp)applicationContext;
    }

    int KeyboardHeight;

    public int getKeyboardHeight() {
        return KeyboardHeight;
    }

    public void setKeyboardHeight(int keyboardHeight) {
        KeyboardHeight = keyboardHeight;
    }
}
