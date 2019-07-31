package io.vin.android.bluetoothprinter.kuaimai;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

/**
 * 快麦打印机，目前只调试了KM-118 KM-218（TSPL指令集）
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class KuaiMaiBluetoothPrinter implements IBluetoothPrinterProtocol {
    String LanguageEncode = "gb2312";
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket mBtSocket;
    public InputStream mInStream;
    public OutputStream mOutStream;

    private String mPrinterName;
    private String mPrinterAddr;

    public KuaiMaiBluetoothPrinter(String printerModelName) {
        this.mPrinterName = printerModelName;
    }

    /**
     * Method     connect
     * Parameters [bluetoothAddress, connectCallback]
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
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

    /**
     * Method     disconnect
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void disconnect() {
        mBtSocket = null;
        mInStream = null;
        mOutStream = null;
    }

    /**
     * Method     initWithSocket
     * Parameters [bluetoothSocket]
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
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

    /**
     * Method     resetSocket
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
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
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void setPage(int width, int height, int orientation) {
        StringBuffer strBuffer = new StringBuffer();
        width = width / 8;
        height = height / 8;
        strBuffer.append(String.format("SIZE %dmm,%dmm\r\n", width, height));
        strBuffer.append("DIRECTION 0,0\r\n");
        strBuffer.append("REFERENCE 0,0\r\n");
        strBuffer.append("SET PEEL OFF\r\n");
        strBuffer.append("SET TEAR ON\r\n");
        strBuffer.append("CLS\r\n");
        strBuffer.append("CODEPAGE Default\r\n");
        byte[] data = new byte[0];
        try {
            data = strBuffer.toString().getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        writeData(data);
    }

    /**
     * Method     画线
     * Parameters
     * startX     线条起始点x坐标
     * startY     线条起始点y坐标
     * endX       线条结束点x坐标
     * endY       线条结束点y坐标
     * lineWidth  线条宽度
     * lineStyle  线条样式
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void drawLine(int startX, int startY, int endX, int endY, int lineWidth, int lineStyle) {
        //BAR x,y,width,height
        //x:起始的 X 坐标。
        // y:起始的 Y 坐标。
        // width:线条的宽度(像素)。
        // height:线条的高度(像素)。

        int width = 0;
        int height = 0;
        if (startX == endX){
            //竖线
            height = endY -startY;
            width = lineWidth;
        }
        if (startY == endY){
            //横线
            height = lineWidth;
            width = endX - startX;

        }
        byte[] data = new byte[0];
        try {
            data = String.format("BAR %d,%d,%d,%d\r\n", startX, startY, width, height).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {

        }
        writeData(data);
    }

    /**
     * Method     画矩形
     * Parameters
     * leftTopX     矩形框左上角x坐标
     * leftTopY     矩形框左上角y坐标
     * rightBottomX 矩形框右下角x坐标
     * rightBottomY 矩形框右下角y坐标
     * lineWidth    线条宽度
     * lineStyle    线条样式
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void drawRect(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int lineWidth, int lineStyle) {
        //BOX x,y,x_end,y_end,line thickness[,radius]
        //x_start:左上角 x 坐标。 y_start:左上角 y 坐标。 x_end:右下角 x 坐标。 y_end:右下角 y 坐标。 Thickness:线条宽度。
        int x_start = leftTopX;
        int y_start = leftTopY;
        int x_end = rightBottomX;
        int y_end = rightBottomY;
        int Thickness = lineWidth;
        byte[] data = new byte[0];
        try {
            data = String.format("BOX %d,%d,%d,%d,%d\r\n", x_start, y_start, x_end, y_end, Thickness).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {

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
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void drawText(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation) {
        //TEXT x,y,"font",rotation,x-multiplication,y-multiplication,[alignment,]"content"
        if (TextUtils.isEmpty(text)){
            return;
        }
        if (STYLE_TEXT_BOLD == textStyle){
            printTextNormaL(startX,  startY,  width,  height,  text,  fontSize,  textStyle,  color,  rotation);
        }else {
            printTextNormaL(startX,  startY,  width,  height,  text,  fontSize,  textStyle,  color,  rotation);
        }
    }

    private void printTextNormaL(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation){
        //        参数:
//        x:文字起始 x 坐标
//        y:文字起始 y 坐标
//        font:
//            height x width
//            0: Monotye CG Triumvirate Bold Condensed, font width and height is stretchable
//            1: 8 x 12 fixed pitch dot font
//            2: 12 x 20 fixed pitch dot font
//            3: 16 x 24 fixed pitch dot font
//            4: 24 x 32 fixed pitch dot font
//            5: 32 x 48 dot fixed pitch font
//            6: 14 x 19 dot fixed pitch font OCR-B
//            7: 21 x 27 dot fixed pitch font OCR-B
//            8: 14 x25 dot fixed pitch font OCR-A
//            9:只有这个字体能够打印中文
//        rotation:打印方向
//            0 : No rotation
//            90 : degrees, in clockwise direction
//            180 : degrees, in clockwise direction
//            270 : degrees, in clockwise direction
//        x_multiplication:x 轴方向文字拉伸的倍数
//        y_multiplication:y 轴方向文字拉伸的倍数
//        content:文本数据

        String font = "1";
        int x_multiplication = 2;
        int y_multiplication = 3;

        //根据字体大小进行缩放
        switch (fontSize) {
            case FONT_SIZE_16:
                //1: 8 x 12 fixed pitch dot font
                font ="1";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_20:
                //2: 12 x 20 fixed pitch dot font
                font ="2";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_24:
                //1: 8 x 12 fixed pitch dot font
                font = "3";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_28:
                //7: 21 x 27 dot fixed pitch font OCR-B
                font = "7";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_32:
                //4: 24 x 32 fixed pitch dot font
                font ="4";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_40:
                //2: 12 x 20 fixed pitch dot font
                font = "2";
                x_multiplication =2;
                y_multiplication = 3;
                break;
            case FONT_SIZE_48:
                //5: 32 x 48 dot fixed pitch font
                font ="5";
                x_multiplication = 1;
                y_multiplication = 1;
                break;
            case FONT_SIZE_56:
                //7: 21 x 27 dot fixed pitch font OCR-B
                font ="5";
                x_multiplication = 2;
                y_multiplication = 2;
                break;
            case FONT_SIZE_64:
                //4: 24 x 32 fixed pitch dot font
                font = "4";
                x_multiplication = 2;
                y_multiplication = 2;
                break;
            case FONT_SIZE_72:
                //3: 16 x 24 fixed pitch dot font
                font ="3";
                x_multiplication = 3;
                y_multiplication = 3;
                break;
            case FONT_SIZE_84:
                //2: 12 x 20 fixed pitch dot font
                font = "1";
                x_multiplication = 4;
                y_multiplication = 6;
                break;
            case FONT_SIZE_96:
                //5: 32 x 48 dot fixed pitch font
                font = "5";
                x_multiplication = 2;
                y_multiplication = 2;
                break;
            case FONT_SIZE_128:
                //4: 24 x 32 fixed pitch dot font
                font = "4";
                x_multiplication = 3;
                y_multiplication = 3;
                break;
            default:
                font = "1";
                x_multiplication = 2;
                y_multiplication = 3;
                break;
        }

        byte[] data = new byte[0];
        try {
            data = String.format("TEXT %d,%d,\"%s\",%d,%d,%d,\"%s\"\r\n",
                    startX,
                    startY,
                    font,
                    rotation,
                    x_multiplication,
                    y_multiplication,
                    text)
                    .getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {

        }
        writeData(data);
    }



    private void printTextBold(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation){

    }


    public void printText2(String x_pos,
                           String y_pos,
                           String bold,
                           String rotation,
                           int size,
                           String code_data) throws Exception {
        String var6 = "1";
        String var7 = "1";
        switch (size) {
            case 1:
                var6 = "1";
                var7 = "1";
                break;
            case 2:
                var6 = "2";
                var7 = "2";
                break;
            case 3:
                var6 = "3";
                var7 = "3";
                break;
            case 4:
                var6 = "4";
                var7 = "4";
                break;
            case 5:
                var6 = "5";
                var7 = "5";
                break;
            case 6:
                var6 = "6";
                var7 = "6";
                break;
            case 7:
                var6 = "7";
                var7 = "7";
        }
        writeData(("TEXT " + x_pos + "," + y_pos + ",\"" + bold + "\"," + rotation + "," + var6 + "," + var7 + ",\"" + code_data + "\"\r\n").getBytes(LanguageEncode));
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
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void drawBarCode(int startX, int startY, int height, int lineWidth, String text, int type, int rotation) {
        //BARCODE X,Y,”code type”,height,human readable,rotation,narrow,wide,[alignment,]”content “
//        rotation:条码方向:
//        0 : No rotation
//        90 : degrees, in clockwise direction 180 : degrees, in clockwise direction 270 : degrees, in clockwise direction
//        code_type:条码类型:
//        128，128M，EAN128 ，39 ，93，UPCA ，MSI ，ITF14
//        narrow :窄条的单位宽度(默认 1)。
//        Wide:宽条码的单位宽度(默认 1)。 1:1、1:2、1:3、2:5、3:7
//        Height:条码高度(单位像素)。
//        Readable:条码数据是否可见
//          0: not readable
//          1: human readable
//        X:条码的起始横坐标。
//        Y:条码的起始纵坐标。
//        code_data:条码数据。
        if (TextUtils.isEmpty(text)){
            return;
        }

        String codeType = "128";

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
            case STYLE_BARCODE_UPCA:
                codeType = "UPCA";
                break;
            default:
                codeType = "128";
                break;

        }

        byte[] data = new byte[0];
        try {
            data = String.format("BARCODE " +
                            "%d," +
                            "%d," +
                            "\"%s\"," +
                            "%d," + "0," +
                            "%d," +
                            "%d," +
                            "%d," +
                            "\"%s\"\r\n",
                    startX,
                    startY,
                    codeType,
                    height,
                    rotation,
                    lineWidth,
                    lineWidth,
                    text).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {

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
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void drawQRCode(int startX, int startY, String text, int unitWidth, int level, int rotation) {
        //QRCODE x,y,ECC Level,cell width,mode,rotation,[model,mask,]"content"
//        rotation:条码方向:
//        0 : No rotation
//        90 : degrees, in clockwise direction 180 : degrees, in clockwise direction 270 : degrees, in clockwise direction
//        X:二维码的起始横坐标。 Y:二维码的起始纵坐标。
//        ecc_level:纠错等级
//              L : 7%
//              M : 15%
//              Q : 25%
//              H : 30%
//        width:宽度:1~10
//        Mode:模式
//              M1: (default), original version
//              M2: enhanced version
//        Data:二维码的数据。
        if (TextUtils.isEmpty(text)){
            return;
        }

        String mode = "M1";
        String ecc_level ="L";
        switch (level) {
            case QRCODE_CORRECTION_LEVEL_L:
                ecc_level ="L";
                break;
            case QRCODE_CORRECTION_LEVEL_M:
                ecc_level ="M";
                break;
            case QRCODE_CORRECTION_LEVEL_Q:
                ecc_level ="Q";
                break;
            case QRCODE_CORRECTION_LEVEL_H:
                ecc_level ="H";
                break;
            default:
                ecc_level ="L";
                break;
        }

        byte[] data = new byte[0];
        try {
            data = String.format("QRCODE "+
                            "%d,"+
                            "%d,"+
                            "%s,"+
                            "%d,"+
                            "%s,"+
                            "%d,"+
                            "\"%s\"\r\n",
                    startX,
                    startY,
                    ecc_level,
                    unitWidth,
                    mode,
                    rotation,
                    text).getBytes(LanguageEncode);
        } catch (UnsupportedEncodingException ex) {

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
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void drawImage(int startX, int startY, Bitmap bitmap, int width, int height) {
        //BITMAP X,Y,width,height,mode,bitmap data...
        //String x_pos,String y_pos,Bitmap bmp ,boolean isNegate
        //BITMAP X,Y,width,height,mode,bitmap data...

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
            data = String.format("BITMAP " +
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
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void feedToNextLabel() {
        byte[] data = new byte[0];
        try {
            data = "FORMFEED\r\n".getBytes(LanguageEncode);
        }catch (UnsupportedEncodingException ex){

        }
        writeData(data);

    }

    /**
     * Method     打印机宽度
     * Parameters
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public int getPrinterWidth() {
        return 0;
    }

    /**
     * Method     获取打印机状态(此方法各家供应商未统一实现)
     * Parameters
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public int getPrinterStatus() {
        int status = -1;
        if (mBtSocket == null||!mBtSocket.isConnected()){
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
            return status;
        }
        //1.查询状态指令
        byte[] bArr = new byte[]{27, 33, 63, 13, 10};
        //2.写入指令
        if (writeData(bArr)) {
            //3.读取返回结果
            byte[] statusBytes = readData(2);
            //正常0，盖子打开1,无纸4
            if (statusBytes.length > 0 && statusBytes[0] == 0) {
                status = IBluetoothPrinterProtocol.STATUS_OK;
            }else if (statusBytes.length > 0 && statusBytes[0] == 1){
                status = IBluetoothPrinterProtocol.STATUS_COVER_OPENED;
            }else if (statusBytes.length > 0 && statusBytes[0] == 4){
                status = IBluetoothPrinterProtocol.STATUS_NOPAPER;
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
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void print(PrintCallback printCallback) {

        byte[] data = new byte[0];
        try {
            data = "PRINT 1,1\r\n".getBytes(LanguageEncode);
        }catch (UnsupportedEncodingException ex){

        }
        writeData(data);
    }

    /**
     * Method     打印并进纸
     * Parameters [printCallback]
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    @Override
    public void printAndFeed(PrintCallback printCallback) {
//        feedToNextLabel();
        print(printCallback);
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
