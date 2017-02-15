package com.umeng.test.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.test.R;
import com.umeng.test.time.ArrayWheelAdapter;
import com.umeng.test.time.OnWheelChangedListener;
import com.umeng.test.time.WheelView;

/**
 * Created by Administrator on 2017/2/15.
 */

public class PickerUtil {

    private Context context;
    private LayoutInflater inflater;
    private loadDataCallBack callBack;
    private Dialog dialog = null;

    public PickerUtil(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void showChooseDialog(String textTile, final String[] arrayString) {
        View v = inflater.inflate(R.layout.activity_single_choice, null);
        final TextView tv_single_choice_title = (TextView) v.findViewById(R.id.single_choice_title);// 标题
        final WheelView wv_single_choice_wv_layout = (WheelView) v.findViewById(R.id.single_choice_wv_layout);// 选择控件
        final TextView wv_single_choice_bt = (TextView) v.findViewById(R.id.single_choice_bt);// 确定按钮
        wv_single_choice_wv_layout.setVisibleItems(5);// 设置每个滚轮的行数
        wv_single_choice_wv_layout.setCyclic(false);// 设置能否循环滚动
        //wv.setLabel("年");// 设置滚轮的标签
        tv_single_choice_title.setText(textTile);

        wv_single_choice_wv_layout.setAdapter(new ArrayWheelAdapter<String>(arrayString));
        wv_single_choice_wv_layout.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // tv.setText(arrayString[wv.getCurrentItem()]);
                callBack.loadDataSuccess(arrayString[wv_single_choice_wv_layout.getCurrentItem()]);
            }
        });
        wv_single_choice_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.loadDataSuccess(arrayString[wv_single_choice_wv_layout.getCurrentItem()]);
                dismissChooseDialog();
            }
        });
        getChooseDialog(context, v);
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
            Log.e("lala",
                    "nameOrHeadDialog(Context context, int who)" + e.toString());
        }
    }

    private void dismissChooseDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            Log.e("lala",
                    "dismissChooseDialog" + e.toString());
        }
    }

    public interface loadDataCallBack {
        public void loadDataSuccess(String result);// 此处返回的result，可以是其他类型的数值，布尔或Bean（UserInfo）文件等等
    }

    public void setCallBack(loadDataCallBack callBack) {
        this.callBack = callBack;
    }
}