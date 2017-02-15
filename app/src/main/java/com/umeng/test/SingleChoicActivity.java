package com.umeng.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.umeng.test.time.ArrayWheelAdapter;
import com.umeng.test.time.OnWheelChangedListener;
import com.umeng.test.time.WheelView;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SingleChoicActivity extends Activity {
    
    private WheelView wv;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);

        final String[] arrayString = {"是", "否"};
        tv = (TextView) findViewById(R.id.tv);
        wv = (WheelView) findViewById(R.id.wv_layout);
        wv.setVisibleItems(5);
        wv.setCyclic(false);
        wv.setLabel("年");


        wv.setAdapter(new ArrayWheelAdapter<String>(arrayString));
        wv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                tv.setText(arrayString[wv.getCurrentItem()]);
            }
        });
    }
}
