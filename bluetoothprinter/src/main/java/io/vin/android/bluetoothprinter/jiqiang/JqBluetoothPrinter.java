package io.vin.android.bluetoothprinter.jiqiang;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.text.TextUtils;

import io.vin.android.bluetoothprinter.jiqiang.core.JqBtCenterManagerProtocol;
import io.vin.android.bluetoothprinter.jiqiang.core.JqPrinter;
import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

/**
 * 济强打印机驱动（ESC指令集）
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class JqBluetoothPrinter implements IBluetoothPrinterProtocol {
    int status =-1;
    JqBtCenterManagerProtocol manager;
    JqPrinter printer;
    String mPrinterModelName;
    int unitY = 0;

    public JqBluetoothPrinter(String printerModelName){
        mPrinterModelName = printerModelName;
        manager = new JqBtCenterManagerProtocol();
        printer = new JqPrinter(manager);
    }
    
    @Override
    public void connect(String bluetoothAddress, ConnectCallback connectCallback) {
        manager.connectPrinter(bluetoothAddress,connectCallback);
    }

    @Override
    public void disconnect() {
        manager.disconnect();
    }

    @Override
    public void initWithSocket(BluetoothSocket bluetoothSocket) {
        manager.initPrinterWithSocket(bluetoothSocket);
    }

    @Override
    public void resetSocket() {
        manager.resetPrinterSocket();
    }

    @Override
    public void setPage(int width, int height, int orientation) {
        printer.setPage(width,height,orientation);
    }

    @Override
    public void drawLine(int startX, int startY, int endX, int endY, int lineWidth, int lineStyle) {
        startY = (startY +=unitY)>0?startY:0;
        endY = (endY+= unitY)>0?endY:0;
        if (STYLE_LINE_FULL == lineStyle){
            printer.drawLine(COLOR_BLACK, startX, startY, endX, endY,lineWidth);
        }else{
            //暂不支持画虚线
            printer.drawDashLine(COLOR_BLACK,startX, startY, endX, endY, lineWidth, 0,0);
        }
    }

    @Override
    public void drawRect(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int lineWidth, int lineStyle) {
       leftTopY = (leftTopY+=unitY)>0?leftTopY:0;
       rightBottomY = (rightBottomY+=unitY)>0?rightBottomY:0;
        if (IBluetoothPrinterProtocol.STYLE_LINE_FULL == lineStyle ){
            printer.drawRectFill(COLOR_BLACK, leftTopX,  leftTopY,  rightBottomX,  rightBottomY);
        }else {
            printer.drawRect(COLOR_BLACK, leftTopX,  leftTopY,  rightBottomX,  rightBottomY,lineWidth);
        }
    }

    @Override
    public void drawText(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation) {
        if (TextUtils.isEmpty(text)){
            return;
        }
        startY = (startY+=unitY)>0?startY:0;
        if (width == 0 || height == 0) {
            //不换行
            printer.drawText(text, startX, startY, color, fontSize, textStyle, rotation);
        } else {
            //换行
            int widthTmp =0;
            int startYTmp =startY;
            String textTmp ="";
            char[] array = text.toCharArray();
            for (int i = 0; i < array.length; ++i) {
                if ((char) ((byte) array[i]) != array[i]) {
                    //中文
                    widthTmp += fontSize;
                } else {
                    //英文
                    widthTmp += fontSize/2;
                }

                if (widthTmp >= width) {
                    textTmp += String.valueOf(array[i]);
                    printer.drawText(textTmp, startX, startYTmp, color, fontSize, textStyle, rotation);
                    widthTmp =0;
                    startYTmp += fontSize + 2;
                    textTmp = "";
                }else {
                    textTmp += String.valueOf(array[i]);
                }
            }

            if (!textTmp.isEmpty()){
                printer.drawText(textTmp, startX, startYTmp, color, fontSize, textStyle, rotation);
            }
        }
    }

    @Override
    public void drawBarCode(int startX, int startY, int height, int lineWidth, String text, int type, int rotation) {
        if (TextUtils.isEmpty(text)){
            return;
        }
        startY = (startY+=unitY)>0?startY:0;
        printer.drawBarCode(text, startX, startY, height, lineWidth, type, rotation);
    }

    @Override
    public void drawQRCode(int startX, int startY, String text, int unitWidth, int level, int rotation) {
        if (TextUtils.isEmpty(text)){
            return;
        }
        startY = (startY+=unitY)>0?startY:0;
        printer.drawQRCode(text, startX, startY, unitWidth,0, level, rotation);
    }

    @Override
    public void drawImage(int startX, int startY, Bitmap bitmap, int width, int height) {
        startY = (startY+=unitY)>0?startY:0;
        if (width == 0||height ==0){
            printer.drawImage(bitmap,startX,startY);
        }else {
            printer.drawImage(bitmap,startX,startY,width,height);
        }
    }

    @Override
    public void feedToNextLabel() {
        printer.feedToNextLabel();
    }

    @Override
    public int getPrinterWidth() {
        return printer.getPrintWidth();
    }

    @Override
    public int getPrinterStatus() {
        return printer.getPrinterStatus();
    }

    @Override
    public void print(PrintCallback printCallback) {
        printer.print(printCallback);
    }

    @Override
    public void printAndFeed(PrintCallback printCallback) {
        printer.printAndFeed(printCallback);
    }

    @Override
    public boolean isFullySupport() {
        return true;
    }
}
