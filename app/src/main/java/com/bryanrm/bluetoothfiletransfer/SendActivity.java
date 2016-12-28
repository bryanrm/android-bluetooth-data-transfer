package com.bryanrm.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            arrayList.add(device.getName() + System.lineSeparator() + device.getAddress());
            System.out.println(device.getName() + System.lineSeparator() + device.getAddress());
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }

}
