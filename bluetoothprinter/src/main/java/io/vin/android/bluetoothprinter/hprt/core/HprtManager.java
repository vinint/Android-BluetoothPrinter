package io.vin.android.bluetoothprinter.hprt.core;

import android.bluetooth.BluetoothSocket;
import java.lang.reflect.Field;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;


public class HprtManager {
    public void connectPrinter(String s, ConnectCallback connectCallback) {
        try {
            BTOperator.isShake = false;
            if (HPRTPrinterHelper.PortOpen("Bluetooth," + s) == 0) {
                connectCallback.onConnectSuccess();
            } else {
                connectCallback.onConnectFail("连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            HPRTPrinterHelper.PortClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPrinterWithSocket(BluetoothSocket socket) {
        HPRTPrinterHelper.initPrinterWithSocket(socket);
    }

    public void resetPrinterSocket() {
        try {
            Field printerField = HPRTPrinterHelper.class.getDeclaredField("Printer");
            printerField.setAccessible(true);
            printerField.set(null, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
}
