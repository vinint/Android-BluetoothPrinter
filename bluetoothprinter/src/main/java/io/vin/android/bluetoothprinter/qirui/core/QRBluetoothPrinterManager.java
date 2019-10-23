package io.vin.android.bluetoothprinter.qirui.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;


public class QRBluetoothPrinterManager {
    private long mCurrentTimeMillis = 0;
    private static final String TAG = QRBluetoothPrinterManager.class.getSimpleName();
    public int PrinterName;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ConnectCallback connectCallback;
    private BluetoothDevice device;
    private InputStream inputStream;
    private IntentFilter intentFilter;
    private boolean isConnected = false;
    private boolean isPrinted = false;
    private boolean isRunning = false;
    private int orientation = 0;
    private OutputStream outputStream;
    private PrintCallback printCallback;
    public BluetoothSocket socket;
    private Runnable stopSPPScanRunnable;
    public boolean isQRprinter = true;

    private Map<String, Boolean> printerMacMap = new HashMap<>();

    public QRBluetoothPrinterManager(String name) {
        if (name.contains("380")) {
            this.PrinterName = 1;
        } else {
            this.PrinterName = 0;
        }
    }

    public void connectPrinter(String address, ConnectCallback connectCallback) {
        this.device = this.bluetoothAdapter.getRemoteDevice(address);
        this.connectCallback = connectCallback;
        boolean isPrinterConnected = false;
        for (int i = 0; i < 3; i++) {
            sleep(200);
            if (SPPConnect()) {
                isPrinterConnected = true;
                break;
            }
        }
        if (isPrinterConnected) {
            onDeviceConnected();
            if (PrinterName == 1) {
                Boolean tmp = printerMacMap.get(address);
                if (tmp != null) {
                    isQRprinter = tmp;
                } else {
                    isQRprinter = !checkIsHprtPrinter();
                    printerMacMap.put(address, isQRprinter);
                }
            }
        } else {
            connectCallback.onConnectFail("连接失败，请重试");
        }
    }

    private void onDeviceConnected() {
        this.isConnected = true;
        if (this.connectCallback != null) {
            this.connectCallback.onConnectSuccess();
        }
        this.isRunning = true;
//        runRecDataThread();
    }

    public void disconnect() {
        disSppConnect();
        if (this.isConnected) {
            this.isRunning = false;
            this.isConnected = false;
        }
    }

    public void setPage(int width, int height, int orientation) {
        this.orientation = orientation;
        this.isPrinted = false;
        write("! 0 200 200 " + height + " 1\r\nPAGE-WIDTH " + width + "\r\n");
    }

    public void drawRect(int color, int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int lineWidth) {
        write("BOX " + leftTopX + " " + leftTopY + " " + rightBottomX + " " + rightBottomY + " " + lineWidth + "\r\n");
    }

    public void drawRectFill(int color, int x1, int y1, int x2, int y2) {
        write("INVERSE-LINE " + x1 + " " + y1 + " " + x2 + " " + y1 + " " + Math.abs(y2 - y1) + "\r\n");
    }

    public void drawLine(int color, int x1, int y1, int x2, int y2, int lineWidth) {
        write("LINE " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + lineWidth + "\r\n");
    }

    public void drawDashLine(int color, int startX, int startY, int endX, int endY, int lineWidth, int solid, int blank) {
        write("LPLINE " + startX + " " + startY + " " + endX + " " + endY + " " + lineWidth + "\r\n");
    }

    public void drawText(String text, int x, int y, int color, int fontSize, int style, int rotation) {
        String textCommand;
        String fontString;
        String sizeString;
        String stringData = "";
        if ((style & 1) == 1) {
            stringData = stringData + "SETBOLD 1\r\n";
        }
        if ((style & 2) == 2) {
        }
        if ((style & 4) == 4) {
            stringData = stringData + "UNDERLINE ON\r\n";
        }
        switch (rotation) {
            case 0:
                if (color != 1) {
                    textCommand = "T";
                    break;
                } else {
                    textCommand = "TR";
                    break;
                }
            case 90:
                if (color != 1) {
                    textCommand = "T90";
                    break;
                } else {
                    textCommand = "TR90";
                    break;
                }
            case 180:
                if (color != 1) {
                    textCommand = "T180";
                    break;
                } else {
                    textCommand = "TR180";
                    break;
                }
            case 270:
                if (color != 1) {
                    textCommand = "T270";
                    break;
                } else {
                    textCommand = "TR270";
                    break;
                }
            default:
                if (color != 1) {
                    textCommand = "T";
                    break;
                } else {
                    textCommand = "TR";
                    break;
                }
        }
        int scale = 0;
        if (this.PrinterName != 1) {
            switch (fontSize) {
                case 16:
                    fontString = "55";
                    sizeString = "0";
                    break;
                case 20:
                    fontString = "6";
                    sizeString = "0";
                    break;
                case 24:
                    fontString = "8";
                    sizeString = "0";
                    break;
                case 28:
                    fontString = "7";
                    sizeString = "0";
                    break;
                case 32:
                    fontString = "4";
                    sizeString = "0";
                    break;
                case 40:
                    fontString = "6";
                    sizeString = "0";
                    scale = 2;
                    break;
                case 48:
                    fontString = "8";
                    sizeString = "0";
                    scale = 2;
                    break;
                case 56:
                    fontString = "7";
                    sizeString = "0";
                    scale = 2;
                    break;
                case 64:
                    fontString = "4";
                    sizeString = "0";
                    scale = 2;
                    break;
                case 72:
                    fontString = "8";
                    sizeString = "0";
                    scale = 3;
                    break;
                case 84:
                    fontString = "7";
                    sizeString = "0";
                    scale = 3;
                    break;
                case 96:
                    fontString = "4";
                    sizeString = "0";
                    scale = 3;
                    break;
                default:
                    fontString = "55";
                    sizeString = "0";
                    scale = 1;
                    break;
            }
        } else {
            switch (fontSize) {
                case 16:
                    fontString = "55";
                    sizeString = "0";
                    break;
                case 20:
                    fontString = "3";
                    sizeString = "1";
                    break;
                case 24:
                    fontString = "24";
                    sizeString = "0";
                    break;
                case 28:
                    fontString = "28";
                    sizeString = "0";
                    break;
                case 32:
                    fontString = "4";
                    sizeString = "0";
                    break;
                case 40:
                    fontString = "3";
                    sizeString = "1";
                    scale = 2;
                    break;
                case 48:
                    fontString = "24";
                    sizeString = "0";
                    scale = 2;
                    break;
                case 56:
                    fontString = "4";
                    sizeString = "3";
                    break;
                case 64:
                    fontString = "4";
                    sizeString = "0";
                    scale = 2;
                    break;
                case 72:
                    fontString = "24";
                    sizeString = "0";
                    scale = 3;
                    break;
                case 84:
                    fontString = "28";
                    sizeString = "0";
                    scale = 3;
                    break;
                case 96:
                    fontString = "4";
                    sizeString = "0";
                    scale = 3;
                    break;
                default:
                    fontString = "55";
                    sizeString = "0";
                    scale = 1;
                    break;
            }
        }
        write("SETMAG " + scale + " " + scale + "\r\n");
        stringData = stringData + textCommand + " " + fontString + " " + sizeString + " " + x + " " + y + " " + text + "\r\n";
        if ((style & 1) == 1) {
            stringData = stringData + "SETBOLD 0\r\n";
        }
        if ((style & 4) == 4) {
            stringData = stringData + "UNDERLINE OFF\r\n";
        }
        write(stringData);
        write("SETMAG 1 1 \r\n");
    }

    public void drawBarCode(String text, int x, int y, int height, int lineWidth, int type, int rotation) {
        if (!TextUtils.isEmpty(text)) {
            String bartype;
            String command = "B";
            if (rotation == 90) {
                command = "VB";
            } else if (rotation == 180) {
                command = "B";
            } else if (rotation == 270) {
                command = "VB";
            }
            switch (type) {
                case 0:
                    bartype = "128";
                    break;
                case 1:
                    bartype = "39";
                    break;
                case 2:
                    bartype = "93";
                    break;
                case 3:
                    bartype = "CODABAR";
                    break;
                case 4:
                    bartype = "EAN8";
                    break;
                case 5:
                    bartype = "EAN13";
                    break;
                case 6:
                    bartype = "UPCA";
                    break;
                case 7:
                    bartype = "UPCE";
                    break;
                case 8:
                    bartype = "I2OF5";
                    break;
                default:
                    bartype = "128";
                    break;
            }
            write(command + " " + bartype + " " + lineWidth + " 1 " + height + " " + x + " " + y + " " + text + "\r\n");
        }
    }

    public void drawQRCode(String text, int x, int y, int dotWidth, int version, int level, int rotation) {
        if (!TextUtils.isEmpty(text) && dotWidth >= 1) {
            String levelText;
            String B = "BARCODE";
            String VB = "VBARCODE";
            String cmd = B;
            if (this.PrinterName == 1) {
                if (rotation == 90) {
                } else if (rotation == 180) {

                } else if (rotation == 270) {
                }
            } else if (rotation == 90) {
                cmd = VB;
            } else if (rotation == 180) {
                cmd = B;
            } else if (rotation == 270) {
                cmd = VB;
            }
            if (dotWidth < 2) {
                dotWidth = 2;
            }
            if (dotWidth > 6) {
                dotWidth = 6;
            }
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
            write("SETQRVER " + level + "\n");
            write(cmd + " QR " + x + " " + y + " M 2 U " + dotWidth + "\r\n" + levelText + "A," + text + "\r\nENDQR\r\n");
        }
    }

    public void drawImage(Bitmap bmp, int x, int y) {
        if (bmp != null) {
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            int bytesPerRow = ((width - 1) / 8) + 1;
            String data = printHexString(ImageUtils.getCompressedBinaryzationBytes(bmp, true));
            StringBuilder sb = new StringBuilder();

            sb.append("EG");
            sb.append(" ");
            sb.append(bytesPerRow);
            sb.append(" ");
            sb.append(height);
            sb.append(" ");
            sb.append(x);
            sb.append(" ");
            sb.append(y);
            sb.append(" ");
            sb.append(data);
            sb.append("\r\n");
            write(sb.toString());
        }
    }

    private byte[] getByte(byte[] dataByte, int start, int length) {
        byte[] newDataByte = new byte[length];
        for (int i = 0; i < length; i++) {
            newDataByte[i] = dataByte[start + i];
        }
        return newDataByte;
    }

    private String printHexString(byte[] b) {
        String a = "";
        for (byte b2 : b) {
            String hex = Integer.toHexString(b2 & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            a = a + hex;
        }
        return a;
    }

    private String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    private String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    private byte[] imageProcess(Bitmap bitmap, int width, int height) {
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

    public void drawImage(Bitmap bitmap, int x, int y, int width, int height) {
        drawImage(resizeImage(bitmap, width, height), x, y);
    }

    private Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w) / ((float) width);
        float scaleHeight = ((float) h) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public void print(PrintCallback printCallback) {
        mCurrentTimeMillis = System.currentTimeMillis();
        String stringData;
        this.printCallback = printCallback;
        switch (this.orientation) {
            case 0:
                stringData = "PRINT\r\n";
                break;
            case 1:
                stringData = "POPRINT\r\n";
                break;
            case 2:
                stringData = "PRINT\r\n";
                break;
            case 3:
                stringData = "PRINT\r\n";
                break;
            default:
                stringData = "PRINT\r\n";
                break;
        }
        write(stringData);
//        new Timer().schedule(new TimerTask() {
//            public void run() {
//                if (!QRBluetoothPrinterManager.this.isPrinted) {
//                    QRBluetoothPrinterManager.this.write(new byte[]{(byte) 16, (byte) 4, (byte) 5});
//                }
//            }
//        }, 10000);
    }

    public void printAndFeed(PrintCallback printCallback) {
        feedToNextLabel();
        print(printCallback);
        if (isQRprinter && (this.PrinterName == 1)) {
            write(new byte[]{(byte) 14});
            return;
        }
    }

    public void feedToNextLabel() {
        if (isQRprinter && (this.PrinterName == 1)) {
            //启瑞QR380A
        } else {
            write("GAP-SENSE\r\nFORM\r\n");
        }
    }

    public int getPrintWidth() {
        return 576;
    }

    private IntentFilter getIntentFilter() {
        if (this.intentFilter == null) {
            this.intentFilter = new IntentFilter();
            this.intentFilter.addAction("android.bluetooth.device.action.FOUND");
            this.intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
            this.intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
            this.intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED");
            this.intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            this.intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        }
        return this.intentFilter;
    }

    private void setupRunnable() {
        if (this.stopSPPScanRunnable == null) {
            this.stopSPPScanRunnable = new Runnable() {
                public void run() {
                    if (QRBluetoothPrinterManager.this.bluetoothAdapter.isDiscovering()) {
                        QRBluetoothPrinterManager.this.bluetoothAdapter.cancelDiscovery();
                    }
                }
            };
        }
    }

    private boolean stopSPPSearch() {
        if (this.bluetoothAdapter.isDiscovering()) {
            return this.bluetoothAdapter.cancelDiscovery();
        }
        return true;
    }

    private synchronized boolean SPPConnect() {
        boolean result;
        result = false;
        try {
            initSocket();
            sleep(100);
            try {
                this.socket.connect();
                sleep(100);
                result = true;
            } catch (IOException e) {
                this.connectCallback.onConnectFail(e.toString());
                sleep(100);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            this.connectCallback.onConnectFail(e2.toString());
        }
        return result;
    }

    private void initSocket() throws IOException {
        String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        this.socket = this.device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        this.inputStream = this.socket.getInputStream();
        this.outputStream = this.socket.getOutputStream();
    }

    private synchronized void disSppConnect() {
        new Thread(new Runnable() {
            public void run() {
                QRBluetoothPrinterManager.this.isRunning = false;
                try {
                    if (QRBluetoothPrinterManager.this.socket != null) {
                        QRBluetoothPrinterManager.this.socket.close();
                        QRBluetoothPrinterManager.this.socket = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int write(byte[] b) {
        int packets = 0;
        int length_bytes = b.length;
        if (length_bytes > 1000) {
            int startPoint = 0;
            byte[] bytes = new byte[1000];
            while (length_bytes > 1000) {
                System.arraycopy(b, startPoint, bytes, 0, 1000);
                try {
                    this.outputStream.write(bytes);
                    startPoint += 1000;
                    length_bytes -= 1000;
                    packets++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (length_bytes != 1000) {
                length_bytes = b.length % 1000;
            }
            if (length_bytes > 0) {
                byte[] bytes_last = new byte[length_bytes];
                System.arraycopy(b, startPoint, bytes_last, 0, length_bytes);
                try {
                    this.outputStream.write(bytes_last);
                    packets++;
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return packets;
        }
        try {
            this.outputStream.write(b);
            packets = 0 + 1;
        } catch (IOException e22) {
            e22.printStackTrace();
        }
        return packets;
    }

    public int write(byte[] b, int offset, int len) {
        byte[] by = new byte[len];
        System.arraycopy(b, offset, by, 0, len);
        return write(by);
    }

    public int write(String string) {
        int i = 0;
        if (string.length() >= 1) {
            try {
                i = write(string.getBytes("GB2312"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    private void runRecDataThread() {
        new Thread(new Runnable() {
            public void run() {
                byte[] buffer = new byte[1000];
                while (QRBluetoothPrinterManager.this.isRunning) {
                    try {
                        if (QRBluetoothPrinterManager.this.inputStream != null) {
                            int length = QRBluetoothPrinterManager.this.inputStream.read(buffer);
                        }
                        if (QRBluetoothPrinterManager.this.printCallback != null) {
                            Log.i(QRBluetoothPrinterManager.TAG, QRBluetoothPrinterManager.this.ByteArrToHex(buffer));
                            if (buffer[0] == (byte) 79 && buffer[1] == (byte) 75) {
                                QRBluetoothPrinterManager.this.printCallback.onPrintSuccess();
                                QRBluetoothPrinterManager.this.isPrinted = true;
                            } else if (buffer[0] == (byte) 69 && buffer[1] == (byte) 82) {
                                QRBluetoothPrinterManager.this.write(new byte[]{(byte) 16, (byte) 4, (byte) 5});
                                QRBluetoothPrinterManager.this.isPrinted = true;
                            } else if ((buffer[0] & 16) != 0) {
                                QRBluetoothPrinterManager.this.printCallback.onPrintFail(1);
                            } else if ((buffer[0] & 1) != 0) {
                                QRBluetoothPrinterManager.this.printCallback.onPrintFail(2);
                            }
                        }
                    } catch (IOException e) {
                        QRBluetoothPrinterManager.this.isRunning = false;
                    }
                }
            }
        }).start();
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initPrinterWithSocket(BluetoothSocket socket) {
        this.socket = socket;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
            onDeviceConnected();
            if (PrinterName == 1) {
                String address = socket.getRemoteDevice().getAddress();
                Boolean tmp = printerMacMap.get(address);
                if (tmp != null) {
                    isQRprinter = tmp;
                } else {
                    isQRprinter = !checkIsHprtPrinter();
                    printerMacMap.put(address, isQRprinter);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetPrinterSocket() {
        this.socket = null;
        this.inputStream = null;
        this.outputStream = null;
        this.isConnected = false;
        this.isRunning = false;
    }

    /**
     * Method     获取启瑞打印机状态
     * Parameters
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/8/21 上午10:20
     * Modifytime 2018/8/21 上午10:20
     */
    public int printerStatusQR() {
        //防止启瑞打印机 调用打印后，立即调用获取状态，获取不到状态
        long now = System.currentTimeMillis();
        if ((mCurrentTimeMillis != 0) && (now - mCurrentTimeMillis) < 2000) {
            //386设备
            long millis = 2000;
            if (isQRprinter && PrinterName == 1) {
                //380设备
                millis = 2000;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        int status = -1;
        if (this.isConnected && socket.isConnected()) {
            byte[] Cmd = new byte[]{16, 4, 5};
            flushReadBuffer();
            boolean result = false;
            try {
                this.outputStream.write(Cmd, 0, 3);
                result = true;
            } catch (Exception var5) {
                result = false;
            }
            if (!result) {
                status = 32;
                return status;
            } else {
                byte[] Rep = ReadData(3);
                int length = Rep.length;
                if (length > 0) {
                    if (Rep[0] == 0) {
                        //打印机正常
                        status = 0;
                    } else if ((Rep[0] & 16) != 0) {
                        //盖子打开
                        status = 1;
                    } else if ((Rep[0] & 1) != 0) {
                        //缺纸
                        status = 2;
                    } else if ((Rep[0] & 2) != 0) {
                        //打印机过热
                        return IBluetoothPrinterProtocol.STATUS_OVER_HEATING;
                    } else if ((Rep[0] & 4) != 0) {
                        //电量低
                        status = 4;
                    } else if ((Rep[0] & 8) != 0) {
                        //打印中
                        status = 16;
                    } else if (length >= 2 && Rep[0] == 79 && Rep[1] == 75) {
                        status = 0;
                    } else {
                        status = 8;
                    }
                } else {
                    //读不到值，默认打印中
                    status = 16;
                }
            }
        } else {
            status = 32;
        }
        return status;
    }

    /**
     * Method     获取汉印打印机状态
     * Parameters
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/8/21 上午10:20
     * Modifytime 2018/8/21 上午10:20
     */
    public int printerStatusHprt() {
        //防止HM-300刷成QR380打印机 调用打印后，立即调用获取状态，获取不到状态
        long now = System.currentTimeMillis();
        if ((mCurrentTimeMillis != 0) && (now - mCurrentTimeMillis) < 2000) {
            //386设备
            long millis = 2000;
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        int status = -1;
        if (this.isConnected && socket.isConnected()) {
            byte[] Cmd = new byte[]{27, 104};
            flushReadBuffer();
            boolean result = false;
            try {
                this.outputStream.write(Cmd, 0, 2);
                result = true;
            } catch (Exception var5) {
                result = false;
            }
            if (!result) {
                status = 32;
                return status;
            } else {
                byte[] Rep = ReadData(3);
                int length = Rep.length;
                if (length > 0) {
                    int tmpStatus = Rep[length - 1] & 255;
                    if (tmpStatus == 0) {
                        //打印机准备就绪
                        status = 0;
                    } else if (tmpStatus == 1) {
                        //打印机打印中
                        status = 16;
                    } else if (tmpStatus == 2) {
                        //打印机缺纸
                        status = 2;
                    } else if (tmpStatus == 6) {
                        //打印机开盖
                        status = 1;
                    } else if (tmpStatus == 8) {
                        //电量低
                        status = 4;
                    } else if (Rep[0] == 110) {
                        status = -2;
                    } else {
                        //其他错误
                        status = -1;
                    }
                } else {
                    //打印机已经断开
                    status = 32;
                }
            }
        } else {
            status = 32;
        }
        return status;
    }


    /**
     * Method     检查名称为380的机器是否为汉印刷成的
     * Parameters
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/8/20 下午1:48
     * Modifytime 2018/8/20 下午1:48
     */
    public boolean checkValidPrinter2(String address) {
        boolean result = false;
        if (isConnected) {
            byte[] Rep = new byte[1];
            flushReadBuffer();
            boolean writeResult = false;
            byte[] cmdJni = new byte[]{30, 100, 67};
            try {
                this.outputStream.write(cmdJni, 0, cmdJni.length);
                writeResult = true;
            } catch (Exception var5) {
                writeResult = false;
            }
            if (writeResult && read(Rep, 0, 1, 3000) && Rep[0] >= 48 && Rep[0] <= 57) {
                result = true;
            }
        }
        return result;
    }

    public void drawImageCG(int start_x, int start_y, int bmp_size_x, int bmp_size_y, Bitmap bmp) {
        String command = "";
        int bytesWidth;
        if (bmp_size_x % 8 == 0) {
            bytesWidth = bmp_size_x / 8;
        } else {
            bytesWidth = bmp_size_x / 8 + 1;
        }

        if (!(bytesWidth > 999 | bmp_size_y > '\uffff')) {
            byte[] bmpData = this.imageProcess(bmp, bmp_size_x, bmp_size_y);
            command = "CG " + bytesWidth + " " + bmp_size_y + " " + start_x + " " + start_y + " ";
            String back = "\r\n\r\n";
            write(command);
            write(bmpData);
            write(back);
        }
    }

    private boolean flushReadBuffer() {
        byte[] buffer = new byte[64];
        if (!isConnected) {
            return false;
        } else {
            while (true) {
                boolean var2 = false;

                try {
                    int r1 = this.inputStream.available();
                    if (r1 == 0) {
                        return true;
                    }

                    if (r1 > 0) {
                        if (r1 > 64) {
                            r1 = 64;
                        }
                        this.inputStream.read(buffer, 0, r1);
                    }
                } catch (IOException var5) {
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException var4) {
                }
            }
        }
    }

    private boolean read(byte[] buffer, int offset, int length, int timeout_read) {
        if (!isConnected) {
            return false;
        } else {
            if (timeout_read < 200) {
                timeout_read = 200;
            }

            if (timeout_read > 5000) {
                timeout_read = 5000;
            }

            try {
                long start_time = SystemClock.elapsedRealtime();
                long cur_time = 0L;
                int need_read = length;
                boolean var10 = false;

                while (true) {
                    if (this.inputStream.available() > 0) {
                        int cur_readed = this.inputStream.read(buffer, offset, need_read);
                        offset += cur_readed;
                        need_read -= cur_readed;
                    }

                    if (need_read == 0) {
                        return true;
                    }

                    cur_time = SystemClock.elapsedRealtime();
                    if (cur_time - start_time > (long) timeout_read) {
                        return false;
                    }

                    TimeUnit.MILLISECONDS.sleep(20);
                }
            } catch (Exception var11) {
                return false;
            }
        }
    }

    private byte[] ReadData(int second) {
        byte[] data = new byte[0];
        if (this.inputStream == null) {
            return data;
        }
        int e = 0;
        while (e < second) {
            try {
                int available = this.inputStream.available();
                if (available > 0) {
                    data = new byte[available];
                    this.inputStream.read(data);
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

    /**
     * Method     检查是否是汉印刷成的QR380A
     * Parameters []
     * Return     boolean
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2019-04-26 15:38
     * Modifytime 2019-04-26 15:38
     */
    public boolean checkIsHprtPrinter() {
        boolean isHprt = false;
        if (isConnected) {
            flushReadBuffer();
            byte[] cmdJni = new byte[]{27, 104};
            try {
                this.outputStream.write(cmdJni, 0, cmdJni.length);
                byte[] respBytes = ReadData(3);
                if (respBytes == null || respBytes.length == 0) {
                    //启瑞打印机
                    isHprt = false;
                } else {
                    //汉英打印机
                    isHprt = true;
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        return isHprt;
    }

}
