package io.vin.android.bluetoothprinter.jiabo;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

public class JiaBoBluetoothPrinterFactory implements IBluetoothPrinterFactory {
    IBluetoothPrinterProtocol printer = null;
    String printerModelName;

    public JiaBoBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null) {
            printer = new JiaBoBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
