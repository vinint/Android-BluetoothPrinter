package io.vin.android.bluetoothprinter.demo.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.vin.android.bluetoothprinter.demo.R;


public class BluetoothDeviceAdapter extends BaseAdapter {

    private List<BluetoothDevice> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public BluetoothDeviceAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<BluetoothDevice> devices){
        this.objects = devices;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.bluetooth_adapter_device, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((BluetoothDevice)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(BluetoothDevice object, ViewHolder holder) {
        holder.deviceName.setText(object.getName());
        holder.deviceMAC.setText(object.getAddress());
    }

    protected class ViewHolder {
        public TextView deviceName;
        public TextView deviceMAC;
        public ViewHolder(View view) {
           deviceName =  view.findViewById(R.id.tv_device_name);
           deviceMAC = view.findViewById(R.id.tv_device_mac);
        }
    }
}
