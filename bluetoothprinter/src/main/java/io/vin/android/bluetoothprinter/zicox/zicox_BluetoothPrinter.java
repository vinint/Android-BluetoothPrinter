package io.vin.android.bluetoothprinter.zicox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import io.vin.android.bluetoothprinter.zicox.core._PrinterPageImpl;
import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;


public class zicox_BluetoothPrinter implements IBluetoothPrinterProtocol {

    private Context context;
    private String mPrinterName;

    public zicox_BluetoothPrinter(String printerModelName) {
        this.mPrinterName = printerModelName;
    }

    static int _w;
    static int _h;
    static int _orientation;
    private static OutputStream myOutStream = null;
    private static InputStream myInStream = null;
    private static BluetoothSocket mySocket = null;
    private static BluetoothAdapter myBluetoothAdapter;
    private static BluetoothDevice myDevice;
    private static int myBitmapHeight = 0;
    private static int myBitmapWidth = 0;
    private static int PrinterDotWidth = 576;
    private static int PrinterDotPerMM = 8;
    private int _r;
    //private int _gap=0;
    private static _PrinterPageImpl impl = new _PrinterPageImpl();

    private static boolean zp_printer_status_detect() {
        byte data[] = new byte[4];
        data[0] = 0x1d;
        data[1] = (byte) 0x99;
        return SPPWrite(data, 2);
    }

    public static int zp_printer_status_get(int timeout) {
        byte readata[] = new byte[4];
        int a = 0;
        if (!SPPReadTimeout(readata, 4, timeout)) {

            return -1;
        }
        if (readata[0] != (byte) 0x1D)
            return -1;
        if (readata[1] != (byte) 0x99)
            return -1;
        if (readata[3] != (byte) 0xFF)
            return -1;

        byte status = readata[2];
        if ((status & 0x1) != 0)
            a = 1;// 缺纸
        if ((status & 0x02) != 0)
            a = 2;// 开盖

        return a;
    }

    /**************************************************************/
    private static boolean SPPReadTimeout(byte[] Data, int DataLen, int Timeout) {
        int i;
        for (i = 0; i < (Timeout / 5); i++) {
            try {
                if (myInStream.available() >= DataLen) {
                    try {
                        myInStream.read(Data, 0, DataLen);
                        return true;
                    } catch (IOException e) {

                        return false;
                    }
                }
            } catch (IOException e) {

                return false;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {

                return false;
            }
        }

        return false;
    }

    /************************************************************************************
     * *************************************************************************
     * *********
     */


    public boolean connect(String address) {

        if (address == "")
            return false;
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBluetoothAdapter == null)
            return false;
        myDevice = myBluetoothAdapter.getRemoteDevice(address);
        if (myDevice == null)
            return false;
        if (!SPPOpen(myBluetoothAdapter, myDevice))
            return false;
        return true;
        // isConnected=true;
    }

    private UUID SPP_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private boolean SPPOpen(BluetoothAdapter BluetoothAdapter,
                            BluetoothDevice btDevice) {

        Log.e("a", "SPPOpen");
        myBluetoothAdapter = BluetoothAdapter;
        myDevice = btDevice;

        if (!myBluetoothAdapter.isEnabled()) {
            return false;
        }
        myBluetoothAdapter.cancelDiscovery();
        try {
            mySocket = myDevice.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            mySocket.connect();
        } catch (IOException e2) {
            return false;
        }

        try {
            myOutStream = mySocket.getOutputStream();
        } catch (IOException e3) {
            try {
                mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        try {
            myInStream = mySocket.getInputStream();
        } catch (IOException e3) {
            try {
                mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        Log.e("a", "SPPOpen OK");
        return true;
    }

    private void SPPClose() {
        try {
            mySocket.close();
        } catch (IOException e) {
        }
        // isConnected=false;
    }

    @Override
    public void disconnect() {
        SPPClose();
    }


    private static boolean SPPWrite(byte[] Data, int DataLen) {
        // if(!isConnected)return false;
        try {
            myOutStream.write(Data, 0, DataLen);
        } catch (IOException e) {

            return false;
        }
        return true;
    }


    public void Write(byte[] Data) {
        SPPWrite(Data);
    }

    public static boolean SPPWrite(byte[] Data) {
        try {
            myOutStream.write(Data);
        } catch (IOException e) {

            return false;
        }
        return true;
    }

    @Override
    public void connect(String bluetoothAddress, ConnectCallback connectCallback) {

        if (!connect(bluetoothAddress)) {
            connectCallback.onConnectFail("");
        } else {
            connectCallback.onConnectSuccess();
        }

    }

    @Override
    public void initWithSocket(BluetoothSocket bluetoothSocket) {


    }

    @Override
    public void resetSocket() {


    }

    @Override
    public void setPage(int width, int height, int orientation) {


        if (orientation == 0) _r = 0;
        if (orientation == 1) _r = 180;
        if (orientation == 2) _r = 90;
        if (orientation == 3) _r = 270;
        impl.Create(width, height);
    }

    @Override
    public void drawLine(int startX, int startY, int endX, int endY,
                         int lineWidth, int lineStyle) {

        impl.DrawLine(startX, startY, endX, endY, lineWidth);
    }

    @Override
    public void drawRect(int leftTopX, int leftTopY, int rightBottomX,
                         int rightBottomY, int lineWidth, int lineStyle) {

        impl.Drawbox(leftTopX, leftTopY, rightBottomX, rightBottomY, lineWidth);
    }

    @Override
    public void drawText(int startX, int startY, int width, int height,
                         String text, int fontSize, int textStyle, int color, int rotation) {

        if (width == 0 || height == 0) {
            impl.DrawText(startX, startY, text, fontSize, rotation, 0, false, false, 0);
        } else {
            int f_height = 0;

            if (fontSize == 16) {
                f_height = 16;
            }
            if (fontSize == 20) {
                f_height = 20;
            }
            if (fontSize == 28) {
                f_height = 28;
            }
            if (fontSize == 24) {
                f_height = 24;
            }
            if (fontSize == 32) {
                f_height = 32;
            }
            if (fontSize == 40) {

                f_height = 40;
            }
            if (fontSize == 48) {
                f_height = 48;
            }
            if (fontSize == 56) {
                f_height = 56;
            }
            if (fontSize == 64) {
                f_height = 64;
            }

            char[] array1 = text.toCharArray();

            int a = 0, b = 0, cc = 0;

            for (char c : array1) {
                if (c >= '0' && c <= '9') {
                    a++;
                } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                    b++;
                } else {
                    cc++;
                }
            }
            int _length = (a * (f_height / 2)) + (b * (f_height / 2)) + (cc * f_height);
            char[] array = text.toCharArray();
            int mNum = 0, eNum = 0;
            int y = 0;
            String ss = "";
            int i = 0;
            int _size = 0;
            for (char c : array) {

                if (c >= '0' && c <= '9') {
                    ss = ss + String.valueOf(text.charAt(i));
                    i++;
                    mNum = (f_height / 2) + mNum;
                    eNum = (f_height / 2) + eNum;
                    _size = _length - eNum;

                    if (mNum >= width) {
                        impl.DrawText(startX, startY + y, ss, fontSize, 0, 0, false, false, 0);
                        y = y + f_height;
                        ss = "";
                        mNum = 0;
                    }
                } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {

                    ss = ss + String.valueOf(text.charAt(i));
                    i++;
                    mNum = (f_height / 2) + mNum;
                    eNum = (f_height / 2) + eNum;
                    _size = _length - eNum;
                    if (mNum >= width) {
                        impl.DrawText(startX, startY + y, ss, fontSize, 0, 0, false, false, 0);
                        y = y + f_height;
                        ss = "";
                        mNum = 0;
                    }
                } else {
                    ss = ss + String.valueOf(text.charAt(i));
                    i++;
                    mNum = f_height + mNum;
                    eNum = f_height + eNum;
                    _size = _length - eNum;
                    if (mNum >= width) {
                        impl.DrawText(startX, startY + y, ss, fontSize, 0, 0, false, false, 0);
                        y = y + f_height;
                        ss = "";
                        mNum = 0;
                    }
                }
            }
            impl.DrawText(startX, startY + y, ss, fontSize, rotation, 0, false, false, 0);
            y = y + f_height;
            ss = "";
            mNum = 0;
        }
    }

    @Override
    public void drawBarCode(int startX, int startY, int height, int lineWidth,
                            String text, int type, int rotation) {

        String type_ = "128";
        if (type == 0)
            type_ = "128";
        if (type == 1)
            type_ = "39";
        if (type == 2)
            type_ = "93";
        if (type == 3)
            type_ = "CODABAR";
        if (type == 4)
            type_ = "EAN8";
        if (type == 5)
            type_ = "EAN13";
        if (type == 6)
            type_ = "UPCA";
        if (type == 7)
            type_ = "UPCE";
        if (type == 8)
            type_ = "I2OF5";
        impl.DrawBarcode1D(type_, startX, startY, text, lineWidth, height, rotation, 0);
    }

    @Override
    public void drawQRCode(int startX, int startY, String text, int unitWidth,
                           int level, int rotation) {

        String type_ = "L";
        if (level == 0)
            type_ = "L";
        if (level == 1)
            type_ = "M";
        if (level == 2)
            type_ = "Q";
        if (level == 3)
            type_ = "H";

        impl.DrawBarcodeQRcode(startX, startY, text, unitWidth, type_, false);
    }

    @Override
    public void drawImage(int startX, int startY, Bitmap bitmap, int width,
                          int height) {

        impl.DrawBitmap(bitmap, startX, startY, false);
    }

    @Override
    public void feedToNextLabel() {

        //_gap=1;
        SPPWrite(new byte[]{0x1d, 0x0c});

    }

    @Override
    public int getPrinterWidth() {

        return 0;
    }

    @Override
    public int getPrinterStatus() {
        int status = -1;

        if (mySocket == null||!mySocket.isConnected()){
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
            return status;
        }

        if (zp_printer_status_detect()){
            status = zp_printer_status_get(8000);
        }else {
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
        }
        return status;
    }

    @Override
    public void print(PrintCallback printCallback) {

        SPPWrite(impl.GetData(_r, 0), impl.getDataLen());

    }

    @Override
    public void printAndFeed(PrintCallback printCallback) {

        SPPWrite(impl.GetData(_r, 1), impl.getDataLen());
    }

    @Override
    public boolean isFullySupport() {

        return true;
    }


}
