package io.vin.android.bluetoothprinter.kuaimai.core;

import android.util.Log;

import java.io.UnsupportedEncodingException;

public class LogUlit {
    public static void writeFileToSDCard(byte[] data, String r8, String r9, boolean r10, boolean r11) {
        try {
            Log.d("打印机指令",new String(data,"GBK"));
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }

    }

    public static void writeFileToSDCard(byte[] bArr) {
        writeFileToSDCard(bArr, HPRTConst.FOLDER, HPRTConst.FOLDER_NAME, true, true);
    }
}
