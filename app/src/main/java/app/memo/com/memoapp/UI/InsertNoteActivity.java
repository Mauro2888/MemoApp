package app.memo.com.memoapp.UI;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
    private HelperClass mHelper;
    private ImageButton mBtnColorPicker;
    private ImageView mColorSelected;
    private CollapsingToolbarLayout mCoolapsToolbar;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouch = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        setTitle(R.string.memo);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_note);
        mCoolapsToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsToolbar);
        mCoolapsToolbar.setTitle("Insert Memo");
        mInsTitle = (EditText)findViewById(R.id.ins_title);
        mInsTitle.setTypeface(null, Typeface.BOLD);

//        //FILTER MAX WORD
//        int maxLengthTitle = 20 ;
//        mInsTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthTitle)});

        mInsNote = (EditText)findViewById(R.id.ins_nota);
        mMemoDate = new MemoUtils().GetDate();

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getWritableDatabase();
        contentValues = new ContentValues();


        mBtnColorPicker = (ImageButton)findViewById(R.id.pickerColor) ;
        mColorSelected = (ImageView)findViewById(R.id.colorSelected);
        mBtnColorPicker.setOnTouchListener(mOnTouchListener);


        mBtnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MemoUtils().GetRandomMaterialColor(InsertNoteActivity.this, "");
                new MemoUtils().PreferenceSave(InsertNoteActivity.this, "colorSaved", new MemoUtils().GetRandomMaterialColor(InsertNoteActivity.this, "A100"));
                mColorSelected.setBackgroundColor(new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
                mColorSelected.setVisibility(View.VISIBLE);
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
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,new MemoUtils().random());
            }else {
                contentValues.put(ContractMemoApp.MemoAppContract.COLUMN_COLOR,new MemoUtils().PreferenceRestore(InsertNoteActivity.this, "colorSaved", 0));
            }
            getContentResolver().insert(ContractMemoApp.MemoAppContract.URI_CONTENT,contentValues);
            finish();
        }
    }

}
