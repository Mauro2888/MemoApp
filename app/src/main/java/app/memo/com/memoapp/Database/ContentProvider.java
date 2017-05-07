package app.memo.com.memoapp.Database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Msi-Locale on 27/03/2017.
 */

public class ContentProvider extends android.content.ContentProvider {

    public static final int ALL_ROW = 100;
    public static  final int SELECT_ROW = 101;

    public static final int ALL_ROW_ATTACH = 200;
    public static final int SELECT_ROW_ATTACH = 201;

    public static final int ALL_ROW_FAV = 300;
    public static final int SELECT_ROW_FAV = 301;
    public static final int ROW_NOTE_AND_ID = 302;



    public static final UriMatcher sUriMatcher = new UriMatcher(android.content.UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ContractMemoApp.AUTHORITY,ContractMemoApp.PATH_TABLE,ALL_ROW);
        sUriMatcher.addURI(ContractMemoApp.AUTHORITY,ContractMemoApp.PATH_TABLE + "/#",SELECT_ROW);

        sUriMatcher.addURI(ContractMemoApp.AUTHORITY, ContractMemoApp.PATH_TABLE_IMAGE, ALL_ROW_ATTACH);
        sUriMatcher.addURI(ContractMemoApp.AUTHORITY, ContractMemoApp.PATH_TABLE_IMAGE + "/#", SELECT_ROW_ATTACH);

        sUriMatcher.addURI(ContractMemoApp.AUTHORITY, ContractMemoApp.PATH_TABLE_FAV, ALL_ROW_FAV);
        sUriMatcher.addURI(ContractMemoApp.AUTHORITY, ContractMemoApp.PATH_TABLE_FAV + "/#", SELECT_ROW_FAV);

    }



    private HelperClass mHelper;
    private SQLiteDatabase mSQLite;

    @Override
    public boolean onCreate() {
        mHelper = new HelperClass(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        mSQLite = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SELECT_ROW:
                mSQLite.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = mSQLite.insert(ContractMemoApp.MemoAppContract.TABLE_NAME, null, value);
                        if (_id != -1) {
                            count++;
                        }
                    }
                    mSQLite.setTransactionSuccessful();
                } finally {
                    mSQLite.close();
                    mSQLite.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri returnUri;
        mSQLite = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){
            case ALL_ROW:
                long id = mSQLite.insertOrThrow(ContractMemoApp.MemoAppContract.TABLE_NAME, null, contentValues);
                if (id  > 0){
                    returnUri = ContentUris.withAppendedId(ContractMemoApp.MemoAppContract.URI_CONTENT,id);
                }else {
                    throw new IllegalArgumentException("Errore" + uri);
                }
                break;
            case ALL_ROW_ATTACH:
                long id2 = mSQLite.insertOrThrow(ContractMemoApp.MemoAppContract.TABLE_NAME_IMAGE, null, contentValues);
                if (id2 > 0) {
                    returnUri = ContentUris.withAppendedId(ContractMemoApp.MemoAppContract.URI_CONTENT_IMAGE, id2);
                } else {
                    throw new IllegalArgumentException("Error add Fav" + uri);
                }
                break;
            case ALL_ROW_FAV:
                long id3 = mSQLite.insertWithOnConflict(ContractMemoApp.MemoAppContract.TABLE_NAME_FAV, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id3 > 0) {
                    returnUri = ContentUris.withAppendedId(ContractMemoApp.MemoAppContract.URI_CONTENT_FAV, id3);
                } else {
                    throw new IllegalArgumentException("Error add Fav" + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Error Insert " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        mSQLite.close();
        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projector, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrds) {

        mSQLite = mHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){
            case ALL_ROW:
                cursor = mSQLite.query(
                    ContractMemoApp.MemoAppContract.TABLE_NAME,
                    projector,
                    selection,
                    selectionArgs,
                    null,
                    null,
                        sortOrds);
            break;
            case ALL_ROW_ATTACH:
                cursor = mSQLite.query(
                        ContractMemoApp.MemoAppContract.TABLE_NAME_IMAGE,
                        projector,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrds);
                break;
            case ALL_ROW_FAV:
                cursor = mSQLite.query(
                        ContractMemoApp.MemoAppContract.TABLE_NAME_FAV,
                        projector,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrds);
                break;
            case SELECT_ROW:
                selection = ContractMemoApp.MemoAppContract._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mSQLite.query(
                        ContractMemoApp.MemoAppContract.TABLE_NAME,
                        projector,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrds);
                break;
            case SELECT_ROW_FAV:
                selection = ContractMemoApp.MemoAppContract._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mSQLite.query(
                        ContractMemoApp.MemoAppContract.TABLE_NAME_FAV,
                        projector,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrds);
                break;

            case SELECT_ROW_ATTACH:
                selection = ContractMemoApp.MemoAppContract._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mSQLite.query(
                        ContractMemoApp.MemoAppContract.TABLE_NAME_IMAGE,
                        projector,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrds);
                break;
            default:
                throw new IllegalArgumentException("Errore query" + uri);


        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        mSQLite = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int row;
        switch (match){
            case SELECT_ROW:
                String id = uri.getPathSegments().get(1);
                String mSel = ContractMemoApp.MemoAppContract._ID + "= ? ";
                String[] mSelectionArgs = new String[]{id};
                row = mSQLite.delete(ContractMemoApp.MemoAppContract.TABLE_NAME,mSel,mSelectionArgs);
                break;
            case SELECT_ROW_FAV:
                String id_fav = uri.getPathSegments().get(1);
                selection = ContractMemoApp.MemoAppContract._ID + "= ? ";
                selectionArgs = new String[]{id_fav};
                row = mSQLite.delete(ContractMemoApp.MemoAppContract.TABLE_NAME_FAV, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("errore delete" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return row;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int returned = 0;
        int match = sUriMatcher.match(uri);
        switch (match){
            case ALL_ROW:
                returned = mSQLite.update(
                        ContractMemoApp.MemoAppContract.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case ALL_ROW_FAV:
                returned = mSQLite.update(
                        ContractMemoApp.MemoAppContract.TABLE_NAME_FAV,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case SELECT_ROW:
                String id = uri.getPathSegments().get(1);
                String mSel = ContractMemoApp.MemoAppContract._ID + "= ? ";
                String[] mSelectionArgs = new String[]{id};
                returned = mSQLite.update(ContractMemoApp.MemoAppContract.TABLE_NAME,
                        contentValues,
                        mSel,
                        mSelectionArgs);
                break;
            case SELECT_ROW_FAV:
                String id_fav = uri.getPathSegments().get(1);
                String mSel_fav = ContractMemoApp.MemoAppContract._ID + "= ? ";
                String[] mSelectionArgs_fav = new String[]{id_fav};
                returned = mSQLite.update(ContractMemoApp.MemoAppContract.TABLE_NAME_FAV,
                        contentValues,
                        mSel_fav,
                        mSelectionArgs_fav);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returned;
    }
}
