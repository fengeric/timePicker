package com.umeng.test.Util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.test.R;

/**
 * Created by Administrator on 2017/2/17.
 */

public class ChoosePhotoUtil {
    private Context context;
    private LayoutInflater inflater;
    private loadDataCallBack callBack;
    private pressBtCallBack pressCallBack;
    private Dialog dialog = null;

    public ChoosePhotoUtil(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**@title 展示五选框
     * @params
     * @return type
     **/
    public void showChoosePhotoDialog() {
        try {
            View v = inflater.inflate(R.layout.choose_photo_dialog_layout, null);

            final TextView tv_album = (TextView) v.findViewById(R.id.choose_photo_album);// 标题
            final TextView tv_camera = (TextView) v.findViewById(R.id.choose_photo_camera);// 标题
            final TextView tv_back = (TextView) v.findViewById(R.id.choose_photo_back);// 标题

            tv_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissChooseDialog();
                }
            });
            tv_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissChooseDialog();

                }
            });
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissChooseDialog();

                }
            });

            getChooseDialog(context, v);
        } catch (Exception e) {
            LogUtil.e(ChoosePhotoUtil.class, "showFiveChooseDialog", e);
        }
    }
    /*
     * 弹出dialog
     */
    public void getChooseDialog(Context context, View v) {
        dialog = new Dialog(context, R.style.set_dialog_style);
        try {
            dialog.setCancelable(true);// 可以用返回键取消
            dialog.setContentView(v);
            dialog.setCanceledOnTouchOutside(true);// 点击其它区域取消dialog
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            //设置显示动画
            window.setWindowAnimations(R.style.set_dialog_style);
            wlp.gravity = Gravity.BOTTOM;// 设置在最下面
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
            dialog.show();
        } catch (Exception e) {
            LogUtil.e(ChoosePhotoUtil.class, "nameOrHeadDialog(Context context, int who)", e);
        }
    }

    private void dismissChooseDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            LogUtil.e(ChoosePhotoUtil.class, "dismissChooseDialog", e);
        }
    }

    public interface loadDataCallBack {
        public void loadDataSuccess(String result);
    }

    public void setCallBack(loadDataCallBack callBack) {
        this.callBack = callBack;
    }

    public interface pressBtCallBack {
        public void pressSuccess(boolean isPressYes);
    }

    public void setCallBack(pressBtCallBack callBack) {
        this.pressCallBack = callBack;
    }

}
