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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
    View.OnTouchListener mTouchedListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouched = true;
            return false;
        }
    };
    private FloatingActionButton mfavBtn;
    private Toolbar bottomTools;
    private int mNoteLength;
    private int mTitleLength;
    private EditText mTitleEdit;
    private EditText mNoteEdit;
    private TextView mLastEdit;
    private HelperClass  mHelper;
    private CollapsingToolbarLayout mCollapsToolBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        setTitle("Edit Memo");

        mCollapsToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsToolbar);
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
        mfavBtn.setOnTouchListener(mTouchedListener);

        mfavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View viewColor = inflater.inflate(R.layout.popwindow_color, null);


                final PopupWindow popWindowColor = new PopupWindow(viewColor,
                        CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT,
                        CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT);

                popWindowColor.setFocusable(true);
                popWindowColor.setOutsideTouchable(true);
                popWindowColor.setEnterTransition(new AutoTransition());
                popWindowColor.showAtLocation(mfavBtn, Gravity.CENTER_HORIZONTAL, 0, 0);

                ImageButton mBtnBlue = (ImageButton) viewColor.findViewById(R.id.btnBlue);
                ImageButton mBtnRed = (ImageButton) viewColor.findViewById(R.id.btnRed);
                ImageButton mBtnGreen = (ImageButton) viewColor.findViewById(R.id.btnGreen);
                ImageButton mBtOrange = (ImageButton) viewColor.findViewById(R.id.btnOrange);
                ImageButton mBtnPink = (ImageButton) viewColor.findViewById(R.id.btnPink);
                ImageButton mBtnGray = (ImageButton) viewColor.findViewById(R.id.btnGray);


                mBtnGray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialGray));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });


                mBtOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialOrange));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });

                mBtnPink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialPink));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });


                mBtnGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialGreen));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });

                mBtnRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialRed));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();

                    }
                });
                mBtnBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialBlue));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });
            }
        });
//                mHelper = new HelperClass(getApplicationContext());
//                mSQLdata = mHelper.getWritableDatabase();
//                contentValuesFav = new ContentValues();
//                contentValuesFav.put(ContractMemoApp.MemoAppContract.COLUMN_FAV_TITLE, mTitleEdit.getText().toString());
//                contentValuesFav.put(ContractMemoApp.MemoAppContract.COLUMN_FAV_NOTETXT, mNoteEdit.getText().toString());
//                getContentResolver().insert(ContractMemoApp.MemoAppContract.URI_CONTENT_FAV, contentValuesFav);
//                new MemoUtils().SnackBar(mCoordinatorLayout, R.string.addedFav);
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

            //retore color for entire activity
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorValue));
            mCollapsToolBar.setBackgroundColor(colorValue);
            getWindow().setStatusBarColor(colorValue);

            mTitleEdit.setText(titleTxt);
            mNoteEdit.setText(noteTxt);
            mLastEdit.setText(dateTxt);
            Log.d("TAG IMAGE", "" + uriData);

            Glide.with(DetailActivity.this).load(uriData).into(mImageViewAddImage);


            //get data for widget
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editorManager = sharedPreferences.edit();
            editorManager.putString("title", titleTxt);
            editorManager.putString("note", noteTxt);
            editorManager.putInt("color", colorValue);
            editorManager.commit();


            mNoteLength = mNoteEdit.getText().length();
            mTitleLength = mTitleEdit.getText().length();
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
            case R.id.btnAddImage:
                Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
                addImage.setType("image/*");
                startActivityForResult(Intent.createChooser(addImage, "Select Image"), 12);
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

            if (!mTouched) {
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,colorValue);
                contentValues.put(ContractMemoApp.MemoAppContract.COlUMN_IMAGE_URI, new MemoUtils().PreferenceRestoreUriImage(DetailActivity.this, "UriImageSave"));
            }else {
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
            }
            getContentResolver().update(mContentUri,contentValues,null,null);
            finish();
        }
    }

    private void startToolsbarEdit() {
        bottomTools = (Toolbar) findViewById(R.id.toolbar_bottom);
        bottomTools.setBackgroundColor(colorValue);
        bottomTools.inflateMenu(R.menu.tools_menu);
        bottomTools.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12 && resultCode == RESULT_OK) {
            Uri uriImage = data.getData();
            if (uriImage != null) {
                new MemoUtils().PreferenceSaveImageUri(DetailActivity.this, "UriImageSave", uriImage.toString());
                mImageViewAddImage.setVisibility(View.VISIBLE);
                Glide.with(DetailActivity.this).load(uriImage).fitCenter().into(mImageViewAddImage);
            } else return;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
