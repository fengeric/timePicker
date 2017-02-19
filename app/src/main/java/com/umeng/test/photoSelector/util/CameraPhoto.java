package com.umeng.test.photoSelector.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.umeng.test.MyImageViewActivity;
import com.umeng.test.R;
import com.umeng.test.photoSelector.view.FixGridLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;


public class CameraPhoto {
	public final String sdPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/happerFix";
	public String photo = "photo.jpg";
	private LayoutInflater inflater;
	private View viewImage;
	public HashMap<String, String> imageUrlList = new HashMap<String, String>();
	private FixGridLayout fixGridLayout;

	private Animation shake;// 加载动画资源文件
	private Context context1;
	public static Bitmap bitmap = null;

	public CameraPhoto(FixGridLayout fixGridLayout, Context context) {
		this.fixGridLayout = fixGridLayout;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.context1 = context;
		shake = AnimationUtils.loadAnimation(context, R.anim.shake);// 加载动画资源文件
	}

	public void showCameraPhoto(int CAMREA_RESQUSET, Activity activity) {
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File director = new File(sdPath);
		File tmpFile = new File(sdPath, photo);
		if (!director.exists()) {
			director.mkdir();
		}
		if (tmpFile != null) {
			Uri outputFileUri = Uri.fromFile(tmpFile);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					outputFileUri);
			activity.startActivityForResult(intent, CAMREA_RESQUSET);
		}
	}

	public void addImageView(Bitmap bitMap, Context context) {
		try {
			viewImage = inflater.inflate(R.layout.photo_image, null);
			final ImageView imageview = (ImageView) viewImage
					.findViewById(R.id.image_view);

			// LayoutParams layoutParams=new LayoutParams(260, 320);
			// imageview.setLayoutParams(layoutParams);
			String fileName = saveFile(bitMap);
			setImageBitMap(fileName, imageview);
			int widthHeight = context.getResources().getDimensionPixelSize(
					R.dimen.frame_layout_contract_image_btn_width_height);
			int marginLeft = context.getResources().getDimensionPixelSize(
					R.dimen.iamge_photo_margin_left_right);
			LayoutParams params = new LayoutParams(widthHeight, widthHeight);
			params.setMargins(marginLeft, 0, marginLeft, 0);
			viewImage.setLayoutParams(params);
			final Button btn_del = (Button) viewImage
					.findViewById(R.id.image_del);

			btn_del.setTag(viewImage);
			btn_del.setTag(R.id.delete_file_name, fileName);
			btn_del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					View viewImage = (View) v.getTag();
					String fileName = (String) v.getTag(R.id.delete_file_name);
					deleteImageFile(fileName);
					imageUrlList.remove(fileName);
					fixGridLayout.removeView(viewImage);
				}
			});

			btn_del.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					changeBtDelState(imageview, btn_del);
					return true;
				}
			});

			imageview.setTag(viewImage);
			imageview.setTag(R.id.delete_file_name, fileName);
			imageview.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						// View viewImage = (View) v.getTag();
						String fileName = (String) v
								.getTag(R.id.delete_file_name);
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inJustDecodeBounds = false;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
						bitmap = BitmapFactory.decodeFile(
								imageUrlList.get(fileName), opts);
						Intent in = new Intent();
						in.setClass(context1, MyImageViewActivity.class);
						context1.startActivity(in);
					} catch (Exception e) {
						Util.showLog(getClass(),
								"imageview.setOnClickListener", e);
					}
				}
			});

			imageview.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View arg0) {
					changeBtDelState(imageview, btn_del);
					return true;
				}
			});

			fixGridLayout.addView(viewImage, 0);
		} catch (Exception e) {
			Util.showLog(getClass(), "addViewImageView", e);
		}
	}

	private void changeBtDelState(ImageView imageview, Button btn_del) {
		try {
			imageview.startAnimation(shake); // 给组件播放动画效果

			int value_visibility = btn_del.getVisibility();// 0 --------
															// VISIBLE
															// 可见
			// 4 -------- INVISIBLE
			// 不可见但是占用布局空间
			// 8 -------- GONE 不可见也不占用布局空间

			if (value_visibility == 4) {
				btn_del.setVisibility(View.VISIBLE);
			} else if (value_visibility == 0) {
				btn_del.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			Util.showLog(getClass(), "changeBtDelState()", e);
		}
	}

	private void setImageBitMap(String fileName, ImageView imageView) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = false;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
			Bitmap bitmap = BitmapFactory.decodeFile(
					imageUrlList.get(fileName), opts);
			imageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			Util.showLog(getClass(), "setImageBitMap", e);
		}

	}

	private void deleteImageFile(String fileName) {
		try {
			File file = new File(imageUrlList.get(fileName));
			file.delete();
		} catch (Exception e) {
			Util.showLog(getClass(), "deleteFile", e);
		}
	}

	public Bitmap compressBySize(String pathName, int targetWidth,
			int targetHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		// 得到图片的宽度、高度；
		float imgWidth = opts.outWidth;
		float imgHeight = opts.outHeight;
		// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
		// int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
		// int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
		int widthRatio = (int) (imgWidth / (float) targetWidth);
		int heightRatio = (int) (imgHeight / (float) targetHeight);
		opts.inSampleSize = 1;
		if (widthRatio > 1 || widthRatio > 1) {
			if (widthRatio > heightRatio) {
				opts.inSampleSize = widthRatio;
			} else {
				opts.inSampleSize = heightRatio;
			}
		}
		// 设置好缩放比例后，加载图片进内容；
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, opts);
		return bitmap;
	}

	private String saveFile(Bitmap bitmap) {
		FileOutputStream b = null;
		// ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
		File file = new File(sdPath);
		if (!file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		Date date = new Date();
		String fileName = date.getTime() + ".jpg";
		String fileUrl = sdPath + "/" + fileName;
		imageUrlList.put(fileName, fileUrl);
		try {
			b = new FileOutputStream(fileUrl);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileName;
	}

}
