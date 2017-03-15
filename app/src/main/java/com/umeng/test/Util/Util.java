package com.umeng.test.Util;

import android.app.Activity;
import android.content.Context;

import com.umeng.test.R;

/**
 * Created by Administrator on 2017/2/17.
 */

public class Util {
    public static final int CODE_GALLERY_REQUEST = 0xa0;
    public static final int CODE_CAMERA_REQUEST = 0xa1;
    public static final int CODE_RESULT_REQUEST = 0xa2;

    public static final int MAX_PIC = 10;// 最多上传的图片数量
    public static final String INTENT_MAX_PIC_KEY = "max_pic";

    public static void ActivitySkip(Context context) {
        try {
            ((Activity) context).overridePendingTransition(
                    R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        } catch (Exception e) {
            LogUtil.e(Util.class, "ActivitySkip", e);
        }

    }

    public static void ActivityExit(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.activity_in_from_left,
                    R.anim.activity_out_to_right);
        } catch (Exception e) {
            LogUtil.e(Util.class, "ActivityExit", e);
        }
    }
}
