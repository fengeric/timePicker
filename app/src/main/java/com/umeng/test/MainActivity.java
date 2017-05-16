package com.umeng.test;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;

import com.umeng.test.util.ChoosePhotoUtil;
import com.umeng.test.util.LogUtil;
import com.umeng.test.util.Util;
import com.umeng.test.adapter.GridviewAdapter;
import com.umeng.test.view.MyGridView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ChoosePhotoUtil choosePhotoUtil;// 弹出拍照及相册弹框的公共类
    private String IMAGE_FILE_NAME = null;// 照相之后的图片的名称
    private File file;//存储照片的文件夹
    private ArrayList<String> list = new ArrayList<>();// 展示的照片的集合
    private MyGridView myGridView;// 展示照片的控件
    private GridviewAdapter myAdapter;// 展示照片的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choosePhotoUtil = new ChoosePhotoUtil(this);
        initView();
    }

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
        // 创建文件夹
        file = new File(Util.photoPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        choosePhotoUtil.showChoosePhotoDialog();
        choosePhotoUtil.setCallBack(new ChoosePhotoUtil.chooseCallBack() {
            @Override
            public void onChooseAlbum() {
                Intent intentFromGallery = new Intent();
                // 设置文件类型
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery,
                        Util.CODE_GALLERY_REQUEST);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            String realPath = "";// 图片的绝对路径
            switch (requestCode) {
                case Util.CODE_CAMERA_REQUEST:
                    File tempFile = new File(Util.photoPath, IMAGE_FILE_NAME);
                    Uri uri1 = Uri.fromFile(tempFile);
                    realPath = uri1.getPath();
                    break;
                case Util.CODE_GALLERY_REQUEST:
                    Uri uri2 = intent.getData();
                    realPath = getRealPathFromURI(uri2);
                    break;
                default:

                    break;
            }
            list.add("file://" + realPath);
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.e(getClass(), "onActivityResult", e);
        }
    }

    /**@title 获取图片的绝对路径
     * @params 
     * @return type 
     **/
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
