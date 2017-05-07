package app.memo.com.memoapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

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

        String create_dataBase = "CREATE TABLE IF NOT EXISTS " +
                ContractMemoApp.MemoAppContract.TABLE_NAME + "( " +
                ContractMemoApp.MemoAppContract.ID_NOTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractMemoApp.MemoAppContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContractMemoApp.MemoAppContract.COLUMN_NOTETXT + " TEXT NOT NULL, " +
                ContractMemoApp.MemoAppContract.COLUMN_DATE + " TEXT, " +
                ContractMemoApp.MemoAppContract.COLUMN_COLOR + " INTEGER, " +
                ContractMemoApp.MemoAppContract.COLUMN_RECORD_AUDIO + " TEXT, " +
                ContractMemoApp.MemoAppContract.COLUMN_NOTA_ID_ATTACH + " INTEGER, " +
                " FOREIGN KEY (" + ContractMemoApp.MemoAppContract.COlUMN_ATTACHMENT_ID + ") REFERENCES "
                + ContractMemoApp.MemoAppContract.TABLE_NAME_IMAGE + _ID +
                ")";
        sqLiteDatabase.execSQL(create_dataBase);


        String create_database_images = "CREATE TABLE IF NOT EXISTS " +
                ContractMemoApp.MemoAppContract.TABLE_NAME_IMAGE + "( " +
                ContractMemoApp.MemoAppContract.COlUMN_ATTACHMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractMemoApp.MemoAppContract.COLUMN_URI_IMAGE + " TEXT " +
                ")";
        sqLiteDatabase.execSQL(create_database_images);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractMemoApp.MemoAppContract.TABLE_NAME);
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractMemoApp.MemoAppContract.TABLE_NAME_IMAGE);
        onCreate(sqLiteDatabase);

    }
}
