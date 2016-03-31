package com.example.zahnubeidanci.utils;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtils {
	/**弹出安全的吐司
	 * @param activity
	 * @param msg
	 */
	public static void showSafeToast(final Activity activity, final String msg) {
		if (Thread.currentThread().getName().equals("main")) {
			Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		} else {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
