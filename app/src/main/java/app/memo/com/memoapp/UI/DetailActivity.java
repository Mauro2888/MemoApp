package app.memo.com.memoapp.UI;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.memo.com.memoapp.Database.ContractMemoApp;
import app.memo.com.memoapp.Database.HelperClass;
import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.R;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public SQLiteDatabase mSQLdata;
    public boolean mTouched = false;
    public int colorValue;
    ImageView mImageViewAddImage;
    Uri mContentUri;
    ContentValues contentValues;
    CoordinatorLayout mCoordinatorLayout;
    ContentValues contentValuesFav;
    View.OnTouchListener mTouchedListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouched = true;
            return false;
        }
    };
    private FloatingActionButton mfavBtn;
    private PopupWindow popupWindowTools;
    private int mNoteLength;
    private int mTitleLength;
    private EditText mTitleEdit;
    private EditText mNoteEdit;
    private TextView mLastEdit;
    private HelperClass  mHelper;
    private boolean mTouchedColor = false;
    private CollapsingToolbarLayout mCollapsToolBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        setTitle("");

        mCollapsToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsToolbar);
        mCollapsToolBar.setTitle(getResources().getString(R.string.memoEdit));
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutTools);
        mfavBtn = (FloatingActionButton) findViewById(R.id.addFavouriteBtn);
        mTitleEdit = (EditText) findViewById(R.id.ins_title_detail);
        mTitleEdit.setTypeface(null, Typeface.BOLD);
        mNoteEdit = (EditText) findViewById(R.id.ins_nota_detail);
        mLastEdit = (TextView)findViewById(R.id.last_edit_txt);
        mTitleEdit.setTypeface(null, Typeface.BOLD);
        mImageViewAddImage = (ImageView) findViewById(R.id.imageViewAdd);

        Intent UriData = getIntent();
        mContentUri = UriData.getData();
        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getReadableDatabase();
        getSupportLoaderManager().initLoader(0,null,this);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleEdit.setOnTouchListener(mTouchedListener);
        mNoteEdit.setOnTouchListener(mTouchedListener);


        mNoteEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                LayoutInflater inflaterPopWindow = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View viewInfalter = inflaterPopWindow.inflate(R.layout.popwindow_tools, null);

                popupWindowTools = new PopupWindow(viewInfalter,
                        CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT,
                        CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT);

                popupWindowTools.showAtLocation(mfavBtn, Gravity.TOP | Gravity.LEFT, 10, 100);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                ImageButton mBtnAddImage = (ImageButton) viewInfalter.findViewById(R.id.btnAddImage);

                mBtnAddImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
                        addImage.setType("image/*");
                        startActivityForResult(Intent.createChooser(addImage, "pick image"), 12);
                    }
                });


                return false;
            }
        });


        mfavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHelper = new HelperClass(getApplicationContext());
                mSQLdata = mHelper.getWritableDatabase();
                contentValuesFav = new ContentValues();
                contentValuesFav.put(ContractMemoApp.MemoAppContract.COLUMN_FAV_TITLE, mTitleEdit.getText().toString());
                contentValuesFav.put(ContractMemoApp.MemoAppContract.COLUMN_FAV_NOTETXT, mNoteEdit.getText().toString());
                getContentResolver().insert(ContractMemoApp.MemoAppContract.URI_CONTENT_FAV, contentValuesFav);
                new MemoUtils().SnackBar(mCoordinatorLayout, R.string.addedFav);

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projector = {
                ContractMemoApp.MemoAppContract.COLUMN_TITLE,
                ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,
                ContractMemoApp.MemoAppContract.COLUMN_DATE,
                ContractMemoApp.MemoAppContract.COLUMN_COLOR,
                ContractMemoApp.MemoAppContract.COlUMN_IMAGE_URI
        };
        CursorLoader cursorLoader = new CursorLoader(this,mContentUri,projector,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() < 1){
            return;
        }

        if(data.moveToFirst()){
            int title = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_TITLE);
            int note = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT);
            int date = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_DATE);
            int color = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_COLOR);
            int imageUriGet = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COlUMN_IMAGE_URI);

            String titleTxt = data.getString(title);
            String noteTxt = data.getString(note);
            String dateTxt = data.getString(date);
            colorValue = data.getInt(color);
            String uriData = data.getString(imageUriGet);

            mTitleEdit.setText(titleTxt);
            mNoteEdit.setText(noteTxt);
            mLastEdit.setText(dateTxt);
            Log.d("TAG IMAGE", "" + uriData);
            Glide.with(DetailActivity.this).load(uriData).into(mImageViewAddImage);

            //retore color for entire activity
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorValue));
            mCollapsToolBar.setBackgroundColor(colorValue);
            getWindow().setStatusBarColor(colorValue);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editorManager = sharedPreferences.edit();
            editorManager.putString("title", titleTxt);
            editorManager.putString("note", noteTxt);
            editorManager.putInt("color", colorValue);
            editorManager.commit();

            mNoteLength = mNoteEdit.getText().toString().trim().length();
            mTitleLength = mTitleEdit.getText().toString().trim().length();
            Log.d("TAG", "length " + mNoteLength);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ins_note_menu:
                InsertNote();
                break;
            case R.id.delete_note_menu:
                DeleteNote();
                break;
            case R.id.shareNote:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mNoteEdit.getText().toString());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share note with..."));
                break;
            case android.R.id.home:
                if (mTouched) {
                    DiscartAlert();
                } else {
                    onBackPressed();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mTouched || mNoteEdit.length() > mNoteLength || mTitleEdit.length() > mTitleLength) {
            DiscartAlert();
        } else {
            finish();
        }
    }

    protected void DeleteNote() {
        final AlertDialog.Builder alertDelete = new AlertDialog.Builder(this, R.style.CustomAlert);
        alertDelete.setMessage(R.string.delete_note_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.delete_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getContentResolver().delete(mContentUri, null, null);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing
                    }
                }).create().show();

    }

    protected void DiscartAlert() {
        final AlertDialog.Builder alertDiscard = new AlertDialog.Builder(this);
        alertDiscard.setMessage(R.string.alert_discard_message)
                .setCancelable(false)
                .setNeutralButton(R.string.discard_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailActivity.this, R.string.discard_btn, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.save_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //SALVO
                InsertNote();
                Toast.makeText(DetailActivity.this, R.string.save_btn, Toast.LENGTH_SHORT).show();
                finish();

            }
        }).create().show();
    }

    public void InsertNote(){
        String TitleControl = mTitleEdit.getText().toString();
        String NoteControl = mNoteEdit.getText().toString();
        if (TitleControl.isEmpty() && NoteControl.isEmpty()){
            Toast.makeText(this, "Please Insert a Note", Toast.LENGTH_SHORT).show();
        }else{
            contentValues = new ContentValues();
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_TITLE,mTitleEdit.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,mNoteEdit.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_DATE, new MemoUtils().GetDate());
            contentValues.put(ContractMemoApp.MemoAppContract.COlUMN_IMAGE_URI, new MemoUtils().PreferenceRestoreUriImage(DetailActivity.this, "UriImageSave"));

            if (!mTouchedColor){
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,colorValue);
            }else {
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
            }
            getContentResolver().update(mContentUri,contentValues,null,null);
            popupWindowTools.dismiss();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12 && resultCode == RESULT_OK) {
            Uri uriImage = data.getData();
            new MemoUtils().PreferenceSaveImageUri(DetailActivity.this, "UriImageSave", uriImage.toString());
            Glide.with(DetailActivity.this).load(uriImage).fitCenter().into(mImageViewAddImage);
            popupWindowTools.dismiss();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
