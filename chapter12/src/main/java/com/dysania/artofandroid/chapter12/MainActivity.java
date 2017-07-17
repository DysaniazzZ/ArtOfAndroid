package com.dysania.artofandroid.chapter12;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image_view);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//        mImageView.setImageResource(R.drawable.bolt);
//        mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bolt));
                mImageView.setImageBitmap(BitmapUtil
                        .decodeBitmapFromResource(getResources(), R.drawable.bolt, mImageView.getWidth(), mImageView.getHeight()));
            }
        });
    }
}
