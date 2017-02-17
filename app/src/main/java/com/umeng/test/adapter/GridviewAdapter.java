package com.umeng.test.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.umeng.test.R;
import com.umeng.test.Util.ImageLoad;

import java.util.ArrayList;

public class GridviewAdapter extends BaseAdapter {
	private Context mcontext;
	private ArrayList<String> list_img;
	private LayoutInflater inflater;
	private ImageLoad imageLoad;
	private String img_url ;

	public GridviewAdapter(Context context, ArrayList<String> list_img_url) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gridview_item, null);
			holder = new MyHolder();
			holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}
		img_url = list_img.get(position);
		imageLoad.loadImage(holder.iv_img, img_url);
		return convertView;
	}

	class MyHolder {
		ImageView iv_img;// 箭头
	}
}
