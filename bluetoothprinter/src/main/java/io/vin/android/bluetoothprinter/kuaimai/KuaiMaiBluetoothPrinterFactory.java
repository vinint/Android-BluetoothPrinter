package io.vin.android.bluetoothprinter.kuaimai;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

public class KuaiMaiBluetoothPrinterFactory implements IBluetoothPrinterFactory{
    IBluetoothPrinterProtocol printer = null;
    String printerModelName;

    public KuaiMaiBluetoothPrinterFactory(String printerModelName){
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null){
            printer = new KuaiMaiBluetoothPrinter(printerModelName);
        }
        return printer;
    }
}
