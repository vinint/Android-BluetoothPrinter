package io.vin.android.bluetoothprinter.snbc;

import io.vin.android.bluetoothprinter.zicox.ZicoxBluetoothPrinter;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;

class SnbcBluetoothPrinter extends ZicoxBluetoothPrinter {
    public SnbcBluetoothPrinter(String printerModelName) {
        super(printerModelName);
    }

    @Override
    public int getPrinterStatus() {
        int status = STATUS_OK;
        if (printer.mySocket == null || !printer.mySocket.isConnected()) {
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
        }
        return status;
    }
}
