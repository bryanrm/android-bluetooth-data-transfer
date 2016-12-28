package com.bryanrm.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
    private Context context;
    private BluetoothSocket socket;

    public FileTransfer(InputStream inputStream, BluetoothDevice device, Context context) {
        this.inputStream = inputStream;
        this.device = device;
        this.context = context;
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
                        try { outputStream.close(); }
                        catch (IOException e) { }
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
                createToast(context.getString(R.string.toast_success_send));
                break;
            case Constants.ERROR_OUTPUT_STREAM:
                createToast(context.getString(R.string.toast_error_output_stream));
                break;
            case Constants.ERROR_BYTES:
                createToast(context.getString(R.string.toast_error_bytes));
                break;
            case Constants.ERROR_CONNECT:
                createToast(context.getString(R.string.toast_error_connect));
                break;
            case Constants.ERROR_SOCKET:
                createToast(context.getString(R.string.toast_error_socket));
                break;
        }
        cancelSocket();
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

    private void cancelSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) { }
        }
    }

    private void createToast(String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
