package io.vin.android.bluetoothprinter.zicox;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.text.TextUtils;

import io.vin.android.bluetoothprinter.zicox.core.ZicoxCentralManager;
import io.vin.android.bluetoothprinter.zicox.core.ZicoxPrinter;
import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

/**
 * 芝柯打印机驱动（指令集CPCL）
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class ZicoxBluetoothPrinter implements IBluetoothPrinterProtocol{
    public ZicoxCentralManager manager;
    public ZicoxPrinter printer;

    public ZicoxBluetoothPrinter(String printerModelName){
        printer = new ZicoxPrinter();
        manager = new ZicoxCentralManager(printer);
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
        if (STYLE_LINE_FULL == lineStyle){
            printer.drawLine(COLOR_BLACK, startX, startY, endX, endY,lineWidth);
        }else{
            printer.drawDashLine(COLOR_BLACK,startX, startY, endX, endY, lineWidth, 0,0);
        }
    }

    @Override
    public void drawRect(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int lineWidth, int lineStyle) {
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
        printer.drawBarCode(text, startX, startY, height, lineWidth, type, rotation);
    }

    @Override
    public void drawQRCode(int startX, int startY, String text, int unitWidth, int level, int rotation) {
        if (TextUtils.isEmpty(text)){
            return;
        }
        printer.drawQRCode(text, startX, startY, unitWidth,0, level, rotation);
    }

    @Override
    public void drawImage(int startX, int startY, Bitmap bitmap, int width, int height) {
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
        int status = -1;
        if (printer.mySocket == null||!printer.mySocket.isConnected()){
            status = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
            return status;
        }
        printer.zp_printer_status_detect();
        int inx = printer.zp_printer_status_get(8000);
        if (inx == -1) {
            status = -1;
        }
        if (inx == 1) {
            status =1;
        }
        if (inx == 2) {
            status =2;
        }
        if (inx == 0) {
            status = 0;
        }
        return status;
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
