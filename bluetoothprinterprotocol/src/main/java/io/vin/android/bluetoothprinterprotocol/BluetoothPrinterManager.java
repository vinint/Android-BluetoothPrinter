package io.vin.android.bluetoothprinterprotocol;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**
 * Bluetooth Printer Manager
 * 打印机管理
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class BluetoothPrinterManager {
    private static BluetoothPrinterManager printerManager;
    //当前打印机名称和mac地址
    private String printerName = "";
    private String printerAddress = "";
    //上次请求查询时间、打印机状态
    private int lastPrinterStatus = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
    private long lastTimeMillis = System.currentTimeMillis();
    //打印机协议、打印机协议工厂
    private HashMap<String, IBluetoothPrinterFactory> printerFactories = new HashMap();
    private IBluetoothPrinterFactory mBluetoothPrinterFactory;
    private IBluetoothPrinterProtocol mBluetoothPrinterProtocol;

    private boolean connectState = false;
    private HashMap<String, Integer> name2Type = new HashMap();

    private BluetoothPrinterManager() {
    }

    public static synchronized BluetoothPrinterManager getInstance() {
        BluetoothPrinterManager bluetoothPrinterManager;
        synchronized (BluetoothPrinterManager.class) {
            if (printerManager == null) {
                printerManager = new BluetoothPrinterManager();
            }
            bluetoothPrinterManager = printerManager;
        }
        return bluetoothPrinterManager;
    }

    /**
     * Method     注册打印机驱动
     * Parameters
     * printerModelName 打印机型号名
     * factory     打印机实例化工厂
     * type        类型(用于决定该打印机对应的打印模板)
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    public synchronized void registerPrinter(String printerModelName, IBluetoothPrinterFactory factory, int type) {
        if (printerModelName == null || printerModelName.length() < 1 || factory == null) {
            throw new IllegalArgumentException("printerModelName or AbstractPrinterFactory can't be null");
        }
        this.printerFactories.put(printerModelName, factory);
        this.name2Type.put(printerModelName, Integer.valueOf(type));
    }

    /**
     * Method     撤销注册打印机驱动
     * Parameters
     * printerModelName 打印机型号名
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    public synchronized void unregisterPrinter(String printerModelName) {
        if (printerModelName == null || printerModelName.length() < 1) {
            return;
        }
        this.printerFactories.remove(printerModelName);
    }

    /**
     *Method     检查打印机是否被支持
     *Parameters [printerModelName]
     *Return     boolean
     *Author     Vin
     *Mail       vinintg@gmail.com
     */
    public synchronized boolean checkPrinterSupported(String printerName){
        for (String key : this.printerFactories.keySet()) {
            if (printerName.startsWith(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method     connect
     * Parameters [printerModelName, printerAddress]
     * Return     boolean
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    public synchronized boolean connect(final String printerName, final String printerAddress) {
        mBluetoothPrinterFactory = null;
        mBluetoothPrinterProtocol = null;
        for (String key : this.printerFactories.keySet()) {
            if (printerName.startsWith(key)) {
                mBluetoothPrinterFactory = printerFactories.get(key);
                break;
            }
        }
        if (mBluetoothPrinterFactory == null){return false;}
        mBluetoothPrinterProtocol = mBluetoothPrinterFactory.create();
        mBluetoothPrinterProtocol.connect(printerAddress, new ConnectCallback() {
            @Override
            public void onConnectFail(String str) {
                connectState = false;
            }

            @Override
            public void onConnectSuccess() {
                BluetoothPrinterManager.this.printerName = printerName;
                BluetoothPrinterManager.this.printerAddress = printerAddress;
                connectState = true;
            }
        });

        //更新打印机状态、时间
        if (connectState){
            lastPrinterStatus = IBluetoothPrinterProtocol.STATUS_OK;
        }else {
            lastPrinterStatus = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
        }
        lastTimeMillis = System.currentTimeMillis();

        return connectState;
    }

    /**
     *Method     使用已经连接的socket初始化
     *Parameters [printerName, printerAddress, socket]
     *Return     void
     *Author     Vin
     *Mail       vinintg@gmail.com
     */
    public synchronized void initWithSocket(String printerName, String printerAddress, BluetoothSocket socket){
        mBluetoothPrinterFactory = null;
        mBluetoothPrinterProtocol = null;
        for (String key : this.printerFactories.keySet()) {
            if (printerName.startsWith(key)) {
                mBluetoothPrinterFactory = printerFactories.get(key);
                break;
            }
        }
        if (mBluetoothPrinterFactory == null){return;}
        mBluetoothPrinterProtocol = mBluetoothPrinterFactory.create();
        this.connectState = true;
        this.printerName = printerName;
        this.printerAddress = printerAddress;
        mBluetoothPrinterProtocol.initWithSocket(socket);

        //更新打印机状态、时间
        lastPrinterStatus = IBluetoothPrinterProtocol.STATUS_OK;
        lastTimeMillis = System.currentTimeMillis();
    }

    /**
     * Method     断开打印机
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    public synchronized void disconnect() {
        if (mBluetoothPrinterProtocol!=null){
            mBluetoothPrinterProtocol.disconnect();
            mBluetoothPrinterProtocol = null;
            printerAddress = "";
            printerName = "";

            this.connectState = false;
            //更新打印机状态、时间
            lastPrinterStatus = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
            lastTimeMillis = System.currentTimeMillis();
        }
    }

    /**
     * Method     获取打印机状态
     * Parameters []
     * Return     int
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    public synchronized int printerStatus() {
        int printerStatus = IBluetoothPrinterProtocol.STATUS_DISCONNECT;
        Log.d("蓝牙打印机-接口调用时间",String.valueOf(System.currentTimeMillis()));
        if (mBluetoothPrinterProtocol!=null){
            Log.d("蓝牙打印机-状态开始时间",String.valueOf(System.currentTimeMillis()));
            printerStatus = mBluetoothPrinterProtocol.getPrinterStatus();
            Log.d("蓝牙打印机-状态结束时间",String.valueOf(System.currentTimeMillis()));
        }
        //更新打印机状态、时间
        lastPrinterStatus = printerStatus;
        lastTimeMillis = System.currentTimeMillis();
        return printerStatus;
    }

    /**
     *Method     获取打印机状态（优先取缓存）
     *Parameters []
     *Return     int
     *Author     Vin
     *Mail       vinintg@gmail.com
     */
    public synchronized int printerStatusWithCache(long intervalMilliseconds){
        long current = System.currentTimeMillis();
        if (current-lastTimeMillis < intervalMilliseconds){
            return lastPrinterStatus;
        }
        return printerStatus();
    }

    /**
     *Method     设备驱动是否完全支持
     *Parameters []
     *Return     boolean
     *Author     Vin
     *Mail       vinintg@gmail.com
     */
    public synchronized boolean isFullySupport(){
        boolean isFullySupport = false;
        if (mBluetoothPrinterProtocol!=null){
            isFullySupport = mBluetoothPrinterProtocol.isFullySupport();
        }
        return isFullySupport;
    }

    /**
     * Method     getPrinterName
     * 获取打印机名字
     * Parameters []
     * Return     java.lang.String
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    public synchronized String getPrinterName(){
        return this.printerName;
    }

    /**
     * Method     getPrinterAddress
     * 获取打印机MAC
     * Parameters []
     * Return     java.lang.String
     * Author     Vin
     * Mail       vinintg@gmail.com
     */
    public synchronized String getPrinterAddress(){
        return this.printerAddress;
    }

    /**
     *Method     获取打印机模板
     *Parameters []
     *Return     int
     *Author     Vin
     *Mail       vinintg@gmail.com
     */
    public synchronized int getPrinterType() {
        for (Map.Entry<String, Integer> entry : this.name2Type.entrySet()) {
            if (this.printerName.startsWith((String) entry.getKey())) {
                return ((Integer) entry.getValue()).intValue();
            }
        }
        return 0;
    }

    /**
     *Method     获取蓝牙打印实例
     *Parameters []
     *Return     IBluetoothPrinterProtocol
     *Author     Vin
     *Mail       vinintg@gmail.com
     */
    public synchronized IBluetoothPrinterProtocol getBluetoothPrinterProtocol(){
        return mBluetoothPrinterProtocol;
    }
}
