package io.vin.android.bluetoothprinter.kuaimai.core;

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
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class BTOperator implements IPort {
    private static final UUID f = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String g = "";
    private static String h = "";
    private static String i = "";
    public static boolean isShake = true;
    private String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private BluetoothAdapter bluetoothAdapter;
    public InputStream inputStream;
    public OutputStream outputStream;
    public BluetoothSocket bluetoothSocket;
    public BluetoothDevice bluetoothDevice;
    private Context context = null;
    public boolean isOpen = false;
    private int l = 0;
    private boolean m = false;
    private Thread thread;
    private Readerthread readerthread;
    private int p;
    private boolean q = false;
    private boolean r = false;
    byte[] s;
    int t = 0;

    public class Readerthread extends Thread {
        public Readerthread(byte[] bArr) {
            BTOperator.this.s = bArr;
        }

        public void run() {
            super.run();
            BTOperator.this.thread = new Thread() {
                public final void run() {
                    try {
                        for (int i = 0; i < 2; i++) {
                            TimeUnit.MILLISECONDS.sleep(1000);
                        }
                        BTOperator.this.p = -1;
                        BTOperator.this.q = false;
                    } catch (InterruptedException e) {
                        BTOperator.this.p = -1;
                        BTOperator.this.q = false;
                    }
                }
            };
            BTOperator.this.thread.start();
            try {
                BTOperator.this.p = BTOperator.this.inputStream.read(BTOperator.this.s);
                BTOperator.this.q = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BTOperator(Context context) {
        this.context = context;
        i = "HPRT";
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BTOperator(Context context, String str) {
        this.context = context;
        h = str;
        i = str;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void IsBLEType(boolean z) {
        this.m = z;
    }

    public void InitPort() {
    }

    public void SetReadTimeout(int i) {
    }

    public void SetWriteTimeout(int i) {
    }

    public boolean OpenPort(UsbDevice usbDevice) {
        return false;
    }

    public boolean OpenPort(String str, String str2) {
        return false;
    }

    @SuppressLint({"NewApi"})
    public boolean OpenPort(String str) {
        Object obj = 1;
        this.bluetoothAdapter.cancelDiscovery();
        g = str;
        if (str == null) {
            return false;
        }
        if (!g.contains(":")) {
            return false;
        }
        if (g.length() != 17) {
            return false;
        }
        if (VERSION.SDK_INT >= 15) {
            obj = null;
        }
        int i;
        try {
            this.bluetoothDevice = this.bluetoothAdapter.getRemoteDevice(g);
            if (obj != null) {
                this.bluetoothSocket = this.bluetoothDevice.createRfcommSocketToServiceRecord(f);
            } else {
                this.bluetoothSocket = this.bluetoothDevice.createInsecureRfcommSocketToServiceRecord(f);
            }
            this.bluetoothAdapter.cancelDiscovery();
            if (this.bluetoothAdapter.isDiscovering()) {
                i = 0;
                while (i < 5) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    i++;
                    if (this.bluetoothAdapter.cancelDiscovery()) {
                        break;
                    }
                }
            }
            this.bluetoothSocket.connect();
        } catch (Exception e) {
            try {
                this.bluetoothSocket = (BluetoothSocket) this.bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{Integer.TYPE}).invoke(this.bluetoothDevice, new Object[]{Integer.valueOf(1)});
                if (this.bluetoothAdapter.isDiscovering()) {
                    i = 0;
                    while (i < 5) {
                        TimeUnit.MILLISECONDS.sleep(500);
                        i++;
                        if (this.bluetoothAdapter.cancelDiscovery()) {
                            break;
                        }
                    }
                }
                this.bluetoothSocket.connect();
            } catch (Exception e2) {
                Log.d("PRTLIB", "BTO_ConnectDevice --> create " + e2.getMessage());
                return false;
            }
        }
        try {
            h = this.bluetoothDevice.getName();
            this.isOpen = a();
//            if (this.isOpen && isShake) {
//                this.t = 0;
//                this.isOpen = b();
//                if (this.isOpen) {
//                    return this.isOpen;
//                }
//                ClosePort();
//            }
            return this.isOpen;
        } catch (Exception e22) {
            e22.printStackTrace();
            return false;
        }
    }

    public boolean ClosePort() {
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
                this.inputStream = null;
            }
            if (this.outputStream != null) {
                this.outputStream.close();
                this.outputStream = null;
            }
            if (this.bluetoothSocket == null) {
                return true;
            }
            this.bluetoothSocket.close();
            this.bluetoothSocket = null;
            return true;
        } catch (IOException e) {
            System.out.println("BTO_ConnectDevice close " + e.getMessage());
            return false;
        }
    }

    public int WriteData(byte[] bArr) {
        return WriteData(bArr, 0, bArr.length);
    }

    public int WriteData(byte[] bArr, int i) {
        return WriteData(bArr, 0, i);
    }

    public int WriteData(byte[] bArr, int i, int i2) {
        try {
            Log.d("TSPL指令",new String(bArr,"GBK")+"\r\n");
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
        try {
            if (this.outputStream == null) {
                return -1;
            }
            if (this.l >= 2) {
                return -1;
            }
            int i3;
            byte[] bArr2 = new byte[10000];
            int i4 = i2 / 10000;
            for (int i5 = 0; i5 < i4; i5++) {
                for (i3 = i5 * 10000; i3 < (i5 + 1) * 10000; i3++) {
                    bArr2[i3 % 10000] = bArr[i3];
                }
                this.outputStream.write(bArr2, 0, bArr2.length);
                this.outputStream.flush();
                if (KuiaMaiPrinterHelper.isWriteLog) {
                    if (KuiaMaiPrinterHelper.isHex) {
                        LogUlit.writeFileToSDCard(KuiaMaiPrinterHelper.bytetohex(bArr2).getBytes(), HPRTConst.FOLDER, HPRTConst.FOLDER_NAME, true, true);
                    } else {
                        LogUlit.writeFileToSDCard(bArr2, HPRTConst.FOLDER, HPRTConst.FOLDER_NAME, true, true);
                    }
                }
            }
            if (i2 % 10000 != 0) {
                byte[] bArr3 = new byte[(bArr.length - (i4 * 10000))];
                for (i3 = i4 * 10000; i3 < bArr.length; i3++) {
                    bArr3[i3 - (i4 * 10000)] = bArr[i3];
                }
                this.outputStream.write(bArr3, 0, bArr3.length);
                this.outputStream.flush();
                if (KuiaMaiPrinterHelper.isWriteLog) {
                    if (KuiaMaiPrinterHelper.isHex) {
                        LogUlit.writeFileToSDCard(KuiaMaiPrinterHelper.bytetohex(bArr3).getBytes(), HPRTConst.FOLDER, HPRTConst.FOLDER_NAME, true, true);
                    } else {
                        LogUlit.writeFileToSDCard(bArr3, HPRTConst.FOLDER, HPRTConst.FOLDER_NAME, true, true);
                    }
                }
            }
            this.l = 0;
            return i2;
        } catch (IOException e) {
            if (this.isOpen) {
                if (this.l == 1) {
                    this.l = 0;
                    return -1;
                } else if (OpenPort(g)) {
                    this.l++;
                    return WriteData(bArr, i, i2);
                }
            }
            this.l = 0;
            Log.d("PRTLIB", "WriteData --> error " + e.getMessage());
            return -1;
        }
    }

    public byte[] ReadData(int i) {
        IOException e;
        InterruptedException e2;
        int i2 = 0;
        byte[] bArr = new byte[0];
        if (this.inputStream != null && this.l < 2) {
            while (i2 < i * 10) {
                try {
                    int available = this.inputStream.available();
                    if (available > 0) {
                        byte[] bArr2 = new byte[available];
                        try {
                            this.inputStream.read(bArr2);
                            bArr = bArr2;
                            i2 = (i * 10) + 1;
                        } catch (IOException e3) {
                            IOException iOException = e3;
                            bArr = bArr2;
                            e = iOException;
                        }
                    } else {
                        TimeUnit.MILLISECONDS.sleep(100);
                        i2++;
                    }
                } catch (IOException e5) {
                    e = e5;
                } catch (InterruptedException e6) {
                    e2 = e6;
                }
            }
        }
        return bArr;
    }

    public boolean IsOpen() {
        return this.isOpen;
    }

    public String GetPortType() {
        return "Bluetooth";
    }

    public String GetPrinterName() {
        return h;
    }

    private boolean a() {
        Log.d("PRTLIB", "BTO_GetIOInterface...");
        try {
            this.inputStream = this.bluetoothSocket.getInputStream();
            this.outputStream = this.bluetoothSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            Log.d("PRTLIB", "BTO_GetIOInterface " + e.getMessage());
            return false;
        }
    }

    public String GetPrinterModel() {
        return h;
    }

    private boolean b() {
        Log.d("PRTLIB", "CheckPrinter...");
        byte[] obj = new byte[16];
        byte[] bArr = new byte[19];
        KuiaMaiPrinterHelper.logcat("MD5Rand:" + KuiaMaiPrinterHelper.bytetohex(bArr));
        KuiaMaiPrinterHelper.logcat("MD5Return:" + KuiaMaiPrinterHelper.bytetohex(obj));
        if (WriteData(bArr) > 0) {
            byte[] ReadData = ReadData(3);
            KuiaMaiPrinterHelper.logcat("PrinterReturn:" + KuiaMaiPrinterHelper.bytetohex(ReadData));
            int length = ReadData.length;
            if (length == 0) {
                if (WriteData(bArr) <= 0) {
                    return false;
                }
                ReadData = ReadData(3);
                if (ReadData.length == 0) {
                    return false;
                }
            }
            for (int i = 0; i < length; i++) {
                if (obj[i] != ReadData[i]) {
                    Log.d("PRTLIB", "CheckPrinterNot Right Printer." + obj.toString());
                    return false;
                }
            }
            Log.d("PRTLIB", "CheckPrinterRight Printer succeed.");
            return true;
        }
        Log.d("PRTLIB", "CheckPrinterNot Right Printer.Write Error!");
        return false;
    }

    public int Readdata(byte[] bArr) {
        this.q = true;
        this.r = true;
        this.p = 0;
        this.readerthread = new Readerthread(bArr);
        this.readerthread.start();
        while (this.r) {
            if (!this.q) {
                if (this.readerthread != null) {
                    Thread thread = this.readerthread;
                    this.readerthread = null;
                    thread.interrupt();
                    thread = this.thread;
                    this.thread = null;
                    thread.interrupt();
                }
                this.r = false;
            }
        }
        return this.p;
    }
}
