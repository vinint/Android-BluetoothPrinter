package io.vin.android.bluetoothprinter.RT;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

public class RTBluetoothPrinterFactory implements IBluetoothPrinterFactory {
    IBluetoothPrinterProtocol printer = null;
    String printerModelName;

    public RTBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            printer = new RTBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
