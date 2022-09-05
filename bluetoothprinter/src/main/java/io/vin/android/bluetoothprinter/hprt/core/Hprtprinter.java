package io.vin.android.bluetoothprinter.hprt.core;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import io.vin.android.bluetoothprinterprotocol.PrintCallback;


public class Hprtprinter {
    private int mOrientation = 0;

    public void setPage(int i, int i1, int i2) {
        this.mOrientation = i2;
        try {
            HPRTPrinterHelper.printAreaSize("0", "200", "200", "" + i1, "1");
            HPRTPrinterHelper.PageWidth("" + i);
            HPRTPrinterHelper.setSpeed("4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawRect(int i, int i1, int i2, int i3, int i4, int i5) {
        if (i == 0) {
            try {
                HPRTPrinterHelper.Box("" + i1, "" + i2, "" + i3, "" + i4, "" + i5);
            } catch (Exception e) {
            }
        }
    }

    public void drawRectFill(int i, int i1, int i2, int i3, int i4) {
        int i5 = 0;
        if (i == 0) {
            try {
                HPRTPrinterHelper.Line("" + i1, "" + i2, "" + i3, "" + i2, "" + (i4 - i2));
                return;
            } catch (Exception e) {
                return;
            }
        }
        HPRTPrinterHelper.Line("" + i1, "" + i2, "" + i3, "" + i2, "" + (i4 - i2));
        String str = "" + (i1 + -10 < 0 ? 0 : i1 - 10);
        String str2 = "" + (i2 + -10 < 0 ? 0 : i2 - 10);
        String str3 = "" + (i3 + 10);
        StringBuilder append = new StringBuilder().append("");
        if (i2 - 10 >= 0) {
            i5 = i2 - 10;
        }
        HPRTPrinterHelper.InverseLine(str, str2, str3, append.append(i5).toString(), "" + ((i4 - i2) + 20));
    }

    public void drawLine(int i, int i1, int i2, int i3, int i4, int i5) {
        int i6 = 0;
        if (i == 0) {
            try {
                HPRTPrinterHelper.Line("" + i1, "" + i2, "" + i3, "" + i4, "" + i5);
                return;
            } catch (Exception e) {
                return;
            }
        }
        int a;
        int b;
        HPRTPrinterHelper.Line("" + i1, "" + i2, "" + i3, "" + i4, "" + i5);
        if (i1 < i3) {
            a = i1;
        } else {
            a = i3;
        }
        if (i2 < i4) {
            b = i2;
        } else {
            b = i4;
        }
        String str = "" + (a + -10 < 0 ? 0 : a - 10);
        String str2 = "" + (b + -10 < 0 ? 0 : b - 10);
        StringBuilder append = new StringBuilder().append("");
        if (i1 <= i3) {
            i1 = i3;
        }
        String stringBuilder = append.append(i1 + 10).toString();
        StringBuilder append2 = new StringBuilder().append("");
        if (b - 10 >= 0) {
            i6 = b - 10;
        }
        String stringBuilder2 = append2.append(i6).toString();
        append2 = new StringBuilder().append("");
        if (i2 <= i4) {
            i2 = i4;
        }
        HPRTPrinterHelper.InverseLine(str, str2, stringBuilder, stringBuilder2, append2.append((i2 - b) + 20).toString());
    }

    public void drawDashLine(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    public void drawText(String text, int x, int y, int color, int fontSize, int style, int rotation) {
        if (!TextUtils.isEmpty(text)) {
            String fontString;
            String sizeString;
            String command = HPRTPrinterHelper.TEXT;
            boolean bold = false;
            int scale = 1;
            switch (rotation) {
                case 0:
                    command = HPRTPrinterHelper.TEXT;
                    break;
                case 90:
                    command = HPRTPrinterHelper.TEXT270;
                    break;
                case 180:
                    command = HPRTPrinterHelper.TEXT180;
                    break;
                case 270:
                    command = HPRTPrinterHelper.TEXT90;
                    break;
            }
            if ((style & 1) == 1) {
                bold = true;
            }
            switch (fontSize) {
                case 16:
                    fontString = "55";
                    sizeString = "0";
                    break;
                case 20:
                    fontString = "3";
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
                    fontString = "3";
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
                case 128:
                    fontString = "4";
                    sizeString = "0";
                    scale = 4;
                    break;
                case 140:
                    fontString = "28";
                    sizeString = "0";
                    scale = 5;
                    break;
                case 160:
                    fontString = "4";
                    sizeString = "0";
                    scale = 5;
                    break;
                default:
                    fontString = "55";
                    sizeString = "0";
                    scale = 1;
                    break;
            }
            if (bold) {
                try {
                    HPRTPrinterHelper.SetBold("1");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            HPRTPrinterHelper.SetMag(String.valueOf(scale), String.valueOf(scale));
            HPRTPrinterHelper.Text(command, fontString, sizeString, String.valueOf(x), String.valueOf(y), text);
            HPRTPrinterHelper.SetMag("1", "1");
            if (bold) {
                HPRTPrinterHelper.SetBold("0");
            }
        }
    }

    public void drawBarCode(String text, int x, int y, int height, int lineWidth, int type, int rotation) {
        if (!TextUtils.isEmpty(text)) {
            try {
                String bari4 = HPRTPrinterHelper.BARCODE;
                switch (rotation) {
                    case 0:
                        bari4 = HPRTPrinterHelper.BARCODE;
                        break;
                    case 90:
                        bari4 = HPRTPrinterHelper.VBARCODE;
                        break;
                    case 180:
                        bari4 = HPRTPrinterHelper.BARCODE;
                        break;
                    case 270:
                        bari4 = HPRTPrinterHelper.VBARCODE;
                        break;
                }
                switch (type) {
                    case 0:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.code128, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 1:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.code39, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 2:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.code93, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 3:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.CODABAR, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 4:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.EAN8, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 5:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.EAN13, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 6:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.UPCA, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 7:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.UPCE, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    case 8:
                        HPRTPrinterHelper.Barcode(bari4, HPRTPrinterHelper.I2OF5, "" + lineWidth, "1", "" + height, "" + x, "" + y, false, "7", "0", "5", text);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
            }
        }
    }

    public void drawQRCode(String text, int x, int y, int unitWidth, int version, int level, int rotation) {
        if (!TextUtils.isEmpty(text) && unitWidth >= 1) {
            String levelText;
            String command = HPRTPrinterHelper.BARCODE;
            switch (rotation) {
                case 0:
                    command = HPRTPrinterHelper.BARCODE;
                    break;
                case 90:
                    command = HPRTPrinterHelper.VBARCODE;
                    break;
                case 180:
                    command = HPRTPrinterHelper.BARCODE;
                    break;
                case 270:
                    command = HPRTPrinterHelper.VBARCODE;
                    break;
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
            try {
                byte[] bData = (command + " QR " + x + " " + y + " M " + 2 + " U " + unitWidth + "\r\n" + levelText + "A," + text + "\r\nENDQR\r\n").getBytes(HPRTPrinterHelper.LanguageEncode);
                IPort printer = getPrinter();
                if (printer != null) {
                    printer.WriteData(bData);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private IPort getPrinter() {
        Field field = null;
        try {
            field = HPRTPrinterHelper.class.getDeclaredField("Printer");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field == null) {
            try {
                field = HPRTPrinterHelper.class.getDeclaredField("o");
            } catch (NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                return (IPort) field.get(null);
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            }
        }
        return null;
    }

    public void drawImage(Bitmap bitmap, int i, int i1) {
        try {
            HPRTPrinterHelper.Expanded("" + i, "" + i1, bitmap, (byte) 0);
        } catch (Exception e) {
        }
    }

    public void drawImage(Bitmap bitmap, int i, int i1, int i2, int i3) {
        try {
            HPRTPrinterHelper.Expanded("" + i, "" + i1, bitmap, (byte) 0);
        } catch (Exception e) {
        }
    }

    public void print(PrintCallback printCallback) {
        try {
            if (this.mOrientation == 0) {
                HPRTPrinterHelper.Print();
            } else {
                HPRTPrinterHelper.PoPrint();
            }
            checkPrintState(printCallback);
        } catch (Exception e) {
            printCallback.onPrintFail(-1);
        }
    }

    private void checkPrintState(PrintCallback callback) {
        try {
            HPRTPrinterHelper.WriteData(new byte[]{(byte) 16, (byte) 4, (byte) 5});
            String statusStr = new String(HPRTPrinterHelper.ReadData(5));
            if (statusStr.contains("OK")) {
                callback.onPrintSuccess();
            } else if (statusStr.equals("ERROR")) {
                queryPrinterStatus(callback);
            } else {
                callback.onPrintSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onPrintFail(-1);
        }
    }

    public void queryPrinterStatus(PrintCallback callback) {
        if (HPRTPrinterHelper.Printer == null) {
            callback.onPrintFail(32);
            return;
        }
        if (((BTOperator) HPRTPrinterHelper.Printer).mmSocket == null){
            callback.onPrintFail(32);
            return;
        }
        if (!((BTOperator) HPRTPrinterHelper.Printer).mmSocket.isConnected()) {
            callback.onPrintFail(32);
            return;
        }
        //1.清除写入流
        while (HPRTPrinterHelper.Printer.ReadData(1).length > 0) {
        }

        //2.写入状态指令
        int status = -1;
        byte[] cmd = new byte[]{27, 104};
        int var2;
        byte[] resp = null;
        if (HPRTPrinterHelper.Printer.WriteData(cmd) > 0 && (var2 = (resp = HPRTPrinterHelper.Printer.ReadData(3)).length) > 0) {
            status = resp[var2 - 1] & 255;
        } else {
            //打印机已经断开
            callback.onPrintFail(32);
            return;
        }
        if (resp[0] ==110){
            callback.onPrintFail(-2);
        }else if (status == 0) {
            //打印机准备就绪
            callback.onPrintSuccess();
        } else if (status == 1) {
            //打印机打印中
            callback.onPrintFail(16);
        } else if (status == 2) {
            //打印机缺纸
            callback.onPrintFail(2);
        } else if ((status == 6)||(status ==12)) {
            //打印机开盖
            callback.onPrintFail(1);
        } else if (status ==8){
            //电量低
            callback.onPrintFail(4);
        }else {
            //其他错误
            callback.onPrintFail(-1);
        }
    }

    public void printAndFeed(PrintCallback printCallback) {
        try {
            HPRTPrinterHelper.Form();
            if (this.mOrientation == 0) {
                HPRTPrinterHelper.Print();
            } else {
                HPRTPrinterHelper.PoPrint();
            }
            checkPrintState(printCallback);
        } catch (Exception e) {
            printCallback.onPrintFail(-1);
        }
    }

    public void feedToNextLabel() {
        try {
            HPRTPrinterHelper.Form();
        } catch (Exception e) {
        }
    }

    public int getPrintWidth() {
        return 576;
    }

}
