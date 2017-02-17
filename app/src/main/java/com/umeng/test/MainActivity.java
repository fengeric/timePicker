package com.umeng.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.umeng.test.Util.ChoosePhotoUtil;

public class MainActivity extends Activity {
    private ChoosePhotoUtil choosePhotoUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choosePhotoUtil = new ChoosePhotoUtil(this);
    }

    public void btnOnclick(View view){
        choosePhotoUtil.showChoosePhotoDialog();
    }
}
