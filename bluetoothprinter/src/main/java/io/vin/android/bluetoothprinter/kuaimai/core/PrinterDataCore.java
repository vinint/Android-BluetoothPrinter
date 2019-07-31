package io.vin.android.bluetoothprinter.kuaimai.core;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;

public class PrinterDataCore {
    private int B = 0;
    public int BitmapWidth = 0;
    private int C = 0;
    public byte CompressMode = (byte) 0;
    public byte HalftoneMode = (byte) 1;
    public int PrintDataHeight = 0;
    public byte ScaleMode = (byte) 0;

    public byte[] PrintDataFormat(Bitmap bitmap) {
        try {
            if (this.HalftoneMode > (byte) 0) {
                return b(bitmap);
            }
            return c(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] b(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = (width + 7) >> 3;
        try {
            int i2;
            int i3;
            int i4;
            this.PrintDataHeight = height;
            this.BitmapWidth = i;
            int i5 = width * height;
            int i6 = i * height;
            int[] iArr = new int[i5];
            bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
            int i7 = 0;
            int i8 = 0;
            while (i7 < i5) {
                i2 = iArr[i7];
                i3 = i8 + 1;
                iArr[i8] = ((byte) ((int) (((((double) Color.red(i2)) * 0.29891d) + (((double) Color.green(i2)) * 0.58661d)) + (((double) Color.blue(i2)) * 0.11448d)))) & 255;
                if (i7 == 112000) {
                    System.out.println("");
                }
                if (i7 == 223999) {
                    System.out.println("");
                }
                if (i7 == 168000) {
                    System.out.println("");
                }
                i7++;
                i8 = i3;
            }
            for (i2 = 0; i2 < height; i2++) {
                i7 = 0;
                i3 = i2 * width;
                while (i7 < width) {
                    float f;
                    if (iArr[i3] > 128) {
                        f = (float) (iArr[i3] - 255);
                        iArr[i3] = 255;
                    } else {
                        f = (float) (iArr[i3] + 0);
                        iArr[i3] = 0;
                    }
                    if (i7 < width - 1) {
                        i4 = i3 + 1;
                        iArr[i4] = iArr[i4] + ((int) (0.4375d * ((double) f)));
                    }
                    if (i2 < height - 1) {
                        if (i7 > 1) {
                            i4 = (i3 + width) - 1;
                            iArr[i4] = iArr[i4] + ((int) (0.1875d * ((double) f)));
                        }
                        i4 = i3 + width;
                        iArr[i4] = iArr[i4] + ((int) (0.3125d * ((double) f)));
                        if (i7 < width - 1) {
                            i4 = (i3 + width) + 1;
                            iArr[i4] = ((int) (0.0625d * ((double) f))) + iArr[i4];
                        }
                    }
                    i7++;
                    i3++;
                }
                if (i2 == 140) {
                    System.out.println("");
                }
                if (i2 == 279) {
                    System.out.println("");
                }
                if (i2 == 210) {
                    System.out.println("");
                }
            }
            byte[] bArr = new byte[i6];
            for (i5 = 0; i5 < height; i5++) {
                int i9 = 0;
                i7 = i5 * i;
                i3 = 0;
                i2 = i5 * width;
                while (i9 < width) {
                    i6 = i9 % 8;
                    i4 = i2 + 1;
                    if (iArr[i2] <= 128) {
                        i3 |= 128 >> i6;
                    }
                    i9++;
                    if (i6 == 7 || i9 == width) {
                        i2 = i7 + 1;
                        bArr[i7] = (byte) i3;
                        i3 = 0;
                        if (i2 == 14000) {
                            System.out.println("");
                        }
                        if (i2 == 28000) {
                            System.out.println("");
                        }
                        if (i2 == 21000) {
                            System.out.println("");
                        }
                        i7 = i2;
                        i2 = i4;
                    } else {
                        i2 = i4;
                    }
                }
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] c(Bitmap bitmap) {
        try {
            int i;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            this.PrintDataHeight = height;
            this.BitmapWidth = (width % 8 == 0 ? width : ((width / 8) + 1) * 8) / 8;
            int i2 = height * this.BitmapWidth;
            byte[] bArr = new byte[i2];
            for (i = 0; i < i2; i++) {
                bArr[i] = (byte) 0;
            }
            int i3 = 0;
            int i4 = 0;
            while (i4 < height) {
                int[] iArr = new int[width];
                bitmap.getPixels(iArr, 0, width, 0, i4, width, 1);
                i = 0;
                int i5 = i3;
                for (int i6 = 0; i6 < width; i6++) {
                    i++;
                    int i7 = iArr[i6];
                    if (i > 8) {
                        i5++;
                        i = 1;
                    }
                    if (i7 != -1) {
                        int i8 = 1 << (8 - i);
                        if ((Color.blue(i7) + (Color.red(i7) + Color.green(i7))) / 3 < 128) {
                            bArr[i5] = (byte) (bArr[i5] | i8);
                        }
                    }
                }
                i4++;
                i3 = this.BitmapWidth * (i4 + 1);
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] sysCopy(List<byte[]> list) {
        int i = 0;
        for (byte[] length : list) {
            i = length.length + i;
        }
        byte[] obj = new byte[i];
        i = 0;
        for (byte[] length2 : list) {
            System.arraycopy(length2, 0, obj, i, length2.length);
            i = length2.length + i;
        }
        return obj;
    }
}
