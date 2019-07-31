package io.vin.android.bluetoothprinter.zicox.core;

import android.bluetooth.BluetoothSocket;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;

public class ZicoxCentralManager  {
    private ZicoxPrinter zpPrintSDK;

    public ZicoxCentralManager(ZicoxPrinter zpPrintSDK) {
        this.zpPrintSDK = zpPrintSDK;
    }

    public void connectPrinter(String address, ConnectCallback callback) {
        if (this.zpPrintSDK.connect(address)) {
            callback.onConnectSuccess();
        } else {
            callback.onConnectFail("连接打印机失败");
        }
    }

    public void disconnect() {
        this.zpPrintSDK.disconnect();
    }

    public void initPrinterWithSocket(BluetoothSocket socket) {
        this.zpPrintSDK.initPrinterWithSocket(socket);
    }

    public void resetPrinterSocket() {
        this.zpPrintSDK.resetPrinterSocket();
    }
}
