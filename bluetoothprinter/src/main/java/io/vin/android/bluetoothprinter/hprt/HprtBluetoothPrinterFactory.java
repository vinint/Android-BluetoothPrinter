package io.vin.android.bluetoothprinter.hprt;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 汉印打印机驱动工厂
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class HprtBluetoothPrinterFactory implements IBluetoothPrinterFactory {
    HprtBluetoothPrinter printer = null;
    String printerModelName;

    public HprtBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            printer = new HprtBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
