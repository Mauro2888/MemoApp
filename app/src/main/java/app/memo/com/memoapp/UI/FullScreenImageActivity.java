package app.memo.com.memoapp.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import app.memo.com.memoapp.R;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView mFullScreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Uri getUri = getIntent().getParcelableExtra("uriFullScreen");
        mFullScreenImage = (ImageView) findViewById(R.id.imageFullScreen);
        Glide.with(FullScreenImageActivity.this).load(getUri).into(mFullScreenImage);

    }
}
