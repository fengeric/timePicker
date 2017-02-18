package com.umeng.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.umeng.test.Util.LogUtil;
import com.umeng.test.Util.Util;
import com.umeng.test.adapter.GridviewAdapter;
import com.umeng.test.view.MyGridView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> list = new ArrayList<>();// 展示的照片的集合
    private MyGridView myGridView;// 展示照片的控件
    private GridviewAdapter myAdapter;// 展示照片的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    // 初始化
    private void initView() {
        try {
            myGridView = (MyGridView) findViewById(R.id.gridview_display_image);
            myAdapter = new GridviewAdapter(this, list);
            myGridView.setAdapter(myAdapter);
            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(MainActivity.this, MyImageViewActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("pos", position);
                    in.putExtra("list", list);
                    startActivity(in);
                    Util.ActivitySkip(MainActivity.this);
                }
            });
        } catch (Exception e) {
            LogUtil.e(getClass(), "initView", e);
        }
    }

    public void btnOnclick(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case Util.CODE_CAMERA_REQUEST:
                    break;
                case Util.CODE_GALLERY_REQUEST:
                    break;
                default:

                    break;
            }
            // myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.e(getClass(), "onActivityResult", e);
        }
    }

}
