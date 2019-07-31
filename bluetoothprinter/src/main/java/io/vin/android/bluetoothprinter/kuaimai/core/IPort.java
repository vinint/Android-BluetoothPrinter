package io.vin.android.bluetoothprinter.kuaimai.core;

import android.hardware.usb.UsbDevice;

public interface IPort {
    public static final boolean IsPortOpen = false;
    public static final String PortType = "";
    public static final String paramPortSetting = "";

    boolean ClosePort();

    String GetPortType();

    String GetPrinterModel();

    String GetPrinterName();

    void InitPort();

    void IsBLEType(boolean z);

    boolean IsOpen();

    boolean OpenPort(UsbDevice usbDevice);

    boolean OpenPort(String str);

    boolean OpenPort(String str, String str2);

    byte[] ReadData(int i);

    void SetReadTimeout(int i);

    void SetWriteTimeout(int i);

    int WriteData(byte[] bArr);

    int WriteData(byte[] bArr, int i);

    int WriteData(byte[] bArr, int i, int i2);
}
