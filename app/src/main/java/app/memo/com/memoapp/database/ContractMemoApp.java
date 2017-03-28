package app.memo.com.memoapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.util.Arrays;

/**
 * Created by Msi-Locale on 27/03/2017.
 */

public class ContractMemoApp {

    public static final String AUTHORITY = "app.memo.com.memoapp";
    public static final String PATH_TABLE = "memoAppItems";
    public static final Uri URI_BASE = Uri.parse("content://"+ AUTHORITY);


    public ContractMemoApp() {
    }

    public static class MemoAppContract implements BaseColumns{

        public static final Uri URI_CONTENT = Uri.withAppendedPath(URI_BASE,PATH_TABLE);


        public static final String TABLE_NAME = "memo";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOTETXT = "note";

    }
}
