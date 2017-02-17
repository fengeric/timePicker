package com.umeng.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.umeng.test.Util.ChoosePhotoUtil;
import com.umeng.test.Util.LogUtil;
import com.umeng.test.Util.Util;
import com.umeng.test.adapter.GridviewAdapter;
import com.umeng.test.view.MyGridView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ChoosePhotoUtil choosePhotoUtil;
    private String IMAGE_FILE_NAME = null;// 照相之后的图片的名称
    private File file;
    private ArrayList<String> list = new ArrayList<>();
    private MyGridView myGridView;
    private GridviewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choosePhotoUtil = new ChoosePhotoUtil(this);
        initView();
    }

    private void initView(){
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

    public void btnOnclick(View view){
        // 创建文件夹
        file = new File(Util.photoPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        choosePhotoUtil.showChoosePhotoDialog();
        choosePhotoUtil.setCallBack(new ChoosePhotoUtil.chooseCallBack() {
            @Override
            public void onChooseAlbum() {

            }

            @Override
            public void onChooseCamera() {
                IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";
                Intent intentFromCapture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(file, IMAGE_FILE_NAME)));
                startActivityForResult(intentFromCapture,
                        Util.CODE_CAMERA_REQUEST);
            }

            @Override
            public void onChooseBack() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case Util.CODE_CAMERA_REQUEST:
                    File tempFile = new File(Util.photoPath, IMAGE_FILE_NAME);
                    Uri uri = Uri.fromFile(tempFile);
                    // cropRawPhoto(uri);
                    Log.v("lala", "path:" + uri.getPath());
                    list.add("file://" + uri.getPath());
                    myAdapter.notifyDataSetChanged();

                    break;

                default:
                    break;
            }
        } catch (Exception e) {
          LogUtil.e(getClass(), "onActivityResult", e);
        }
    }
}
