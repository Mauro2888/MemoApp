package app.memo.com.memoapp.UI;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import app.memo.com.memoapp.Database.ClickItem;
import app.memo.com.memoapp.Database.CursorAdapterMemo;
import app.memo.com.memoapp.Database.HelperClass;
import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.R;

import static app.memo.com.memoapp.Database.ContractMemoApp.MemoAppContract.URI_CONTENT;

public class MainActivityMemo extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,ClickItem {

    public static final int LOADER_ID = 111;
    private static final int REQUEST_CODE = 1022;
    HelperClass mHelper;
    SQLiteDatabase mSQLdata;
    private RecyclerView mRecyclerMemo ;
    private RecyclerView.LayoutManager mLayoutManager;
    private CursorAdapterMemo mAdapterMemo;
    private com.github.clans.fab.FloatingActionButton mFloatAddNote;
    private com.github.clans.fab.FloatingActionButton mFloatAddNoteFast;
    private com.github.clans.fab.FloatingActionButton mFloatAddNoteReg;
    private EditText mInsNota;
    private EditText mInsTitle;
    private ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_memo);
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        setTitle("Memo List");

        mRecyclerMemo = (RecyclerView)findViewById(R.id.recyclerMemo);
        mFloatAddNote = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.floatingActionButtonAdd);
        mFloatAddNoteFast = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.floatingActionButtonAddFast);
        mFloatAddNoteReg = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.floatingActionButtonAddReg);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerMemo.setHasFixedSize(true);
        mRecyclerMemo.setLayoutManager(mLayoutManager);
        mAdapterMemo = new CursorAdapterMemo(this);
        mRecyclerMemo.setAdapter(mAdapterMemo);

        mAdapterMemo.setmClickItem(MainActivityMemo.this);

        mHelper = new HelperClass(this);
        mSQLdata = mHelper.getWritableDatabase();
        contentValues = new ContentValues();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = (int) viewHolder.itemView.getTag();
                Uri Uri = URI_CONTENT.buildUpon().appendPath(String.valueOf(pos)).build();
                getContentResolver().delete(Uri,null,null);

            }
        }).attachToRecyclerView(mRecyclerMemo);

        //FAB OPTION-------------------------------------------

        mFloatAddNoteReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpeechToText();
            }
        });


        mFloatAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insertNote = new Intent(MainActivityMemo.this,InsertNoteActivity.class);
                startActivity(insertNote);
            }
        });

        mFloatAddNoteFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivityMemo.this, R.style.CustomAlert);
                final View alertView = getLayoutInflater().inflate(R.layout.alertdialog_layout,null);
                mInsNota = (EditText)alertView.findViewById(R.id.ins_nota);
                mInsTitle = (EditText)alertView.findViewById(R.id.ins_title);

                alert.setView(alertView);
                alert.setCancelable(false);
                alert.setTitle(R.string.insert_note_title);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Empty
                    }
                });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                //get alert button
                final AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (!mInsNota.getText().toString().trim().isEmpty() && !mInsTitle.getText().toString().trim().isEmpty()){
                            contentValues.put("title",mInsTitle.getText().toString());
                            contentValues.put("note",mInsNota.getText().toString());
                            contentValues.put("date",new MemoUtils().GetDate());
                            contentValues.put("color",new MemoUtils().random());
                            alertView.getContext().getContentResolver().insert(URI_CONTENT,contentValues);
                            dialog.dismiss();
                            mSQLdata.close();
                        }else {
                            Toast.makeText(MainActivityMemo.this, "No Note", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
    }

    @Override
    public void OnclickItem(View view, int pos) {
        Intent DetailActivity = new Intent(MainActivityMemo.this, DetailActivity.class);
        pos = (int) view.getTag();
        Uri Uri = android.net.Uri.withAppendedPath(URI_CONTENT, String.valueOf(pos));
        DetailActivity.setData(Uri);
        startActivity(DetailActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri Url = URI_CONTENT;
        CursorLoader cursorLoader = new CursorLoader(this,Url,null,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapterMemo.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterMemo.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        if (searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return false;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }

        return super.onCreateOptionsMenu(menu);
    }

    public void SpeechToText(){
        Intent speech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speech.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.ITALIAN);
        speech.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Something...");
        try {
            startActivityForResult(speech, REQUEST_CODE);
        }catch (ActivityNotFoundException a){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    contentValues.put("title","Vocal Memo");
                    contentValues.put("note",result.get(0).toString());
                    contentValues.put("date",new MemoUtils().GetDate());
                    contentValues.put("color",new MemoUtils().random());
                    getContentResolver().insert(URI_CONTENT,contentValues);
                }
                break;
        }
    }
}
