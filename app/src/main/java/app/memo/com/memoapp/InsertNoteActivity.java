package app.memo.com.memoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.memo.com.memoapp.database.ContractMemoApp;
import app.memo.com.memoapp.database.HelperClass;

public class InsertNoteActivity extends AppCompatActivity {

    private EditText mInsTitle;
    private EditText mInsNote;

    SQLiteDatabase  mSQLdata;
    HelperClass  mHelper;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        mInsTitle = (EditText)findViewById(R.id.ins_title);
        mInsNote = (EditText)findViewById(R.id.ins_nota);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ins_fast_note:
               InsertNote();
        }
        return super.onOptionsItemSelected(item);
    }

    public void InsertNote(){
        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_TITLE,mInsTitle.getText().toString());
        contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT,mInsNote.getText().toString());
        getContentResolver().insert(ContractMemoApp.MemoAppContract.URI_CONTENT,contentValues);
        finish();
    }
}
