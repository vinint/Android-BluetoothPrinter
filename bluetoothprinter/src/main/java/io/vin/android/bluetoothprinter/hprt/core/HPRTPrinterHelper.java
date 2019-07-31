package io.vin.android.bluetoothprinter.hprt.core;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

public class HPRTPrinterHelper implements Serializable {
    private static final long serialVersionUID = 5311387967596233051L;
    public static final int ACTIVITY_CONNECT_BT = 3;
    public static final int ACTIVITY_CONNECT_WIFI = 4;
    public static final int ACTIVITY_IMAGE_FILE = 1;
    public static final int ACTIVITY_PRNFILE = 2;
    public static String BARCODE = "BARCODE";
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
    public static String CENTER = "CENTER";
    public static String CODABAR = "CODABAR";
    ;
    public static String CODABAR16 = "CODABAR16";
    public static String COMPRESSED_GRAPHICS = "CG";
    public static String CONCAT = "CONCAT";
    public static String EAN13 = "EAN13";
    public static String EAN132 = "EAN132";
    public static String EAN135 = "EAN135";
    public static String EAN8 = "EAN8";
    public static String EAN82 = "EAN82";
    public static String EAN85 = "EAN85";
    public static String ENDSTATUS = "CC";
    public static String EXPANDED_GRAPHICS = "EG";
    public static String F39 = "F39";
    public static String F39C = "F39C";
    public static String FIM = "FIM";
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
    public static String I2OF5 = "I2OF5";
    public static String I2OF5C = "I2OF5C";
    public static String I2OF5G = "I2OF5G";
    private static boolean Is_BLE_Type = false;
    public static String LEFT = "LEFT";
    public static String LanguageEncode = "gb2312";
    //    public static String LanguageEncode = "GBK";
    public static String MSI = "MSI";
    public static String MSI10 = "MSI10";
    public static String MSI1010 = "MSI1010";
    public static String MSI1110 = "MSI1110";
    public static String POSTNET = "POSTNET";
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_ERROR = 3;
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_ONOFFLINE = 2;
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_PAPER = 4;
    public static final int PRINTER_REAL_TIME_STATUS_ITEM_PRINTER = 1;
    static final int PROPERTY_LENGTH = 500;
    public static Context PreContext = null;
    public static IPort Printer = null;
    private static int PrinterClass;
    private static String PrinterName = null;
    public static String RIGHT = "RIGHT";
    public static String TEXT = "T";
    public static String TEXT180 = "T180";
    public static String TEXT270 = "T270";
    public static String TEXT90 = "T90";
    public static String TEXT_Anti_White = "TR";
    public static String UCCEAN128 = "UCCEAN128";
    public static String UPCA = "UPCA";
    public static String UPCA2 = "UPCA2";
    public static String UPCA5 = "UPCA5";
    public static String UPCE = "UPCE";
    public static String UPCE2 = "UPCE2";
    public static String UPCE5 = "UPCE5";
    public static String VBARCODE = "VBARCODE";
    public static String VCOMPRESSED_GRAPHICS = "VCG";
    public static String VCONCAT = "VCONCAT";
    public static String VEXPANDED_GRAPHICS = "VEG";
    public static String code128 = "128";
    public static String code39 = "39";
    public static String code39C = "39C";
    public static String code93 = "93";
    public static boolean isHex = false;
    public static boolean isLog = false;
    private static boolean isPortOpen;
    public static boolean isWriteLog = false;
    private static int printer_class = 2;
    private static String sPortType = "";
    public int BitmapWidth = 0;
    public int PrintDataHeight = 0;

    public HPRTPrinterHelper(Context context, String printerName) {
        PreContext = context;
        PrinterName = printerName;
    }

    public void IsBLEType(boolean isBLEType) {
        Is_BLE_Type = isBLEType;
    }

    public static void setSelf() throws Exception {
        Printer.WriteData(new byte[]{(byte) 29, (byte) 40, HPRT_FULL_CUT_FEED, (byte) 2, (byte) 0, (byte) 0, (byte) 2});
    }

    public static void papertype_CPCL(int page) throws Exception {
        Printer.WriteData(new byte[]{(byte) 27, (byte) 99, (byte) 48, (byte) page});
    }

    public static int printAreaSize(String offset, String Horizontal, String Vertical, String height, String qty) throws Exception {
        return Printer.WriteData(("! " + offset + " " + Horizontal + " " + Vertical + " " + height + " " + qty + "\r\n").getBytes(LanguageEncode));
    }

    public static int Print() throws Exception {
        String str = "";
        return Printer.WriteData("PRINT\r\n".getBytes(LanguageEncode));
    }

    public static int PoPrint() throws Exception {
        String str = "";
        return Printer.WriteData("POPRINT\r\n".getBytes(LanguageEncode));
    }

    public static int Encoding(String code) throws Exception {
        String data = "";
        return Printer.WriteData(("ENCODING " + code + "\r\n").getBytes(LanguageEncode));
    }

    public static int BackFeed(String backfeed) throws Exception {
        String data = "";
        return Printer.WriteData(("BACKFEED " + backfeed + "\r\n").getBytes(LanguageEncode));
    }

    public static int Form() throws Exception {
        String str = "";
        return Printer.WriteData("FORM\r\n".getBytes(LanguageEncode));
    }

    public static int Note(String note) throws Exception {
        String data = "";
        return Printer.WriteData(("; " + note + "\r\n").getBytes(LanguageEncode));
    }

    public static int Abort() throws Exception {
        String str = "";
        return Printer.WriteData("END\r\n".getBytes(LanguageEncode));
    }

    public static int Text(String command, String font, String size, String x, String y, String data) {
        String strdata = "";
        int result = 0;
        try {
            result = Printer.WriteData((command + " " + font + " " + size + " " + x + " " + y + " " + data + "\r\n").getBytes(LanguageEncode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int Concat(String command, String x, String y, String[] data) throws Exception {
        String strdata = command + " " + x + " " + y + "\r\n";
        for (int bData = 0; bData < data.length; bData++) {
            strdata = strdata + data[bData] + " ";
            if ((bData + 1) % 4 == 0) {
                strdata = strdata + "\r\n";
            }
        }
        return Printer.WriteData((strdata + "ENDCONCAT\r\n").getBytes(LanguageEncode));
    }

    public static int ML(String ml) throws Exception {
        String strdata = "";
        return Printer.WriteData(("ML " + ml + "\r\n").getBytes(LanguageEncode));
    }

    public static int ENDML() throws Exception {
        String str = "";
        return Printer.WriteData("ENDML\r\n".getBytes(LanguageEncode));
    }

    public static int Count(String ml) throws Exception {
        String strdata = "";
        return Printer.WriteData(("COUNT " + ml + "\r\n").getBytes(LanguageEncode));
    }

    public static int SetMag(String width, String height) {
        String strdata = "";
        int result = -1;
        try {
            result = Printer.WriteData(("SETMAG " + width + " " + height + "\r\n").getBytes(LanguageEncode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int Align(String align) throws Exception {
        String strdata = "";
        return Printer.WriteData((align + "\r\n").getBytes(LanguageEncode));
    }

    public static int Barcode(String command, String type, String width, String ratio, String height, String x, String y, boolean undertext, String number, String size, String offset, String data) throws Exception {
        String strdata = "";
        strdata = command + " " + type + " " + width + " " + ratio + " " + height + " " + x + " " + y + " " + data + "\r\n";
        if (undertext) {
            strdata = "BARCODE-TEXT " + number + " " + size + " " + offset + "\r\n" + strdata + "BARCODE-TEXT OFF\r\n";
        }
        return Printer.WriteData(strdata.getBytes(LanguageEncode));
    }

    public static int PrintQR(String command, String x, String y, String M, String U, String data) throws Exception {
        String strdata = "";
        return Printer.WriteData((command + " QR " + x + " " + y + " M " + M + " U " + U + "\r\nMA," + data + "\r\nENDQR\r\n").getBytes(LanguageEncode));
    }

    public static int PrintPDF417(String command, String x, String y, String XD, String YD, String C, String S, String data) throws Exception {
        String strdata = "";
        return Printer.WriteData((command + " PDF-417 " + x + " " + y + " XD " + XD + " YD " + YD + " C " + C + " S " + S + "\r\n" + data + "\r\nENDPDF\r\n").getBytes(LanguageEncode));
    }

    public static int Box(String X0, String Y0, String X1, String Y1, String width) throws Exception {
        String strdata = "";
        return Printer.WriteData(("BOX " + X0 + " " + Y0 + " " + X1 + " " + Y1 + " " + width + "\r\n").getBytes(LanguageEncode));
    }

    public static int Line(String X0, String Y0, String X1, String Y1, String width) {
        String strdata = "";
        int result = -1;
        try {
            result = Printer.WriteData(("LINE " + X0 + " " + Y0 + " " + X1 + " " + Y1 + " " + width + "\r\n").getBytes(LanguageEncode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int InverseLine(String X0, String Y0, String X1, String Y1, String width) {
        String strdata = "";
        int result = -1;
        try {
            result = Printer.WriteData(("INVERSE-LINE " + X0 + " " + Y0 + " " + X1 + " " + Y1 + " " + width + "\r\n").getBytes(LanguageEncode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int Expanded(String x, String y, String url) throws Exception {
        int width1;
        Bitmap bmp = BitmapFactory.decodeFile(url);
        if (bmp.getWidth() % 8 == 0) {
            width1 = bmp.getWidth() / 8;
        } else {
            width1 = (bmp.getWidth() / 8) + 1;
        }
        int bmpHeight = bmp.getHeight();
        if (((bmpHeight > SupportMenu.USER_MASK ? 1 : 0) | (width1 > 999 ? 1 : 0)) != 0) {
            return -1;
        }
        byte[] printImage = PrintImage(url, (byte) 0, (byte) 0);
        String strdata = "";
        strdata = "CG " + width1 + " " + bmpHeight + " " + x + " " + y + " ";
        String str = "\r\n";
        int WriteData = Printer.WriteData(strdata.getBytes(LanguageEncode));
        WriteData = Printer.WriteData(printImage);
        return Printer.WriteData(str.getBytes(LanguageEncode));
    }

    public static int Expanded(String x, String y, Bitmap bmp, byte by) throws Exception {
        int width1;
        if (bmp.getWidth() % 8 == 0) {
            width1 = bmp.getWidth() / 8;
        } else {
            width1 = (bmp.getWidth() / 8) + 1;
        }
        int bmpHeight = bmp.getHeight();
        if (((bmpHeight > SupportMenu.USER_MASK ? 1 : 0) | (width1 > 999 ? 1 : 0)) != 0) {
            return -1;
        }
        byte[] printImage = PrintBitmap(bmp, by, (byte) 0);
        String strdata = "";
        strdata = "CG " + width1 + " " + bmpHeight + " " + x + " " + y + " ";
        String str = "\r\n";
        int WriteData = Printer.WriteData(strdata.getBytes(LanguageEncode));
        WriteData = Printer.WriteData(printImage);
        return Printer.WriteData(str.getBytes(LanguageEncode));
    }

    public static int printText(String str) throws Exception {
        return Printer.WriteData(str.getBytes(LanguageEncode));
    }

    public static int Contrast(String contrast) throws Exception {
        String strdata = "";
        return Printer.WriteData(("CONTRAST " + contrast + "\r\n").getBytes(LanguageEncode));
    }

    public static int Speed(String speed) throws Exception {
        String strdata = "";
        return Printer.WriteData(("SPEED " + speed + "\r\n").getBytes(LanguageEncode));
    }

    public static int SetSp(String setsp) throws Exception {
        String strdata = "";
        return Printer.WriteData(("SETSP " + setsp + "\r\n").getBytes(LanguageEncode));
    }

    public static int Prefeed(String prefeed) throws Exception {
        String strdata = "";
        return Printer.WriteData(("PREFEED " + prefeed + "\r\n").getBytes(LanguageEncode));
    }

    public static int Postfeed(String posfeed) throws Exception {
        String strdata = "";
        return Printer.WriteData(("POSFEED " + posfeed + "\r\n").getBytes(LanguageEncode));
    }

    public static int Country(String country) throws Exception {
        String strdata = "";
        return Printer.WriteData(("COUNTRY " + country + "\r\n").getBytes(LanguageEncode));
    }

    public static int Beep(String beep) throws Exception {
        String strdata = "";
        return Printer.WriteData(("BEEP " + beep + "\r\n").getBytes(LanguageEncode));
    }

    public static int Underline(boolean UL) throws Exception {
        String strdata = "";
        if (UL) {
            strdata = "UNDERLINE ON\r\n";
        } else {
            strdata = "UNDERLINE OFF\r\n";
        }
        return Printer.WriteData(strdata.getBytes(LanguageEncode));
    }

    public static int Wait(String wait) throws Exception {
        String strdata = "";
        return Printer.WriteData(("WAIT " + wait + "\r\n").getBytes(LanguageEncode));
    }

    public static int PageWidth(String pw) throws Exception {
        String strdata = "";
        return Printer.WriteData(("PW " + pw + "\r\n").getBytes(LanguageEncode));
    }

    public static int setSpeed(String speed) throws Exception {
        return Printer.WriteData(("SPEED " + speed + "\r\n").getBytes(LanguageEncode));
    }


    public static int Setlf(String SF) throws Exception {
        String strdata = "";
        return Printer.WriteData(("! U! SETLP " + SF + "\r\n").getBytes(LanguageEncode));
    }

    public static int Setlp(String font, String size, String spacing) throws Exception {
        String strdata = "";
        return Printer.WriteData(("! U1 SETLP " + font + " " + size + " " + spacing + "\r\n").getBytes(LanguageEncode));
    }

    public static int RowSetBold(String bold) throws Exception {
        return Printer.WriteData(("! U1 SETBOLD " + bold + "\r\n").getBytes(LanguageEncode));
    }

    public static int RowSetX(String X) throws Exception {
        return Printer.WriteData(("! U1 LMARGIN " + X + "\r\n").getBytes(LanguageEncode));
    }

    public static int printSingleInterface(String url, HashMap<String, String> map) throws Exception {
        if (url == null && map == null) {
            return 1;
        }
        File file = new File(url);
        if (file == null) {
            Log.e("TGA", url + "路径错误");
            return 1;
        }
        FileInputStream fis = new FileInputStream(file);
        if (fis == null) {
            Log.e("TGA", "文件不存在");
            return 1;
        }
        String path = new String(InputStreamToByte(fis), "utf-8");
        if (path == null) {
            return 1;
        }
        for (String string : map.keySet()) {
            path = path.replace(string, (CharSequence) map.get(string));
        }
        return WriteData(path.getBytes("GBK"));
    }

    public static int printSingleInterface(InputStream is, HashMap<String, String> pum) throws Exception {
        if (is == null || pum == null) {
            return 1;
        }
        String path = new String(InputStreamToByte(is), "utf-8");
        for (String string : pum.keySet()) {
            path = path.replace(string, (CharSequence) pum.get(string));
        }
        return PrintData(path);
    }

    private static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        while (true) {
            int ch = is.read();
            if (ch != -1) {
                bytestream.write(ch);
            } else {
                byte[] imgdata = bytestream.toByteArray();
                bytestream.close();
                return imgdata;
            }
        }
    }

    public static int PrintData(String str) throws Exception {
        return Printer.WriteData(str.getBytes(LanguageEncode));
    }

    public static int SetBold(String bold) {
        int result = -1;
        try {
            result = Printer.WriteData(("SETBOLD " + bold + "\r\n").getBytes(LanguageEncode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getstatus() throws Exception {
        if (Printer.WriteData(new byte[]{(byte) 27, (byte) 104}) <= 0) {
            return -1;
        }
        byte[] ry = Printer.ReadData(3);
        int rylegth = ry.length;
        if (rylegth <= 0) {
            return -1;
        }
        logcat(bytetohex(ry));
        return ry[rylegth - 1] & 255;
    }

    public static int AutLine(String x, String y, int width, int size, String str) throws Exception {
        int var11;
        int iReturn;
        int i = str.length();
        byte textsize = (byte) 24;
        String data = "";
        switch (size) {
            case 1:
                textsize = (byte) 9;
                break;
            case 2:
                textsize = (byte) 8;
                break;
            case 3:
                textsize = (byte) 16;
                break;
            case 4:
                textsize = (byte) 32;
                break;
            case 8:
                textsize = (byte) 24;
                break;
            case 55:
                textsize = (byte) 16;
                break;
        }
        if ((i * textsize) % width == 0) {
            var11 = (i * textsize) / width;
        } else {
            var11 = ((i * textsize) / width) + 1;
        }
        String[] datastr = new String[var11];
        for (iReturn = 0; iReturn < var11; iReturn++) {
            if (iReturn == var11 - 1) {
                datastr[iReturn] = str.substring((width / textsize) * iReturn, str.length());
            } else {
                datastr[iReturn] = str.substring((width / textsize) * iReturn, (iReturn + 1) * (width / textsize));
            }
        }
        for (iReturn = 0; iReturn < var11; iReturn++) {
            data = data + "TEXT " + size + " 0 " + x + " " + (Integer.valueOf(y).intValue() + (iReturn * textsize)) + " " + datastr[iReturn] + "\r\n";
        }
        System.out.println(data);
        return Printer.WriteData(data.getBytes(LanguageEncode));
    }

    public static int AutCenter(String command, String x, String y, int width, int size, String str) throws Exception {
        String data = "";
        byte textsize = (byte) 24;
        switch (size) {
            case 1:
                textsize = (byte) 3;
                break;
            case 2:
                textsize = (byte) 24;
                break;
            case 3:
                textsize = (byte) 16;
                break;
            case 4:
                textsize = (byte) 32;
                break;
            case 8:
                textsize = (byte) 24;
                break;
            case 55:
                textsize = (byte) 16;
                break;
        }
        if (command.equals(TEXT)) {
            data = "TEXT " + size + " 0 " + ((int) (((((float) width) - ((((float) str.getBytes("GBK").length) * ((float) textsize)) / 2.0f)) / 2.0f) + ((float) Integer.valueOf(x).intValue()))) + " " + y + " " + str + "\r\n";
        } else {
            data = command + " " + size + " 0 " + x + " " + ((int) (((((float) width) - ((((float) str.getBytes("GBK").length) * ((float) textsize)) / 2.0f)) / 2.0f) + ((float) Integer.valueOf(y).intValue()))) + " " + str + "\r\n";
        }
        return Printer.WriteData(data.getBytes(LanguageEncode));
    }

    public static int PrintTextCPCL(String command, int font, String x, String y, String data, int n, boolean Iscenter, int width) throws Exception {
        if (font == 1) {
            font = 24;
        }
        boolean isbole = false;
        boolean isInverse = false;
        boolean isD_width = false;
        boolean isD_height = false;
        if ((n & 1) == 1) {
            isbole = true;
        }
        if ((n & 2) == 2) {
            isInverse = true;
        }
        if ((n & 4) == 4) {
            isD_width = true;
        }
        if ((n & 8) == 8) {
            isD_height = true;
        }
        if (isbole) {
            SetBold("1");
        }
        if (isD_height) {
            SetMag("1", "2");
        }
        if (isD_width) {
            SetMag("2", "1");
        }
        if (isD_width & isD_height) {
            SetMag("2", "2");
        }
        String b = "55";
        switch (font) {
            case 1:
                b = "1";
                break;
            case 16:
                b = "55";
                break;
            case 24:
                b = "8";
                break;
            case 32:
                b = "4";
                break;
        }
        if (Iscenter) {
            AutCenter(command, x, y, width, Integer.valueOf(b).intValue(), data);
        } else {
            Text(command, b, "0", x, y, data);
        }
        if (isD_height | isD_width) {
            SetMag("1", "1");
        }
        if (isInverse) {
            float a;
            if (command.equals(TEXT)) {
                if (isD_width) {
                    a = ((float) Integer.valueOf(x).intValue()) + (((float) data.getBytes("GBK").length) * ((float) font));
                } else {
                    a = ((float) Integer.valueOf(x).intValue()) + ((((float) data.getBytes("GBK").length) * ((float) font)) / 2.0f);
                }
                if (isD_height) {
                    if (Iscenter) {
                        InverseLine(x, y, "" + width, y, "" + (font * 2));
                    } else {
                        InverseLine(x, y, "" + a, y, "" + (font * 2));
                    }
                } else if (Iscenter) {
                    InverseLine(x, y, "" + width, y, "" + font);
                } else {
                    InverseLine(x, y, "" + a, y, "" + font);
                }
            } else {
                if (isD_width) {
                    a = ((float) Integer.valueOf(y).intValue()) + (((float) data.getBytes("GBK").length) * ((float) font));
                } else {
                    a = ((float) Integer.valueOf(y).intValue()) + ((((float) data.getBytes("GBK").length) * ((float) font)) / 2.0f);
                }
                int xb1;
                if (isD_height) {
                    if (Iscenter) {
                        xb1 = Integer.valueOf(x).intValue() - (font * 2);
                        InverseLine("" + xb1, y, "" + xb1, "" + (Integer.valueOf(y).intValue() + width), "" + (font * 2));
                    } else {
                        xb1 = Integer.valueOf(x).intValue() - (font * 2);
                        InverseLine("" + xb1, y, "" + xb1, "" + a, "" + (font * 2));
                    }
                } else if (Iscenter) {
                    xb1 = Integer.valueOf(x).intValue() - font;
                    InverseLine("" + xb1, y, "" + xb1, "" + (Integer.valueOf(y).intValue() + width), "" + font);
                } else {
                    xb1 = Integer.valueOf(x).intValue() - font;
                    InverseLine("" + xb1, y, "" + xb1, "" + a, "" + font);
                }
            }
        }
        if (isbole) {
            SetBold("0");
        }
        return 0;
    }

    public static int WriteData(byte[] bData) throws Exception {
        return Printer.WriteData(bData);
    }

    public static byte[] ReadData(int second) throws Exception {
        return Printer.ReadData(second);
    }

    public static byte[] GetPrinterVersion(byte[] bData) throws Exception {
        WriteData(new byte[]{(byte) 29, (byte) 73, HPRT_FULL_CUT_FEED});
        return ReadData(3);
    }

    public static String PortType() {
        return sPortType;
    }

    public static boolean IsOpened() {
        return isPortOpen;
    }

    public static int PortOpen(String portSetting) throws Exception {
        if (portSetting.trim().length() <= 4) {
            return -1;
        }
        String[] strPort = portSetting.split(",");
        if (strPort[0].equals("Bluetooth")) {
            if (strPort.length != 2) {
                return -1;
            }
            Printer = new BTOperator(PreContext, PrinterName);
            Printer.IsBLEType(Is_BLE_Type);
            isPortOpen = Printer.OpenPort(strPort[1]);
            sPortType = "Bluetooth";
            if (!isPortOpen) {
                return -1;
            }
        }
        return 0;
    }

    public static void initPrinterWithSocket(BluetoothSocket socket) {
        BTOperator.isShake = false;
        Printer = new BTOperator(PreContext, PrinterName);
        Printer.IsBLEType(Is_BLE_Type);
        ((BTOperator) Printer).mmSocket = socket;
        try {
            ((BTOperator) Printer).mmInStream = socket.getInputStream();
            ((BTOperator) Printer).mmOutStream = socket.getOutputStream();
            ((BTOperator) Printer).blnOpenPort = true;
            ((BTOperator) Printer).mmDevice = socket.getRemoteDevice();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sPortType = "Bluetooth";
        isPortOpen = true;

    }

    public static boolean PortClose() throws Exception {
        boolean blnOK = true;
        if (Printer != null) {
            blnOK = Printer.ClosePort();
        }
        isPortOpen = !blnOK;
        return blnOK;
    }

    private static byte[] PrintBitmap(Bitmap bmp, byte halftoneType, byte scaleMode) throws Exception {
        return CreateBitmapPrintDatas(bmp, halftoneType, scaleMode);
    }

    private static byte[] PrintImage(String filePath, byte halftoneType, byte scaleMode) throws Exception {
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        return PrintBitmap(bmp, halftoneType, scaleMode);
    }

    private static byte[] CreateBitmapPrintDatas(Bitmap bmp, byte halftoneType, byte scaleMode) {
        PrinterDataCore PDC = new PrinterDataCore();
        PDC.HalftoneMode = halftoneType;
        PDC.ScaleMode = scaleMode;
        return PDC.PrintDataFormat(bmp);
    }

    public static boolean PrintBinaryFile(String filePath) throws Exception {
        Printer.WriteData(getByteArrayFromFile(filePath));
        return true;
    }

    private static byte[] getByteArrayFromFile(String fileName) {
        byte[] byteArray = null;
        try {
            File file = new File(fileName);
            File file2;
            if (file.exists() && file.isFile() && file.canRead()) {
                byteArray = null;
                try {
                    FileInputStream e = new FileInputStream(file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int count = e.read(buffer);
                        if (count <= 0) {
                            break;
                        }
                        baos.write(buffer, 0, count);
                    }
                    byteArray = baos.toByteArray();
                    e.close();
                    baos.flush();
                    baos.close();
                } catch (Exception var8) {
                    var8.printStackTrace();
                }
                file2 = file;
                return byteArray;
            }
            file2 = file;
            return byteArray;
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return byteArray;
    }

    public static int getEndStatus() throws Exception {
        int lastIndexOf = -1;
        byte[] readData = null;
        while (lastIndexOf == -1) {
            readData = ReadData(16);
            if (readData.length == 0) {
                return -1;
            }
            String bytetohex = bytetohex(readData);
            logcat("打印完成时状态：" + bytetohex);
            lastIndexOf = bytetohex.lastIndexOf(ENDSTATUS);
            logcat("lastIndexOf：" + lastIndexOf);
        }
        return readData[(lastIndexOf / 3) + 1];
    }

    public static String getPrintName() throws Exception {
        String name = "";
        int lastIndexOf = -1;
        byte[] readData = null;
        if (WriteData(new byte[]{(byte) 29, (byte) 73, (byte) 67}) <= 0) {
            return name;
        }
        while (lastIndexOf == -1) {
            readData = ReadData(3);
            if (readData.length == 0) {
                return name;
            }
            String bytetohex = bytetohex(readData);
            logcat("获取打印机名称：" + bytetohex);
            lastIndexOf = bytetohex.lastIndexOf("5F");
        }
        name = new String(readData);
        return name.substring(lastIndexOf + 1, name.length() - 1);
    }

    public static String getPrintModel() throws Exception {
        String model = "";
        int lastIndexOf = -1;
        byte[] readData = null;
        if (printText("! U1 NAME\r\n") <= 0) {
            return model;
        }
        while (lastIndexOf == -1) {
            readData = ReadData(3);
            if (readData.length == 0) {
                return model;
            }
            String bytetohex = bytetohex(readData);
            logcat("获取打印机版本号：" + bytetohex);
            lastIndexOf = bytetohex.lastIndexOf("56");
        }
        model = new String(readData);
        return model.substring(lastIndexOf, model.length() - 1);
    }

    public static String getPrintID() throws Exception {
        String ID = "";
        int lastIndexOf = -1;
        String bytetohex = "";
        if (WriteData(new byte[]{(byte) 27, (byte) 28, (byte) 38, (byte) 32, (byte) 86, (byte) 49, (byte) 32, (byte) 103, (byte) 101, (byte) 116, (byte) 118, (byte) 97, (byte) 108, (byte) 32, (byte) 34, (byte) 118, (byte) 101, (byte) 114, (byte) 115, (byte) 105, (byte) 111, (byte) 110, (byte) 34, (byte) 13, (byte) 10}) <= 0) {
            return ID;
        }
        while (lastIndexOf == -1) {
            byte[] readData1 = ReadData(3);
            if (readData1.length == 0) {
                return ID;
            }
            bytetohex = bytetohex(readData1);
            logcat("获取打印机版本号：" + bytetohex);
            lastIndexOf = bytetohex.lastIndexOf("22");
        }
        return bytetohex.substring(lastIndexOf + 3, bytetohex.length() - 4);
    }

    public static String bytetoString(byte[] bytearray) {
        String result = "";
        if (bytearray != null) {
            for (byte b : bytearray) {
                result = result + b + " ";
            }
        }
        return result;
    }

    public static String bytetohex(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        byte[] var5 = data;
        int var4 = data.length;
        for (int var3 = 0; var3 < var4; var3++) {
            stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(var5[var3])}));
        }
        return stringBuilder.toString();
    }

    public static void logcat(String log) {
        if (isLog) {
            System.out.println(log);
        }
    }

    public static void printBitmap(int x, int y, Bitmap bitmap, boolean isform) throws Exception {
        int num = bitmap.getHeight() / 50;
        int bit_Y = 0;
        int tobitmap = 0;
        while (tobitmap < num) {
            printAreaSize("0", "200", "200", "" + 50, "1");
            Bitmap tobitmap1 = Tobitmap(bitmap, bitmap.getWidth(), 50, 0, bit_Y);
            if (tobitmap == 0) {
                Expanded("" + x, "" + y, tobitmap1, (byte) 0);
            } else {
                Expanded("" + x, "0", tobitmap1, (byte) 0);
            }
            if (((tobitmap == num + -1) & (isform & ((bitmap.getHeight() % 50 == 0))))) {
//            if (((tobitmap == num + -1 ? 1 : 0) & (isform & (bitmap.getHeight() % 50 == 0 ? 1 : 0))) != 0) {
                Form();
            }
            Print();
            bit_Y += 50;
            tobitmap++;
        }
        if (bitmap.getHeight() % 50 != 0) {
            printAreaSize("0", "200", "200", "" + (bitmap.getHeight() - (50 * num)), "1");
            Expanded("" + x, "0", Tobitmap(bitmap, bitmap.getWidth(), bitmap.getHeight() - (50 * num), 0, bit_Y), (byte) 0);
            if (isform) {
                Form();
            }
            Print();
        }
    }

    public static Bitmap Tobitmap(Bitmap bitmap, int width, int height, int x, int y) {
        return Bitmap.createBitmap(bitmap, x, y, width, height);
    }
}
