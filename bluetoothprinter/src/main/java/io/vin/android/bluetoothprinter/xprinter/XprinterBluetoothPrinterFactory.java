package io.vin.android.bluetoothprinter.xprinter;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

/**
 * 芯烨打印机（珠海创微电子科技有限公司）
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class XprinterBluetoothPrinterFactory implements IBluetoothPrinterFactory {

    IBluetoothPrinterProtocol printer = null;
    String printerModelName;

    public XprinterBluetoothPrinterFactory(String printerModelName) {
        this.printerModelName = printerModelName;
    }

    @Override
    public IBluetoothPrinterProtocol create() {
        if (printer == null)
        {
            String tmpName = printerModelName.toUpperCase();
            if (tmpName.startsWith("XP-P323B"))
                return new XprinterBluetoothPrinter("XP-P323B");
        }
        return printer;
    }
}
