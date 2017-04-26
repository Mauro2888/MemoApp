package app.memo.com.memoapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Msi-Locale on 27/03/2017.
 */

public class HelperClass extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "memoapp.db";
    public static int DATABASE_VERSION = 2;


    public HelperClass(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_dataBase = "CREATE TABLE IF NOT EXISTS " +
                ContractMemoApp.MemoAppContract.TABLE_NAME + "( " +
                ContractMemoApp.MemoAppContract._ID + " INTEGER PRIMARY KEY, " +
                ContractMemoApp.MemoAppContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContractMemoApp.MemoAppContract.COLUMN_NOTETXT + " TEXT NOT NULL, " +
                ContractMemoApp.MemoAppContract.COLUMN_DATE + " TEXT, " +
                ContractMemoApp.MemoAppContract.COLUMN_COLOR + " INTEGER, " +
                ContractMemoApp.MemoAppContract.COlUMN_IMAGE_URI + " TEXT, " +
                ContractMemoApp.MemoAppContract.COLUMN_RECORD_AUDIO + " TEXT " +
                ")";
        sqLiteDatabase.execSQL(create_dataBase);


        String create_database_favourite = "CREATE TABLE IF NOT EXISTS " +
                ContractMemoApp.MemoAppContract.TABLE_NAME_FAV + "( " +
                ContractMemoApp.MemoAppContract._ID + " INTEGER PRIMARY KEY, " +
                ContractMemoApp.MemoAppContract.COLUMN_FAV_TITLE + " TEXT UNIQUE, " +
                ContractMemoApp.MemoAppContract.COLUMN_FAV_NOTETXT + " TEXT NOT NULL, " +
                ContractMemoApp.MemoAppContract.COLUMN_FAV_DATE + " TEXT, " +
                ContractMemoApp.MemoAppContract.COLUMN_FAV_COLOR + " INTEGER, " +
                ContractMemoApp.MemoAppContract.COlUMN_FAV_IMAGE_URI + " TEXT " +
                ")";
        sqLiteDatabase.execSQL(create_database_favourite);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractMemoApp.MemoAppContract.TABLE_NAME);
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractMemoApp.MemoAppContract.TABLE_NAME_FAV);
        onCreate(sqLiteDatabase);

    }
}
