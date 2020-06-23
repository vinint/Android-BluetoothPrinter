package io.vin.android.bluetoothprinter.snbc;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 新北洋打印机驱动工厂类
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class SnbcBluetoothPrinterFactory implements IBluetoothPrinterFactory {
    SnbcBluetoothPrinter printer;
    String printerModelName;

    public SnbcBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            printer = new SnbcBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
