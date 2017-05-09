package app.memo.com.memoapp.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Msi-Locale on 27/03/2017.
 */

public class ContractMemoApp {

    public static final String AUTHORITY = "app.memo.com.memoapp";
    public static final String PATH_TABLE = "memoAppItems";
    public static final String PATH_TABLE_IMAGE = "memoAppImages";
    public static final String PATH_TABLE_FAV = "memoAppFav";
    public static final Uri URI_BASE = Uri.parse("content://"+ AUTHORITY);


    public ContractMemoApp() {
    }

    public static class MemoAppContract implements BaseColumns{

        public static final Uri URI_CONTENT = Uri.withAppendedPath(URI_BASE,PATH_TABLE);
        public static final Uri URI_CONTENT_FAV = Uri.withAppendedPath(URI_BASE, PATH_TABLE_FAV);
        public static final Uri URI_CONTENT_IMAGE = Uri.withAppendedPath(URI_BASE, PATH_TABLE_IMAGE);

        public static final String TABLE_NAME = "memo";
        public static final String ID_NOTE = _ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOTETXT = "note";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_RECORD_AUDIO = "record";
        public static final String COLUMN_IMAGES = "images_uri";
        public static final String COlUMN_ID_NOTE = "id_note";

        public static final String TABLE_NAME_FAV = "memofav";
        public static final String COLUMN_FAV_TITLE = "titlefav";
        public static final String COLUMN_FAV_NOTETXT = "notefav";
        public static final String COLUMN_FAV_DATE = "datefav";
        public static final String COLUMN_FAV_COLOR = "colorfav";
        public static final String COlUMN_FAV_IMAGE_URI = "imageurifav";


        public static final String TABLE_NAME_IMAGE = "memoimage";
        public static final String COlUMN_ATTACHMENT_ID = _ID;
        public static final String COLUMN_URI_IMAGE = "urimage";


    }
}
