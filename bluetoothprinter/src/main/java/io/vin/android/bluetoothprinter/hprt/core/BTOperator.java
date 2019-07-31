package io.vin.android.bluetoothprinter.hprt.core;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Build.VERSION;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class BTOperator implements IPort {
    private static String InPrinterName = "";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String PrinterName = "";
    private static String bluetoothAddress = "";
    public static boolean isShake = true;
    private String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private int IsReConnect = 0;
    private boolean Is_BLE_Type = false;
    private boolean Isokread = false;
    private boolean Isreturn = false;
    private Context PreContext = null;
    public boolean blnOpenPort = false;
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothDevice mmDevice;
    public InputStream mmInStream;
    public OutputStream mmOutStream;
    public BluetoothSocket mmSocket;
    int num = 0;
    byte[] readData1;
    private int readDataN;
    private Readerthread readerthread;
    private Thread timing1;

    public class Readerthread extends Thread {
        public Readerthread(byte[] Data) {
            BTOperator.this.readData1 = Data;
        }

        public void run() {
            super.run();
            BTOperator.this.timing1 = new Thread() {
                public void run() {
                    int e = 0;
                    while (e < 2) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(1000);
                            e++;
                        } catch (InterruptedException e2) {
                            BTOperator.this.readDataN = -1;
                            BTOperator.this.Isokread = false;
                            return;
                        }
                    }
                    BTOperator.this.readDataN = -1;
                    BTOperator.this.Isokread = false;
                }
            };
            BTOperator.this.timing1.start();
            try {
                BTOperator.this.readDataN = BTOperator.this.mmInStream.read(BTOperator.this.readData1);
                BTOperator.this.Isokread = false;
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }
    }

    public BTOperator(Context context) {
        this.PreContext = context;
        InPrinterName = "HPRT";
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BTOperator(Context context, String strPrinterName) {
        this.PreContext = context;
        PrinterName = strPrinterName;
        InPrinterName = strPrinterName;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void IsBLEType(boolean isBLEType) {
        this.Is_BLE_Type = isBLEType;
    }

    public void InitPort() {
    }

    public void SetReadTimeout(int readTimeout) {
    }

    public void SetWriteTimeout(int writeTimeout) {
    }

    public boolean OpenPort(UsbDevice usbdevice) {
        return false;
    }

    public boolean OpenPort(String PortParam, String PortNumber) {
        return false;
    }

    @SuppressLint({"NewApi"})
    public boolean OpenPort(String PortParam) {
        boolean isOldVersion = false;
        this.mBluetoothAdapter.cancelDiscovery();
        bluetoothAddress = PortParam;
        if (bluetoothAddress == null) {
            return false;
        }
        if (!bluetoothAddress.contains(":")) {
            return false;
        }
        if (bluetoothAddress.length() != 17) {
            return false;
        }
        if (VERSION.SDK_INT < 15) {
            isOldVersion = true;
        }
        try {
            this.mmDevice = this.mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
            if (isOldVersion) {
                this.mmSocket = this.mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } else {
                this.mmSocket = this.mmDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            }
            this.mBluetoothAdapter.cancelDiscovery();
            if (this.mBluetoothAdapter.isDiscovering()) {
                int e = 0;
                while (e < 5) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    e++;
                    if (this.mBluetoothAdapter.cancelDiscovery()) {
                        break;
                    }
                }
            }
            this.mmSocket.connect();
        } catch (Exception e2) {
            try {
                this.mmSocket = (BluetoothSocket) this.mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{Integer.TYPE}).invoke(this.mmDevice, new Object[]{Integer.valueOf(1)});
                if (this.mBluetoothAdapter.isDiscovering()) {
                    int iCnt = 0;
                    while (iCnt < 5) {
                        TimeUnit.MILLISECONDS.sleep(500);
                        iCnt++;
                        if (this.mBluetoothAdapter.cancelDiscovery()) {
                            break;
                        }
                    }
                }
                this.mmSocket.connect();
            } catch (Exception var7) {
                Log.d("PRTLIB", "BTO_ConnectDevice --> create " + var7.getMessage());
                return false;
            }
        }
        try {
            PrinterName = this.mmDevice.getName();
            this.blnOpenPort = GetIOInterface();
            if (this.blnOpenPort && isShake) {
                this.num = 0;
                this.blnOpenPort = CheckPrinter();
                if (this.blnOpenPort) {
                    return this.blnOpenPort;
                }
                ClosePort();
            }
            return this.blnOpenPort;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean ClosePort() {
        try {
            if (this.mmInStream != null) {
                this.mmInStream.close();
                this.mmInStream = null;
            }
            if (this.mmOutStream != null) {
                this.mmOutStream.close();
                this.mmOutStream = null;
            }
            if (this.mmSocket == null) {
                return true;
            }
            this.mmSocket.close();
            this.mmSocket = null;
            return true;
        } catch (IOException var3) {
            System.out.println("BTO_ConnectDevice close " + var3.getMessage());
            return false;
        }
    }

    public int WriteData(byte[] Data) {
        return WriteData(Data, 0, Data.length);
    }

    public int WriteData(byte[] Data, int intDataLength) {
        return WriteData(Data, 0, intDataLength);
    }

    public int WriteData(byte[] Data, int intOffset, int intDataLength) {
        try {
            if (this.mmOutStream == null) {
                return -1;
            }
            if (this.IsReConnect >= 2) {
                return -1;
            }
            int bytetohex;
            byte[] e = new byte[10000];
            int numb = intDataLength / 10000;
            for (int buffend = 0; buffend < numb; buffend++) {
                for (bytetohex = buffend * 10000; bytetohex < (buffend + 1) * 10000; bytetohex++) {
                    e[bytetohex % 10000] = Data[bytetohex];
                }
                this.mmOutStream.write(e, 0, e.length);
//                this.mmOutStream.flush();
                if (HPRTPrinterHelper.isWriteLog && HPRTPrinterHelper.isHex) {
                    HPRTPrinterHelper.bytetohex(e);
                }
            }
            if (intDataLength % 10000 != 0) {
                byte[] var10 = new byte[(Data.length - (numb * 10000))];
                for (bytetohex = numb * 10000; bytetohex < Data.length; bytetohex++) {
                    var10[bytetohex - (numb * 10000)] = Data[bytetohex];
                }
                this.mmOutStream.write(var10, 0, var10.length);
//                this.mmOutStream.flush();
                if (HPRTPrinterHelper.isWriteLog && HPRTPrinterHelper.isHex) {
                    HPRTPrinterHelper.bytetohex(var10);
                }
            }
            this.IsReConnect = 0;
            return intDataLength;
        } catch (IOException var9) {
            if (this.blnOpenPort) {
                if (this.IsReConnect == 1) {
                    this.IsReConnect = 0;
                    return -1;
                } else if (OpenPort(bluetoothAddress)) {
                    this.IsReConnect++;
                    return WriteData(Data, intOffset, intDataLength);
                }
            }
            this.IsReConnect = 0;
            Log.d("PRTLIB", "WriteData --> error " + var9.getMessage());
            return -1;
        }
    }

    public byte[] ReadData(int second) {
        byte[] Data = new byte[0];
        if (this.mmInStream == null) {
            return Data;
        }
        if (this.IsReConnect >= 2) {
            return Data;
        }
        int e = 0;
        while (e < second * 10) {
            try {
                int available = this.mmInStream.available();
                if (available > 0) {
                    Data = new byte[available];
                    this.mmInStream.read(Data);
                    e = (second * 10) + 1;
                } else {
                    TimeUnit.MILLISECONDS.sleep(100);
                    e++;
                }
            } catch (IOException var6) {
                var6.printStackTrace();
                break;
            } catch (InterruptedException var7) {
                Data = new byte[]{110};
                var7.printStackTrace();
                break;
            }
        }
        return Data;
    }

    public boolean IsOpen() {
        return this.blnOpenPort;
    }

    public String GetPortType() {
        return "Bluetooth";
    }

    public String GetPrinterName() {
        return PrinterName;
    }

    private boolean GetIOInterface() {
        Log.d("PRTLIB", "BTO_GetIOInterface...");
        try {
            this.mmInStream = this.mmSocket.getInputStream();
            this.mmOutStream = this.mmSocket.getOutputStream();
            return true;
        } catch (IOException var2) {
            Log.d("PRTLIB", "BTO_GetIOInterface " + var2.getMessage());
            return false;
        }
    }

    public String GetPrinterModel() {
        return PrinterName;
    }

    private boolean CheckPrinter() {
        Log.d("PRTLIB", "CheckPrinter...");
        byte[] MD5Return = new byte[16];
        byte[] MD5Rand = new byte[19];
        HPRTPrinterHelper.logcat("MD5Rand:" + HPRTPrinterHelper.bytetohex(MD5Rand));
        HPRTPrinterHelper.logcat("MD5Return:" + HPRTPrinterHelper.bytetohex(MD5Return));
        byte[] MD5Test = new byte[4];
        String data = "";
        if (WriteData(MD5Rand) <= 0) {
            Log.d("PRTLIB", "CheckPrinterNot Right Printer.Write Error!");
            return false;
        }
        byte[] var10 = ReadData(3);
        Log.e("PrinterReturn:", HPRTPrinterHelper.bytetohex(var10));
        HPRTPrinterHelper.logcat("PrinterReturn:" + HPRTPrinterHelper.bytetohex(var10));
        int var11 = var10.length;
        if (var11 == 0) {
            if (WriteData(MD5Rand) <= 0) {
                return false;
            }
            var10 = ReadData(3);
            if (var10.length == 0) {
                return false;
            }
        }
        for (int i = 0; i < var11; i++) {
            if (MD5Return[i] != var10[i]) {
                Log.d("PRTLIB", "CheckPrinterNot Right Printer." + MD5Return.toString());
                return false;
            }
        }
        Log.d("PRTLIB", "CheckPrinterRight Printer succeed.");
        return true;
    }

    public int Readdata(byte[] Data) {
        this.Isokread = true;
        this.Isreturn = true;
        this.readDataN = 0;
        this.readerthread = new Readerthread(Data);
        this.readerthread.start();
        while (this.Isreturn) {
            if (!this.Isokread) {
                if (this.readerthread != null) {
                    Readerthread dummy = this.readerthread;
                    this.readerthread = null;
                    dummy.interrupt();
                    Thread dummy1 = this.timing1;
                    this.timing1 = null;
                    dummy1.interrupt();
                }
                this.Isreturn = false;
            }
        }
        return this.readDataN;
    }
}
