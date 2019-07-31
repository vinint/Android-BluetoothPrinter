package io.vin.android.bluetoothprinter.jiqiang.core;


public class JqPrinterFactory {
    private JqBtCenterManagerProtocol mJqBtCenterManagerProtocol;
    private JqPrinter mJqPrinter;

    public JqPrinter createBluetoothPrinter() {
        if (this.mJqBtCenterManagerProtocol == null) {
            this.mJqBtCenterManagerProtocol = new JqBtCenterManagerProtocol();
        }
        if (this.mJqPrinter != null) {
            return this.mJqPrinter;
        }
        this.mJqPrinter = new JqPrinter(this.mJqBtCenterManagerProtocol);
        return this.mJqPrinter;
    }

    public JqBtCenterManagerProtocol createBluetoothCentralManager() {
        if (this.mJqBtCenterManagerProtocol != null) {
            return this.mJqBtCenterManagerProtocol;
        }
        this.mJqBtCenterManagerProtocol = new JqBtCenterManagerProtocol();
        return this.mJqBtCenterManagerProtocol;
    }
}
