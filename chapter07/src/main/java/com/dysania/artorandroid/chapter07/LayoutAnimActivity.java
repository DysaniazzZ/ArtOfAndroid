package com.dysania.artorandroid.chapter07;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by DysaniazzZ on 06/07/2017.
 */

public class LayoutAnimActivity extends AppCompatActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LayoutAnimActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_anim);

        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add("item: " + (i + 1));
        }

        ListView lvLayout = (ListView) findViewById(R.id.lv_layout);
        lvLayout.setAdapter(new ArrayAdapter<>(LayoutAnimActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, items));
    }
}
