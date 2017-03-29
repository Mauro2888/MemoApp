package app.memo.com.memoapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import app.memo.com.memoapp.database.ContractMemoApp;
import app.memo.com.memoapp.database.HelperClass;

public class DetailActivity extends AppCompatActivity {

    TextView mTitleText;
    Uri content;
    public SQLiteDatabase mSQLdata;
    private HelperClass mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(R.string.memo_detail);

        mTitleText = (TextView)findViewById(R.id.txtTitle);

        Intent getData = getIntent();
        content = getData.getData();
        Toast.makeText(this, "URI " + content, Toast.LENGTH_SHORT).show();
        mTitleText.setText(content.toString());
        mHelper = new HelperClass(getApplicationContext());
        mSQLdata = mHelper.getReadableDatabase();



    }

}
