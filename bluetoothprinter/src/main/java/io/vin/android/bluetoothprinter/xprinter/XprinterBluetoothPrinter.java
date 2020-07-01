package io.vin.android.bluetoothprinter.xprinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

public class XprinterBluetoothPrinter implements IBluetoothPrinterProtocol {

    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket mBtSocket;
    public InputStream mInStream;
    public OutputStream mOutStream;

    private String mPrinterName;
    private String mPrinterAddr;

    String LanguageEncode = "gb2312";

    public XprinterBluetoothPrinter(String mPrinterName) {
        this.mPrinterName = mPrinterName;
    }

    @Override
    public void connect(String bluetoothAddress, ConnectCallback connectCallback) {
        this.mPrinterAddr = bluetoothAddress;
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
        try {
            //device.setPin("0000".getBytes());
            BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();
            if (bluetoothSocket.isConnected()) {
                //初始化一些属性
                mBtSocket = bluetoothSocket;
                mOutStream = bluetoothSocket.getOutputStream();
                mInStream = bluetoothSocket.getInputStream();
                connectCallback.onConnectSuccess();
            } else {
                connectCallback.onConnectFail("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            connectCallback.onConnectFail(ex.getMessage());
        }

    }

    @Override
    public void disconnect() {
        mBtSocket = null;
        mInStream = null;
        mOutStream = null;
    }

    @Override
    public void initWithSocket(BluetoothSocket bluetoothSocket) {
        try {
            mPrinterName = bluetoothSocket.getRemoteDevice().getName();
            mPrinterAddr = bluetoothSocket.getRemoteDevice().getAddress();
            mBtSocket = bluetoothSocket;
            mOutStream = bluetoothSocket.getOutputStream();
            mInStream = bluetoothSocket.getInputStream();
        } catch (IOException ex) {

        }
    }

    @Override
    public void resetSocket() {
        mBtSocket = null;
        mInStream = null;
        mOutStream = null;
    }

    @Override
    public void setPage(int width, int height, int orientation) {

        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("! 0 200 200 " + height + " 1 \r\n");
        strBuffer.append("PW " + width + "\r\n");

        if (orientation == ORIENTATION_DEFAULT)
            strBuffer.append("PRINT-ORIENT 0\r\n");
        else
            strBuffer.append("PRINT-ORIENT 1\r\n");

        byte[] data = new byte[0];
        try {
            data = strBuffer.toString().getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writeData(data);
        Log.d("TAG", strBuffer.toString());
    }

    @Override
    public void drawLine(int startX, int startY, int endX, int endY, int lineWidth, int lineStyle) {
        byte[] data = new byte[0];
        String str = String.format("LINE %d %d %d %d %d\r\n", startX, startY, endX, endY, lineWidth);

        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writeData(data);
        Log.d("TAG", str);
    }

    @Override
    public void drawRect(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int lineWidth, int lineStyle) {
        byte[] data = new byte[0];
        String str = String.format("BOX %d %d %d %d %d\r\n", leftTopX, leftTopY, rightBottomX, rightBottomY, lineWidth);

        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);
        Log.d("TAG", str);
    }

    public void printText(int startX, int startY, String text, int fontSize, int textStyle, int rotation) {

        String ul = "UNDERLINE OFF\r\n";
        String bold = "SETBOLD 0\r\n";
        String font = "0";
        String cmd = "T";
        String str = "";
        int xMul = 1;
        int yMul = 1;
        int size = 0;

        switch (fontSize) {
            case FONT_SIZE_16:
                font = "1";
                xMul = 1;
                yMul = 1;
                break;
            case FONT_SIZE_20:
                font = "1";
                xMul = 1;
                yMul = 1;
                break;
            case FONT_SIZE_24:
                font = "0";
                xMul = 1;
                yMul = 1;
                break;
            case FONT_SIZE_28:
                font = "0";
                xMul = 1;
                yMul = 1;
                break;
            case FONT_SIZE_32:
                font = "1";
                xMul = 2;
                yMul = 2;
                break;
            case FONT_SIZE_40:
                font = "1";
                xMul = 2;
                yMul = 2;
                break;
            case FONT_SIZE_48:
                font = "0";
                xMul = 2;
                yMul = 2;
                break;
            case FONT_SIZE_56:
                font = "0";
                xMul = 2;
                yMul = 2;
                break;
            case FONT_SIZE_64:
                font = "1";
                xMul = 4;
                yMul = 4;
                break;
            case FONT_SIZE_72:
                font = "0";
                xMul = 3;
                yMul = 3;
                break;
            case FONT_SIZE_84:
                font = "0";
                xMul = 3;
                yMul = 3;
                break;
            case FONT_SIZE_96:
                font = "0";
                xMul = 4;
                yMul = 4;
                break;
            case FONT_SIZE_128:
                font = "0";
                xMul = 5;
                yMul = 5;
                break;
            default:
                font = "1";
                xMul = 1;
                yMul = 1;
                break;
        }

        switch (rotation) {
            case 0:
                cmd = "T";
                break;
            case 90:
                cmd = "T270";
                break;
            case 180:
                cmd = "T180";
                break;
            case 270:
                cmd = "T90";
                break;
            default:
                cmd = "T";
                break;
        }

        if ((STYLE_TEXT_BOLD & textStyle) == STYLE_TEXT_BOLD)
            bold = "SETBOLD 2\r\n";
        if ((STYLE_TEXT_UNDERLINE & textStyle) == STYLE_TEXT_UNDERLINE)
            ul = "UNDERLINE ON\r\n";

        str += bold;
        str += ul;
        str += String.format("SETMAG %d %d\r\n", xMul, yMul);
        str += String.format("%s %s %d %d %d %s\r\n", cmd, font, size, startX, startY, text);

        byte data[] = new byte[0];
        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);

        Log.d("TAG", str);
    }

    @Override
    public void drawText(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation) {

        if (TextUtils.isEmpty(text)) {
            return;
        }

        //不换行
        if (width == 0 || height == 0) {
            printText(startX, startY, text, fontSize, textStyle, rotation);

        } else {
            //换行
            int widthTmp = 0;
            int startYTmp = startY;
            String textTmp = "";
            char[] array = text.toCharArray();
            for (int i = 0; i < array.length; ++i) {
                if ((char) ((byte) array[i]) != array[i]) {
                    //中文
                    widthTmp += fontSize;
                } else {
                    //英文
                    widthTmp += fontSize / 2;
                }

                if (widthTmp >= width) {
                    textTmp += String.valueOf(array[i]);
                    printText(startX, startYTmp, textTmp, fontSize, textStyle, rotation);
                    widthTmp = 0;
                    startYTmp += fontSize + 2;
                    textTmp = "";
                } else {
                    textTmp += String.valueOf(array[i]);
                }
            }

            if (!textTmp.isEmpty()) {
                printText(startX, startYTmp, textTmp, fontSize, textStyle, rotation);
            }
        }
    }

    @Override
    public void drawBarCode(int startX, int startY, int height, int lineWidth, String text, int type, int rotation) {

        if (TextUtils.isEmpty(text)) {
            return;
        }

        String codeType = "128";
        String cmd;
        String str;
        int ratio = 1; //wid : narrow = 2:1

        switch (type) {
            case STYLE_BARCODE_CODE128:
                codeType = "128";
                break;
            case STYLE_BARCODE_CODE39:
                codeType = "39";
                break;
            case STYLE_BARCODE_CODE93:
                codeType = "93";
                break;
            case STYLE_BARCODE_CODABAR:
                codeType = "CODABAR";
                break;
            case STYLE_BARCODE_EAN8:
                codeType = "EAN8";
                break;
            case STYLE_BARCODE_EAN13:
                codeType = "EAN13";
                break;
            case STYLE_BARCODE_UPCA:
                codeType = "UPCA";
                break;
            case STYLE_BARCODE_UPCE:
                codeType = "UPCE";
                break;
            case STYLE_BARCODE_I2OF5:
                codeType = "I2OF5";
                break;
        }

        if (rotation == 90 || rotation == 270)
            cmd = "VB";
        else
            cmd = "B";

        str = String.format("%s %s %d %d %d %d %d %s\r\n", cmd, codeType, lineWidth, ratio, height, startX, startY, text);

        byte data[] = new byte[0];
        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);
        Log.d("TAG", str);
    }

    @Override
    public void drawQRCode(int startX, int startY, String text, int unitWidth, int level, int rotation) {

        if (TextUtils.isEmpty(text)) {
            return;
        }

        String str = "";
        String eccLevel = "M";


        switch (level) {
            case QRCODE_CORRECTION_LEVEL_L:
                eccLevel = "L";
                break;
            case QRCODE_CORRECTION_LEVEL_M:
                eccLevel = "M";
                break;
            case QRCODE_CORRECTION_LEVEL_Q:
                eccLevel = "Q";
                break;
            case QRCODE_CORRECTION_LEVEL_H:
                eccLevel = "H";
                break;
            default:
                eccLevel = "M";
        }


        str = String.format("BARCODE QR %d %d M %d U %d\r\n", startX, startY, 2, unitWidth);
        str += String.format("%sA,%s\r\n", eccLevel, text);
        str += "ENDQR\r\n";

        byte data[] = new byte[0];
        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);
        Log.d("TAG", str);
    }

    @Override
    public void drawImage(int startX, int startY, Bitmap bitmap, int width, int height) {

        String str = "";
        str = String.format("CG %d %d %d %d ", (width + 7) / 8, height, startX, startY);
        byte[] imgData = imageProcess(bitmap);

        byte data[] = new byte[0];
        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);
        writeData(imgData);

        Log.d("TAG", str);
    }

    @Override
    public void feedToNextLabel() {
        byte data[] = new byte[0];
        String str = "HOME\r\n";

        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);
        Log.d("TAG", str);
    }

    @Override
    public int getPrinterWidth() {
        return 0;
    }

    @Override
    public int getPrinterStatus() {
        int status = -1;
        if (mBtSocket == null || !mBtSocket.isConnected()) {
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
            return status;
        }
        //1.查询状态指令
        byte[] bArr = new byte[]{0x1D, 0x99 - 256, 0x00, 0x00};
        //2.写入指令
        if (writeData(bArr)) {
            //3.读取返回结果
            byte[] statusBytes = readData(2);

            if (statusBytes.length > 0 && statusBytes[2] == 0) {
                status = IBluetoothPrinterProtocol.STATUS_OK;
            } else if (statusBytes.length > 0 && ((statusBytes[2] & 0x01) > 0)) {
                status = IBluetoothPrinterProtocol.STATUS_NOPAPER;
            } else if (statusBytes.length > 0 && ((statusBytes[2] & 0x02) > 0)) {
                status = IBluetoothPrinterProtocol.STATUS_COVER_OPENED;
            } else if (statusBytes.length > 0 && ((statusBytes[2] & 0x04) > 0)) {
                status = IBluetoothPrinterProtocol.STATUS_OVER_HEATING;
            } else if (statusBytes.length > 0 && ((statusBytes[2] & 0x08) > 0)) {
                status = IBluetoothPrinterProtocol.STATUS_BATTERY_LOW;
            } else if (statusBytes.length > 0 && ((statusBytes[2] & 0x10) > 0)) {
                status = IBluetoothPrinterProtocol.STATUS_PRINTING;
            }
        } else {
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
        }

        Log.d("TAG", "status = " + status + "");

        return status;
    }

    @Override
    public void print(PrintCallback printCallback) {
        String str = "PRINT\r\n";
        byte data[] = new byte[0];

        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);
        Log.d("TAG", str);
    }

    @Override
    public void printAndFeed(PrintCallback printCallback) {

        String str = "FORM\r\nPRINT\r\n";
        byte data[] = new byte[0];

        try {
            data = str.getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writeData(data);
        Log.d("TAG", str);
    }

    @Override
    public boolean isFullySupport() {
        return true;
    }


    public boolean writeData(byte[] data) {
        boolean result = false;
        int length = data.length;
        try {
            if (this.mOutStream == null) {
                return result;
            }
            int i3;
            byte[] bArr = new byte[10000];
            int i4 = length / 10000;
            for (int i5 = 0; i5 < i4; i5++) {
                for (i3 = i5 * 10000; i3 < (i5 + 1) * 10000; i3++) {
                    bArr[i3 % 10000] = data[i3];
                }
                this.mOutStream.write(bArr, 0, bArr.length);
                this.mOutStream.flush();
            }
            if (length % 10000 != 0) {
                byte[] bArr2 = new byte[(data.length - (i4 * 10000))];
                for (i3 = i4 * 10000; i3 < data.length; i3++) {
                    bArr2[i3 - (i4 * 10000)] = data[i3];
                }
                this.mOutStream.write(bArr2, 0, bArr2.length);
                this.mOutStream.flush();
            }
            result = true;
        } catch (IOException e) {
            result = false;
        }

        return result;
    }

    //读取数据
    private byte[] readData(int second) {
        byte[] data = new byte[0];
        if (this.mInStream == null) {
            return data;
        }
        int e = 0;
        while (e < second) {
            try {
                int available = this.mInStream.available();
                if (available > 0) {
                    data = new byte[available];
                    this.mInStream.read(data);
                    return data;
                } else {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    e++;
                }
            } catch (IOException var6) {
                var6.printStackTrace();
            } catch (InterruptedException var7) {
                data = new byte[]{110};
                var7.printStackTrace();
            }
        }
        return data;
    }

    private byte[] imageProcess(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int index = 0;
        try {
            int bytesWidth;
            int i;
            if (width % 8 == 0) {
                bytesWidth = width / 8;
            } else {
                bytesWidth = (width / 8) + 1;
            }
            int byteSize = height * bytesWidth;
            byte[] bArr = new byte[byteSize];
            for (i = 0; i < byteSize; i++) {
                bArr[i] = (byte) 0;
            }
            for (int j = 0; j < height; j++) {
                int[] tempArray = new int[width];
                bitmap.getPixels(tempArray, 0, width, 0, j, width, 1);
                int indexLine = 0;
                for (i = 0; i < width; i++) {
                    indexLine++;
                    int pixel = tempArray[i];
                    if (indexLine > 8) {
                        indexLine = 1;
                        index++;
                    }
                    if (pixel != -1) {
                        int temp = 1 << (8 - indexLine);
                        if (((Color.red(pixel) + Color.green(pixel)) + Color.blue(pixel)) / 3 < 128) {
                            bArr[index] = (byte) (bArr[index] | temp);
                        }
                    }
                }
                index = bytesWidth * (j + 1);
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
