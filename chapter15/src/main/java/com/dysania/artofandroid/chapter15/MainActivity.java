package com.dysania.artofandroid.chapter15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById(R.id.root_layout).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        findViewById(R.id.include_layout).setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }
}
