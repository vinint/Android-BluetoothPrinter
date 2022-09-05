package io.vin.android.bluetoothprinter.qirui;

import android.app.Application;
import android.content.Context;

import io.vin.android.bluetoothprinter.mamayz.CompatibleMamayz;
import io.vin.android.bluetoothprinter.mamayz.CompatibleMamayzTspl;
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
    Context context;

    public QRBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }
    public QRBluetoothPrinterFactory(String printerModelName, Context context){
        this.printerModelName = printerModelName;
        this.context = context.getApplicationContext();
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printerModelName.toUpperCase().contains("QR-488BT")){
            printer = new CompatibleMamayzTspl(context);
        } else if (printerModelName.toUpperCase().contains("QR-365")){
            printer = new CompatibleMamayz(context);
        }else {
            printer = new QRBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
