package app.memo.com.memoapp.UI;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;

import app.memo.com.memoapp.Database.ContractMemoApp;
import app.memo.com.memoapp.Database.HelperClass;
import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.R;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String DEFAULT_STORAGE_LOCATION = "/sdcard/MemoAudioRecording";
    private static final int REQUEST_PERMISSION_ID = 201;
    private static final int LOADER_ID_DETAIL = 5043;
    public SQLiteDatabase mSQLdata;
    public boolean mTouched = false;
    Uri mContentUri;
    String dateTxt;
    int IDPosition;
    ContentValues contentValues;
    CoordinatorLayout mCoordinatorLayout;
    //variable record
    View.OnTouchListener mTouchedListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouched = true;
            return false;
        }
    };
    String[] permission = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
    private FloatingActionButton mChangeColorBtn;
    private LinearLayout mLinear;
    private Toolbar bottomTools;
    private int mNoteLength;
    private ImageButton mBtnDeleteImage;
    private int mTitleLength;
    private EditText mTitleEdit;
    private EditText mNoteEdit;
    private TextView mLastEdit;
    private HelperClass  mHelper;
    private ImageView mImageAdded;
    private CollapsingToolbarLayout mCollapsToolBar;
    private int colorValue;
    //Audio var
    private File fileAudio = null;
    private MediaRecorder mMediaRecord;
    private String uriREC;
    //getPermission
    private boolean requestRecordAudioPermission = false;
    private boolean requestWriteExternalStorage = false;
    private boolean requestPhotoCapture = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        setTitle("");

        mBtnDeleteImage = (ImageButton) findViewById(R.id.deleteImageBtn);
        mImageAdded = (ImageView) findViewById(R.id.imageViewAdd);
        mLinear = (LinearLayout) findViewById(R.id.linearLayout_editBox);
        mCollapsToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsToolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutTools);
        mChangeColorBtn = (FloatingActionButton) findViewById(R.id.changeColorBtn);
        mTitleEdit = (EditText) findViewById(R.id.ins_title_detail);
        mTitleEdit.setTypeface(null, Typeface.BOLD);
        mNoteEdit = (EditText) findViewById(R.id.ins_nota_detail);
        mLastEdit = (TextView)findViewById(R.id.last_edit_txt);
        mTitleEdit.setTypeface(null, Typeface.BOLD);
        bottomTools = (Toolbar) findViewById(R.id.toolbar_bottom);
        bottomTools.setVisibility(View.INVISIBLE);


        Intent UriData = getIntent();
        mContentUri = UriData.getData();
        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getReadableDatabase();

        getSupportLoaderManager().initLoader(LOADER_ID_DETAIL, null, this);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mTitleEdit.setOnTouchListener(mTouchedListener);
        mNoteEdit.setOnTouchListener(mTouchedListener);
        mChangeColorBtn.setOnTouchListener(mTouchedListener);

        mImageAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fullScreenImages = new Intent(DetailActivity.this, FullScreenImageActivity.class);
                fullScreenImages.putExtra("uriFullScreen", new MemoUtils().PreferenceRestoreUriImage(DetailActivity.this, "UriImageSave"));
                startActivity(fullScreenImages);
            }
        });


        mBtnDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImageFromDetail();
            }
        });

        mChangeColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog colorDialog = new Dialog(DetailActivity.this);
                colorDialog.setContentView(R.layout.color_dialog);
                colorDialog.setTitle(R.string.choose_color);

                final ImageView mBtnBlue = (ImageView) colorDialog.findViewById(R.id.btnBlue);
                final ImageView mBtnRed = (ImageView) colorDialog.findViewById(R.id.btnRed);
                final ImageView mBtnGreen = (ImageView) colorDialog.findViewById(R.id.btnGreen);
                final ImageView mBtOrange = (ImageView) colorDialog.findViewById(R.id.btnOrange);
                final ImageView mBtnPink = (ImageView) colorDialog.findViewById(R.id.btnPink);
                final ImageView mBtnDarkPink = (ImageView) colorDialog.findViewById(R.id.btnGray);
                final ImageView mBtnDarkIndigo = (ImageView) colorDialog.findViewById(R.id.btnIndigo);
                final ImageView mBtnLightGreen = (ImageView) colorDialog.findViewById(R.id.btnLightGreen);

                mBtnDarkPink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialDarkPink));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();
                    }

                });

                mBtOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialOrange));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();

                    }
                });

                mBtnPink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialPink));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();

                    }
                });

                mBtnGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialGreen));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();


                    }
                });

                mBtnRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialRed));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();


                    }
                });
                mBtnBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialBlue));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();

                    }
                });

                mBtnDarkIndigo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialIndigo));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();

                    }
                });

                mBtnLightGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialLightGreen));
                        mCollapsToolBar.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                        insColor();
                        colorDialog.dismiss();

                    }
                });

                colorDialog.show();
            }
        });
//
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projector = {
                ContractMemoApp.MemoAppContract._ID,
                ContractMemoApp.MemoAppContract.COLUMN_TITLE,
                ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,
                ContractMemoApp.MemoAppContract.COLUMN_DATE,
                ContractMemoApp.MemoAppContract.COLUMN_COLOR,
                ContractMemoApp.MemoAppContract.COLUMN_IMAGES,
//                ContractMemoApp.MemoAppContract.COLUMN_RECORD_AUDIO
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
//            int uriRecord = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_RECORD_AUDIO);
            int images = data.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_IMAGES);


            String titleTxt = data.getString(title);
            String noteTxt = data.getString(note);
            dateTxt = data.getString(date);
            colorValue = data.getInt(color);
            String UriImage = data.getString(images);

            if (UriImage != null) {
                mImageAdded.setVisibility(View.VISIBLE);
                mBtnDeleteImage.setVisibility(View.VISIBLE);
                Glide.with(DetailActivity.this).load(UriImage).centerCrop().into(mImageAdded);
            } else {
                mImageAdded.setVisibility(View.GONE);
                mBtnDeleteImage.setVisibility(View.GONE);
            }


//            uriREC = data.getString(uriRecord);
//            if (uriREC != null) {
//                bottomTools.setVisibility(View.VISIBLE);
//                RecordToolbar();
//            } else bottomTools.setVisibility(View.GONE);


            mTitleEdit.setText(titleTxt);
            mNoteEdit.setText(noteTxt);
            mLastEdit.setText(getString(R.string.lastEdit) + "  " + dateTxt);


            //retore color for entire activity
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorValue));
            mCollapsToolBar.setBackgroundColor(colorValue);
            getWindow().setStatusBarColor(colorValue);
            getWindow().setNavigationBarColor(colorValue);


            //get lenght for edit Text
            mNoteLength = mNoteEdit.getText().length();
            mTitleLength = mTitleEdit.getText().length();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void deleteImageFromDetail() {
        final AlertDialog.Builder alertDelete = new AlertDialog.Builder(this, R.style.CustomAlert);
        alertDelete.setView(R.layout.alertdialog_delete_image_layout);
        alertDelete.setCancelable(false)
                .setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues deleteImages = new ContentValues();
                        deleteImages.putNull(ContractMemoApp.MemoAppContract.COLUMN_IMAGES);
                        getContentResolver().update(mContentUri, deleteImages, null, null);
                    }
                })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing
                    }
                }).create().show();
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
                break;

            case R.id.btnAddImage:
                Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
                addImage.setType("image/*");
                startActivityForResult(Intent.createChooser(addImage, "Select Image"), 12);
                break;
//            case R.id.btnAddPhoto:
//                if (Build.VERSION.SDK_INT > 23){
//                    requestPermissions(permission,REQUEST_PERMISSION_ID);
//                }
//                Intent addPhotoFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(addPhotoFromCamera,150);
//            break;

//            case R.id.recordMenu:
//                //request method
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(permission, REQUEST_PERMISSION_ID);
//                }
//                RecordToolbar();
//                break;
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
        alertDelete.setView(R.layout.alertdialog_delete_layout);
        alertDelete.setCancelable(false)
                .setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
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
        alertDiscard.setView(R.layout.alertdialog_layout_discard_save);
        alertDiscard.setCancelable(false)
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
                Toast.makeText(DetailActivity.this, R.string.memo_saved, Toast.LENGTH_SHORT).show();
                finish();

            }
        }).create().show();
    }

    public void InsertNote(){
        contentValues = new ContentValues();
        String TitleControl = mTitleEdit.getText().toString();
        String NoteControl = mNoteEdit.getText().toString();
        if (TitleControl.isEmpty() && NoteControl.isEmpty()){
            Toast.makeText(this, "Please Insert a Note", Toast.LENGTH_SHORT).show();
        }else{
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_TITLE,mTitleEdit.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,mNoteEdit.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_DATE, new MemoUtils().GetDate());
        }
        getContentResolver().update(mContentUri, contentValues, null, null);
        new MemoUtils().RemovePreferenceSharedString(DetailActivity.this, "UriImageSave");
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK) {
            Uri uriDataImage = data.getData();
            if (uriDataImage != null) {
                new MemoUtils().PreferenceSaveImageUri(DetailActivity.this, "UriImageSave", uriDataImage.toString());
                mSQLdata = mHelper.getWritableDatabase();
                ContentValues images = new ContentValues();
                images.put(ContractMemoApp.MemoAppContract.COLUMN_IMAGES, String.valueOf(uriDataImage));
                getContentResolver().update(mContentUri, images, null, null);
            }

        }

    }


    public void insColor() {
        contentValues = new ContentValues();
        contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR, new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
        getContentResolver().update(mContentUri, contentValues, null, null);
    }

    public void createNewImageView(String UriDataImage) {
        ImageView imageView = new ImageView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        mLinear.addView(imageView);
        Glide.with(DetailActivity.this).load(UriDataImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    private void RecordToolbar() {
        bottomTools.setVisibility(View.VISIBLE);
        ImageButton mBtnRec = (ImageButton) findViewById(R.id.btnRecordVoice);
        final ImageButton mBtnStop = (ImageButton) findViewById(R.id.btnStopRecord);
        final ImageButton mBtnPlay = (ImageButton) findViewById(R.id.btnPlayRecord);

        if (uriREC != null) {
            mBtnPlay.setEnabled(true);
            mBtnRec.setEnabled(false);
            mBtnStop.setEnabled(false);
        }

        mBtnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileAudio = null;
                File audioDir = new File(DEFAULT_STORAGE_LOCATION);
                if (!audioDir.exists()) {
                    try {
                        audioDir.mkdir();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    fileAudio = File.createTempFile("recordMemo", ".3gp", audioDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaRecord = new MediaRecorder();
                mMediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecord.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mMediaRecord.setMaxDuration(50000);
                mMediaRecord.setOutputFile(fileAudio.getPath());
                try {
                    mMediaRecord.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaRecord.start();

                Toast.makeText(DetailActivity.this, "Start Record" + fileAudio, Toast.LENGTH_SHORT).show();
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaRecord.stop();
                mMediaRecord.release();
                addRecordToDatabase();
                Toast.makeText(DetailActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
            }
        });
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mPlayAudio = new MediaPlayer();

                try {

                    mPlayAudio.setDataSource(DetailActivity.this, Uri.parse(uriREC));
                    mPlayAudio.prepare();
                    mPlayAudio.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mMediaRecord) {
            mMediaRecord.release();
            Toast.makeText(getApplicationContext(), "Finish Recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void addRecordToDatabase() {
        contentValues = new ContentValues();
        contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_RECORD_AUDIO, fileAudio.getAbsolutePath());
        new MemoUtils().PreferenceSaveRecord(DetailActivity.this, "audioFileRecord", fileAudio.getAbsolutePath());
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(new MemoUtils().PreferenceRestoreRecord(DetailActivity.this, "audioFileRecord", null))));
        getContentResolver().update(mContentUri, contentValues, null, null);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSION_ID:
                requestRecordAudioPermission = grantResults[0] == getPackageManager().PERMISSION_GRANTED;
                requestWriteExternalStorage = grantResults[1] == getPackageManager().PERMISSION_GRANTED;
                requestPhotoCapture = grantResults[2] == getPackageManager().PERMISSION_GRANTED;

                break;
        }
        if (!requestRecordAudioPermission) finish();
        if (!requestWriteExternalStorage) finish();
        if (!requestPhotoCapture) finish();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
