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

    public static final UriMatcher sUriMatcher = new UriMatcher(android.content.UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ContractMemoApp.AUTHORITY,ContractMemoApp.PATH_TABLE,ALL_ROW);
        sUriMatcher.addURI(ContractMemoApp.AUTHORITY,ContractMemoApp.PATH_TABLE + "/#",SELECT_ROW);
    }

    private HelperClass mHelper;
    private SQLiteDatabase mSQLite;

    @Override
    public boolean onCreate() {
        mHelper = new HelperClass(getContext());
        return true;
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri returnUri;
        mSQLite = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long id;
        switch (match){
            case ALL_ROW:
                id = mSQLite.insertOrThrow(ContractMemoApp.MemoAppContract.TABLE_NAME,null,contentValues);
                if (id  > 0){
                    returnUri = ContentUris.withAppendedId(ContractMemoApp.MemoAppContract.URI_CONTENT,id);
                }else {
                    throw new IllegalArgumentException("Errore" + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Error Insert " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri,null);
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
            case SELECT_ROW:
                String id = uri.getPathSegments().get(1);
                String mSel = ContractMemoApp.MemoAppContract._ID + "= ? ";
                String[] mSelectionArgs = new String[]{id};
                returned = mSQLite.update(ContractMemoApp.MemoAppContract.TABLE_NAME,
                        contentValues,
                        mSel,
                        mSelectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returned;
    }
}
