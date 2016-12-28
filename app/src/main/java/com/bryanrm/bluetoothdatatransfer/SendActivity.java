package com.bryanrm.bluetoothdatatransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

// Created by Bryan R. Martinez on 12/27/2016
public class SendActivity extends AppCompatActivity {
    private Uri uri;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        uri = getIntent().getData();
        arrayList = new ArrayList<>();
        populateListView();
    }

    private void populateListView() {
        arrayList.clear();
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices())
            arrayList.add(device.getName() + System.lineSeparator() + device.getAddress());

        final ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                String[] deviceInfo = listItem.toString().split(System.lineSeparator());

                if (deviceInfo.length == 2) {
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceInfo[1]);
                    try {
                        new DataTransfer(getContentResolver().openInputStream(uri),
                                device, getApplicationContext()).execute();
                    } catch (FileNotFoundException e) { }
                }
            }
        });
    }

}
