package io.vin.android.bluetoothprinter.jiqiang.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;

public class JqBtCenterManagerProtocol {
    private static final String TAG = "JQ";
    private boolean isOpen;
    public BluetoothSocket mmBtSocket;
    public InputStream mmInStream;
    public OutputStream mmOutStream;
    private BluetoothAdapter mBluetoothAdapter;


    public void connectPrinter(String addr, ConnectCallback callback) {
        if (mBluetoothAdapter==null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(addr);
        try {
//            device.setPin("0000".getBytes());
            BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();
            if (bluetoothSocket.isConnected()){
                callback.onConnectSuccess();
                initPrinterWithSocket(bluetoothSocket);
            }else {
                callback.onConnectFail("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            callback.onConnectFail(ex.getMessage());
        }

    }

    public void disconnect() {
        if (this.mmBtSocket == null) {
            this.isOpen = false;
            Log.e(TAG, "mmBtSocket null");
            return;
        }
        if (this.isOpen) {
            try {
                if (this.mmOutStream != null) {
                    this.mmOutStream.close();
                    this.mmOutStream = null;
                }
                if (this.mmInStream != null) {
                    this.mmInStream.close();
                    this.mmOutStream = null;
                }
                this.mmBtSocket.close();
            } catch (Exception e) {
                this.isOpen = false;
                Log.e(TAG, "close exception");
                return;
            }
        }
        this.isOpen = false;
        this.mmBtSocket = null;
    }

    public boolean write(byte[] buffer, int offset, int length) {
        if (!this.isOpen) {
            return false;
        }
        if (this.mmBtSocket == null) {
            Log.e(TAG, "mmBtSocket null");
            return false;
        } else if (this.mmOutStream == null) {
            return false;
        } else {
            try {
                this.mmOutStream.write(buffer, offset, length);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public boolean read(byte[] buffer, int offset, int length, int timeout_read) {
        if (!this.isOpen) {
            return false;
        }
        if (timeout_read < 200) {
            timeout_read = 200;
        }
        try {
            long start_time = SystemClock.elapsedRealtime();
            int need_read = length;
            while (true) {
                if (this.mmInStream.available() > 0) {
                    int cur_readed = this.mmInStream.read(buffer, offset, need_read);
                    offset += cur_readed;
                    need_read -= cur_readed;
                }
                if (need_read == 0) {
                    return true;
                }
                if (SystemClock.elapsedRealtime() - start_time > ((long) timeout_read)) {
                    Log.e(TAG, "read timeout");
                    return false;
                }
                TimeUnit.MILLISECONDS.sleep(20);
            }
        } catch (Exception e) {
            Log.e(TAG, "read exception");
            disconnect();
            return false;
        }
    }

    public void initPrinterWithSocket(BluetoothSocket socket) {
        this.mmBtSocket = socket;
        try {
            this.mmOutStream = this.mmBtSocket.getOutputStream();
            this.mmInStream = this.mmBtSocket.getInputStream();
            this.isOpen = true;
        } catch (IOException e) {
            this.mmBtSocket = null;
        }
    }

    public void resetPrinterSocket() {
        this.mmBtSocket = null;
        this.mmOutStream = null;
        this.mmInStream = null;
        this.isOpen = false;
    }
}
