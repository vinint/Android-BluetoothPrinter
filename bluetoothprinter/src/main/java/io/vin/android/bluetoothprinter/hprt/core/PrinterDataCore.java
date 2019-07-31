package io.vin.android.bluetoothprinter.hprt.core;

import android.graphics.Bitmap;
import android.graphics.Color;


import java.util.ArrayList;
import java.util.List;

public class PrinterDataCore {
    public int BitmapWidth = 0;
    public byte CompressMode = (byte) 0;
    public byte HalftoneMode = (byte) 1;
    private int LBlank = 0;
    public int PrintDataHeight = 0;
    private int RBlank = 0;
    public byte ScaleMode = (byte) 0;

    public byte[] PrintDataFormat(Bitmap bmp) {
        try {
//            if (this.HalftoneMode > (byte) 0) {
//                return GetImageDataRasterMono(bmp);
//            }
            return CreatePrintBitmpaData(bmp);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private byte[] CompressPrintData(byte[] pData) {
        try {
            int BlankDatasSize;
            byte[] e = new byte[this.BitmapWidth];
            ArrayList AllDatas = new ArrayList();
            ArrayList LineDatas = new ArrayList();
            ArrayList BlankDatas = new ArrayList();
            for (BlankDatasSize = 0; BlankDatasSize < this.PrintDataHeight; BlankDatasSize++) {
                int BlankDatasSize1;
                boolean bStream = true;
                int var14 = 0;
                int var15 = 0;
                int iRowColIdx = BlankDatasSize * this.BitmapWidth;
                e = new byte[this.BitmapWidth];
                for (BlankDatasSize1 = 0; BlankDatasSize1 < this.BitmapWidth; BlankDatasSize1++) {
                    byte var17 = pData[iRowColIdx + BlankDatasSize1];
                    if (var17 != (byte) 0) {
                        if (BlankDatasSize1 == 0) {
                            var14 = 0;
                        } else if (var14 > var15) {
                            var14 = var15;
                        }
                        var15 = BlankDatasSize1;
                        bStream = false;
                    }
                    e[BlankDatasSize1] = var17;
                }
                if (bStream) {
                    BlankDatas.add(e);
                } else {
                    if (this.LBlank == 0) {
                        this.LBlank = var14;
                    } else {
                        if (this.LBlank < var14) {
                            var14 = this.LBlank;
                        }
                        this.LBlank = var14;
                    }
                    if (this.RBlank >= var15) {
                        var15 = this.RBlank;
                    }
                    this.RBlank = var15;
                    BlankDatasSize1 = BlankDatas.size();
                    if (BlankDatasSize1 > 0) {
                        if (BlankDatasSize1 > 24) {
                            if (LineDatas.size() > 0) {
                                AllDatas.add(TrimBitmapBlank(LineDatas));
                            }
                            AllDatas.add(CreateFeedLineCMD(BlankDatas));
                            LineDatas = new ArrayList();
                        } else {
                            LineDatas.addAll(BlankDatas);
                        }
                        BlankDatas = new ArrayList();
                    }
                    LineDatas.add(e);
                }
            }
            BlankDatasSize = BlankDatas.size();
            if (BlankDatasSize <= 0) {
                AllDatas.add(TrimBitmapBlank(LineDatas));
            } else if (BlankDatasSize > 24) {
                if (LineDatas.size() > 0) {
                    AllDatas.add(TrimBitmapBlank(LineDatas));
                }
                AllDatas.add(CreateFeedLineCMD(BlankDatas));
            } else {
                LineDatas.addAll(BlankDatas);
                AllDatas.add(TrimBitmapBlank(LineDatas));
            }
            return sysCopy(AllDatas);
        } catch (Exception var13) {
            var13.printStackTrace();
            return null;
        }
    }

    private byte[] CreateFeedLineCMD(List<byte[]> BlankDatas) {
        try {
            ArrayList e = new ArrayList();
            int iLineCnt = BlankDatas.size();
            for (int iLine = 0; iLine < iLineCnt; iLine += 240) {
                byte[] OneFeedCMD = new byte[]{(byte) 27, (byte) 74, (byte) 0};
                if (iLineCnt - iLine > 240) {
                    OneFeedCMD[2] = (byte) -16;
                } else {
                    OneFeedCMD[2] = (byte) (iLineCnt - iLine);
                }
                e.add(OneFeedCMD);
            }
            return sysCopy(e);
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    private byte[] TrimBitmapBlank(List<byte[]> LineDatas) {
        try {
            int e = (this.RBlank - this.LBlank) + 1;
            int iLine = 0;
            int iLineCnt = LineDatas.size();
            int iPKIndex = 0;
            byte[] RtnData = new byte[((e * iLineCnt) + ((iLineCnt % 2300 > 0 ? (iLineCnt / 2300) + 1 : iLineCnt / 2300) * 8))];
            while (iLine < iLineCnt) {
                int iCurPackageLine = iLine + 2300 < iLineCnt ? 2300 : iLineCnt - iLine;
                int iCurPackageBegin = iPKIndex * 2308;
                RtnData[iCurPackageBegin] = (byte) 29;
                RtnData[iCurPackageBegin + 1] = (byte) 118;
                RtnData[iCurPackageBegin + 2] = (byte) 48;
                RtnData[iCurPackageBegin + 3] = this.ScaleMode;
                RtnData[iCurPackageBegin + 4] = (byte) (e % 256);
                RtnData[iCurPackageBegin + 5] = (byte) (e / 256);
                RtnData[iCurPackageBegin + 6] = (byte) (iCurPackageLine % 256);
                RtnData[iCurPackageBegin + 7] = (byte) (iCurPackageLine / 256);
                for (int l = iLine; l < iLineCnt; l++) {
                    System.arraycopy(LineDatas.get(l), this.LBlank, RtnData, ((iPKIndex * 2308) + (l * e)) + 8, e);
                }
                iLine += 2300;
                iPKIndex++;
            }
            this.LBlank = 0;
            this.RBlank = 0;
            return RtnData;
        } catch (Exception var12) {
            var12.printStackTrace();
            return null;
        }
    }

    private byte[] AddPrintCode(byte[] bDatas) {
        try {
            byte[] bArr = new byte[(bDatas.length + 8)];
            bArr[0] = (byte) 29;
            bArr[1] = (byte) 118;
            bArr[2] = (byte) 48;
            bArr[3] = (byte) 0;
            bArr[4] = (byte) (this.BitmapWidth % 256);
            bArr[5] = (byte) (this.BitmapWidth / 256);
            bArr[6] = (byte) ((this.PrintDataHeight % 256) * 4);
            bArr[7] = (byte) ((this.PrintDataHeight / 256) * 4);
            for (int i = 0; i < bDatas.length; i++) {
                bArr[i + 8] = bDatas[i];
            }
            return bArr;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private byte[] CreatePrintBitmpaData(Bitmap bmp) {
        int nDataIndex = 0;
        try {
            int nRealWidth;
            int nWidth = bmp.getWidth();
            int nHeight = bmp.getHeight();
            this.PrintDataHeight = nHeight;
            if (nWidth % 8 == 0) {
                nRealWidth = nWidth;
            } else {
                nRealWidth = ((nWidth / 8) + 1) * 8;
            }
            this.BitmapWidth = nRealWidth / 8;
            int nSize = nHeight * this.BitmapWidth;
            byte[] bArr = new byte[nSize];
            for (int e = 0; e < nSize; e++) {
                bArr[e] = (byte) 0;
            }
            for (int startLine = 0; startLine < nHeight; startLine++) {
                int[] var26 = new int[nWidth];
                bmp.getPixels(var26, 0, nWidth, 0, startLine, nWidth, 1);
                int var25 = 0;
                for (int w = 0; w < nWidth; w++) {
                    var25++;
                    int nPixColor = var26[w];
                    if (var25 > 8) {
                        var25 = 1;
                        nDataIndex++;
                    }
                    if (nPixColor != -1) {
                        int var20 = (byte) 1 << (8 - var25);
                        if (((Color.red(nPixColor) + Color.green(nPixColor)) + Color.blue(nPixColor)) / 3 < 128) {
                            bArr[nDataIndex] = (byte) (bArr[nDataIndex] | var20);
                        }
                    }
                }
                nDataIndex = this.BitmapWidth * (startLine + 1);
            }
            return bArr;
        } catch (Exception var18) {
            var18.printStackTrace();
            return null;
        }
    }

    public byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] length : srcArrays) {
            len += length.length;
        }
        byte[] destArray = new byte[len];
        int destLen1 = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen1, srcArray.length);
            destLen1 += srcArray.length;
        }
        return destArray;
    }
}
