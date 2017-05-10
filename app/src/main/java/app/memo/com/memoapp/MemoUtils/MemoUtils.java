package app.memo.com.memoapp.MemoUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

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
        DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date;
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


    public void PreferenceSaveImageUri(Context context, String key, String value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void RemovePreferenceSharedString(Context context, String key) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(key);
        editor.commit();
    }


    public String PreferenceRestoreUriImage(Context context, String key) {
        SharedPreferences restore = PreferenceManager.getDefaultSharedPreferences(context);
        String resoreInt = restore.getString(key, null);
        return resoreInt;
    }

    public void PreferenceSaveOrder(Context context, String key, String value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String PreferenceRestoreOrder(Context context, String key) {
        SharedPreferences restore = PreferenceManager.getDefaultSharedPreferences(context);
        String resoreInt = restore.getString(key, null);
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

    public void SnackBar(CoordinatorLayout coordinatorLayout, int charSequence) {
        Snackbar mSnackBarSave = Snackbar.make(coordinatorLayout, charSequence, Snackbar.LENGTH_LONG);
        View viewSnack = mSnackBarSave.getView();
        viewSnack.setBackgroundColor(Color.DKGRAY);
        mSnackBarSave.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }).show();
    }

    public void PreferenceSaveRecord(Context context, String key, String value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String PreferenceRestoreRecord(Context context, String key, String value) {
        SharedPreferences restore = PreferenceManager.getDefaultSharedPreferences(context);
        String resoreInt = restore.getString(key, value);
        return resoreInt;
    }
}
