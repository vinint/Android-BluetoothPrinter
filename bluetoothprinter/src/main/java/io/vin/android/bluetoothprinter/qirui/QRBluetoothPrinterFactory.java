package io.vin.android.bluetoothprinter.qirui;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 启瑞打印机驱动工厂类
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class QRBluetoothPrinterFactory implements IBluetoothPrinterFactory {
    QRBluetoothPrinter printer = null;
    String printerModelName;

    public QRBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            printer = new QRBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
