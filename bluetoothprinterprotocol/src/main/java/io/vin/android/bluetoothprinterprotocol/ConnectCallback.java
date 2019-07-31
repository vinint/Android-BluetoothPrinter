package io.vin.android.bluetoothprinterprotocol;


/**
 * ConnectCallback
 * 连接回调
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public interface ConnectCallback {
    void onConnectFail(String str);

    void onConnectSuccess();
}
