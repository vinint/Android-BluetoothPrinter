package io.vin.android.bluetoothprinter.demo.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙广播接收器
 * Author     Vin
 * Mail       vinintg@gmail.com
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    private Handler mHandler;
    private List<BluetoothDevice> mDevices = new ArrayList<>();

    public BluetoothBroadcastReceiver() {
    }

    public BluetoothBroadcastReceiver(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //1.蓝牙状态更新
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            //当前状态
            int EXTRA_STATE = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            //前一个状态
            int EXTRA_PREVIOUS_STATE = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);
            if (BluetoothAdapter.STATE_TURNING_ON == EXTRA_STATE) {

            }
            if (BluetoothAdapter.STATE_ON == EXTRA_STATE) {

            }
            if (BluetoothAdapter.STATE_TURNING_OFF == EXTRA_STATE) {

            }
            if (BluetoothAdapter.STATE_OFF == EXTRA_STATE) {

            }
            return;
        }
        //2.蓝牙模块本身的"可检测模式"
        if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
            //当前可检测模式
            int EXTRA_SCAN_MODE = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
            //前一个可检测模式
            int EXTRA_PREVIOUS_SCAN_MODE = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, -1);
            if (BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE == EXTRA_SCAN_MODE) {
                //设备处于可检测到模式
            }

            if (BluetoothAdapter.SCAN_MODE_CONNECTABLE == EXTRA_SCAN_MODE) {
                //未处于可检测到模式但仍能接收连接

            }
            if (BluetoothAdapter.SCAN_MODE_NONE == EXTRA_SCAN_MODE) {
                //未处于可检测到模式并且无法接收连接

            }

            return;
        }


        //2.发现设备When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            mDevices.add(device);
            // If it's already paired, skip it, because it's been listed already
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            }
            // When discovery is finished, change the Activity title
            return;
        }
        //3.发现设备结束
        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            if (mHandler != null) {
                Message msg = mHandler.obtainMessage();
                mHandler.sendMessage(msg);
            }
        }
    }

    public List<BluetoothDevice> getScanBthDevices() {
        return mDevices;
    }
}
