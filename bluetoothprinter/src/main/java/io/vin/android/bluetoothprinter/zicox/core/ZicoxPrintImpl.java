package io.vin.android.bluetoothprinter.zicox.core;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;


import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ZicoxPrintImpl {
    private static List<FontInfo> _listFontInfo = new ArrayList();
    private int _h;
    private int _w;
    private byte[] listData = new byte[131072];
    private int listDataLen = 0;
    List<DrawBarcode1DItem> listDrawBarcode1D = new ArrayList();
    List<DrawBarcodeQRcodeItem> listDrawBarcodeQRcode = new ArrayList();
    List<DrawBitmapItem> listDrawBitmap = new ArrayList();
    List<DrawBoxItem> listDrawBox = new ArrayList();
    List<DrawLineItem> listDrawLine = new ArrayList();
    List<DrawTextItem> listDrawText = new ArrayList();
    private byte[] totalData = new byte[131072];
    private int totalDataLen = 0;

    class DrawBarcode1DItem {
        int height;
        int lineWidth;
        int rotate;
        String text;
        String type;
        int x;
        int y;

        DrawBarcode1DItem() {
        }
    }

    class DrawBarcodeQRcodeItem {
        String errLevel;
        int rotate;
        int size;
        String text;
        int x;
        int y;

        DrawBarcodeQRcodeItem() {
        }
    }

    class DrawBitmapItem {
        Bitmap bmp;
        boolean rotate;
        int x;
        int y;

        DrawBitmapItem() {
        }
    }

    class DrawBoxItem {
        int width;
        int x0;
        int x1;
        int y0;
        int y1;

        DrawBoxItem() {
        }
    }

    class DrawLineItem {
        int width;
        int x0;
        int x1;
        int y0;
        int y1;

        DrawLineItem() {
        }
    }

    class DrawTextItem {
        boolean bold;
        int fontSize;
        int fontzoom;
        boolean reverse;
        int rotate;
        String text;
        int text_x;
        int text_y;
        boolean underline;

        DrawTextItem() {
        }
    }

    private class FontInfo {
        public String FontName;
        public Typeface FontTypeface;

        private FontInfo() {
        }
    }

    public void Create(int width, int height) {
        this._w = width;
        this._h = height;
        this.listDataLen = 0;
        this.totalDataLen = 0;
        this.listDrawText.clear();
        this.listDrawLine.clear();
        this.listDrawBox.clear();
        this.listDrawBarcode1D.clear();
        this.listDrawBitmap.clear();
        this.listDrawBarcodeQRcode.clear();
    }

    public void Clear() {
        this.listDataLen = 0;
    }

    public void add(byte[] d) {
        System.arraycopy(d, 0, this.listData, this.listDataLen, d.length);
        this.listDataLen += d.length;
    }

    public void feed() {
    }

    public int getDataLen() {
        return this.totalDataLen;
    }

    public byte[] GetData(boolean r) {
        String end;
        String begin = "! 0 200 200 " + this._h + " 1\r\nPAGE-WIDTH " + this._w + "\r\n";
        if (r) {
            end = "ZPROTATE\r\n";
        } else {
            end = "PRINT\r\n";
        }
        this.totalDataLen = (begin.length() + this.listDataLen) + end.length();
        System.arraycopy(begin.getBytes(), 0, this.totalData, 0, begin.length());
        int pos = 0 + begin.length();
        System.arraycopy(this.listData, 0, this.totalData, pos, this.listDataLen);
        System.arraycopy(end.getBytes(), 0, this.totalData, pos + this.listDataLen, end.length());
        this.listDataLen = 0;
        return this.totalData;
    }

    private boolean IsFontPath(String fontName) {
        if (fontName.substring(0, 1).equals("/")) {
            return fontName.contains(".ttf");
        }
        return false;
    }

    private boolean IsFontRes(String fontName) {
        return fontName.contains(".ttf");
    }

    public void DrawText(int text_x, int text_y, String text, int fontSize, int fontzoom, int rotate, boolean bold, boolean reverse, boolean underline) {
        DrawTextItem item = new DrawTextItem();
        item.text_x = text_x;
        item.text_y = text_y;
        item.text = text;
        item.fontSize = fontSize;
        item.fontzoom = fontzoom;
        item.rotate = rotate;
        item.bold = bold;
        item.reverse = reverse;
        item.underline = underline;
        this.listDrawText.add(item);
    }

    private void realDrawText(int pageWidth, int pageHeight, DrawTextItem item) {
        String fontString;
        String sizeString;
        int text_x = item.text_x;
        int text_y = item.text_y;
        String text = item.text;
        int fontSize = item.fontSize;
        int rotate = item.rotate;
        boolean bold = item.bold;
        boolean reverse = item.reverse;
        int scale = 0;
        if (item.underline) {
            add("UNDERLINE ON\r\n".getBytes());
        } else {
            add("UNDERLINE OFF\r\n".getBytes());
        }
        switch (fontSize) {
            case 16:
                fontString = "55";
                sizeString = "0";
                break;
            case 20:
                fontString = "20";
                sizeString = "0";
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
                fontString = "55";
                sizeString = "0";
                scale = 2;
                break;
            case 40:
                fontString = "20";
                sizeString = "0";
                scale = 2;
                break;
            case 48:
                fontString = "24";
                sizeString = "0";
                scale = 2;
                break;
            case 56:
                fontString = "28";
                sizeString = "0";
                scale = 2;
                break;
            case 64:
                fontString = "55";
                sizeString = "0";
                scale = 4;
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
                fontString = "24";
                sizeString = "0";
                scale = 4;
                break;
            default:
                fontString = "55";
                sizeString = "0";
                scale = 1;
                break;
        }
        if (bold) {
            add("SETBOLD 1\r\n".getBytes());
        } else {
            add("SETBOLD 0\r\n".getBytes());
        }
        String cmd = "T";
        switch (rotate) {
            case 0:
                cmd = "T";
                break;
            case 90:
                cmd = "T90";
                break;
            case 180 /*180*/:
                cmd = "T180";
                break;
            case 270:
                cmd = "T270";
                break;
        }
        add(String.format(Locale.ENGLISH, "SETMAG %d %d \r\n", new Object[]{Integer.valueOf(scale), Integer.valueOf(scale)}).getBytes());
        byte[] byteStr = null;
        try {
            byteStr = String.format(Locale.ENGLISH, "%s %s %s %d %d %s\r\n", new Object[]{cmd, fontString, sizeString, Integer.valueOf(text_x), Integer.valueOf(text_y), text}).getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
        }
        if (byteStr != null) {
            add(byteStr);
        }
        if (reverse) {
            try {
                byte[] bytetext = text.getBytes("gbk");
                int block_h = Integer.parseInt(sizeString);
                INVERSE(text_x, text_y, text_x + ((Integer.parseInt(sizeString) / 2) * bytetext.length), text_y, Integer.parseInt(sizeString));
            } catch (UnsupportedEncodingException e2) {
                return;
            }
        }
        add("SETMAG 1 1\r\n".getBytes());
    }

    public void makeDrawText(int pageWidth, int pageHeight) {
        for (int i = 0; i < this.listDrawText.size(); i++) {
            realDrawText(pageWidth, pageHeight, (DrawTextItem) this.listDrawText.get(i));
        }
    }

    public void DrawLine(int x0, int y0, int x1, int y1, int width) {
        DrawLineItem item = new DrawLineItem();
        item.x0 = x0;
        item.y0 = y0;
        item.x1 = x1;
        item.y1 = y1;
        item.width = width;
        this.listDrawLine.add(item);
    }

    private void realDrawLine(int pageWidth, int pageHeight, DrawLineItem item) {
        int x0 = item.x0;
        int y0 = item.y0;
        int x1 = item.x1;
        int y1 = item.y1;
        int width = item.width;
        add(String.format(Locale.ENGLISH, "LINE %d %d %d %d %d\r\n", new Object[]{Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width)}).getBytes());
    }

    public void makeDrawLine(int pageWidth, int pageHeight) {
        for (int i = 0; i < this.listDrawLine.size(); i++) {
            realDrawLine(pageWidth, pageHeight, (DrawLineItem) this.listDrawLine.get(i));
        }
    }

    public void Drawbox(int x0, int y0, int x1, int y1, int width) {
        DrawBoxItem item = new DrawBoxItem();
        item.x0 = x0;
        item.y0 = y0;
        item.x1 = x1;
        item.y1 = y1;
        item.width = width;
        this.listDrawBox.add(item);
    }

    public void INVERSE(int x0, int y0, int x1, int y1, int width) {
        add(String.format(Locale.ENGLISH, "INVERSE-LINE %d %d %d %d %d\r\n", new Object[]{Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width)}).getBytes());
    }

    public void DrawBitmap(Bitmap bmp, int x, int y, boolean rotate) {
        DrawBitmapItem item = new DrawBitmapItem();
        item.bmp = bmp;
        item.x = x;
        item.y = y;
        item.rotate = rotate;
        this.listDrawBitmap.add(item);
    }

    private void realDrawBitmap(int pageWidth, int pageHeight, DrawBitmapItem item) {
        Bitmap bmp = item.bmp;
        int x = item.x;
        int y = item.y;
        boolean rotate = item.rotate;
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int byteCountW = (w + 7) / 8;
        int[] bmpData = new int[(w * h)];
        byte[] outData = new byte[(byteCountW * h)];
        bmp.copyPixelsToBuffer(IntBuffer.wrap(bmpData));
        for (int yy = 0; yy < h; yy++) {
            for (int xx = 0; xx < w; xx++) {
                int c = bmpData[(yy * w) + xx];
                if (((((((c >> 16) & 255) * 30) + (((c >> 8) & 255) * 59)) + ((c & 255) * 11)) + 50) / 100 < 128) {
                    int i = (byteCountW * yy) + (xx / 8);
                    outData[i] = (byte) (outData[i] | (128 >> (xx % 8)));
                }
            }
        }
        String cmd = "EG";
        if (rotate) {
            cmd = "VEG";
        }
        String strCmdHeader = String.format(Locale.ENGLISH, "%s %d %d %d %d ", new Object[]{cmd, Integer.valueOf(byteCountW), Integer.valueOf(h), Integer.valueOf(x), Integer.valueOf(y)});
        String strData = "";
        for (byte ByteToString : outData) {
            strData = strData + ByteToString(ByteToString);
        }
        add((strCmdHeader + strData + "\r\n").getBytes());
    }

    public void makeDrawBitmap(int pageWidth, int pageHeight) {
        for (int i = 0; i < this.listDrawBitmap.size(); i++) {
            realDrawBitmap(pageWidth, pageHeight, (DrawBitmapItem) this.listDrawBitmap.get(i));
        }
    }

    public void DrawBarcode1D(String type, int x, int y, String text, int lineWidth, int height, int rotate) {
        DrawBarcode1DItem item = new DrawBarcode1DItem();
        item.type = type;
        item.x = x;
        item.y = y;
        item.text = text;
        item.lineWidth = lineWidth;
        item.height = height;
        item.rotate = rotate;
        this.listDrawBarcode1D.add(item);
    }

    private void realDraBarcode1D(int pageWidth, int pageHeight, DrawBarcode1DItem item) {
        String type = item.type;
        String text = item.text;
        int lineWidth = item.lineWidth;
        int height = item.height;
        int rotate = item.rotate;
        int x = item.x;
        int y = item.y;
        int width = 0;
        String cmd = "BARCODE";
        if (rotate == 90) {
            cmd = "VBARCODE";
        } else if (rotate == 180) {
            cmd = "B";
            x -= width;
            y -= height;
        } else if (rotate == 270) {
            cmd = "VB";
            x -= height;
            y += width;
        }
        add(String.format(Locale.ENGLISH, "%s %s %d 1 %d %d %d %s\r\n", new Object[]{cmd, type, Integer.valueOf(lineWidth), Integer.valueOf(height), Integer.valueOf(x), Integer.valueOf(y), text}).getBytes());
    }

    public void makeDrawBarcode1D(int pageWidth, int pageHeight) {
        for (int i = 0; i < this.listDrawBarcode1D.size(); i++) {
            realDraBarcode1D(pageWidth, pageHeight, (DrawBarcode1DItem) this.listDrawBarcode1D.get(i));
        }
    }

    public void DrawBarcodeQRcode(int x, int y, String text, int size, String errLevel, int rotate) {
        DrawBarcodeQRcodeItem item = new DrawBarcodeQRcodeItem();
        item.x = x;
        item.y = y;
        item.text = text;
        item.size = size;
        item.errLevel = errLevel;
        item.rotate = rotate;
        this.listDrawBarcodeQRcode.add(item);
    }

    private void realBarcodeQRcode(int pageWidth, int pageHeight, DrawBarcodeQRcodeItem item) {
        int x = item.x;
        int y = item.y;
        String text = item.text;
        int size = item.size;
        String errLevel = item.errLevel;
        int rotation = item.rotate;
        String B = "BARCODE";
        String VB = "VBARCODE";
        int width = 0;
        String cmd = B;
        if (rotation == 90) {
            cmd = VB;
        } else if (rotation == 180) {
            cmd = B;
            x -= width;
            y -= width;
        } else if (rotation == 270) {
            cmd = VB;
            x -= width;
            y += width;
        }
        try {
            add((cmd + " QR " + x + " " + y + " M 2 U " + size + "\n" + errLevel + "A," + text + "\nENDQR\r\n").getBytes("GB2312"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void makeDrawBarcodeQRcode(int pageWidth, int pageHeight) {
        for (int i = 0; i < this.listDrawBarcodeQRcode.size(); i++) {
            realBarcodeQRcode(pageWidth, pageHeight, (DrawBarcodeQRcodeItem) this.listDrawBarcodeQRcode.get(i));
        }
    }

    public void DrawBitmap1(Bitmap bmp, int x, int y, boolean rotate) {
        if (bmp != null) {
            int byteWidth = (bmp.getWidth() + 7) / 8;
            byte[] bmpBuf = new byte[(bmp.getHeight() * byteWidth)];
            short[] buf = new short[(bmp.getWidth() * bmp.getHeight())];
            bmp.copyPixelsToBuffer(ShortBuffer.wrap(buf));
            for (int xx = 0; xx < bmp.getWidth(); xx++) {
                for (int yy = 0; yy < bmp.getHeight(); yy++) {
                    if (buf[(bmp.getWidth() * yy) + xx] == (short) 0) {
                        int i = (byteWidth * yy) + (xx / 8);
                        bmpBuf[i] = (byte) (bmpBuf[i] | (128 >> (xx % 8)));
                    }
                }
            }
            String cmd = "CG";
            if (rotate) {
                cmd = "VCG";
            }
            add(String.format(Locale.ENGLISH, "%s %d %d %d %d \n", new Object[]{cmd, Integer.valueOf(byteWidth), Integer.valueOf(bmp.getHeight()), Integer.valueOf(x), Integer.valueOf(y)}).getBytes());
            add(bmpBuf);
            add("\r\n".getBytes());
        }
    }

    public void PageFree() {
        Clear();
    }

    private String IntToHex(byte data) {
        String r = "";
        switch (data) {
            case (byte) 0:
            case (byte) 1:
            case (byte) 2:
            case (byte) 3:
            case (byte) 4:
            case (byte) 5:
            case (byte) 6:
            case (byte) 7:
            case (byte) 8:
            case (byte) 9:
                r = Character.toString((char) (data + 48));
                break;
            case (byte) 10:
                return "A";
            case (byte) 11:
                return "B";
            case (byte) 12:
                return "C";
            case (byte) 13:
                return "D";
            case (byte) 14:
                return "E";
            case (byte) 15:
                return "F";
            default:
                Log.d("long", "ch is error ");
                break;
        }
        return r;
    }

    private String ByteToString(byte data) {
        String str = "";
        return IntToHex((byte) ((data >> 4) & 15)) + IntToHex((byte) (data & 15));
    }

    private int GetTop(Bitmap b) {
        short[] buf = new short[(b.getWidth() * b.getHeight())];
        b.copyPixelsToBuffer(ShortBuffer.wrap(buf));
        for (int yy = 0; yy < b.getHeight(); yy++) {
            for (int xx = 0; xx < b.getWidth(); xx++) {
                if (buf[(b.getWidth() * yy) + xx] == (short) 0) {
                    return yy;
                }
            }
        }
        return 0;
    }

    private int GetBottom(Bitmap b) {
        short[] buf = new short[(b.getWidth() * b.getHeight())];
        b.copyPixelsToBuffer(ShortBuffer.wrap(buf));
        for (int yy = b.getHeight() - 1; yy >= 0; yy--) {
            for (int xx = 0; xx < b.getWidth(); xx++) {
                if (buf[(b.getWidth() * yy) + xx] == (short) 0) {
                    return yy + 1;
                }
            }
        }
        return 0;
    }

    private int GetLeft(Bitmap b) {
        short[] buf = new short[(b.getWidth() * b.getHeight())];
        b.copyPixelsToBuffer(ShortBuffer.wrap(buf));
        for (int xx = 0; xx < b.getWidth(); xx++) {
            for (int yy = 0; yy < b.getHeight(); yy++) {
                if (buf[(b.getWidth() * yy) + xx] == (short) 0) {
                    return xx;
                }
            }
        }
        return 0;
    }

    private int GetRight(Bitmap b) {
        short[] buf = new short[(b.getWidth() * b.getHeight())];
        b.copyPixelsToBuffer(ShortBuffer.wrap(buf));
        for (int xx = b.getWidth() - 1; xx >= 0; xx--) {
            for (int yy = 0; yy < b.getHeight(); yy++) {
                if (buf[(b.getWidth() * yy) + xx] == (short) 0) {
                    return xx + 1;
                }
            }
        }
        return 0;
    }
}
