package com.bryanrm.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

// Created by Bryan R. Martinez on 12/28/2016
public class FileTransfer extends AsyncTask<Void, Void, Integer> {
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private StringBuilder stringBuilder;

    public FileTransfer(InputStream inputStream, BluetoothDevice device) {
        this.inputStream = inputStream;
        this.device = device;
        stringBuilder = new StringBuilder();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.UUID));
            try {
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                socket.connect();

                outputStream = socket.getOutputStream();
                try {
                    byte[] data = getBytes();
                    if (outputStream != null) {
                        outputStream.write(data);
                        return Constants.SUCCESS_SEND;
                    } else { return Constants.ERROR_OUTPUT_STREAM; }
                } catch (IOException e) { return Constants.ERROR_BYTES; }
            } catch (IOException e) { return Constants.ERROR_CONNECT; }
        } catch (IOException e) { return Constants.ERROR_SOCKET; }
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch (result) {
            case Constants.SUCCESS_SEND:
                
        }
    }

    private byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
