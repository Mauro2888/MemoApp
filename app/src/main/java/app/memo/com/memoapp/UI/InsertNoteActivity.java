package app.memo.com.memoapp.UI;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.memo.com.memoapp.Database.ContractMemoApp;
import app.memo.com.memoapp.Database.HelperClass;
import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.R;

public class InsertNoteActivity extends AppCompatActivity {

    public SQLiteDatabase  mSQLdata;
    public CoordinatorLayout mCoordinatorLayout;
    ContentValues contentValues;
    boolean mTouch = false;
    private String mMemoDate;
    private EditText mInsTitle;
    private EditText mInsNote;
    private ImageView mImageViewAddImage;
    private HelperClass mHelper;
    private FloatingActionButton mBtnColorPicker;
    private CollapsingToolbarLayout mCoolapsToolbar;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouch = true;
            return false;
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        setTitle("");
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_note);
        mCoolapsToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsToolbar);
        mImageViewAddImage = (ImageView) findViewById(R.id.imageViewAdd);
        mCoolapsToolbar.setTitle("Insert Memo");
        mInsTitle = (EditText)findViewById(R.id.ins_title);
        mInsTitle.setTypeface(null, Typeface.BOLD);
//        //FILTER MAX WORD
//        int maxLengthTitle = 20 ;
//        mInsTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthTitle)});

        mInsNote = (EditText)findViewById(R.id.ins_nota);
        mMemoDate = new MemoUtils().GetDate();

        getSupportActionBar().setElevation(0);

        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getWritableDatabase();
        contentValues = new ContentValues();

        mBtnColorPicker = (FloatingActionButton) findViewById(R.id.pickerColor);
        mInsTitle.setOnTouchListener(mOnTouchListener);
        mInsNote.setOnTouchListener(mOnTouchListener);
        mBtnColorPicker.setOnTouchListener(mOnTouchListener);
        new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", ContextCompat.getColor(InsertNoteActivity.this, R.color.materialBlue));


        mBtnColorPicker.setOnClickListener(new View.OnClickListener() {
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
                popWindowColor.showAtLocation(mBtnColorPicker, Gravity.CENTER_HORIZONTAL, 0, 0);

                ImageButton mBtnBlue = (ImageButton) viewColor.findViewById(R.id.btnBlue);
                ImageButton mBtnRed = (ImageButton) viewColor.findViewById(R.id.btnRed);
                ImageButton mBtnGreen = (ImageButton) viewColor.findViewById(R.id.btnGreen);
                ImageButton mBtOrange = (ImageButton) viewColor.findViewById(R.id.btnOrange);
                ImageButton mBtnPink = (ImageButton) viewColor.findViewById(R.id.btnPink);
                ImageButton mBtnGray = (ImageButton) viewColor.findViewById(R.id.btnGray);


                mBtnGray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialDarkPink));
                        mCoolapsToolbar.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });


                mBtOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialOrange));
                        mCoolapsToolbar.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });

                mBtnPink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialPink));
                        mCoolapsToolbar.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });


                mBtnGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialGreen));
                        mCoolapsToolbar.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });

                mBtnRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialRed));
                        mCoolapsToolbar.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();

                    }
                });
                mBtnBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", ContextCompat.getColor(getApplicationContext(), R.color.materialBlue));
                        mCoolapsToolbar.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0)));
                        getWindow().setStatusBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        getWindow().setNavigationBarColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                        popWindowColor.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ins_note,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()){
            case R.id.ins_note_menu:
               InsertNote();
                break;
            case R.id.btnAddImage:
                Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
                addImage.setType("image/*");
                startActivityForResult(Intent.createChooser(addImage, "Select Image"), 12);
                break;
            case android.R.id.home:
                if (mTouch || mInsTitle.length() > 0 || mInsNote.length() > 0) {
                    DiscartAlert();
                } else {
                    onBackPressed();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mTouch || mInsTitle.length() > 0 || mInsNote.length() > 0) {
            DiscartAlert();
        } else {
            finish();
        }
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
                        Toast.makeText(InsertNoteActivity.this, R.string.discard_btn, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.save_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //SALVO
                InsertNote();
                Toast.makeText(InsertNoteActivity.this, R.string.save_btn, Toast.LENGTH_SHORT).show();
                finish();

            }
        }).create().show();
    }

    public void InsertNote(){
        String TitleControl = mInsTitle.getText().toString();
        String NoteControl = mInsNote.getText().toString();
        if (TitleControl.isEmpty() && NoteControl.isEmpty()){
            Toast.makeText(this, R.string.alert_pls_ins_note, Toast.LENGTH_SHORT).show();
        }else{
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_TITLE,mInsTitle.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,mInsNote.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_DATE,mMemoDate);
            if (!mTouch){
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR, ContextCompat.getColor(getApplicationContext(), R.color.materialBlue));
            }else {
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
            }
            getContentResolver().insert(ContractMemoApp.MemoAppContract.URI_CONTENT,contentValues);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12 && resultCode == RESULT_OK) {
            Uri uriImage = data.getData();
            if (uriImage != null) {
                new MemoUtils().PreferenceSaveImageUri(InsertNoteActivity.this, "UriImageSave", uriImage.toString());
                Glide.with(InsertNoteActivity.this).load(uriImage).fitCenter().into(mImageViewAddImage);
                contentValues.put(ContractMemoApp.MemoAppContract.COlUMN_IMAGE_URI, new MemoUtils().PreferenceRestoreUriImage(InsertNoteActivity.this, "UriImageSave"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
