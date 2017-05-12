package app.memo.com.memoapp.UI;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.memo.com.memoapp.Database.ContractMemoApp;
import app.memo.com.memoapp.Database.HelperClass;
import app.memo.com.memoapp.R;

public class FullScreenImageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ImageView mFullScreenImage;
    private Uri getUri;
    public SQLiteDatabase mSQLdata;
    private HelperClass  mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        mFullScreenImage = (ImageView) findViewById(R.id.imageFullScreen);


        getUri = getIntent().getData();
        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getReadableDatabase();
        fullScreen();

        getSupportLoaderManager().initLoader(1023,null,this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String [] projector = {
                ContractMemoApp.MemoAppContract.COLUMN_IMAGES
//                ContractMemoApp.MemoAppContract.COLUMN_RECORD_AUDIO
        };

        CursorLoader cursorLoader = new CursorLoader(this,getUri,projector,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() < 1){
            return;
        }

        if(data.moveToFirst()) {
            int images = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_IMAGES);

            String UriImage = data.getString(images);
            Toast.makeText(this, "" + UriImage, Toast.LENGTH_SHORT).show();
            Glide.with(FullScreenImageActivity.this).load(UriImage).into(mFullScreenImage);
            Log.d("URIAGE","" + UriImage);
            mSQLdata.close();
        }


    }

    @Override
    protected void onPause() {
        getSupportLoaderManager().restartLoader(1023,null,this);
        super.onPause();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void fullScreen() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("TAG", "Turning immersive mode mode off. ");
        } else {
            Log.i("TAG", "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }

}
