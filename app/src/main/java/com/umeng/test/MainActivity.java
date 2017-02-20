package com.umeng.test;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.umeng.test.Util.LogUtil;
import com.umeng.test.Util.Util;
import com.umeng.test.adapter.GridviewAdapter;
import com.umeng.test.photoSelector.model.IntentConstants;
import com.umeng.test.photoSelector.model.PhotoModel;
import com.umeng.test.photoSelector.ui.PhotoSelectorActivity;
import com.umeng.test.view.MyGridView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> list = new ArrayList<>();// 展示的照片的集合
    private MyGridView myGridView;// 展示照片的控件
    private GridviewAdapter myAdapter;// 展示照片的适配器


    private final int CAMREA_RESQUSET = 1;
    private final int REQUEST_CODE_GETPHOTO = 102;
    private ArrayList<PhotoModel> photos;
    public static final int PHOTOZOOM = 2; // 缩放
    public static final String IMAGE_UNSPECIFIED = "image/*";


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
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),
                PhotoSelectorActivity.class);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_GETPHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if (requestCode == CAMREA_RESQUSET && resultCode == RESULT_OK) {
                photos = (ArrayList<PhotoModel>) intent
                        .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                Log.v("lala", "--a1--" + photos.get(0).getOriginalPath());
                list.add("file://" + photos.get(0).getOriginalPath());
                // setBitmap(camerPhoto.sdPath + "/" + camerPhoto.photo);
                // startUpPic(camerPhoto.sdPath + "/" + camerPhoto.photo);
                // clipImageFromLocal(camerPhoto.sdPath + "/" + camerPhoto.photo);
            } else if (requestCode == REQUEST_CODE_GETPHOTO
                    && resultCode == RESULT_OK) {
                photos = (ArrayList<PhotoModel>) intent
                        .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                for (int i = 0; i < photos.size(); i++) {
                    list.add("file://" + photos.get(i).getOriginalPath());
                }
                Log.v("lala", "--a2--" + photos.get(0).getOriginalPath());
                // clipImageFromLocal(photos.get(0).getOriginalPath());
            } else if (requestCode == PHOTOZOOM) {
                String path = getImagePath(intent);
                Log.v("lala", "--a3--" + path);
                // clipImageFromLocal(path);
                // setBitmap(path);
                // startUpPic(path);
            }
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.e(getClass(), "onActivityResult", e);
        }
    }

    private String getImagePath(Intent data) {
        String path = "";
        try {
            Uri originalUri = data.getData(); // 获得图片的uri
            String[] proj = {MediaStore.Images.Media.DATA};

            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(originalUri, proj, null, null, null);
            // 按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            // 最后根据索引值获取图片路径
            path = cursor.getString(column_index);
            return path;
        } catch (Exception e) {
            LogUtil.e(getClass(), "getImagePath", e);
            return path;
        }
    }
}
