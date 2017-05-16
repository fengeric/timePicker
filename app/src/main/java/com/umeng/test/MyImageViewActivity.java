package com.umeng.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.test.util.ImageLoad;
import com.umeng.test.util.LogUtil;
import com.umeng.test.util.Util;
import com.umeng.test.view.TouchImageView;

import java.util.ArrayList;

/**@title MyImageViewActivity 查看图片页面
 * @author Eric
 * @date 2017/1/17
 **/
public class MyImageViewActivity extends Activity implements
        OnPageChangeListener {

    private ViewPager viewPager;// viewpager实现左右滑动图片
    private ImageView[] tips;// 点点
    private TouchImageView[] mImageViews; // 装ImageView数组
    private ArrayList<String> orgUrl = new ArrayList<String>();// 图片url集合
    private ImageLoad imageLoad;// 加载图片公共类
    private int position = 0;
    private TextView head_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_image_layout);// 布局

        Intent in = getIntent();
        orgUrl = in.getExtras().getStringArrayList("list");// 接受从上一个页面传来的图片地址集合
        position = in.getExtras().getInt("pos");// 从上一个页传过来的位置
        if (orgUrl == null || orgUrl.size() == 0) {
            finish();
        }
        imageLoad = new ImageLoad(getApplicationContext());// 实例化imageload并且传默认图片给
        // changeHeader();// 更改头部标题
        init();// 实例化
    }

    private void init() {
        try {
            head_title = (TextView) findViewById(R.id.tv_head_right);
            setTitleText();
            ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            // 将点点加入到ViewGroup中
            tips = new ImageView[orgUrl.size()];
            for (int i = 0; i < tips.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LayoutParams(10, 10));
                tips[i] = imageView;
                if (i == 0) {
                    tips[i].setBackgroundResource(R.drawable.banner_dian_focus);
                } else {
                    tips[i].setBackgroundResource(R.drawable.banner_dian_blur);
                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        new LayoutParams(LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT));
                layoutParams.leftMargin = 5;
                layoutParams.bottomMargin = 100;
                layoutParams.rightMargin = 5;
                group.addView(imageView, layoutParams);
            }

            // 将图片装载到数组中
            mImageViews = new TouchImageView[orgUrl.size()];
            for (int i = 0; i < mImageViews.length; i++) {
                TouchImageView imageView = new TouchImageView(this);

                mImageViews[i] = imageView;

                imageLoad.loadImage(imageView, orgUrl.get(i));
            }

            // 设置Adapter
            viewPager.setAdapter(new MyAdapter());
            // 设置监听，主要是设置点点的背景
            viewPager.setOnPageChangeListener(this);
            // 设置ViewPager的默认项
            viewPager.setCurrentItem(position);
        } catch (Exception e) {
            LogUtil.e(getClass(), "init()", e);
        }
    }

    private void setTitleText() {
        try {
            if (head_title != null && orgUrl != null && orgUrl.size() > 0) {
                head_title.setText((position + 1) + "/" + orgUrl.size());
            }
        } catch (Exception e) {
            LogUtil.e(getClass(), "setTitleText", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyImageViewActivity.this.finish();
            Util.ActivityExit(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return orgUrl.size();

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;

        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            /**
             * 当只有3张图片或者2张图片的时候，滑动存在BUG问题的修改如下destroyItem方法中不removeView
             */
            // ((ViewPager) container).removeView(mImageViews[position
            // % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */

        @Override
        public Object instantiateItem(View container, int position) {
            try {
                ((ViewPager) container).addView(mImageViews[position
                        % mImageViews.length], 0);
            } catch (Exception e) {
                LogUtil.e(getClass(), "instantiateItem()", e);
            }
            return mImageViews[position % mImageViews.length];

        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position % mImageViews.length);
        this.position = position;
        setTitleText();
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */

    private void setImageBackground(int selectItems) {

        for (int i = 0; i < tips.length; i++) {

            if (i == selectItems) {

                tips[i].setBackgroundResource(R.drawable.banner_dian_focus);

            } else {

                tips[i].setBackgroundResource(R.drawable.banner_dian_blur);

            }

        }

    }

}
