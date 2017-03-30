package app.memo.com.memoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Msi-Locale on 27/03/2017.
 */

public class HelperClass extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "memoapp.db";
    public static int DATABASE_VERSION = 1;


    public HelperClass(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_dataBase = "CREATE TABLE " +
                ContractMemoApp.MemoAppContract.TABLE_NAME + "( " +
                ContractMemoApp.MemoAppContract._ID + " INTEGER PRIMARY KEY, " +
                ContractMemoApp.MemoAppContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContractMemoApp.MemoAppContract.COLUMN_NOTETXT + " TEXT NOT NULL, " +
                ContractMemoApp.MemoAppContract.COLUMN_DATE + " TEXT, " +
                ContractMemoApp.MemoAppContract.COLUMN_COLOR + " INTEGER " +
                ")";
        sqLiteDatabase.execSQL(create_dataBase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractMemoApp.MemoAppContract.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
