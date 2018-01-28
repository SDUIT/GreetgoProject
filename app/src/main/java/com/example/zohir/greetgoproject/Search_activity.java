package com.example.zohir.greetgoproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Search_activity extends AppCompatActivity {

    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        t = (TextView) findViewById(R.id.ip);
        String s = getIntent().getStringExtra("Ip");
        t.setText(s);

    }
}
