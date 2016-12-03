package com.jbs.evecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class CharView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_view);

        Intent intent = getIntent();

        int char_id = intent.getIntExtra("char_id", 0);

        TextView char_id_view = (TextView) findViewById(R.id.view_char_id);
        char_id_view.setText(String.format(Locale.getDefault(),"cid: %1$d",char_id));
    }
}
