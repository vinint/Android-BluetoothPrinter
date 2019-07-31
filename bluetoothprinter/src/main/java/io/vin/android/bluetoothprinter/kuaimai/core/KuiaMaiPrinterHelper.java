package io.vin.android.bluetoothprinter.kuaimai.core;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 快麦打印机K118设备驱动适配
 * Author     Vin
 * Mail       vinintg@gmail.com
 * Createtime 2018/10/30 下午12:00
 * Modifytime 2018/10/30 下午12:00
 */
public class KuiaMaiPrinterHelper implements Serializable {
    private static int A = 2;
    public static final int ACTIVITY_CONNECT_BT = 3;
    public static final int ACTIVITY_CONNECT_WIFI = 4;
    public static final int ACTIVITY_IMAGE_FILE = 1;
    public static final int ACTIVITY_PRNFILE = 2;
    public static final int BC_CODE128 = 73;
    public static final int BC_CODE39 = 69;
    public static final int BC_CODE93 = 72;
    public static final int BC_CODEBAR = 71;
    public static final int BC_EAN13 = 67;
    public static final int BC_EAN8 = 68;
    public static final int BC_ITF = 70;
    public static final int BC_UPCA = 65;
    public static final int BC_UPCE = 66;
    public static int BetweenWriteAndReadDelay = 1500;
    public static final byte HPRT_FULL_CUT = (byte) 0;
    public static final byte HPRT_FULL_CUT_FEED = (byte) 65;
    public static final int HPRT_MODEL_DT210 = 4102;
    public static final int HPRT_MODEL_INVALID = -1;
    public static final int HPRT_MODEL_LP106B = 37121;
    public static final int HPRT_MODEL_LPQ58 = 38401;
    public static final int HPRT_MODEL_LPQ80 = 38402;
    public static final int HPRT_MODEL_MAX = 31;
    public static final int HPRT_MODEL_MLP2 = 4355;
    public static final int HPRT_MODEL_MPD2 = 4401;
    public static final int HPRT_MODEL_MPS3 = 4356;
    public static final int HPRT_MODEL_MPT2 = 4353;
    public static final int HPRT_MODEL_MPT3 = 4354;
    public static final int HPRT_MODEL_MPT8 = 4497;
    public static final int HPRT_MODEL_MPT_E2 = 4433;
    public static final int HPRT_MODEL_PPT2_A = 4113;
    public static final int HPRT_MODEL_PPT2_UR = 4114;
    public static final int HPRT_MODEL_PPTD3 = 4129;
    public static final int HPRT_MODEL_PROPERTY_CONNECT_TYPE = 4;
    public static final int HPRT_MODEL_PROPERTY_KEY_BARCODE = 150;
    public static final int HPRT_MODEL_PROPERTY_KEY_BEEP = 146;
    public static final int HPRT_MODEL_PROPERTY_KEY_BITMAPMODE = 151;
    public static final int HPRT_MODEL_PROPERTY_KEY_BOLD = 50;
    public static final int HPRT_MODEL_PROPERTY_KEY_COMPRESS_MODE = 113;
    public static final int HPRT_MODEL_PROPERTY_KEY_CUT = 147;
    public static final int HPRT_MODEL_PROPERTY_KEY_CUT_SPACING = 148;
    public static final int HPRT_MODEL_PROPERTY_KEY_DESCRIPTION = 35;
    public static final int HPRT_MODEL_PROPERTY_KEY_DPI = 65;
    public static final int HPRT_MODEL_PROPERTY_KEY_DRAWER = 145;
    public static final int HPRT_MODEL_PROPERTY_KEY_FONTS = 49;
    public static final int HPRT_MODEL_PROPERTY_KEY_GET_REMAINING_POWER = 152;
    public static final int HPRT_MODEL_PROPERTY_KEY_ID = 1;
    public static final int HPRT_MODEL_PROPERTY_KEY_IDENTITY = 34;
    public static final int HPRT_MODEL_PROPERTY_KEY_MANUFACTURE = 33;
    public static final int HPRT_MODEL_PROPERTY_KEY_MAX_FONT_SCALE_SIZE = 52;
    public static final int HPRT_MODEL_PROPERTY_KEY_MOTION_H = 67;
    public static final int HPRT_MODEL_PROPERTY_KEY_MOTION_V = 66;
    public static final int HPRT_MODEL_PROPERTY_KEY_NAME = 2;
    public static final int HPRT_MODEL_PROPERTY_KEY_PAGEMODE = 129;
    public static final int HPRT_MODEL_PROPERTY_KEY_PAGEMODE_AREA = 130;
    public static final int HPRT_MODEL_PROPERTY_KEY_PID = 38;
    public static final int HPRT_MODEL_PROPERTY_KEY_PRINT_RECEIPT = 130;
    public static final int HPRT_MODEL_PROPERTY_KEY_STATUS_MODEL = 153;
    public static final int HPRT_MODEL_PROPERTY_KEY_TEAR_SPACING = 149;
    public static final int HPRT_MODEL_PROPERTY_KEY_UNDERLINE = 51;
    public static final int HPRT_MODEL_PROPERTY_KEY_VID = 37;
    public static final int HPRT_MODEL_PROPERTY_KEY_WIDTH = 36;
    public static final int HPRT_MODEL_PROPERTY_PRINTER_CLASS = 3;
    public static final int HPRT_MODEL_PROPERTY_TYPE_BOOL = 1;
    public static final int HPRT_MODEL_PROPERTY_TYPE_BYTE = 4;
    public static final int HPRT_MODEL_PROPERTY_TYPE_INT = 3;
    public static final int HPRT_MODEL_PROPERTY_TYPE_STRING = 2;
    public static final int HPRT_MODEL_PT541 = 5441;
    public static final int HPRT_MODEL_PT562 = 5474;
    public static final int HPRT_MODEL_PT721 = 5921;
    public static final int HPRT_MODEL_TP801 = 4097;
    public static final int HPRT_MODEL_TP805 = 4098;
    public static final int HPRT_MODEL_TP806 = 4099;
    public static final int HPRT_MODEL_UNKNOWN = -1;
    public static final byte HPRT_PARTIAL_CUT = (byte) 1;
    public static final byte HPRT_PARTIAL_CUT_FEED = (byte) 66;
    public static String LanguageEncode = "GBK";
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_ERROR = 3;
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_ONOFFLINE = 2;
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_PAPER = 4;
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_PRINTER = 1;
    private static String h = null;
    public static boolean isHex = false;
    public static boolean isLog = false;
    public static boolean isWriteLog = false;
    private static Context context = null;
    private static boolean isBLEType = false;
    public static IPort iPort = null;
    public static boolean isOpen;
    private static String z = "";
    public int BitmapWidth = 0;
    public int PrintDataHeight = 0;

    public KuiaMaiPrinterHelper() {
    }

    public KuiaMaiPrinterHelper(Context context, String str) {
        KuiaMaiPrinterHelper.context = context;
        h = str;
    }

    public void IsBLEType(boolean isBLE) {
        isBLEType = isBLE;
    }

    /**
     * Method     连接蓝牙打印机
     * Parameters [str]
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午11:05
     * Modifytime 2018/10/30 上午11:05
     */
    public static int PortOpen(String str) {
        if (str.trim().length() <= 4) {
            return -1;
        }
        String[] split = str.split(",");
        IPort bTOperator;
        if (split[0].equals("Bluetooth")) {
            if (split.length != 2) {
                return -1;
            }
            bTOperator = new BTOperator(context, h);
            iPort = bTOperator;
            bTOperator.IsBLEType(isBLEType);
            isOpen = iPort.OpenPort(split[1]);
            z = "Bluetooth";
            if (!isOpen) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Method     自检页SelfTest
     * Parameters []
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午10:33
     * Modifytime 2018/10/30 上午10:33
     */
    public static int SelfTest() {
        int state = -1;
        try {
            state = iPort.WriteData("SELFTEST".getBytes(LanguageEncode));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return state;
    }

    /**
     * Method     设置打印区域
     * Parameters [width, height]
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午10:59
     * Modifytime 2018/10/30 上午10:59
     */
    public static int printAreaSize(String width, String height) {
        try {
            WriteData(("SIZE " + width + "mm," + height + "mm\r\n").getBytes(LanguageEncode));
            WriteData(("DIRECTION 0,0\r\n").getBytes(LanguageEncode));
            WriteData(("REFERENCE 0,0\r\n").getBytes(LanguageEncode));
            WriteData(("SET PEEL OFF\r\n").getBytes(LanguageEncode));
            WriteData(("SET TEAR ON\r\n").getBytes(LanguageEncode));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    /**
     * Method     开始打印
     * Parameters [str, str2]
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午11:03
     * Modifytime 2018/10/30 上午11:03
     */
    public static int Print(String str, String str2) {
        int status = -1;
        try {
            status = WriteData(("PRINT " + str + "," + str2 + "\r\n").getBytes(LanguageEncode));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    /**
     * Method     清除打印机缓存数据
     * Parameters []
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午10:56
     * Modifytime 2018/10/30 上午10:56
     */
    public static int CLS() {
        int status = -1;
        try {
            status = WriteData("CLS\r\n".getBytes(LanguageEncode));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    /**
     * Method     Bar
     * Parameters [x_pos, y_pos, width, height]
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/26 下午5:29
     * Modifytime 2018/10/26 下午5:29
     */
    public static int Bar(String x_pos, String y_pos, String width, String height) {
        int status =-1;
        try {
            String newBarCmd = String.format("BAR %s,%s,%s,%s\r\n", x_pos, y_pos, width, height);
            status = WriteData(newBarCmd.getBytes(LanguageEncode));
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }

        return status;
    }

    /**
     * Method     画矩形框
     * Parameters
     * 参数:
     * x_start:左上角 isOpen 坐标。
     * y_start:左上角 y 坐标。
     * x_end:右下角 isOpen 坐标。
     * y_end:右下角 y 坐标。
     * Thickness:线条宽度。
     * 返回:
     * 大于 0:正常，否则异常。
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/29 下午4:30
     * Modifytime 2018/10/29 下午4:30
     */
    public static int Box(String x_start, String y_start, String x_end, String y_end, String thickness) {
        String newBoxCmd = String.format("BOX %s,%s,%s,%s,%s,%s,%s,%s,%s,%s\r\n",
                x_start, "0",
                y_start, "0",
                x_end, "0",
                y_end, "0",
                thickness, "0");
        int status = -1;
        try {
            status = WriteData(newBoxCmd.getBytes(LanguageEncode));
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
        return status;
    }

    /**
     * Method     printText
     * Parameters
     * <p>
     * 参数:
     * x_pos:文字起始 isOpen 坐标:
     * y_pos:文字起始 y 坐标:
     * font:
     * 0: Monotye CG Triumvirate Bold Condensed, font width and height is stretchable
     * 1: 8 isOpen 12 fixed pitch dot font
     * 2: 12 isOpen 20 fixed pitch dot font
     * 3: 16 isOpen 24 fixed pitch dot font
     * 4: 24 isOpen 32 fixed pitch dot font
     * 5: 32 isOpen 48 dot fixed pitch font
     * 6: 14 isOpen 19 dot fixed pitch font OCR-B
     * 7: 21 isOpen 27 dot fixed pitch font OCR-B
     * 8: 14 x25 dot fixed pitch font OCR-A
     * 9:只有这个字体能够打印中文。
     * Rotation:打印方向。
     * 0 : No rotation
     * 90 : degrees, in clockwise direction
     * 180 : degrees, in clockwise direction
     * 270 : degrees, in clockwise direction
     * x_multiplication:isOpen 轴方向文字拉伸的倍数。
     * y_multiplication:y 轴方向文字拉伸的倍数。
     * code_data:文本数据。
     * 返回:
     * 大于 0:正常，否则异常。
     * <p>
     * <p>
     * <p>
     * <p>
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/26 下午5:32
     * Modifytime 2018/10/26 下午5:32
     */
    public static int printText(String x_pos, String y_pos, String font, String rotation, String x_multiplication, String y_multiplication, String data) {
        int status = -1;
        try {
            status = WriteData(("TEXT " + x_pos + "," + y_pos + ",\"" + font + "\"," + rotation + "," + x_multiplication + "," + y_multiplication + ",\"" + data + "\"\r\n").getBytes(LanguageEncode));
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
        return status;
    }

    /**
     * Method     打印条码
     * Parameters
     * x_pos:条码的起始横坐标。
     * y_pos:条码的起始纵坐标。
     * rotation:条码方向:
     * 0 : No rotation
     * 90 : degrees, in clockwise direction
     * 180 : degrees, in clockwise direction
     * 270 : degrees, in clockwise direction
     * code_type:条码类型:
     * 128，128M，EAN128 ，39 ，93，UPCA ，MSI ，ITF14
     * narrow :
     * 窄条的单位宽度(默认 1)。
     * Wide:宽条码的单位宽度(默认 1)。
     * Height:条码高度(单位像素)。
     * Readable:条码数据是否可见
     * 0: not readable
     * 1: human readable
     * <p>
     * code_data:条码数据。
     * 返回:
     * 大于 0:正常，否则异常。
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/26 下午5:09
     * Modifytime 2018/10/26 下午5:09
     */
    public static int printBarcode(String x_pos, String y_pos, String code_type, String height, String readable, String rotation, String narrow, String wide, String code_data) {
        int status =-1;
        try {
            status = WriteData(("BARCODE " + x_pos + "," + y_pos + ",\"" + code_type + "\"," + height + "," + readable + "," + rotation + "," + narrow + "," + wide + ",\"" + code_data + "\"\r\n").getBytes(LanguageEncode));
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
        return status;
    }

    /**
     * Method     printQRcode
     * Parameters [x_pos, y_pos, ecc_level, width, mode, rotation, code_data]
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/29 下午2:09
     * Modifytime 2018/10/29 下午2:09
     */
    public static int printQRcode(String x_pos, String y_pos, String ecc_level, String width, String mode, String rotation, String code_data) {
        int status = -1;
        try {
            status = WriteData(("QRCODE " + x_pos + "," + y_pos + "," + ecc_level + "," + width + "," + mode + "," + rotation + ",\"" + code_data + "\"\r\n").getBytes(LanguageEncode));
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
        return status;
    }

//    int printImage(String x_pos,String y_pos,Bitmap bmp ,boolean isNegate)
//    参数:
//    X:图片起始的 x 坐标。 Y:图片起始的 y 坐标。 bmap:图片的 Bitmap 的对象。 isNegate:图片反白。
//    True:正常显示。 False:取反显示。

    public static int printImage(String x_pos, String y_pos, Bitmap bitmap, boolean isNegate) {
        int width;
        if (bitmap.getWidth() % 8 == 0) {
            width = bitmap.getWidth() / 8;
        } else {
            width = (bitmap.getWidth() / 8) + 1;
        }
        int height = bitmap.getHeight();

        byte[] bArr = bitmap2Byte(bitmap);
        int status = -1;
        try {
            status = WriteData(("BITMAP " + x_pos + "," + y_pos + "," + width + "," + height + "," + "1" + ",").getBytes(LanguageEncode));
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
        if (isNegate) {
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = (byte) (bArr[i] ^ -1);
            }
        }
        WriteData(bArr);
        return status;
    }

    /**
     * Method     获取打印机状态
     * Parameters []
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午10:48
     * Modifytime 2018/10/30 上午10:48
     */
    public static int Status() {
        int status = -1;
        byte[] bArr = new byte[]{27, 33, 63, 13, 10};
        WriteData(bArr);
        byte[] statusBytes = ReadData(2);
        if (statusBytes.length > 0 && statusBytes[0] == 0) {
            status = 0;
        }
        return status;

    }

    /**
     * Method     传输打印数据
     * Parameters [str]
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午11:06
     * Modifytime 2018/10/30 上午11:06
     */
    public static int PrintData(String str) {
        int status = -1;
        try {
            status = WriteData(str.getBytes(LanguageEncode));
        } catch (UnsupportedEncodingException ex) {

        }
        return status;
    }

    /**
     * Method     写入数据
     * Parameters [data]
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午10:53
     * Modifytime 2018/10/30 上午10:53
     */
    public static int WriteData(byte[] data) {
        return iPort.WriteData(data);
    }

    /**
     * Method     读取数据
     * Parameters seconds 秒
     * Return     byte[]
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/10/30 上午10:52
     * Modifytime 2018/10/30 上午10:52
     */
    public static byte[] ReadData(int seconds) {
        return iPort.ReadData(seconds);
    }

    public static int Gap(String str, String str2) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_gap = androidsdk_tspl.hprt_cmd_tspl_gap(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_gap;
    }

    public static int Bline(String str, String str2) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_bline = androidsdk_tspl.hprt_cmd_tspl_bline(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_bline;
    }

    public static int Offset(String str) throws Exception {
        byte[] bArr = new byte[500];
        byte[] bArr2 = new byte[500];
        int hprt_cmd_tspl_offset = androidsdk_tspl.hprt_cmd_tspl_offset(A, bArr2, bArr, bArr.length, new int[1]);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_offset;
    }

    public static int Speed(String str) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_speed = androidsdk_tspl.hprt_cmd_tspl_speed(A, str.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_speed;
    }

    public static int Density(String str) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_density = androidsdk_tspl.hprt_cmd_tspl_density(A, str.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_density;
    }

    public static int Direction(String str) throws Exception {
        return iPort.WriteData(("DIRECTION " + str).getBytes(LanguageEncode));
    }

    public static int Reference(String str, String str2) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_reference = androidsdk_tspl.hprt_cmd_tspl_reference(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_reference;
    }

    public static int Codepage(String str) throws Exception {
        LanguageEncode = PublicFunction.getLanguageEncode(str);
        if ("Default,Chinese Simplified".contains(str)) {
            return 1;
        }
        return iPort.WriteData(("CODEPAGE " + str + "\r\n").getBytes(LanguageEncode));
    }

    public static int Feed(String str) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_feed = androidsdk_tspl.hprt_cmd_tspl_feed(A, str.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_feed;
    }

    public static int Formfeed() throws Exception {
        byte[] bArr = new byte[500];
        int hprt_cmd_tspl_formfeed = androidsdk_tspl.hprt_cmd_tspl_formfeed(A, bArr, bArr.length, new int[1]);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_formfeed;
    }

    public static int Home() throws Exception {
        byte[] bArr = new byte[500];
        int hprt_cmd_tspl_home = androidsdk_tspl.hprt_cmd_tspl_home(A, bArr, bArr.length, new int[1]);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_home;
    }

    public static int Sound(String str, String str2) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_sound = androidsdk_tspl.hprt_cmd_tspl_sound(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_sound;
    }

    public static int Cut() throws Exception {
        byte[] bArr = new byte[500];
        int hprt_cmd_tspl_cut = androidsdk_tspl.hprt_cmd_tspl_cut(A, bArr, bArr.length, new int[1]);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_cut;
    }

    public static int SetTear(String str) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_set_tear = androidsdk_tspl.hprt_cmd_tspl_set_tear(A, str.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_set_tear;
    }

    public static int Limitfeed(String str) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_limitfeed = androidsdk_tspl.hprt_cmd_tspl_limitfeed(A, str.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_limitfeed;
    }

    public static int Erase(String str, String str2, String str3, String str4) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_erase = androidsdk_tspl.hprt_cmd_tspl_erase(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), str3.getBytes(LanguageEncode), str4.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_erase;
    }

    public static int Dmatrix(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_dmatrix = androidsdk_tspl.hprt_cmd_tspl_dmatrix(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), str3.getBytes(LanguageEncode), str4.getBytes(LanguageEncode), str5.getBytes(LanguageEncode), str6.getBytes(LanguageEncode), str7.getBytes(LanguageEncode), str8.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_dmatrix;
    }

    public static int Maxicode(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_maxicode = androidsdk_tspl.hprt_cmd_tspl_maxicode(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), str3.getBytes(LanguageEncode), str4.getBytes(LanguageEncode), str5.getBytes(LanguageEncode), str6.getBytes(LanguageEncode), str7.getBytes(LanguageEncode), str8.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_maxicode;
    }

    public static int Pdf417(String str, String str2, String str3, String str4, String str5, String str6) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_pdf417 = androidsdk_tspl.hprt_cmd_tspl_pdf417(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), str3.getBytes(LanguageEncode), str4.getBytes(LanguageEncode), str5.getBytes(LanguageEncode), str6.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_pdf417;
    }

    public static int Putbmp(String str, String str2, String str3) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_putbmp = androidsdk_tspl.hprt_cmd_tspl_putbmp(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), str3.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_putbmp;
    }

    public static int Putpcx(String str, String str2, String str3) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_putpcx = androidsdk_tspl.hprt_cmd_tspl_putpcx(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), str3.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_putpcx;
    }

    public static int Reverse(String str, String str2, String str3, String str4) throws Exception {
        byte[] bArr = new byte[500];
        int[] iArr = new int[1];
        int hprt_cmd_tspl_reverse = androidsdk_tspl.hprt_cmd_tspl_reverse(A, str.getBytes(LanguageEncode), str2.getBytes(LanguageEncode), str3.getBytes(LanguageEncode), str4.getBytes(LanguageEncode), bArr, bArr.length, iArr);
        iPort.WriteData(bArr);
        return hprt_cmd_tspl_reverse;
    }


    public static String PortType() {
        return z;
    }

    public static boolean IsOpened() {
        return isOpen;
    }

    public static boolean PortClose() {
        boolean ClosePort;
        boolean z = true;
        if (iPort != null) {
            ClosePort = iPort.ClosePort();
        } else {
            ClosePort = true;
        }
        if (ClosePort) {
            z = false;
        }
        isOpen = z;
        return ClosePort;
    }

    public static int printSingleInterface(String str, HashMap<String, String> hashMap) throws Exception {
        if (str == null && hashMap == null) {
            return 1;
        }
        Iterator it = hashMap.keySet().iterator();
        String str2 = new String(inputStream2Byte(new FileInputStream(new File(str))), "utf-8");
        while (true) {
            String str3 = str2;
            if (it.hasNext()) {
                str2 = (String) it.next();
                str2 = str3.replace(str2, (CharSequence) hashMap.get(str2));
            } else {
                WriteData(new StringBuilder(String.valueOf(str3)).append("\r\n").toString().getBytes(LanguageEncode));
                return 0;
            }
        }
    }

    public static int printSingleInterface(InputStream inputStream, HashMap<String, String> hashMap) throws Exception {
        if (inputStream == null || hashMap == null) {
            return 1;
        }
        Iterator it = hashMap.keySet().iterator();
        String str = new String(inputStream2Byte(inputStream), "utf-8");
        while (true) {
            String str2 = str;
            if (it.hasNext()) {
                str = (String) it.next();
                str = str2.replace(str, (CharSequence) hashMap.get(str));
            } else {
                System.out.println("path:" + str2);
                PrintData(str2);
                return 0;
            }
        }
    }

    private static byte[] inputStream2Byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                return toByteArray;
            }
            byteArrayOutputStream.write(read);
        }
    }

    public static byte[] bitmap2Byte(Bitmap bitmap) {
        PrinterDataCore printerDataCore = new PrinterDataCore();
        printerDataCore.HalftoneMode = (byte) 0;
        printerDataCore.ScaleMode = (byte) 0;
        return printerDataCore.PrintDataFormat(bitmap);
    }

    public static boolean PrintBinaryFile(String str) throws Exception {
        iPort.WriteData(filePath2Byte(str));
        return true;
    }

    private static byte[] filePath2Byte(String str) {
        try {
            File file = new File(str);
            if (!file.exists() || !file.isFile() || !file.canRead()) {
                return null;
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read <= 0) {
                        byte[] toByteArray = byteArrayOutputStream.toByteArray();
                        fileInputStream.close();
                        byteArrayOutputStream.flush();
                        byteArrayOutputStream.close();
                        return toByteArray;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String bytetoString(byte[] bArr) {
        String str = "";
        if (bArr != null) {
            int length = bArr.length;
            int i = 0;
            while (i < length) {
                String stringBuilder = new StringBuilder(String.valueOf(str)).append(bArr[i]).append(" ").toString();
                i++;
                str = stringBuilder;
            }
        }
        return str;
    }

    public static String bytetohex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length);
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(bArr[i])}));
        }
        return stringBuilder.toString();
    }

    public static void logcat(String str) {
        if (isLog) {
            System.out.println(str);
        }
    }
}
