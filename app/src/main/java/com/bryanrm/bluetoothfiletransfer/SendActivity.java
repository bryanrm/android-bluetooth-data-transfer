package com.bryanrm.bluetoothfiletransfer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

// Created by Bryan R. Martinez on 12/27/2016
public class SendActivity extends AppCompatActivity {
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        uri = getIntent().getData();
    }

}
