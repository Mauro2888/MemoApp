package app.memo.com.memoapp;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.database.ContractMemoApp;
import app.memo.com.memoapp.database.HelperClass;

public class InsertNoteActivity extends AppCompatActivity {

    private EditText mInsTitle;
    private EditText mInsNote;
    public SQLiteDatabase  mSQLdata;
    private HelperClass  mHelper;
    String mMemoDate;
    private static final int REQUEST_CODE = 1022;
    ContentValues contentValues;
    private ImageButton mBtnColorPicker;
    private ImageView mColorSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        setTitle(R.string.memo);
        mInsTitle = (EditText)findViewById(R.id.ins_title);
        mInsNote = (EditText)findViewById(R.id.ins_nota);
        mMemoDate = new MemoUtils().GetDate();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getWritableDatabase();
        contentValues = new ContentValues();
        mBtnColorPicker = (ImageButton)findViewById(R.id.pickerColor) ;
        mColorSelected = (ImageView)findViewById(R.id.colorSelected);
        mBtnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MemoUtils().ColorPicker(InsertNoteActivity.this);
                mColorSelected.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                mColorSelected.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.ins_fast_note:
               InsertNote();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void InsertNote(){
        String TitleControl = mInsTitle.getText().toString();
        String NoteControl = mInsNote.getText().toString();
        if (TitleControl.isEmpty() && NoteControl.isEmpty()){
            Toast.makeText(this, "Please Insert a Note", Toast.LENGTH_SHORT).show();
        }else{
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_TITLE,mInsTitle.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,mInsNote.getText().toString());
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_DATE,mMemoDate);
            contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,new MemoUtils().PreferenceRestore(InsertNoteActivity.this,"colorSaved",0));
            getContentResolver().insert(ContractMemoApp.MemoAppContract.URI_CONTENT,contentValues);
            finish();
        }
    }


}
