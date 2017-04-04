package app.memo.com.memoapp.MemoUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.PreferenceManager;

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

    public void ColorPicker(final Context context){
        GetRandomMaterialColor(context,"A100");

    }
    public void PreferenceSave(Context context, String key,int value){
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(key,value);
        editor.commit();
    }
    public int PreferenceRestore(Context context, String key, int value){
        SharedPreferences restore = PreferenceManager.getDefaultSharedPreferences(context);
        int resoreInt = restore.getInt(key,value);
        return resoreInt;
    }

    public int random(){
        Random random = new Random();
        int colorRandom = Color.argb(255, random.nextInt(256),random.nextInt(256),random.nextInt(256));
        return colorRandom;
    }

    public int GetRandomMaterialColor(Context context, String colorType){
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor_"+ colorType, "array",context.getPackageName());

        if (arrayId != 0){
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int)(Math.random() * colors.length());
            returnColor = colors.getColor(index,Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

}
