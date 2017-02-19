package com.umeng.test.photoSelector.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.umeng.test.R;


public class Util {

	public static final String TAG = "error";
	private static Toast toast;
	/**裁剪图片的返回码*/
	public static final int CLIP_IMAGE_RESULT_CODE = 0X050;

	// 打印日志
	public static void showLog(@SuppressWarnings("rawtypes") Class clazz,
			String methodName, Exception e) {
		if (clazz != null && e != null) {
			Log.d(TAG,
					clazz.getName() + "---" + methodName + "--"
							+ e.getMessage());
		}
	}

	// 显示toast提示
	public static void showToast(Context context, String message) {
		try {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} catch (Exception e) {
			Log.d("error", "Util--showToast(Context context,String message)"
					+ e.toString());
		}
	}

	public static void ActivityExit(Activity activity) {
		try {
			activity.overridePendingTransition(R.anim.activity_in_from_left,
					R.anim.activity_out_to_right);
		} catch (Exception e) {
			showLog(Util.class, "ActivityExit", e);
		}
	}
}
