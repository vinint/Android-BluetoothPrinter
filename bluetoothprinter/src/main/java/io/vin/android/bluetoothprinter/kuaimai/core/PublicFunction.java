package io.vin.android.bluetoothprinter.kuaimai.core;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PublicFunction {
    public static final String PREFS_NAME = "MyPrefsFile";
    private Context D = null;
    private List<String> E = new ArrayList();
    private List<String> F = new ArrayList();
    private ArrayList<HashMap<String, String>> G = new ArrayList();
    private String H = "";


    public PublicFunction() {

    }
    public PublicFunction(Context context) {
        this.D = context;
    }

    public String EnableDevice(String str) {
        String str2 = "";
        if (str.equals("WiFi")) {
            str2 = c();
        }
        if (str.equals("Bluetooth")) {
            return d();
        }
        return str2;
    }

    private String c() {
        try {
            if (this.D == null) {
                return "Invalid Context";
            }
            WifiManager wifiManager = (WifiManager) this.D.getSystemService(Context.WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static String d() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!defaultAdapter.isEnabled()) {
                defaultAdapter.enable();
            }
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String DisableDevice(String str) {
        String str2 = "";
        if (str.equals("WiFi")) {
            str2 = e();
        }
        if (str.equals("Bluetooth")) {
            return f();
        }
        return str2;
    }

    private String e() {
        try {
            if (this.D == null) {
                return "Invalid Context";
            }
            WifiManager wifiManager = (WifiManager) this.D.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static String f() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter.isEnabled()) {
                defaultAdapter.disable();
            }
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void ShowMessageDialog(String str, String str2) {
        try {
            new Builder(this.D).setTitle(str).setMessage(str2).setPositiveButton("ȷ��", new OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void WriteSharedPreferencesData(String str, String str2) {
        this.D.getSharedPreferences(PREFS_NAME, 0).edit().putString(str, str2).commit();
    }

    public String ReadSharedPreferencesData(String str) {
        return this.D.getSharedPreferences(PREFS_NAME, 0).getString(str, "");
    }

    public int CountSubString(String str, String str2) {
        int indexOf = str.indexOf(str2);
        while (indexOf != -1) {
            indexOf = str.indexOf(str2, (indexOf + 1) + 1);
        }
        return 0;
    }

    public int GetStringIndex(String str, String str2, int i, boolean z) {
        int i2 = -1;
        for (int i3 = 0; i3 < i; i3++) {
            i2 = str.indexOf(str2, i2 + 1);
            if (i2 == -1) {
                break;
            }
        }
        if (i2 != -1) {
            return i2;
        }
        if (z) {
            return str.length();
        }
        return -1;
    }

    public String CreateRepeatString(String str, int i) {
        String str2 = "";
        for (int i2 = 0; i2 < i; i2++) {
            str2 = new StringBuilder(String.valueOf(str2)).append(str).toString();
        }
        return str2;
    }

    public String ReverseString(String str) {
        String str2 = "";
        if (str.length() > 0) {
            int length = str.length();
            while (length > 0) {
                String stringBuilder = new StringBuilder(String.valueOf(str2)).append(str.substring(length - 1, length)).toString();
                length--;
                str2 = stringBuilder;
            }
        }
        return str2;
    }

    public String ReadTxtFile(String str) {
        String str2 = "";
        if (str.substring(0, 7).equals("file://")) {
            str = str.substring(7);
        }
        File file = new File(str);
        if (!file.exists()) {
            return "false";
        }
        if (file.isDirectory()) {
            return "false";
        }
        try {
            InputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    if ((byte) -17 == str2.substring(0, 1).getBytes()[0]) {
                        str2 = str2.substring(1);
                    }
                    fileInputStream.close();
                    return str2;
                }
                str2 = new StringBuilder(String.valueOf(str2)).append(readLine).append("\n").toString();
            }
        } catch (FileNotFoundException e) {
            return "false";
        } catch (IOException e2) {
            return "false";
        }
    }

    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public ArrayList<HashMap<String, String>> GetSDPicture() {
        try {
            this.G.clear();
            this.E.clear();
            this.F.clear();
            if (!ExistSDCard()) {
                return null;
            }
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            this.H = externalStorageDirectory.getAbsolutePath();
            a(externalStorageDirectory);
            Collections.sort(this.E, String.CASE_INSENSITIVE_ORDER);
            Collections.sort(this.F, String.CASE_INSENSITIVE_ORDER);
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= this.F.size()) {
                    return this.G;
                }
                HashMap hashMap = new HashMap();
                hashMap.put("PathDisplay", (String) this.E.get(i2));
                hashMap.put("PathTag", (String) this.F.get(i2));
                this.G.add(hashMap);
                i = i2 + 1;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private boolean a(File file) {
        try {
            File[] listFiles = file.listFiles();
            for (File file2 : listFiles) {
                if (file2.isDirectory() && !file2.getName().subSequence(0, 1).equals(".") && a(file2)) {
                    this.E.add(file2.getAbsolutePath().replace(this.H, ""));
                    this.F.add(file2.getAbsolutePath());
                }
            }
            for (File file22 : listFiles) {
                if (!file22.isDirectory() && b(file22)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }

    private static boolean b(File file) {
        String name = file.getName();
        try {
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf <= 0) {
                return false;
            }
            name = name.substring(lastIndexOf + 1, name.length());
            if (name.toLowerCase().equals("jpg") || name.toLowerCase().equals("jpeg") || name.toLowerCase().equals("bmp") || name.toLowerCase().equals("png") || name.toLowerCase().equals(".gif")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    public byte[] sysCopy(List<byte[]> list) {
        int i = 0;
        for (byte[] length : list) {
            i = length.length + i;
        }
        byte[] obj = new byte[i];
        i = 0;
        for (byte[] length2 : list) {
            System.arraycopy(length2, 0, obj, i, length2.length);
            i = length2.length + i;
        }
        return obj;
    }

    public byte[] ArrayCopy(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        byte[] obj = new byte[((i + i3) - i2)];
        if (bArr != null) {
            System.arraycopy(bArr, 0, obj, 0, i);
        }
        System.arraycopy(bArr2, i2, obj, i, i3);
        return obj;
    }

    public String getCodePageIndex(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("Default", "Default");
        hashMap.put("Chinese Simplified", "Chinese Simplified");
        hashMap.put("USA", "USA");
        hashMap.put("British", "BRI");
        hashMap.put("German", "GER");
        hashMap.put("French", "FRE");
        hashMap.put("Danish", "DAN");
        hashMap.put("Italian", "ITA");
        hashMap.put("Spanish", "SPA");
        hashMap.put("Swedish", "SWE");
        hashMap.put("Swiss", "SWI");
        hashMap.put("United States", "437");
        hashMap.put("Multilingual", "850");
        hashMap.put("Slavic", "852");
        hashMap.put("Portuguese", "860");
        hashMap.put("Canadian/French", "863");
        hashMap.put("Nordic", "865");
        hashMap.put("Turkish", "857");
        hashMap.put("Central Europe", "1250");
        hashMap.put("Latin I", "1252");
        hashMap.put("Greek", "1253");
        hashMap.put("Turkish", "1254");
        hashMap.put("Iran", "Iran");
        hashMap.put("Iran II", "Iran II");
        return (String) hashMap.get(str);
    }

    public static String getLanguageEncode(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("Default", "gb2312");
        hashMap.put("Chinese Simplified", "GBK");
        hashMap.put("USA", "iso8859-1");
        hashMap.put("BRI", "iso8859-1");
        hashMap.put("GER", "iso8859-1");
        hashMap.put("FRE", "iso8859-1");
        hashMap.put("DAN", "iso8859-10");
        hashMap.put("ITA", "iso8859-16");
        hashMap.put("SPA", "iso8859-1");
        hashMap.put("SWE", "iso8859-1");
        hashMap.put("SWI", "iso8859-1");
        hashMap.put("437", "iso8859-1");
        hashMap.put("850", "iso8859-1");
        hashMap.put("852", "iso8859-5");
        hashMap.put("860", "iso8859-1");
        hashMap.put("863", "iso8859-1");
        hashMap.put("865", "iso8859-4");
        hashMap.put("857", "iso8859-3");
        hashMap.put("1250", "iso8859-2");
        hashMap.put("1252", "iso8859-6");
        hashMap.put("1253", "iso8859-7");
        hashMap.put("1254", "iso8859-3");
        hashMap.put("Iran", "iso8859-6");
        hashMap.put("Iran II", "iso8859-6");
        return (String) hashMap.get(str);
    }
}
