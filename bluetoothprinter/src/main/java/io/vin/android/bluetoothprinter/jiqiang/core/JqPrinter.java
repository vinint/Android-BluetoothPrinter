package io.vin.android.bluetoothprinter.jiqiang.core;

import android.graphics.Bitmap;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import java.io.UnsupportedEncodingException;

import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

public class JqPrinter {
    public enum PAGE_ROTATE {
        x0,
        x90,
        x180
    }

    public static final int STATE_BATTERYLOW_UNMASK = 4;
    public static final int STATE_COVEROPEN_UNMASK = 16;
    public static final int STATE_NOPAPER_UNMASK = 1;
    public static final int STATE_OVERHEAT_UNMASK = 2;
    public static final int STATE_PRINTING_UNMASK = 8;
    private byte[] _cmd = new byte[14];
    private JqBtCenterManagerProtocol mPort;

    public JqPrinter(JqBtCenterManagerProtocol port) {
        this.mPort = port;
    }

    public boolean writeNULL() {
        this._cmd[0] = (byte) 0;
        return this.mPort.write(this._cmd, 0, 1);
    }

    public boolean writeGBK(String text) {
        try {
            byte[] data = text.getBytes("GBK");
            if (this.mPort.write(data, 0, data.length)) {
                return writeNULL();
            }
            return false;
        } catch (UnsupportedEncodingException e) {
            Log.e("JQ", "Sting getBytes('GBK') failed");
            return false;
        }
    }

    public void setPage(int pageWidth, int pageHeight, int rotate) {
        PAGE_ROTATE rotate1 = PAGE_ROTATE.x0;
        if (rotate == 0) {
        } else if (rotate == 1) {
            rotate1 = PAGE_ROTATE.x180;
        } else if (rotate == 2) {
            rotate1 = PAGE_ROTATE.x90;
        }
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 91;
        this._cmd[2] = (byte) 1;
        this._cmd[3] = (byte) 0;
        this._cmd[4] = (byte) 0;
        this._cmd[5] = (byte) 0;
        this._cmd[6] = (byte) 0;
        this._cmd[7] = (byte) pageWidth;
        this._cmd[8] = (byte) (pageWidth >> 8);
        this._cmd[9] = (byte) pageHeight;
        this._cmd[10] = (byte) (pageHeight >> 8);
        this._cmd[11] = (byte) rotate1.ordinal();
        this.mPort.write(this._cmd, 0, 12);
    }

    public void drawRect(int color, int x1, int y1, int x2, int y2, int lineWidth) {
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 38;
        this._cmd[2] = (byte) 1;
        this._cmd[3] = (byte) x1;
        this._cmd[4] = (byte) (x1 >> 8);
        this._cmd[5] = (byte) y1;
        this._cmd[6] = (byte) (y1 >> 8);
        this._cmd[7] = (byte) x2;
        this._cmd[8] = (byte) (x2 >> 8);
        this._cmd[9] = (byte) y2;
        this._cmd[10] = (byte) (y2 >> 8);
        this._cmd[11] = (byte) lineWidth;
        this._cmd[12] = (byte) (lineWidth >> 8);
        this._cmd[13] = getByteColor(color);
        this.mPort.write(this._cmd, 0, 14);
    }

    public void drawRectFill(int color, int x1, int y1, int x2, int y2) {
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 42;
        this._cmd[2] = (byte) 0;
        this._cmd[3] = (byte) x1;
        this._cmd[4] = (byte) (x1 >> 8);
        this._cmd[5] = (byte) y1;
        this._cmd[6] = (byte) (y1 >> 8);
        this._cmd[7] = (byte) x2;
        this._cmd[8] = (byte) (x2 >> 8);
        this._cmd[9] = (byte) y2;
        this._cmd[10] = (byte) (y2 >> 8);
        this._cmd[11] = getByteColor(color);
        this.mPort.write(this._cmd, 0, 12);
    }

    private byte getByteColor(int color) {
        return color == 1 ? (byte) 0 : (byte) 1;
    }

    public void drawLine(int color, int x1, int y1, int x2, int y2, int lineWidth) {
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 92;
        this._cmd[2] = (byte) 1;
        this._cmd[3] = (byte) x1;
        this._cmd[4] = (byte) (x1 >> 8);
        this._cmd[5] = (byte) y1;
        this._cmd[6] = (byte) (y1 >> 8);
        this._cmd[7] = (byte) x2;
        this._cmd[8] = (byte) (x2 >> 8);
        this._cmd[9] = (byte) y2;
        this._cmd[10] = (byte) (y2 >> 8);
        this._cmd[11] = (byte) lineWidth;
        this._cmd[12] = (byte) (lineWidth >> 8);
        this._cmd[13] = getByteColor(color);
        this.mPort.write(this._cmd, 0, 14);
    }

    public void drawDashLine(int color, int x1, int y1, int x2, int y2, int lineWidth, int solid, int blank) {
        drawLine(color, x1, y1, x2, y2, lineWidth);
    }

    public void drawText(String text, int x, int y, int color, int fontSize, int style, int rotation) {
        if (x >= 0 && y >= 0) {
            int fontType = 0;
            if ((style & 1) != 0) {
                fontType = 0 | 1;
            }
            if ((style & 4) != 0) {
                fontType |= 2;
            }
            if (color == 1) {
                fontType |= 4;
            }
            switch (rotation) {
                case 90:
                    fontType |= 48;
                    y -= fontSize;
                    break;
                case 180:
                    fontType |= 32;
                    x -= fontSize;
                    y -= fontSize;
                    break;
                case 270:
                    fontType |= 16;
                    x -= fontSize;
                    break;
                default:
                    fontType |= 0;
                    break;
            }
            int ex = 0 & 15;
            int ey = 0 & 15;
            fontType = (fontType | 0) | 0;
            this._cmd[0] = (byte) 26;
            this._cmd[1] = (byte) 84;
            this._cmd[2] = (byte) 1;
            this._cmd[3] = (byte) x;
            this._cmd[4] = (byte) (x >> 8);
            this._cmd[5] = (byte) y;
            this._cmd[6] = (byte) (y >> 8);
            this._cmd[7] = (byte) fontSize;
            this._cmd[8] = (byte) (fontSize >> 8);
            this._cmd[9] = (byte) fontType;
            this._cmd[10] = (byte) (fontType >> 8);
            this.mPort.write(this._cmd, 0, 11);
            writeGBK(text);
        }
    }

    private void _1D_barcode_base(int x, int y, int type, int height, int unit_width, int rotate, String text) {
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 48;
        this._cmd[2] = (byte) 0;
        this._cmd[3] = (byte) x;
        this._cmd[4] = (byte) (x >> 8);
        this._cmd[5] = (byte) y;
        this._cmd[6] = (byte) (y >> 8);
        this._cmd[7] = (byte) type;
        this._cmd[8] = (byte) height;
        this._cmd[9] = (byte) (height >> 8);
        this._cmd[10] = (byte) unit_width;
        this._cmd[11] = (byte) rotate;
        this.mPort.write(this._cmd, 0, 12);
        writeGBK(text);
    }

    public void drawBarCode(String text, int x, int y, int height, int lineWidth, int type, int rotation) {
        if (!TextUtils.isEmpty(text)) {
            int i = x;
            int i2 = y;
            int i3 = height;
            _1D_barcode_base(i, i2, 73, i3, lineWidth + 1, getByteRotation(rotation), text);
        }
    }

    private byte getByteRotation(int rotation) {
        switch (rotation) {
            case 90:
                return (byte) 3;
            case 180:
                return (byte) 2;
            case 270:
                return (byte) 1;
            default:
                return (byte) 0;
        }
    }

    public void drawQRCode(String text, int x, int y, int unitWidth, int version, int level, int rotation) {
        if (!TextUtils.isEmpty(text) && unitWidth >= 1) {
            this._cmd[0] = (byte) 26;
            this._cmd[1] = (byte) 49;
            this._cmd[2] = (byte) 0;
            this._cmd[3] = (byte) version;
            this._cmd[4] = (byte) level;
            this._cmd[5] = (byte) x;
            this._cmd[6] = (byte) (x >> 8);
            this._cmd[7] = (byte) y;
            this._cmd[8] = (byte) (y >> 8);
            this._cmd[9] = (byte) unitWidth;
            this._cmd[10] = getByteRotation(rotation);
            this.mPort.write(this._cmd, 0, 11);
            writeGBK(text);
        }
    }

    private boolean imageBase(int x, int y, int width, int height, byte[] data, boolean Reverse, int Rotate, int EnlargeX, int EnlargeY) {
        if (width < 0 || height < 0) {
            return false;
        }
        int WidthByte = ((width - 1) / 8) + 1;
        if (WidthByte * height != data.length) {
            return false;
        }
        short ShowType = (short) 0;
        if (Reverse) {
            ShowType = (short) 1;
        }
        ShowType = (short) (((EnlargeY << 14) & 61440) | ((short) (((EnlargeX << 8) & 3840) | ((short) (((Rotate << 1) & 6) | ShowType)))));
        int HeightWrited = 0;
        int HeightLeft = height;
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 33;
        this._cmd[2] = (byte) 1;
        this._cmd[7] = (byte) width;
        this._cmd[8] = (byte) (width >> 8);
        this._cmd[11] = (byte) ShowType;
        this._cmd[12] = (byte) (ShowType >> 8);
        while (true) {
            this._cmd[3] = (byte) x;
            this._cmd[4] = (byte) (x >> 8);
            this._cmd[5] = (byte) y;
            this._cmd[6] = (byte) (y >> 8);
            if (HeightLeft > 10) {
                this._cmd[9] = (byte) 10;
                this._cmd[10] = (byte) 0;
                this.mPort.write(this._cmd, 0, 13);
                this.mPort.write(data, HeightWrited * WidthByte, 10 * WidthByte);
                switch (Rotate) {
                    case 0:
                        y += (EnlargeX + 1) * 10;
                        break;
                    case 1:
                        x -= (EnlargeY + 1) * 10;
                        break;
                    case 2:
                        y -= (EnlargeX + 1) * 10;
                        break;
                    case 3:
                        x += (EnlargeY + 1) * 10;
                        break;
                    default:
                        break;
                }
                HeightWrited += 10;
                HeightLeft -= 10;
            } else {
                this._cmd[9] = (byte) HeightLeft;
                this._cmd[10] = (byte) (HeightLeft >> 8);
                this.mPort.write(this._cmd, 0, 13);
                return this.mPort.write(data, HeightWrited * WidthByte, HeightLeft * WidthByte);
            }
        }
    }

    private boolean PixelIsBlack(int color, int gray_threshold) {
        return ((int) (((((double) ((float) ((16711680 & color) >> 16))) * 0.299d) + (((double) ((float) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & color) >> 8))) * 0.587d)) + (((double) ((float) (color & 255))) * 0.114d))) < gray_threshold;
    }

    private byte[] CovertImageHorizontal(Bitmap bitmap, int gray_threshold) {
        int i;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int BytesPerLine = ((width - 1) / 8) + 1;
        byte[] data = new byte[(BytesPerLine * height)];
        for (i = 0; i < data.length; i++) {
            data[i] = (byte) 0;
        }
        int index = 0;
        for (i = 0; i < height; i++) {
            for (int j = 0; j < BytesPerLine; j++) {
                for (int k = 0; k < 8; k++) {
                    int x = (j << 3) + k;
                    int y = i;
                    if (x < width && PixelIsBlack(bitmap.getPixel(x, y), gray_threshold)) {
                        data[index] = (byte) (data[index] | ((byte) (1 << k)));
                    }
                }
                index++;
            }
        }
        return data;
    }

    public void drawImage(Bitmap bitmap, int x, int y) {
        imageBase(x, y, bitmap.getWidth(), bitmap.getHeight(), CovertImageHorizontal(bitmap, 128), false, 0, 0, 0);
    }

    public void drawImage(Bitmap bitmap, int x, int y, int width, int height) {
        drawImage(Bitmap.createScaledBitmap(bitmap, width, height, true), x, y);
    }

    public void print(PrintCallback printCallback) {
        printAndFeed(printCallback);
    }

    public void printAndFeed(PrintCallback printCallback) {
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 79;
        this._cmd[2] = (byte) 0;
        this.mPort.write(this._cmd, 0, 3);
        this._cmd[0] = (byte) 26;
        this._cmd[1] = (byte) 12;
        this._cmd[2] = (byte) 0;
        this.mPort.write(this._cmd, 0, 3);
        this._cmd[0] = (byte) 16;
        this._cmd[1] = (byte) 4;
        this._cmd[2] = (byte) 5;
        this.mPort.write(this._cmd, 0, 3);
        byte[] state = new byte[2];
        if (!this.mPort.read(state, 0, 2, 10000)) {
            return;
        }
        if ((state[0] & 16) != 0) {
            printCallback.onPrintFail(1);
        } else if ((state[0] & 1) != 0) {
            printCallback.onPrintFail(2);
        } else {
            if ((state[0] & 4) != 0) {
            }
            if ((state[0] & 2) != 0) {
            }
            if ((state[0] & 8) != 0) {
                printCallback.onPrintSuccess();
            } else {
                printCallback.onPrintSuccess();
            }
        }
    }

    public void feedToNextLabel() {
    }

    public int getPrinterStatus(){
        int status = -1;
        if (mPort.mmBtSocket == null||!mPort.mmBtSocket.isConnected()){
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
            return status;
        }
        this._cmd[0] = (byte) 16;
        this._cmd[1] = (byte) 4;
        this._cmd[2] = (byte) 5;
        this.mPort.write(this._cmd, 0, 3);
        byte[] state = new byte[2];
        if (!this.mPort.read(state, 0, 2, 10000)) {
            return status;
        }
        if ((state[0] & 16) != 0) {
            //盖子打开
            status = 1;
        } else if ((state[0] & 1) != 0) {
            //没有纸张
            status = 2;
        } else {
            if ((state[0] & 4) != 0) {
            }
            if ((state[0] & 2) != 0) {
            }
            if ((state[0] & 8) != 0) {
                status = 0;
            } else {
                status = 0;
            }
        }
        return status;
    }

    public int getPrintWidth() {
        return 576;
    }
}
