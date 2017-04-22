package app.memo.com.memoapp.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Msi-Locale on 27/03/2017.
 */

public class ContractMemoApp {

    public static final String AUTHORITY = "app.memo.com.memoapp";
    public static final String PATH_TABLE = "memoAppItems";
    public static final String PATH_TABLE_PREFERENCES = "memoAppFavourite";
    public static final Uri URI_BASE = Uri.parse("content://"+ AUTHORITY);


    public ContractMemoApp() {
    }

    public static class MemoAppContract implements BaseColumns{

        public static final Uri URI_CONTENT = Uri.withAppendedPath(URI_BASE,PATH_TABLE);
        public static final Uri URI_CONTENT_FAV = Uri.withAppendedPath(URI_BASE, PATH_TABLE_PREFERENCES);

        public static final String TABLE_NAME = "memo";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOTETXT = "note";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_COLOR = "color";
        public static final String COlUMN_IMAGE_URI = "imageuri";

        public static final String TABLE_NAME_FAV = "memofav";
        public static final String COLUMN_FAV_TITLE = "titlefav";
        public static final String COLUMN_FAV_NOTETXT = "notefav";
        public static final String COLUMN_FAV_DATE = "datefav";
        public static final String COLUMN_FAV_COLOR = "colorfav";

    }
}
