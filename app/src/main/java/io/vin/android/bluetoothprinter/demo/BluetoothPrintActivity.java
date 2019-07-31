package io.vin.android.bluetoothprinter.demo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.vin.android.bluetoothprinter.demo.adapter.BluetoothDeviceAdapter;
import io.vin.android.bluetoothprinter.demo.receiver.BluetoothBroadcastReceiver;
import io.vin.android.bluetoothprinter.hprt.HprtBluetoothPrinterFactory;
import io.vin.android.bluetoothprinter.jiqiang.JqBluetoothPrinterFactory;
import io.vin.android.bluetoothprinter.kuaimai.KuaiMaiBluetoothPrinterFactory;
import io.vin.android.bluetoothprinter.qirui.QRBluetoothPrinterFactory;
import io.vin.android.bluetoothprinter.zicox.ZicoxBluetoothPrinterFactory;
import io.vin.android.bluetoothprinterprotocol.BluetoothPrinterManager;
import io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol;
import io.vin.android.bluetoothprinterprotocol.PrintCallback;

import static io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol.COLOR_BLACK;
import static io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol.FONT_SIZE_32;
import static io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol.QRCODE_CORRECTION_LEVEL_H;
import static io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol.QRCODE_UNIT_WIDTH_6;
import static io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol.STYLE_BARCODE_CODE128;
import static io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol.STYLE_ROTATION_0;
import static io.vin.android.bluetoothprinterprotocol.IBluetoothPrinterProtocol.STYLE_TEXT_NO;

public class BluetoothPrintActivity extends FragmentActivity implements AdapterView.OnItemClickListener ,View.OnClickListener{
    String TAG = "BluetoothPrintActivity";
    private RxPermissions rxPermissions;
    private BluetoothBroadcastReceiver mReceiver;
    private BluetoothAdapter mBluetoothAdapter;

    private ListView mLvDevices;
    private BluetoothDeviceAdapter mAdapter;
    private List<BluetoothDevice> mDevices = new ArrayList<>();
    private Handler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity_print);
        rxPermissions = new RxPermissions(this);
        initViews();
        //初始化打印机
        initBluetoothPrinter();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }

    private void initViews() {
        findViewById(R.id.btn_scan).setOnClickListener(this);
        findViewById(R.id.btn_print).setOnClickListener(this);

        mLvDevices = findViewById(R.id.lv_devices);
        mAdapter = new BluetoothDeviceAdapter(this);
        mLvDevices.setAdapter(mAdapter);
        mLvDevices.setOnItemClickListener(this);
        mHandler = new Handler(getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //扫描蓝牙设备完成
                mDevices = mReceiver.getScanBthDevices();
                mDevices.addAll(mBluetoothAdapter.getBondedDevices());
                mAdapter.setData(mDevices);
                mAdapter.notifyDataSetChanged();
                BluetoothPrintActivity.this.unregisterReceiver(mReceiver);
            }
        };


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestCode.REQUEST_ENABLE_BT == requestCode) {
            if (RESULT_OK == resultCode) {
                //打开蓝牙成功
                return;
            }
            if (RESULT_CANCELED == resultCode) {
                //打开蓝牙失败
                return;
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cancelDiscoveryBthDevices();
        //连接打印机
        BluetoothDevice item = mDevices.get(position);

        String printerName = item.getName();
        String printerMac = item.getAddress();

        Boolean isSupport = BluetoothPrinterManager
                .getInstance()
                .checkPrinterSupported(printerName);

        if (isSupport){
            if (BluetoothPrinterManager.getInstance().connect(printerName, printerMac)){
                Toast.makeText(this, "连接成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "连接失败", Toast.LENGTH_LONG).show();
            }
        }else {
            //不支持
            Toast.makeText(this, "不支持该型号打印机", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_scan){
            rxPermissions
                    .request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .subscribe(granted -> {
                        if (granted) {
                            turnOnBluetooth();
                            registerBluetoothReceiver(mHandler);
                            startDiscoveryBthDevices();
                        } else {
                            // Oups permission denied
                        }
                    });
        }else if (v.getId() == R.id.btn_print){
            printLab();
        }
    }

    /**
     * Method     打开蓝牙开关
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/5/18 下午2:39
     * Modifytime 2018/5/18 下午2:39
     */
    private void turnOnBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //1.判断设备本身是否具备蓝牙模块
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        //2.开启蓝牙模块
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, RequestCode.REQUEST_ENABLE_BT);
        }

    }

    /**
     * Method     检查已经绑定的设备
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/5/18 下午3:15
     * Modifytime 2018/5/18 下午3:15
     */
    private void checkBondedDevices() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView

            }
        }
    }

    /**
     * Method     注册蓝牙接收器
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/5/18 下午3:22
     * Modifytime 2018/5/18 下午3:22
     */
    private void registerBluetoothReceiver(Handler handler) {
        mReceiver = new BluetoothBroadcastReceiver(handler);
        //注册蓝牙状态监听
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);
        //注册蓝牙可检测模式
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        this.registerReceiver(mReceiver, filter1);
        //注册发现设备监听Register for broadcasts when a device is discovered
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter2);
        //注册发现设备结束监听Register for broadcasts when discovery has finished
        IntentFilter filter3 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter3);
    }

    /**
     * Method     扫描发现蓝牙设备
     * Parameters []
     * Return     void
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/5/18 下午3:17
     * Modifytime 2018/5/18 下午3:17
     */
    private Boolean startDiscoveryBthDevices() {
        return mBluetoothAdapter.startDiscovery();
    }

    /**
     * Method     取消发现蓝牙设备
     * Parameters []
     * Return     java.lang.Boolean
     * Author     Vin
     * Mail       vinintg@gmail.com
     * Createtime 2018/5/18 下午3:59
     * Modifytime 2018/5/18 下午3:59
     */
    private Boolean cancelDiscoveryBthDevices() {
        return mBluetoothAdapter.cancelDiscovery();
    }

    private void initBluetoothPrinter() {
        //添加支持的蓝牙打印机
        //启瑞
        BluetoothPrinterManager.getInstance().registerPrinter("QR-386", new QRBluetoothPrinterFactory("QR-386"), 3);
        BluetoothPrinterManager.getInstance().registerPrinter("QR380", new QRBluetoothPrinterFactory("QR380"), 3);
        //汉印
        BluetoothPrinterManager.getInstance().registerPrinter("HM-A300", new HprtBluetoothPrinterFactory(""), 2);
        BluetoothPrinterManager.getInstance().registerPrinter("HM-A330", new HprtBluetoothPrinterFactory(""), 2);
        BluetoothPrinterManager.getInstance().registerPrinter("HM-A318", new HprtBluetoothPrinterFactory(""), 2);
        BluetoothPrinterManager.getInstance().registerPrinter("HM-A320", new HprtBluetoothPrinterFactory(""), 2);
        BluetoothPrinterManager.getInstance().registerPrinter("HM-A360", new HprtBluetoothPrinterFactory(""), 2);
        BluetoothPrinterManager.getInstance().registerPrinter("HM-A350", new HprtBluetoothPrinterFactory(""), 2);
        BluetoothPrinterManager.getInstance().registerPrinter("HM-M35", new HprtBluetoothPrinterFactory(""), 2);
        //济强
        BluetoothPrinterManager.getInstance().registerPrinter("JLP352", new JqBluetoothPrinterFactory(""), 1);
        BluetoothPrinterManager.getInstance().registerPrinter("WYH888", new JqBluetoothPrinterFactory(""), 1);
        BluetoothPrinterManager.getInstance().registerPrinter("EXP342", new JqBluetoothPrinterFactory("EXP342"), 1);
        //芝柯
        BluetoothPrinterManager.getInstance().registerPrinter("ZTO588", new ZicoxBluetoothPrinterFactory(""), 4);
        BluetoothPrinterManager.getInstance().registerPrinter("XT423", new ZicoxBluetoothPrinterFactory(""), 4);
        //快麦KM-118B、KM-218BT、KM-300S
        BluetoothPrinterManager.getInstance().registerPrinter("KM-118B", new KuaiMaiBluetoothPrinterFactory("KM-118B"), 1);
        BluetoothPrinterManager.getInstance().registerPrinter("KM-218BT", new KuaiMaiBluetoothPrinterFactory("KM-218BT"), 1);

    }

    private void printLab() {
        IBluetoothPrinterProtocol printerProtocol = BluetoothPrinterManager.getInstance().getBluetoothPrinterProtocol();
        if (printerProtocol == null) {
            return;
        }

        //draw Line
        printerProtocol.drawLine(3*8,13*8,3*8,3*8,3,IBluetoothPrinterProtocol.STYLE_LINE_FULL);

        printerProtocol.drawLine(3*8,3*8,3*8,13*8,3,IBluetoothPrinterProtocol.STYLE_LINE_FULL);

        //draw Text
        printerProtocol.drawText(3*8,5*8,0,0,"test draw text",FONT_SIZE_32,STYLE_TEXT_NO,COLOR_BLACK,STYLE_ROTATION_0);

        //draw Barcode
        printerProtocol.drawBarCode(3*8,8*8,10*8,2,"12345678",STYLE_BARCODE_CODE128,STYLE_ROTATION_0);

        //draw Qrcode
        printerProtocol.drawQRCode(3*8,18*8,"test draw qrcode",QRCODE_UNIT_WIDTH_6,QRCODE_CORRECTION_LEVEL_H,STYLE_ROTATION_0);

        //draw Image


        printerProtocol.printAndFeed(new PrintCallback() {
            @Override
            public void onPrintFail(int code) {

            }

            @Override
            public void onPrintSuccess() {

            }
        });

    }

}


