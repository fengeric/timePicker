package com.umeng.test.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.umeng.test.R;
import com.umeng.test.util.ImageLoad;
import com.umeng.test.inter.ImageDelCallBack;

import java.util.ArrayList;

public class GridviewAdapter extends BaseAdapter {
	private Context mcontext;
	private ArrayList<String> list_img;
	private LayoutInflater inflater;
	private ImageLoad imageLoad;
	private String img_url ;
	private ImageDelCallBack callBack;
	private boolean isShowDelete;

	public GridviewAdapter(Context context, ArrayList<String> list_img_url, ImageDelCallBack callBack) {
		this.callBack = callBack;
		this.mcontext = context;
		this.list_img = list_img_url;
		imageLoad = new ImageLoad(mcontext);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_img.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_img.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position,View convertView, ViewGroup parent) {
		MyHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gridview_item, null);
			holder = new MyHolder();
			holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.iv_img_del = (ImageView) convertView.findViewById(R.id.iv_img_del);
			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}
		img_url = list_img.get(position);
		imageLoad.loadImage(holder.iv_img, img_url);
		holder.iv_img_del.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
		if (position == list_img.size() - 1) {
			holder.iv_img_del.setVisibility(View.GONE);
		}

		holder.iv_img_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onImageDelCallBack(position);
			}
		});
		return convertView;
	}

	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}

	class MyHolder {
		ImageView iv_img;// 箭头
		ImageView iv_img_del;
	}


}
