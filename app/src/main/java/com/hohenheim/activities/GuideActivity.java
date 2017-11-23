package com.hohenheim.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hohenheim.R;


public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Toolbar bar = (Toolbar)findViewById(R.id.toolBar);
        bar.setTitle("IF");
        bar.setLogo(R.drawable.if_logo);
    }
}
