package io.vin.android.bluetoothprinter.jiqiang;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 济强打印机驱动工厂类
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class JqBluetoothPrinterFactory implements IBluetoothPrinterFactory {
    IBluetoothPrinterProtocol printer = null;
    String printerModelName;

    public JqBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            printer = new JqBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
