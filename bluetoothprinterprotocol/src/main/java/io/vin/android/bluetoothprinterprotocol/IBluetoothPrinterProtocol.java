package io.vin.android.bluetoothprinterprotocol;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;

/**
 * Bluetooth Printer Protocol
 * 蓝牙打印机，打印协议
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public interface IBluetoothPrinterProtocol {

    //print orientation打印方向
    int ORIENTATION_DEFAULT = 0;
    int ORIENTATION_DOWN = 1;
    int ORIENTATION_LEFT = 2;
    int ORIENTATION_RIGHT = 3;

    //print color打印颜色
    int COLOR_BLACK = 0; //黑字白底
    int COLOR_WHITE = 1; //白字黑底

    //printer status打印机状态
    int STATUS_OK = 0;          //正常
    int STATUS_COVER_OPENED = 1;//盖子打开
    int STATUS_NOPAPER = 2;     //没有纸张
    int STATUS_BATTERY_LOW = 4; //电量低
    int STATUS_OVER_HEATING = 8;//打印机过热
    int STATUS_PRINTING = 16;   //打印中
    int STATUS_DISCONNECT = 32; //断开
    int STATUS_OTHER = -1;      //其他异常
    int STATUS_OTHER2 = -2;     //线程异常

    //line style线条样式
    int STYLE_LINE_FULL = 0;   //实线
    int STYLE_LINE_DOTTED = 1; //虚线

    //text style字体样式
    int STYLE_TEXT_NO = 0;       //正常
    int STYLE_TEXT_BOLD = 1;     //粗体
    int STYLE_TEXT_ITALIC = 2;   //斜体
    int STYLE_TEXT_UNDERLINE = 4;//下划线

    //font size字体大小
    int FONT_SIZE_16 = 16;
    int FONT_SIZE_20 = 20;
    int FONT_SIZE_24 = 24;
    int FONT_SIZE_28 = 28;
    int FONT_SIZE_32 = 32;
    int FONT_SIZE_40 = 40;
    int FONT_SIZE_48 = 48;
    int FONT_SIZE_56 = 56;
    int FONT_SIZE_64 = 64;
    int FONT_SIZE_72 = 72;
    int FONT_SIZE_84 = 84;
    int FONT_SIZE_96 = 96;
    int FONT_SIZE_128 = 128;
    int FONT_SIZE_140 = 140;//汉印打印机定制字体
    int FONT_SIZE_160 = 160;//汉印打印机定制字体

    //Rotation Angle旋转角度
    int STYLE_ROTATION_0 = 0;
    int STYLE_ROTATION_90 = 90;
    int STYLE_ROTATION_180 = 180;
    int STYLE_ROTATION_270 = 270;

    //barcode type条码类型
    int STYLE_BARCODE_CODE128 = 0;
    int STYLE_BARCODE_CODE39 = 1;
    int STYLE_BARCODE_CODE93 = 2;
    int STYLE_BARCODE_CODABAR = 3;
    int STYLE_BARCODE_EAN8 = 4;
    int STYLE_BARCODE_EAN13 = 5;
    int STYLE_BARCODE_UPCA = 6;
    int STYLE_BARCODE_UPCE = 7;
    int STYLE_BARCODE_I2OF5 = 8;

    //Error correction level二维码纠错级别
    int QRCODE_CORRECTION_LEVEL_L = 0;
    int QRCODE_CORRECTION_LEVEL_M = 1;
    int QRCODE_CORRECTION_LEVEL_Q = 2;
    int QRCODE_CORRECTION_LEVEL_H = 3;

    //二维码模块的单位宽度/单位高度(范围是 1 至 32。默认值为 6)
    int QRCODE_UNIT_WIDTH_1 = 1;
    int QRCODE_UNIT_WIDTH_2 = 2;
    int QRCODE_UNIT_WIDTH_3 = 3;
    int QRCODE_UNIT_WIDTH_4 = 4;
    int QRCODE_UNIT_WIDTH_5 = 5;
    int QRCODE_UNIT_WIDTH_6 = 6;
    int QRCODE_UNIT_WIDTH_7 = 7;
    int QRCODE_UNIT_WIDTH_8 = 8;
    int QRCODE_UNIT_WIDTH_9 = 9;
    int QRCODE_UNIT_WIDTH_10 = 10;
    int QRCODE_UNIT_WIDTH_11 = 11;
    int QRCODE_UNIT_WIDTH_12 = 12;
    int QRCODE_UNIT_WIDTH_13 = 13;
    int QRCODE_UNIT_WIDTH_14 = 14;
    int QRCODE_UNIT_WIDTH_15 = 15;
    int QRCODE_UNIT_WIDTH_16 = 16;
    int QRCODE_UNIT_WIDTH_17 = 17;
    int QRCODE_UNIT_WIDTH_18 = 18;
    int QRCODE_UNIT_WIDTH_19 = 19;
    int QRCODE_UNIT_WIDTH_20 = 20;
    int QRCODE_UNIT_WIDTH_21 = 21;
    int QRCODE_UNIT_WIDTH_22 = 22;
    int QRCODE_UNIT_WIDTH_23 = 23;
    int QRCODE_UNIT_WIDTH_24 = 24;
    int QRCODE_UNIT_WIDTH_25 = 25;
    int QRCODE_UNIT_WIDTH_26 = 26;
    int QRCODE_UNIT_WIDTH_27 = 27;
    int QRCODE_UNIT_WIDTH_28 = 28;
    int QRCODE_UNIT_WIDTH_29 = 29;
    int QRCODE_UNIT_WIDTH_30 = 30;
    int QRCODE_UNIT_WIDTH_31 = 31;
    int QRCODE_UNIT_WIDTH_32 = 32;


    /**
     * Method     connect
     * connect printer
     * 连接打印机
     * Parameters [bluetoothAddress, connectCallback]
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void connect(String bluetoothAddress, ConnectCallback connectCallback);

    /**
     * Method     disconnect
     * disconnect printer
     * 断开打印机
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void disconnect();

    /**
     * Method     initWithSocket
     * initialize with BluetoothSocket
     * 使用BluetoothSocket初始化
     * Parameters [bluetoothSocket]
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void initWithSocket(BluetoothSocket bluetoothSocket);

    /**
     * Method     resetSocket
     * reset BluetoothSocket
     * 重置BluetoothSocket
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void resetSocket();
    
    /**
     * Method   setPage
     * Set the print paper size and rotation Angle
     * 设置打印纸张大小、旋转角度
     * Parameters
     *          width  宽度（单位像素Pixcls）
     *          height 高度（单位像素Pixcls）
     *          orientation 方向
     *              ORIENTATION_DEFAULT
     *              ORIENTATION_DOWN
     *              ORIENTATION_LEFT
     *              ORIENTATION_RIGHT
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void setPage(int width, int height,int orientation);

    /**
     * Method   drawLine
     * draw Line
     * 绘制线条
     * Parameters
     *          startX     线条起始点x坐标
     *          startY     线条起始点y坐标
     *          endX       线条结束点x坐标
     *          endY       线条结束点y坐标
     *          lineWidth  线条宽度
     *          lineStyle  线条样式（参考上面定义）
     * Return
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void drawLine(int startX, int startY, int endX, int endY, int lineWidth, int lineStyle);

    /**
     * Method   drawRect
     * draw Rect
     * 画矩形
     * Parameters
     *          leftTopX     矩形框左上角x坐标
     *          leftTopY     矩形框左上角y坐标
     *          rightBottomX 矩形框右下角x坐标
     *          rightBottomY 矩形框右下角y坐标
     *          lineWidth    线条宽度
     *          lineStyle    线条样式（参考上面定义）
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void drawRect(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int lineWidth, int lineStyle);

    /**
     * Method   drawText
     * draw Text
     * 画文字
     * Parameters
     *          startX     文字起始X坐标
     *          startY     文字起始Y坐标
     *          width     文字绘制区域宽度(可以为0，不为零的时候文字要根据宽度自动换行)
     *          height    文字绘制区域高度(可以为0，)
     *          text      内容
     *          fontSize  字体大小（参考上面定义）
     *          textStyle 字体样式（参考上面定义）
     *          color 文字颜色（参考上面定义）
     *          rotation  旋转角度（参考上面定义）
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void drawText(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation);

    /**
     * Method    drawBarCode
     *  打印条码
     * Parameters
     *          startX    条码起始X坐标
     *          startY    条码起始Y坐标
     *          height    条码高度
     *          lineWidth 条码线条宽度
     *          text      条码内容
     *          type      条码类型
     *          rotation  旋转角度
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void drawBarCode(int startX ,int startY, int height, int lineWidth, String text, int type, int rotation);

    /**
     * Method   drawQRCode
     * 打印二维码
     * Parameters
     *          startX    起始X坐标
     *          startY    起始Y坐标
     *          text      内容
     *          unitWidth  模块的单位宽度
     *          level     纠错级别
     *          rotation  旋转角度
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void drawQRCode(int startX, int startY, String text, int unitWidth, int level, int rotation);

    /**
     * Method   drawImage
     * 打印图片
     * Parameters
     *          startX 图片起始Y坐标
     *          startY 图片起始Y坐标
     *          bitmap 图片数据
     *          width  图片打印宽度（若为width 或height 为0，则取图片宽高，不为0，则自己缩放）
     *          height 图片打印高度
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void drawImage(int startX, int startY, Bitmap bitmap,int width,int height);

    /**
     * Method     feedToNextLabel
     * 下个标签
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void feedToNextLabel();

    /**
     * Method     getPrinterWidth
     * 打印机宽度
     * Parameters []
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    int getPrinterWidth();

    /**
     * Method     getPrinterStatus
     * get Printer Status
     * 获取打印机状态(此方法各家供应商未统一实现)
     * Parameters []
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    int getPrinterStatus();

    /**
     * Method     print
     * 打印
     * Parameters [printCallback]
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void print(PrintCallback printCallback);

    /**
     * Method     printAndFeed
     * 打印并进纸
     * Parameters [printCallback]
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    void printAndFeed(PrintCallback printCallback);

    /**
     * Method     isFullySupport
     * 驱动是否完全支持
     * Parameters []
     * Return     boolean
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    boolean isFullySupport();

}
