package app.memo.com.memoapp.MemoUtils;

import android.graphics.Color;

import com.bumptech.glide.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Msi-Locale on 29/03/2017.
 */

public class MemoUtils {

    public MemoUtils() {

    }
    public String GetDate(){
        DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy , HH:mm");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date;
    }

}
