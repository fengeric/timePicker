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
    String arrayContent[] = null;
    String text_first_content;
    String text_second_content;

    public PickerUtil(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void showSingleChooseDialog(String textTile, final String[] arrayString) {
        try {
            View v = inflater.inflate(R.layout.activity_single_choice, null);
            final TextView tv_single_choice_title = (TextView) v.findViewById(R.id.single_choice_title);// 标题
            final WheelView wv_single_choice_wv_layout = (WheelView) v.findViewById(R.id.single_choice_wv_layout);// 选择控件
            final TextView wv_single_choice_bt = (TextView) v.findViewById(R.id.single_choice_bt);// 确定按钮
            // wv_single_choice_wv_layout.setVisibleItems(5);// 设置每个滚轮的行数
            // wv_single_choice_wv_layout.setCyclic(false);// 设置能否循环滚动
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
        } catch (Exception e) {
            Log.e("lala", "showSingleChooseDialog" + e.toString());
        }
    }

    public void showTwoChooseDialog(String textTile, final String[] arrayString1, final String[] arrayString2,final String[] arrayStringArea) {
        try {
            arrayContent = arrayString1;
            text_first_content = arrayStringArea[0];
            text_second_content = arrayString1[0];
            View v = inflater.inflate(R.layout.activity_double_choice, null);
            final TextView tv_title = (TextView) v.findViewById(R.id.single_choice_title);// 标题
            final WheelView choice_wv_layout1 = (WheelView) v.findViewById(R.id.choice_wv_layout1);// 选择控件1
            final WheelView choice_wv_layout2 = (WheelView) v.findViewById(R.id.choice_wv_layout2);// 选择控件2
            final TextView choice_bt = (TextView) v.findViewById(R.id.choice_bt);// 确定按钮
            // wv_single_choice_wv_layout.setVisibleItems(5);// 设置每个滚轮的行数
            // wv_single_choice_wv_layout.setCyclic(false);// 设置能否循环滚动
            // wv.setLabel("年");// 设置滚轮的标签
            tv_title.setText(textTile);

            choice_wv_layout1.setAdapter(new ArrayWheelAdapter<String>(arrayStringArea));
            choice_wv_layout1.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    // tv.setText(arrayString[wv.getCurrentItem()]);
                    //callBack.loadDataSuccess(arrayString[choice_wv_layout1.getCurrentItem()]);
                    String chooseText = arrayStringArea[choice_wv_layout1.getCurrentItem()];
                    if (chooseText.equals("上海")) {
                        arrayContent = arrayString1;
                        text_first_content = "上海";
                    } else if (chooseText.equals("周边")) {
                        arrayContent = arrayString2;
                        text_first_content = "周边";
                    }
                }
            });
            choice_wv_layout2.setAdapter(new ArrayWheelAdapter<String>(arrayContent));
            choice_wv_layout2.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    // tv.setText(arrayString[wv.getCurrentItem()]);
                    //callBack.loadDataSuccess(arrayString[choice_wv_layout2.getCurrentItem()]);
                    text_second_content = arrayContent[choice_wv_layout2.getCurrentItem()];
                }
            });
            choice_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.loadDataSuccess(text_first_content + text_second_content);
                    dismissChooseDialog();
                }
            });
            getChooseDialog(context, v);
        } catch (Exception e) {
            Log.e("lala", "showSingleChooseDialog" + e.toString());
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
            Log.e("lala", "dismissChooseDialog" + e.toString());
        }
    }

    public interface loadDataCallBack {
        public void loadDataSuccess(String result);
    }

    public void setCallBack(loadDataCallBack callBack) {
        this.callBack = callBack;
    }
}
