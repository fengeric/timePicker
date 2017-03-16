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
import android.widget.Toast;

import com.umeng.test.Util.LogUtil;
import com.umeng.test.Util.Util;
import com.umeng.test.adapter.GridviewAdapter;
import com.umeng.test.inter.ImageDelCallBack;
import com.umeng.test.photoSelector.model.IntentConstants;
import com.umeng.test.photoSelector.model.PhotoModel;
import com.umeng.test.photoSelector.ui.PhotoSelectorActivity;
import com.umeng.test.view.MyGridView;

import java.util.ArrayList;

public class MainActivity extends Activity implements ImageDelCallBack{
    private ArrayList<String> list_img_display = new ArrayList<>();// 展示的照片的集合(带有上传图片按钮)
    private ArrayList<String> list_img_preview = new ArrayList<>();// 展示的预览的照片的集合(不带有上传图片按钮)
    private MyGridView myGridView;// 展示照片的控件
    private GridviewAdapter myAdapter;// 展示照片的适配器
    private final int CAMREA_RESQUSET = 1;
    private final int REQUEST_CODE_GETPHOTO = 102;
    private ArrayList<PhotoModel> photos;
    public static final int PHOTOZOOM = 2; // 缩放
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private String up_picture_url = "";
    private boolean isShowDelete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        up_picture_url = "drawable://" + R.drawable.up_photo;
        list_img_display.add(up_picture_url);
        initView();
    }

    // 初始化
    private void initView() {
        try {
            myGridView = (MyGridView) findViewById(R.id.gridview_display_image);
            myAdapter = new GridviewAdapter(this, list_img_display, this);
            myGridView.setAdapter(myAdapter);
            myGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                    if (position != list_img_display.size() - 1) {// 不是上传按钮时
                        /*final ImageView iv_del = (ImageView) view.findViewById(R.id.iv_img_del);
                        // int tagId = (Integer) view.getTag(R.id.visible_id);
                        if (view.getTag(R.id.visible_id) != null) {
                            iv_del.setVisibility(View.GONE);
                            // view.setTag(R.id.visible_id, 2);
                            view.setTag(R.id.visible_id, null);
                        } else {
                            iv_del.setVisibility(View.VISIBLE);
                            view.setTag(R.id.visible_id, 1);
                        }
                        iv_del.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                iv_del.setVisibility(View.GONE);
                                view.setTag(R.id.visible_id, null);
                                list_img_display.remove(position);
                                list_img_preview.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }
                        });*/
                        if (isShowDelete) {
                            isShowDelete = false;
                        } else {
                            isShowDelete = true;
                        }
                        myAdapter.setIsShowDelete(isShowDelete);
                    }
                    return false;
                }
            });
            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    int add_index = -1;// 加号的位置
                    for (int i = 0; i < list_img_display.size(); i++) {
                        if (list_img_display.get(i).contains("drawable://")) {
                            add_index = i;
                        }
                    }
                    // 只要加号的位置不等于-1，就证明存在加号
                    if (add_index != -1) {
                        // 如果该item是加号，那么点击去本地相册添加图片
                        if (position == add_index) {
                            if (judgeImageListSize() == Util.MAX_PIC) {
                                Toast.makeText(MainActivity.this, "最多只能上传" + Util.MAX_PIC + "张图片",Toast.LENGTH_LONG).show();
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(),
                                    PhotoSelectorActivity.class);
                            intent.putExtra(Util.INTENT_MAX_PIC_KEY,
                                    Util.MAX_PIC - judgeImageListSize());
                            startActivityForResult(intent,
                                    REQUEST_CODE_GETPHOTO);
                        } else {
                            // 如果该item不是加号，那么点击预览图片
                            previewPic(position);
                        }
                    } else {
                        // 如果该item不是加号，那么点击预览图片
                        previewPic(position);
                    }
                }

                // 预览图片
                private void previewPic(int position) {
                    Intent in = new Intent(MainActivity.this, MyImageViewActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("pos", position);
                    in.putExtra("list", list_img_preview);
                    startActivity(in);
                    Util.ActivitySkip(MainActivity.this);
                }
            });
        } catch (Exception e) {
            LogUtil.e(getClass(), "initView", e);
        }
    }

    /**
     * 判断已经上传了多少图片
     *
     * @return
     */
    private int judgeImageListSize() {
        int size = 0;
        try {
            if (list_img_display != null && list_img_display.size() > 1) {
                size = list_img_display.size() - 1;
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "judgeImageListSize()", e);
        }
        return size;
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
                /*photos = (ArrayList<PhotoModel>) intent
                        .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                Log.v("lala", "--a1--" + photos.get(0).getOriginalPath());
                list_img_display.add("file://" + photos.get(0).getOriginalPath());*/
                setPhotos(intent);
                // setBitmap(camerPhoto.sdPath + "/" + camerPhoto.photo);
                // startUpPic(camerPhoto.sdPath + "/" + camerPhoto.photo);
                // clipImageFromLocal(camerPhoto.sdPath + "/" + camerPhoto.photo);
            } else if (requestCode == REQUEST_CODE_GETPHOTO
                    && resultCode == RESULT_OK) {
                /*photos = (ArrayList<PhotoModel>) intent
                        .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                for (int i = 0; i < photos.size(); i++) {
                    list_img_display.add("file://" + photos.get(i).getOriginalPath());
                }
                Log.v("lala", "--a2--" + photos.get(0).getOriginalPath());*/
                setPhotos(intent);
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

    private void setPhotos(Intent in){
        try {
            photos = (ArrayList<PhotoModel>) in
                    .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
            list_img_display.remove(list_img_display.size() - 1);
            for (int i = 0; i < photos.size(); i++) {
                list_img_display.add("file://" + photos.get(i).getOriginalPath());
                list_img_preview.add("file://" + photos.get(i).getOriginalPath());
            }
            addIconRefreshAdpater();
        } catch (Exception e) {
          LogUtil.e(getClass(), "setPhotos", e);
        }
    }

    // 添加加号，刷新适配器
    private void addIconRefreshAdpater() {
        list_img_display.add(up_picture_url);
        myAdapter.notifyDataSetChanged();
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

    @Override
    public void onImageDelCallBack(int position) {
        list_img_display.remove(position);
        list_img_preview.remove(position);
        myAdapter.notifyDataSetChanged();
    }
}
