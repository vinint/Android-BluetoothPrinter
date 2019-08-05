package io.vin.android.bluetoothprinter.RT;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

    public static void showToast(final Activity activity, final String message) {
        if (activity == null || TextUtils.isEmpty(message)) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
