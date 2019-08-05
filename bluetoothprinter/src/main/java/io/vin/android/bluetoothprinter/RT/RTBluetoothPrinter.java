package io.vin.android.bluetoothprinter.RT;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.RT.RTPrinterLib.RTCpclCmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import io.vin.android.bluetoothprinterprotocol.ConnectCallback;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

public class RTBluetoothPrinter implements IBluetoothPrinterProtocol {
    private final UUID EDR_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//固定，用于串行口服务
    private final String CHARSETNAME = "gb2312";
    private final String TAG="RTBluetoothPrinter";
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket mBtSocket;
    public InputStream mInStream;
    public OutputStream mOutStream;

    private String mPrinterName;
    private String mPrinterAddr;

    private  boolean isDebug=true;
    private  StringBuilder sblogbuff;
    private RTCpclCmd rtCpclcmd;
    private ConnectCallback mconnectCallback;


    public RTBluetoothPrinter(String printerModelName) {
        this.mPrinterName = printerModelName;
        sblogbuff = new StringBuilder();
        rtCpclcmd = new RTCpclCmd();
    }



    @Override
    public void connect(String blueaddr, ConnectCallback connectCallback) {
        this.mPrinterAddr = blueaddr;
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(blueaddr);
        try {
            BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(EDR_UUID);
            bluetoothSocket.connect();
            if (bluetoothSocket.isConnected()) {
                mBtSocket = bluetoothSocket;
                mOutStream = bluetoothSocket.getOutputStream();
                mInStream = bluetoothSocket.getInputStream();
                this.mconnectCallback = connectCallback;
                connectCallback.onConnectSuccess();
            } else {
                connectCallback.onConnectFail("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            connectCallback.onConnectFail(ex.getMessage());
        }
    }

    @Override
    public void disconnect() {
       try {
          mBtSocket.close();
          if (mInStream != null)
              mInStream.close();
          if (mOutStream != null)
             mOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBtSocket = null;
        mInStream = null;
        mOutStream = null;
       if  (this.mconnectCallback!=null)
           this.mconnectCallback.onConnectFail("连接已断开");
    }

    @Override
    public void initWithSocket(BluetoothSocket bluetoothSocket) {
        try {
            mPrinterName = bluetoothSocket.getRemoteDevice().getName();
            mPrinterAddr = bluetoothSocket.getRemoteDevice().getAddress();
            mBtSocket = bluetoothSocket;
            mOutStream = bluetoothSocket.getOutputStream();
            mInStream = bluetoothSocket.getInputStream();
        } catch (IOException ex) {

        }
    }

    @Override
    public void resetSocket() {
        mBtSocket = null;
        mInStream = null;
        mOutStream = null;
    }

    @Override
    public void setPage(int width, int height, int orientation) {
        byte[] data = rtCpclcmd.getHeaderCmd(width,height,orientation);
        writeData(data);
    }

    @Override
    public void drawLine(int startX, int startY, int endX, int endY, int lineWidth, int lineStyle) {
        byte[] data =  rtCpclcmd.getDrawLine(startX, startY, endX, endY,lineWidth, lineStyle);
        writeData(data);

    }

    @Override
    public void drawRect(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int lineWidth, int lineStyle) {
        byte[] data =  rtCpclcmd.getDrawRect(leftTopX, leftTopY, rightBottomX, rightBottomY, lineWidth);
        writeData(data);
    }

    @Override
    public void drawText(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation) {
        if (TextUtils.isEmpty(text)){
            return;
        }

        if (width == 0 || height == 0) {
            //不换行
            innerDrawText(startX,startY, width, height, text, fontSize, textStyle, color, rotation);
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
                    innerDrawText(startX,startYTmp,0,0,textTmp,fontSize,textStyle,color,rotation);
                    widthTmp =0;
                    startYTmp += fontSize + 2;
                    textTmp = "";
                }else {
                    textTmp += String.valueOf(array[i]);
                }
            }

            if (!textTmp.isEmpty()){
                innerDrawText(startX,startYTmp,0,0,textTmp,fontSize,textStyle,color,rotation);
            }
        }
    }

    private void innerDrawText(int startX, int startY, int width, int height, String text, int fontSize, int textStyle, int color, int rotation) {
        byte[] data =  rtCpclcmd.getDrawText(startX,startY, width, height, text, fontSize, textStyle, color, rotation);
        writeData(data);
    }
    @Override
    public void drawBarCode(int startX, int startY, int height, int lineWidth, String text, int type, int rotation) {
        if (TextUtils.isEmpty(text)){
            return;
        }
        byte[] data = rtCpclcmd.getDrawBarCode(startX, startY, height, lineWidth, text, type, rotation);
        writeData(data);
    }

    @Override
    public void drawQRCode(int startX, int startY, String text, int unitWidth, int level, int rotation) {
        if (TextUtils.isEmpty(text)){
            return;
        }
        byte[] data = rtCpclcmd.getDrawQRCode(startX, startY, text, unitWidth, level, rotation);
        writeData(data);
    }

    @Override
    public void drawImage(int startX, int startY, Bitmap bitmap, int width, int height) {
        byte[] data = rtCpclcmd.getDrawImage(startX, startY, bitmap, width, height);
        writeData(data);
    }

    @Override
    public void feedToNextLabel() {


    }

    @Override
    public int getPrinterWidth() {
        return 0;
    }

    @Override
    public int getPrinterStatus() {
        return 0;
    }

    @Override
    public void print(PrintCallback printCallback) {
        byte[] data = new byte[0];
        data = rtCpclcmd.getEndCmd();
        writeData(data);
        if (isDebug){
            Log.i(TAG, "buff=\r\n"+sblogbuff.toString());
        }
    }

    @Override
    public void printAndFeed(PrintCallback printCallback) {
        print(printCallback);
    }

    @Override
    public boolean isFullySupport() {
        return true;
    }
    public void writeData(byte[] data) {
        if (isDebug) {
            try {
                sblogbuff.append(new String(data,CHARSETNAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (mOutStream != null) {
            //            byte[] b = {'\0', '\0', '\0'};
            try {
//                mOutputStream.write(b);//唤醒

                //分包发送
                int cmdSumLen = data.length;
                int pkgNum = 256;
                int loop = cmdSumLen / pkgNum;//完整包1024长度
                int least = cmdSumLen % pkgNum;//剩余1包，不到1024长度

                //完整包1024长度
                byte[] btSend;
                for (int i = 0; i < loop; i++) {
                    btSend = new byte[pkgNum];
                    for (int j = 0; j < pkgNum; j++) {
                        btSend[j] = data[i * pkgNum + j];
                    }
                    mOutStream.write(btSend);
                    this.mOutStream.flush();
                    Thread.sleep(10);
                }

                //剩余包，不到1024长度
                byte[] btLeast = new byte[least];
                for (int i = 0; i < btLeast.length; i++) {
                    btLeast[i] = data[loop * pkgNum + i];
                }
                mOutStream.write(btLeast);


            } catch (IOException e) {
                e.printStackTrace();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
