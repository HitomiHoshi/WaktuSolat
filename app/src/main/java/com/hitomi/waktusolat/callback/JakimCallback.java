package com.hitomi.waktusolat.callback;

import com.hitomi.waktusolat.data.Message;
import com.hitomi.waktusolat.data.WaktuSolatListData;

import java.text.ParseException;

public interface JakimCallback {
    void onSuccess (WaktuSolatListData result) throws ParseException;
    void onError (Message result);
}
