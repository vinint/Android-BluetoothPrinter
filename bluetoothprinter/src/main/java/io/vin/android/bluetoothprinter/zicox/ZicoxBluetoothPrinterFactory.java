package io.vin.android.bluetoothprinter.zicox;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 芝柯打印机驱动工厂类
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class ZicoxBluetoothPrinterFactory implements IBluetoothPrinterFactory{
    ZicoxBluetoothPrinter printer;
    String printerModelName;

    public ZicoxBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            printer = new ZicoxBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
