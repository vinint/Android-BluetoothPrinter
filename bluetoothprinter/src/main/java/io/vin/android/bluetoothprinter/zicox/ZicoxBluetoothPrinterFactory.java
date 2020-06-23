package io.vin.android.bluetoothprinter.zicox;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 芝柯打印机驱动工厂类
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class ZicoxBluetoothPrinterFactory implements IBluetoothPrinterFactory{
    IBluetoothPrinterProtocol printer;
    String printerModelName;

    public ZicoxBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            if (printerModelName.startsWith("YT688")){
                printer = new zicox_BluetoothPrinter(printerModelName);
            }else {
                printer = new ZicoxBluetoothPrinter(printerModelName);
            }
        }
        return printer;
    }
}
