package com.bryanrm.bluetoothfiletransfer;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

// Created by Bryan R. Martinez on 12/27/2016
public class MainActivity extends AppCompatActivity {
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BluetoothAdapter.getDefaultAdapter() == null) { showNoBluetoothSupportDialog(); }
    }

    public void enableBluetooth(View view) {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, Constants.REQUEST_ENABLE_BT);
        }
        else { createToast(getString(R.string.toast_bt_enabled)); }
    }

    public void selectFile(View view) {
        Intent fileChooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileChooser.addCategory(Intent.CATEGORY_OPENABLE);
        fileChooser.setType("*/*");
        startActivityForResult(fileChooser, Constants.REQUEST_READ_CODE);
    }

    public void sendFile(View view) {
        Intent sendFileIntent = new Intent(this, SendActivity.class);
        if (uri != null) {
            sendFileIntent.setData(uri);
            startActivity(sendFileIntent);
        } else { createToast(getString(R.string.toast_error_select_file)); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_ENABLE_BT)
            if (resultCode == Activity.RESULT_OK)
                createToast(getString(R.string.toast_bt_enabled));
            else { createToast(getString(R.string.toast_error_enable_bt)); }
        if (requestCode == Constants.REQUEST_READ_CODE && resultCode == Activity.RESULT_OK)
            if (data != null) {
                uri = data.getData();
                createToast(getString(R.string.toast_file_selected) + uri.getPath());
            } else { createToast(getString(R.string.toast_error_select_file_02)); }
    }

    private void createToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void showNoBluetoothSupportDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_error_title))
                .setMessage(getResources().getString(R.string.dialog_error_message))
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { System.exit(0); }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }
}
