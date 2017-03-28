package app.memo.com.memoapp;

import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Msi-Locale on 26/03/2017.
 */

public class ModelMemo implements Serializable {

    String TitleMemo;
    String NoteMemo;

    public ModelMemo(String titleMemo, String noteMemo) {
        TitleMemo = titleMemo;
        NoteMemo = noteMemo;
    }

    public String getTitleMemo() {
        return TitleMemo;
    }

    public void setTitleMemo(String titleMemo) {
        TitleMemo = titleMemo;
    }

    public String getNoteMemo() {
        return NoteMemo;
    }

    public void setNoteMemo(String noteMemo) {
        NoteMemo = noteMemo;
    }
}
