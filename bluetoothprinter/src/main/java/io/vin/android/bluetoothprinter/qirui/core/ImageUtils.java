package io.vin.android.bluetoothprinter.qirui.core;

import android.graphics.Bitmap;
import android.support.v4.view.MotionEventCompat;

public class ImageUtils {
    public static byte[] getBinaryzationBytes(Bitmap bitmap) {
        return getBinaryzationBytes(bitmap, 128, false);
    }

    public static byte[] getBinaryzationBytes(Bitmap bitmap, boolean reverse) {
        return getBinaryzationBytes(bitmap, 128, reverse);
    }

    public static byte[] getBinaryzationBytes(Bitmap bitmap, int threshold, boolean reverse) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] data = new byte[(width * height)];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = bitmap.getPixel(j, i);
                byte y = (byte) (reverse ? 1 : 0);
                byte n = (byte) (reverse ? 0 : 1);
                int i2 = (i * width) + j;
                if (((int) (((0.299d * ((double) ((16711680 & color) >> 16))) + (0.587d * ((double) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & color) >> 8)))) + (0.114d * ((double) (color & 255))))) >= threshold) {
                    y = n;
                }
                data[i2] = y;
            }
        }
        return data;
    }

    public static byte[] getCompressedBinaryzationBytes(Bitmap bitmap) {
        return getCompressedBinaryzationBytes(bitmap, 128, false);
    }

    public static byte[] getCompressedBinaryzationBytes(Bitmap bitmap, boolean reverse) {
        return getCompressedBinaryzationBytes(bitmap, 128, reverse);
    }

    public static byte[] getCompressedBinaryzationBytes(Bitmap bitmap, int threshold, boolean reverse) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int lineBytes = ((width - 1) / 8) + 1;
        byte[] data = new byte[(lineBytes * height)];
        byte[] imageBytes = getBinaryzationBytes(bitmap, threshold, reverse);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < lineBytes; j++) {
                byte unit = (byte) 0;
                for (int k = 0; k < 8; k++) {
                    if ((j << 3) + k < width) {
                        unit = (byte) (((imageBytes[((i * width) + (j << 3)) + k] & 1) << (7 - k)) | unit);
                    }
                }
                data[(i * lineBytes) + j] = unit;
            }
        }
        return data;
    }
}
