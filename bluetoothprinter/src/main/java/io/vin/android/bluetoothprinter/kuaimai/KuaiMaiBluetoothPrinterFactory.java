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
        if (printer == null) {
            String tmpName = printerModelName.toUpperCase();
            if (tmpName.startsWith("KM-118B") ||
                    tmpName.startsWith("KM-218BT")) {
                printer = new KuaiMaiBluetoothPrinter(printerModelName);
            } else if (tmpName.startsWith("KM-202BT") ||
                    tmpName.startsWith("KM-202MBT")||
                    tmpName.startsWith("KM-202MP")||
                    tmpName.startsWith("KM-360")) {
                printer = new KuaiMaiBluetoothPrinter2(printerModelName);
            }else {
                printer = new KuaiMaiBluetoothPrinter(printerModelName);
            }


        }


        return printer;
    }
}
