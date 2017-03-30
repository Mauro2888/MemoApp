package app.memo.com.memoapp.MemoUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import app.memo.com.memoapp.InsertNoteActivity;
import app.memo.com.memoapp.R;


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

        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(0xffffffff)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {
                    }
                })
            .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] selectedColors) {
                        PreferenceSave(context,"colorSaved",selectedColor);
                    }
                })
            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
                .build()
                .show();
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

}
