package app.memo.com.memoapp.UI;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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

import app.memo.com.memoapp.Database.ContractMemoApp;
import app.memo.com.memoapp.Database.HelperClass;
import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.R;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public SQLiteDatabase mSQLdata;
    public boolean mTouched = false;
    public int colorValue;
    Uri mContentUri;
    ContentValues contentValues;
    CoordinatorLayout mCoordinatorLayout;
    private EditText mTitleEdit;
    private EditText mNoteEdit;
    private TextView mLastEdit;
    private HelperClass  mHelper;
    private ImageButton mBtnColorPicker;
    private ImageView mColorSelected;
    private boolean mTouchedColor = false;
    private PopupWindow mPopMenu;
    private CollapsingToolbarLayout mCollapsToolBar;

    private View.OnTouchListener mTouchColor = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouchedColor = true;
            return false;
        }
    };

    private View.OnTouchListener mOnTouchItems = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouched = true;
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        setTitle("");


        mCollapsToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsToolbar);
        mCollapsToolBar.setTitle("Memo detail");
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutTools);

        mTitleEdit = (EditText) findViewById(R.id.ins_title_detail);
        mTitleEdit.setTypeface(null, Typeface.BOLD);
        mNoteEdit = (EditText) findViewById(R.id.ins_nota_detail);
        mLastEdit = (TextView)findViewById(R.id.last_edit_txt);
        mColorSelected = (ImageView) findViewById(R.id.colorSelected);

        mTitleEdit.setTypeface(null, Typeface.BOLD);

        Intent getData = getIntent();
        mContentUri = getData.getData();
        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getReadableDatabase();
        getSupportLoaderManager().initLoader(0,null,this);

        mTitleEdit.setOnTouchListener(mOnTouchItems);
        mNoteEdit.setOnTouchListener(mOnTouchItems);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mCoordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                View custom = getLayoutInflater().inflate(R.layout.action_bar_detail, null);
                mPopMenu = new PopupWindow(
                        custom,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT);

                if (Build.VERSION.SDK_INT >= 21) {
                    mPopMenu.setElevation(2.0f);
                }


                mBtnColorPicker = (ImageButton) custom.findViewById(R.id.pickerColorTools);
                mBtnColorPicker.setOnTouchListener(mTouchColor);
                mBtnColorPicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MemoUtils().GetRandomMaterialColor(DetailActivity.this, "");
                        new MemoUtils().PreferenceSave(DetailActivity.this, "colorSaved", new MemoUtils().GetRandomMaterialColor(DetailActivity.this, "A100"));
                        mColorSelected.setBackgroundColor(new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
                    }
                });
                mPopMenu.showAtLocation(mCoordinatorLayout, Gravity.LEFT | Gravity.TOP, 50, 0);
                return false;
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projector = {
                ContractMemoApp.MemoAppContract.COLUMN_TITLE,
                ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,
                ContractMemoApp.MemoAppContract.COLUMN_DATE,
                ContractMemoApp.MemoAppContract.COLUMN_COLOR
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

            String titleTxt = data.getString(title);
            String noteTxt = data.getString(note);
            String dateTxt = data.getString(date);
            colorValue = data.getInt(color);

            mTitleEdit.setText(titleTxt);
            mNoteEdit.setText(noteTxt);
            mLastEdit.setText(dateTxt);
            mColorSelected.setBackgroundColor(colorValue);
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
            case android.R.id.home:
                if (!mTouched) {
                    DiscartAlert();
                } else {
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (!mTouched) {
            super.onBackPressed();
            return;
        }
        DiscartAlert();
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
            if (!mTouchedColor){
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,colorValue);
            }else {
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,new MemoUtils().PreferenceRestore(DetailActivity.this, "colorSaved", 0));
            }
            getContentResolver().update(mContentUri,contentValues,null,null);
            finish();
        }
    }
}
