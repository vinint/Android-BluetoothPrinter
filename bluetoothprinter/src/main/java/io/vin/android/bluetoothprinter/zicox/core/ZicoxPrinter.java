package io.vin.android.bluetoothprinter.zicox.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vin.android.bluetoothprinterprotocol.PrintCallback;


public class ZicoxPrinter {
    private UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int _pagerotate;
    private int h;
    private ZicoxPrintImpl impl = new ZicoxPrintImpl();
    private boolean isConnected;
    private BluetoothAdapter myBluetoothAdapter;
    private BluetoothDevice myDevice;
    private InputStream myInStream = null;
    private OutputStream myOutStream = null;
    public BluetoothSocket mySocket = null;
    private int w;

    public class zpPrintCallback implements PrintCallback {
        public void onPrintFail(int errorCode) {
        }

        public void onPrintSuccess() {
        }
    }

    private void Create(int width, int height) {
        this.impl.Create(width, height);
    }

    private byte[] GetPageData(boolean r) {
        return this.impl.GetData(r);
    }

    private int GetPageDataLen() {
        return this.impl.getDataLen();
    }

    private void _feed() {
        this.impl.feed();
    }

    private void Drawbox(int x0, int y0, int x1, int y1, int lineWidth) {
        this.impl.Drawbox(x0, y0, x1, y1, lineWidth);
    }

    private void DrawInverse(int x0, int y0, int x1, int y1, int lineWidth) {
        this.impl.INVERSE(x0, y0, x1, y1, lineWidth);
    }

    private void DrawLine1(int x0, int y0, int x1, int y1, int lineWidth) {
        this.impl.DrawLine(x0, y0, x1, y1, lineWidth);
    }

    private void DrawBitmap(int x, int y, Bitmap bitmap, boolean rotate) {
        this.impl.DrawBitmap(bitmap, x, y, rotate);
    }

    private void DrawBarcode1D(int x, int y, String text, String type, int lineWidth, int height, int rotate) {
        this.impl.DrawBarcode1D(type, x, y, text, lineWidth, height, rotate);
    }

    private void DrawText(int text_x, int text_y, String text, int fontSize, int fontzoom, int rotate, boolean bold, boolean reverse, boolean underline) {
        this.impl.DrawText(text_x, text_y, text, fontSize, fontzoom, rotate, bold, reverse, underline);
    }

    private void DrawBarcodeQRcode(int x, int y, String text, int size, int level, int rotation) {
        String levelText;
        switch (level) {
            case 0:
                levelText = "L";
                break;
            case 1:
                levelText = "M";
                break;
            case 2:
                levelText = "Q";
                break;
            default:
                levelText = "H";
                break;
        }
        this.impl.DrawBarcodeQRcode(x, y, text, size, levelText, rotation);
    }

    private void DrawBarcodeQRcode(int x, int y, String text, int size, String errLeval, int rotate) {
        this.impl.DrawBarcodeQRcode(x, y, text, size, errLeval, rotate);
    }

    private void _text(int pageWidth, int pageHeight) {
        this.impl.makeDrawText(pageWidth, pageHeight);
    }

    private void _line(int pageWidth, int pageHeight) {
        this.impl.makeDrawLine(pageWidth, pageHeight);
    }

    private static void _Box(int pageWidth, int pageHeight) {
    }

    private void _Bitmap(int pageWidth, int pageHeight) {
        this.impl.makeDrawBitmap(pageWidth, pageHeight);
    }

    private void _Barcode1D(int pageWidth, int pageHeight) {
        this.impl.makeDrawBarcode1D(pageWidth, pageHeight);
    }

    private void _BarcodeQRcode(int pageWidth, int pageHeight) {
        this.impl.makeDrawBarcodeQRcode(pageWidth, pageHeight);
    }

    public void drawBarCode(String text, int x, int y, int height, int lineWidth, int type, int rotation) {
        DrawBarcode1D(x, y, text, "128", lineWidth, height, rotation);
    }

    public void drawDashLine(int color, int x1, int y1, int x2, int y2, int lineWidth, int solid, int blank) {
        for (int i = 0; i < x2; i = ((i + 16) - 1) + 1) {
            DrawText(x1 + i, y1 - 10, "-", 24, 0, 0, false, false, false);
        }
    }

    public void drawImage(Bitmap bitmap, int x, int y) {
        DrawBitmap(x, y, bitmap, false);
    }

    public void drawImage(Bitmap bitmap, int x, int y, int width, int height) {
        DrawBitmap(x, y, bitmap, false);
    }

    public void drawLine(int color, int x1, int y1, int x2, int y2, int lineWidth) {
        DrawLine1(x1, y1, x2, y2, lineWidth);
    }

    public void drawQRCode(String text, int x, int y, int unitWidth, int version, int level, int rotation) {
        if (!TextUtils.isEmpty(text) && unitWidth >= 1) {
            DrawBarcodeQRcode(x, y, text, unitWidth, level, rotation);
        }
    }

    public void drawRect(int color, int x1, int y1, int x2, int y2, int lineWidth) {
        Drawbox(x1, y1, x2, y2, lineWidth);
    }

    public void drawRectFill(int color, int x1, int y1, int x2, int y2) {
        DrawInverse(x1, x2, y1, y2, y2 - y1);
    }

    public void drawText(String text, int x, int y, int color, int fontSize, int style, int rotation) {
        boolean _underline = false;
        boolean _bold = false;
        switch (style) {
            case 0:
                _underline = false;
                _bold = false;
                break;
            case 1:
                _bold = true;
                break;
            case 3:
                _bold = true;
                break;
            case 4:
                _underline = true;
                break;
            case 5:
                _bold = true;
                _underline = true;
                break;
            case 6:
                _underline = true;
                break;
            case 7:
                _bold = true;
                _underline = true;
                break;
        }
        DrawText(x, y, text, fontSize, 0, rotation, _bold, false, _underline);
    }

    public void feedToNextLabel() {
        zp_goto_mark_label(30);
    }

    public int getPrintWidth() {
        return 0;
    }

    public void print(PrintCallback callback) {
        boolean rotate = true;
        zp_printer_status_detect();
        int inx = zp_printer_status_get(8000);
        if (inx == -1) {
            callback.onPrintFail(-1);
        }
        if (inx == 1) {
            callback.onPrintFail(1);
        }
        if (inx == 2) {
            callback.onPrintFail(2);
        }
        if (inx == 0) {
            callback.onPrintSuccess();
        }
        if (this._pagerotate == 0) {
            rotate = false;
        }
        _Bitmap(this.w, this.h);
        _text(this.w, this.h);
        _line(this.w, this.h);
        _Box(this.w, this.h);
        _Barcode1D(this.w, this.h);
        _BarcodeQRcode(this.w, this.h);
        SPPWrite(GetPageData(rotate), GetPageDataLen());
    }

    public void printAndFeed(PrintCallback callback) {
        zp_printer_status_detect();
        int inx = zp_printer_status_get(8000);
        if (inx == -1) {
            callback.onPrintFail(-1);
        } else if (inx == 1) {
            callback.onPrintFail(1);
        } else if (inx == 2) {
            callback.onPrintFail(2);
        }
        boolean rotate = this._pagerotate != 0;
        _Bitmap(this.w, this.h);
        _text(this.w, this.h);
        _line(this.w, this.h);
        _Box(this.w, this.h);
        _Barcode1D(this.w, this.h);
        _BarcodeQRcode(this.w, this.h);
        SPPWrite(GetPageData(rotate), GetPageDataLen());
        zp_goto_mark_label(30);
        zp_printer_status_detect();
        inx = zp_printer_status_get(8000);
        if (inx == -1) {
            callback.onPrintFail(-1);
        }
        if (inx == 1) {
            callback.onPrintFail(1);
        }
        if (inx == 2) {
            callback.onPrintFail(2);
        }
        if (inx == 0) {
            callback.onPrintSuccess();
        }
    }

    public void setPage(int width, int height, int orientation) {
        this.w = width;
        this.h = height;
        this._pagerotate = orientation;
        Create(width, height);
    }

    public boolean connect(String add) {
        if (TextUtils.isEmpty(add)) {
            return false;
        }
        this.myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.myBluetoothAdapter == null) {
            return false;
        }
        this.myDevice = this.myBluetoothAdapter.getRemoteDevice(add);
        if (this.myDevice == null || !SPPOpen(this.myBluetoothAdapter, this.myDevice)) {
            return false;
        }
        this.isConnected = true;
        return true;
    }

    public void disconnect() {
        SPPClose();
    }

    public boolean zp_goto_mark_label(int MaxFeedMM) {
        return SPPWrite(new byte[]{(byte) 29, (byte) 12}, 2);
    }

    public boolean SPPWrite(byte[] Data, int DataLen) {
        if (this.isConnected) {
            try {
                this.myOutStream.write(Data, 0, DataLen);
            } catch (IOException e) {
            }
        }
        return false;
    }

    private boolean SPPReadTimeout(byte[] Data, int DataLen, int Timeout) {
        int i = 0;
        while (i < Timeout / 5) {
            try {
                if (this.myInStream.available() >= DataLen) {
                    try {
                        this.myInStream.read(Data, 0, DataLen);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                    i++;
                } catch (InterruptedException e2) {
                    return false;
                }
            } catch (IOException e3) {
                return false;
            }
        }
        return false;
    }

    private void SPPClose() {
        try {
            this.mySocket.close();
        } catch (IOException e) {
        }
        this.isConnected = false;
    }

    private boolean SPPOpen(BluetoothAdapter BluetoothAdapter, BluetoothDevice btDevice) {
        this.myBluetoothAdapter = BluetoothAdapter;
        this.myDevice = btDevice;
        if (!this.myBluetoothAdapter.isEnabled()) {
            return false;
        }
        this.myBluetoothAdapter.cancelDiscovery();
        try {
            this.mySocket = this.myDevice.createRfcommSocketToServiceRecord(this.SPP_UUID);
            try {
                this.mySocket.connect();
                try {
                    this.myOutStream = this.mySocket.getOutputStream();
                    try {
                        this.myInStream = this.mySocket.getInputStream();
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } catch (IOException e2) {
                        try {
                            this.mySocket.close();
                            return false;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return false;
                        }
                    }
                } catch (IOException e4) {
                    try {
                        this.mySocket.close();
                        return false;
                    } catch (IOException e32) {
                        e32.printStackTrace();
                        return false;
                    }
                }
            } catch (IOException e5) {
                return false;
            }
        } catch (IOException e322) {
            e322.printStackTrace();
            return false;
        }
    }

    public void zp_printer_status_detect() {
        SPPWrite(new byte[]{(byte) 29, (byte) -103, (byte) 0, (byte) 0}, 4);
    }

    public int zp_printer_status_get(int timeout) {
        byte[] readata = new byte[4];
        int a = 0;
        if (!SPPReadTimeout(readata, 4, timeout) || readata[0] != (byte) 29 || readata[1] != (byte) -103 || readata[3] != (byte) -1) {
            return -1;
        }
        byte status = readata[2];
        if ((status & 1) != 0) {
            a = 1;
        }
        if ((status & 2) != 0) {
            a = 2;
        }
        return a;
    }

    public void initPrinterWithSocket(BluetoothSocket socket) {
        this.mySocket = socket;
        try {
            this.myOutStream = this.mySocket.getOutputStream();
            this.myInStream = this.mySocket.getInputStream();
            this.isConnected = true;
        } catch (IOException e) {
            this.mySocket = null;
        }
    }

    public void resetPrinterSocket() {
        this.mySocket = null;
        this.myOutStream = null;
        this.myInStream = null;
        this.isConnected = false;
    }
}
