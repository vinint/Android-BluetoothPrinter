package io.vin.android.bluetoothprinter.jiabo;

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
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

/**
 * 佳博打印机驱动-TSPL协议
 */
public class JiaBoBluetoothPrinter implements IBluetoothPrinterProtocol {
    String LanguageEncode = "gb2312";
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket mBtSocket;
    public InputStream mInStream;
    public OutputStream mOutStream;

    /**
     * 蓝牙名称
     */
    private String mPrinterName;
    /**
     * 蓝牙地址
     */
    private String mPrinterAddress;

    public JiaBoBluetoothPrinter(String printerModelName) {
        this.mPrinterName = printerModelName;
    }

    /**
     * 连接打印机
     */
    @Override
    public void connect(String bluetoothAddress, ConnectCallback connectCallback) {
        this.mPrinterAddress = bluetoothAddress;
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
        try {
            BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();
            if (bluetoothSocket.isConnected()) {
                // 初始化属性
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

    /**
     * 断开打印机
     */
    @Override
    public void disconnect() {
        mBtSocket = null;
        mInStream = null;
        mOutStream = null;
    }

    /**
     * 使用 BluetoothSocket 初始化
     */
    @Override
    public void initWithSocket(BluetoothSocket bluetoothSocket) {
        try {
            mPrinterName = bluetoothSocket.getRemoteDevice().getName();
            mPrinterAddress = bluetoothSocket.getRemoteDevice().getAddress();
            mBtSocket = bluetoothSocket;
            mOutStream = bluetoothSocket.getOutputStream();
            mInStream = bluetoothSocket.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 重置 BluetoothSocket
     */
    @Override
    public void resetSocket() {
        mBtSocket = null;
        mInStream = null;
        mOutStream = null;
    }

    /**
     * Method     设置打印纸张大小、旋转角度
     * Parameters
     * width  宽度（单位像素）
     * height 高度（单位像素）
     * orientation 方向
     */
    @Override
    public void setPage(int width, int height, int orientation) {
        StringBuilder strBuilder = new StringBuilder();
        width = width / 8;
        height = height / 8;
        strBuilder.append(String.format(Locale.getDefault(),
                "SIZE %d mm,%d mm\r\n", width, height));

        switch (orientation){
            // 默认
            case ORIENTATION_DEFAULT:
                strBuilder.append("DIRECTION 0\r\n");
                break;
            // 底部
            case ORIENTATION_DOWN:
                strBuilder.append("DIRECTION 1\r\n");
                break;
            default:
                break;
        }

        strBuilder.append("REFERENCE 0,0\r\n");
        strBuilder.append("SET PEEL OFF\r\n");
        strBuilder.append("SET TEAR ON\r\n");
        strBuilder.append("CODEPAGE 437\r\n");
        strBuilder.append("CLS\r\n");
        byte[] data = new byte[0];
        try {
            data = strBuilder.toString().getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * 绘制线条
     * startX 线条起始点 x 坐标
     * startY 线条起始点 y 坐标
     * endX 线条结束点 x 坐标
     * endY 线条结束点 y 坐标
     * lineWidth 线条宽度
     * lineStyle 线条样式
     */
    @Override
    public void drawLine(int startX, int startY, int endX,
                         int endY, int lineWidth,
                         int lineStyle) {
        // BAR x,y,width,height
        // x:起始的 X 坐标。
        // y:起始的 Y 坐标。
        // width:线条的宽度(像素)。
        // height:线条的高度(像素)。

        int width = 0;
        int height = 0;
        if (startX == endX) {
            // 竖线
            if (endY > startY) {
                height = endY - startY;
            } else {
                height = startY - endY;
            }
            width = lineWidth;
        }
        if (startY == endY) {
            // 横线
            height = lineWidth;
            if (endX > startX) {
                width = endX - startX;
            } else {
                width = startX - endX;
            }
        }
        byte[] data = new byte[0];
        try {
            data = String.format(Locale.getDefault(),
                    "BAR %d,%d,%d,%d\r\n", startX, startY, width,
                    height).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * 画矩形
     * leftTopX 矩形框左上角 x 坐标
     * leftTopY 矩形框左上角 y 坐标
     * rightBottomX 矩形框右下角 x 坐标
     * rightBottomY 矩形框右下角 y 坐标
     * lineWidth 线条宽度
     * lineStyle 线条样式
     */
    @Override
    public void drawRect(int leftTopX, int leftTopY, int rightBottomX,
                         int rightBottomY, int lineWidth, int lineStyle) {
        // BOX x,y,x_end,y_end,line thickness[,radius]
        // x_start:左上角 x 坐标。
        // y_start:左上角 y 坐标。
        // x_end:右下角 x 坐标。
        // y_end:右下角 y 坐标。
        // Thickness:线条宽度。
//        int x_start = leftTopX;
//        int y_start = leftTopY;
//        int x_end = rightBottomX;
//        int y_end = rightBottomY;
//        int Thickness = lineWidth;
        byte[] data = new byte[0];
        try {
            data = String.format(Locale.getDefault(),
                    "BOX %d,%d,%d,%d,%d\r\n",
                    leftTopX, leftTopY, rightBottomX, rightBottomY, lineWidth)
                    .getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * Method     画文字
     * Parameters
     * startX     文字起始X坐标
     * startY     文字起始Y坐标
     * width     文字绘制区域宽度(可以为0，不为零的时候文字要根据宽度自动换行)
     * height    文字绘制区域高度(可以为0，)
     * text      内容
     * fontSize  字体大小（参考上面定义）
     * textStyle 字体样式（参考上面定义）
     * color 文字颜色（参考上面定义）
     * rotation  旋转角度（参考上面定义）
     * Return
     */
    @Override
    public void drawText(int startX, int startY, int width, int height,
                         String text, int fontSize, int textStyle, int color,
                         int rotation) {
        // TEXT x,y,"font",rotation,x-multiplication,y-multiplication,"content"
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (width == 0 || height == 0) {
            // 不换行
            printTextALL(startX, startY, width, height, text, fontSize, textStyle, color, rotation);
        } else {
            // 换行
            int widthTmp = 0;
            int startYTmp = startY;
            String textTmp = "";
            char[] array = text.toCharArray();
            for (int i = 0; i < array.length; ++i) {
                if ((char) ((byte) array[i]) != array[i]) {
                    // 中文
                    widthTmp += fontSize;
                } else {
                    // 英文
                    widthTmp += fontSize / 2;
                }

                if (widthTmp >= width) {
                    textTmp += String.valueOf(array[i]);
                    printTextALL(startX, startYTmp, 0, 0,
                            textTmp, fontSize, textStyle, color, rotation);
                    widthTmp = 0;
                    startYTmp += fontSize + 2;
                    textTmp = "";
                } else {
                    textTmp += String.valueOf(array[i]);
                }
            }

            if (!textTmp.isEmpty()) {
                printTextALL(startX, startYTmp, 0, 0, textTmp, fontSize, textStyle, color, rotation);
            }
        }
    }

    private void printTextALL(int startX, int startY, int width,
                              int height, String text, int fontSize,
                              int textStyle, int color, int rotation) {
        if (STYLE_TEXT_BOLD == textStyle) {
            printTextNormaL(startX, startY, width, height, text,
                    fontSize, textStyle, color, rotation);
        } else {
            printTextNormaL(startX, startY, width, height, text,
                    fontSize, textStyle, color, rotation);
        }
    }

    /**
     * @param startX
     * @param startY
     * @param width
     * @param height
     * @param text
     * @param fontSize
     * @param textStyle
     * @param color
     * @param rotation
     */
    private void printTextNormaL(int startX, int startY, int width,
                                 int height, String text, int fontSize,
                                 int textStyle, int color, int rotation) {
        //        参数:
//        x:文字起始 x 坐标
//        y:文字起始 y 坐标
//        font:
//            1 8×12 dot 英数字体
//            2 12×20 dot 英数字体
//            3 16×24 dot 英数字体
//            4 24×32 dot 英数字体
//            5 32×48 dot 英数字体
//            6 14×19 dot 英数字体 OCR-B
//            7 21×27 dot 英数字体 OCR-B
//            8 14×25 dot 英数字体 OCR-A
//            9 9×17 dot 英数字体
//            10 12×24 dot 英数字体
//            TST24.BF2 繁体中文 24×24Font(大五码）
//            TSS24.BF2 简体中文 24×24Font（GB 码）
//            K 韩文 24×24Font（KS 码）
//        rotation:文字旋转角度（顺时针方向）
//            0    0 度
//            90   90 度
//            180  180 度
//            270  270 度
//        x-multiplication X 方向放大倍率 1-10
//        y-multiplication Y 方向放大倍率 1-10
//        content:文本数据

        String font = "TSS16.BF2";
        int x_multiplication = 1;
        int y_multiplication = 1;

        // 根据字体大小进行缩放
        switch (fontSize) {
            case FONT_SIZE_16:
                font = "TSS16.BF2";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_20:
                font = "TSS20.BF2";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_24:
                font = "TSS24.BF2";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_28:
                font = "TSS24.BF2";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_32:
                font = "TSS32.BF2";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_40:
                font = "TSS20.BF2";
                x_multiplication = 2;
                y_multiplication = 2;
                break;
            case FONT_SIZE_48:
                font = "TSS48.BF2";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_56:
                font = "TSS24.BF2";
                x_multiplication = 2;
                y_multiplication = 2;
                break;
            case FONT_SIZE_64:
                font = "TSS32.BF2";
                x_multiplication = 2;
                y_multiplication = 2;
                break;
            case FONT_SIZE_72:
                font = "TSS24.BF2";
                x_multiplication = 3;
                y_multiplication = 3;
                break;
            case FONT_SIZE_84:
                font = "TSS20.BF2";
                x_multiplication = 4;
                y_multiplication = 4;
                break;
            case FONT_SIZE_96:
                font = "TSS48.BF2";
                x_multiplication = 2;
                y_multiplication = 2;
                break;
            case FONT_SIZE_128:
                font = "TSS32.BF2";
                x_multiplication = 4;
                y_multiplication = 4;
                break;
            default:
                x_multiplication = 1;
                y_multiplication = 1;
                break;
        }

        byte[] data = new byte[0];
        try {
            if (STYLE_TEXT_BOLD == textStyle) {
                data = String.format(Locale.getDefault(),
                        "TEXT %d,%d,\"%s\",%d,%d,%d,B1,\"%s\"\r\n",
                        startX,
                        startY,
                        font,
                        rotation,
                        x_multiplication,
                        y_multiplication,
                        text)
                        .getBytes(LanguageEncode);
            } else {
                data = String.format(Locale.getDefault(),
                        "TEXT %d,%d,\"%s\",%d,%d,%d,\"%s\"\r\n",
                        startX,
                        startY,
                        font,
                        rotation,
                        x_multiplication,
                        y_multiplication,
                        text)
                        .getBytes(LanguageEncode);
            }


        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * Method     打印条码
     * Parameters
     * startX    条码起始X坐标
     * startY    条码起始Y坐标
     * height    条码高度
     * lineWidth 条码线条宽度
     * text      条码内容
     * type      条码类型
     * rotation  旋转角度
     * Return     void
     */
    @Override
    public void drawBarCode(int startX, int startY, int height,
                            int lineWidth, String text, int type, int rotation) {
        //BARCODE x,y,"code type",height,human readable,rotation,narrow,wide,"content"
//        rotation:条码方向:
//        0，不旋转
//        90，顺时针方向旋转 90 度
//        180，顺时针方向旋转 180 度
//        270，顺时针方向旋转 270 度
//        code_type:条码类型:
//        128，128M，EAN128 ，39 ，93，UPCA ，MSI ，ITF14
//        narrow :窄条的单位宽度(默认 1)。
//        Wide:宽条码的单位宽度(默认 1)。 1:1、1:2、1:3、2:5、3:7
//        Height:条码高度(单位像素)。
//        Readable:条码数据是否可见
//          0 表示人眼不可识，
//          1 表示人眼可识
//        X:条码的起始横坐标。
//        Y:条码的起始纵坐标。
//        content:条码数据。
        if (TextUtils.isEmpty(text)) {
            return;
        }

        String codeType = "128";
        int narrow = lineWidth + 1;
        int wide = lineWidth * 2;

//        128，128M，EAN128 ，39 ，93，UPCA ，MSI ，ITF14
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
            default:
                codeType = "128";
                break;
        }

        //把旋转角度修改为逆时针
        if (rotation == 90) {
            rotation = 270;
        } else if (rotation == 180) {
            rotation = 180;
        } else if (rotation == 270) {
            rotation = 90;
        }

        byte[] data = new byte[0];
        try {
            data = String.format(Locale.getDefault(),
                    "BARCODE " +
                            "%d," +
                            "%d," +
                            "\"%s\"," +
                            "%d," +
                            "%s," +
                            "%d," +
                            "%d," +
                            "%d," +
                            "\"%s\"\r\n",
                    startX,
                    startY,
                    codeType,
                    height,
                    "0",
                    rotation,
                    narrow,
                    wide,
                    text).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * Method     打印二维码
     * Parameters
     * startX    起始X坐标
     * startY    起始Y坐标
     * text      内容
     * unitWidth  模块的单位宽度
     * level     纠错级别
     * rotation  旋转角度
     * Return     void
     */
    @Override
    public void drawQRCode(int startX, int startY, String text, int unitWidth, int level, int rotation) {
        // QRCODE x,y,ECC level,cell width,mode,rotation,"data string"
//        rotation: 旋转角度（顺时针方向）
//        0 0 度
//        90 90 度
//        180 180 度
//        270 270 度
//        X:二维码的起始横坐标。 Y:二维码的起始纵坐标。
//        ECC level 选择 QRCODE 纠错等级
//              L : 7%
//              M : 15%
//              Q : 25%
//              H : 30%
//        cell width 二维码宽度 1-10
//        mode 手动/自动编码
//              A Auto
//              M Manual
//        Data:二维码的数据。
        if (TextUtils.isEmpty(text)) {
            return;
        }

        String mode = "A";
        String ecc_level = "L";
        switch (level) {
            case QRCODE_CORRECTION_LEVEL_L:
                ecc_level = "L";
                break;
            case QRCODE_CORRECTION_LEVEL_M:
                ecc_level = "M";
                break;
            case QRCODE_CORRECTION_LEVEL_Q:
                ecc_level = "Q";
                break;
            case QRCODE_CORRECTION_LEVEL_H:
                ecc_level = "H";
                break;
            default:
                ecc_level = "L";
                break;
        }
        int width = 1;
        width = unitWidth;
        if (unitWidth < 1){
            width = 1;
        }else if (unitWidth > 10){
            width = 10;
        }

        byte[] data = new byte[0];
        try {
            data = String.format(Locale.getDefault(),
                    "QRCODE " +
                            "%d," +
                            "%d," +
                            "%s," +
                            "%d," +
                            "%s," +
                            "%d," +
                            "\"%s\"\r\n",
                    startX,
                    startY,
                    ecc_level,
                    unitWidth,
                    mode,
                    rotation,
                    text).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * Method     打印图片
     * Parameters
     * startX 图片起始Y坐标
     * startY 图片起始Y坐标
     * bitmap 图片数据
     * width  图片打印宽度（若为width 或height 为0，则取图片宽高，不为0，则自己缩放）
     * height 图片打印高度
     * Return     void
     */
    @Override
    public void drawImage(int startX, int startY, Bitmap bitmap,
                          int width, int height) {
        // BITMAP x,y,width,height,mode,bitmap data...
        // String x_pos,String y_pos,Bitmap bmp ,boolean isNegate
        // BITMAP X,Y,width,height,mode,bitmap data...

        if (bitmap.getWidth() % 8 == 0) {
            width = bitmap.getWidth() / 8;
        } else {
            width = (bitmap.getWidth() / 8) + 1;
        }
        height = bitmap.getHeight();

        byte[] imgData = imageProcess(bitmap);
        for (int i = 0; i < imgData.length; i++) {
            imgData[i] = (byte) (imgData[i] ^ -1);
        }

        byte[] data = new byte[0];
        try {
            data = String.format(Locale.getDefault(),
                    "BITMAP " +
                            "%d," +
                            "%d," +
                            "%d," +
                            "%d," +
                            "1,",
                    startX,
                    startY,
                    width,
                    height).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
        writeData(imgData);
    }

    /**
     * Method     下个标签
     * Parameters []
     * Return     void
     */
    @Override
    public void feedToNextLabel() {
        byte[] data = new byte[0];
        try {
            data = "FORMFEED\r\n".getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * Method     打印机宽度
     * Parameters
     * Return
     */
    @Override
    public int getPrinterWidth() {
        return 0;
    }

    /**
     * Method     获取打印机状态(此方法各家供应商未统一实现)
     * Parameters
     * Return
     */
    @Override
    public int getPrinterStatus() {
        int status = -1;
        if (mBtSocket == null || !mBtSocket.isConnected()) {
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
            return status;
        }
        // 1.查询状态指令
        byte[] bArr = new byte[]{27, 33, 63};
        // 2.写入指令
        if (writeData(bArr)) {
            // 3.读取返回结果
            byte[] statusBytes = readData(2);
            Log.e("dao", "statusBytes[0] ==>" + statusBytes[0]);
            // 正常0，盖子打开1,无纸4,正在打印5
            if (statusBytes.length > 0 && statusBytes[0] == 0) {
                status = IBluetoothPrinterProtocol.STATUS_OK;
            } else if (statusBytes.length > 0 && statusBytes[0] == 1) {
                status = IBluetoothPrinterProtocol.STATUS_COVER_OPENED;
            } else if (statusBytes.length > 0 && statusBytes[0] == 4) {
                status = IBluetoothPrinterProtocol.STATUS_NOPAPER;
            } else if (statusBytes.length > 0 && statusBytes[0] == 32) {
                status = IBluetoothPrinterProtocol.STATUS_PRINTING;
            }
        } else {
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
        }
        return status;
    }

    /**
     * Method     打印
     * Parameters [orientation, printCallback]
     * Return     void
     */
    @Override
    public void print(PrintCallback printCallback) {
        byte[] data = new byte[0];
        try {
            data = "PRINT 1,1\r\n".getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * Method     打印并进纸
     * Parameters [printCallback]
     * Return     void
     */
    @Override
    public void printAndFeed(PrintCallback printCallback) {
//        feedToNextLabel();
        print(printCallback);
    }

    /**
     * 驱动是否完全支持
     */
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

    // 读取数据
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
