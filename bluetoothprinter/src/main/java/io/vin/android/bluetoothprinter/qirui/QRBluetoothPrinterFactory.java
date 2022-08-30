package io.vin.android.bluetoothprinter.qirui;

import android.app.Application;

import io.vin.android.bluetoothprinter.mamayz.CompatibleMamayz;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 启瑞打印机驱动工厂类
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class QRBluetoothPrinterFactory implements IBluetoothPrinterFactory {
    IBluetoothPrinterProtocol printer = null;
    String printerModelName;
    Application application;

    public QRBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }
    public QRBluetoothPrinterFactory(String printerModelName,Application application){
        this.printerModelName = printerModelName;
        this.application = application;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printerModelName.toUpperCase().contains("QR-365")){
            printer = new CompatibleMamayz(application);
        }else {
            printer = new QRBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
