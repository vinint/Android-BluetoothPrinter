package io.vin.android.bluetoothprinterprotocol;

/**
 * PrintCallback
 * 打印回调
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public interface PrintCallback {
    void onPrintFail(int code);

    void onPrintSuccess();
}
