package app.memo.com.memoapp.UI;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Locale;

import app.memo.com.memoapp.Database.ClickItem;
import app.memo.com.memoapp.Database.ContractMemoApp;
import app.memo.com.memoapp.Database.CursorAdapterMemo;
import app.memo.com.memoapp.Database.HelperClass;
import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.R;

import static app.memo.com.memoapp.Database.ContractMemoApp.MemoAppContract.URI_CONTENT;

public class MainActivityMemo extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ClickItem {

    public static final int LOADER_ID = 111;
    private static final int REQUEST_CODE = 1022;
    Uri Uri;
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
    private CoordinatorLayout mCoordinatorLayoutMain;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToogle;
    private NavigationView mNavView;
    private RelativeLayout mEmptyView;
    private FloatingActionMenu mFabMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_memo);
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        setTitle(R.string.home);


        mFabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        mEmptyView = (RelativeLayout) findViewById(R.id.emptyView);
        mRecyclerMemo = (RecyclerView)findViewById(R.id.recyclerMemo);
        mFloatAddNote = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.floatingActionButtonAdd);
        mFloatAddNoteFast = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.floatingActionButtonAddFast);
        mFloatAddNoteReg = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.floatingActionButtonAddReg);
        mCoordinatorLayoutMain = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);
        mNavView = (NavigationView) findViewById(R.id.navMenu);
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToogle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerMemo.setLayoutManager(mLayoutManager);
        mRecyclerMemo.setHasFixedSize(true);
        mRecyclerMemo.setItemAnimator(new DefaultItemAnimator());
        mAdapterMemo = new CursorAdapterMemo(this);
        mRecyclerMemo.setAdapter(mAdapterMemo);
        mAdapterMemo.setmClickItem(MainActivityMemo.this);

        mHelper = new HelperClass(this);
        mSQLdata = mHelper.getWritableDatabase();
        contentValues = new ContentValues();

        //FAV animation

        mRecyclerMemo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 1) {

                    mFabMenu.hideMenuButton(true);
                } else {

                    mFabMenu.showMenuButton(true);
                }
            }
        });


        //SWIPE DELETE
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = (int) viewHolder.itemView.getTag();
                Uri = URI_CONTENT.buildUpon().appendPath(String.valueOf(pos)).build();
                SwiperDeleteAlert(Uri);

            }
        }).attachToRecyclerView(mRecyclerMemo);


        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favouriteNotes:
                        Intent favourite = new Intent(MainActivityMemo.this, FavouriteActivity.class);
                        startActivity(favourite);
                        break;
                    case R.id.allNotes:
                        getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivityMemo.this);
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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
                mInsTitle.setTypeface(null, Typeface.BOLD);

                alert.setView(alertView);
                alert.setCancelable(false);
                alert.setTitle(R.string.insert_alert_note_title);
                alert.setPositiveButton(R.string.save_alert, new DialogInterface.OnClickListener() {
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
                            contentValues.put("color",new MemoUtils().GetRandomMaterialColor(MainActivityMemo.this,"A100"));
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
        Intent DetailActivity = new Intent(view.getContext(), DetailActivity.class);
        pos = (int) view.getTag();
        Uri Uri = android.net.Uri.withAppendedPath(URI_CONTENT, String.valueOf(pos));
        DetailActivity.setData(Uri);
        startActivity(DetailActivity);
    }

    //CURSOR SUPPORT-------------------------------------------------------------
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
        if (mAdapterMemo.getItemCount() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else mEmptyView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterMemo.swapCursor(null);
    }

    //-------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToogle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.about:
                break;

        }
        return super.onOptionsItemSelected(item);
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
                    contentValues.put("color", ContextCompat.getColor(getApplicationContext(), R.color.materialBlue));
                    getContentResolver().insert(ContractMemoApp.MemoAppContract.URI_CONTENT, contentValues);
                }
                break;
        }
    }

    private void SwiperDeleteAlert(final Uri uriContent){
        AlertDialog.Builder alertDelete = new AlertDialog.Builder(MainActivityMemo.this, R.style.CustomAlert);
        alertDelete.setMessage(R.string.delete_note_alert)
                .setCancelable(false)
                .setPositiveButton(R.string.delete_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new MemoUtils().SnackBar(mCoordinatorLayoutMain, R.string.memo_deleted);
                        getContentResolver().delete(uriContent, null, null);
                    }
                })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing
                        getSupportLoaderManager().restartLoader(LOADER_ID,null,MainActivityMemo.this);
                    }
                }).create().show();

    }

}
