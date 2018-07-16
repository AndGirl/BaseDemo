package com.atsgg.mycustomview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * 根据页面改变设置文本  在onPageSelected()中
 * 添加指示点  selector、代码添加view
 * 支持左右无限滑动   设置count数目为最大数值
 * 自动滑动页面  handler消息机制
 * 当手滑动或者按下的时候停止滑动  onPageScrollStateChanged()与onTouch()
 * 添加点击事件 return false;
 */

public class MainActivity extends AppCompatActivity {

    private final int[] imageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c};
    private BannerViewPager mViewPager;
    private LinearLayout mLlGroupPoints;
    private ArrayList<ImageView> mImageViews;
    private MyBannerAdapter mAdapter;
    private int lastPosition = 1;
    private int currentItem;
    private WeakHandler handler = new WeakHandler();
    private long delayTime = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initBanner();
        initViewPager();

    }

    private void initViewPager() {
        startAutoPlay();
        if (mAdapter == null) {
            mAdapter = new MyBannerAdapter();
        }
        mViewPager.setAdapter(mAdapter);
        //默认从1开始
        mViewPager.setFocusable(true);
        mViewPager.setCurrentItem(1);
        mLlGroupPoints.getChildAt(0).setEnabled(true);
        //控制指示器跟VP的展示
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setScrollable(true);

    }

    private void initBanner() {
        mImageViews = new ArrayList<>();
        //构造数据头放最末尾数据，尾端放起始数据
        for (int i = 0; i <= imageIds.length + 1; i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0)
                imageView.setBackgroundResource(imageIds[imageIds.length - 1]);
            else if (i == imageIds.length + 1)
                imageView.setBackgroundResource(imageIds[0]);
            else
                imageView.setBackgroundResource(imageIds[i - 1]);
            mImageViews.add(imageView);
        }

        //构造指示器
        for (int i = 0; i < imageIds.length; i++) {
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 8), DensityUtil.dip2px(this, 8));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(this, 8);
                point.setEnabled(false);
            }
            point.setLayoutParams(params);
            mLlGroupPoints.addView(point);
        }
    }

    private void initView() {
        mViewPager = (BannerViewPager) findViewById(R.id.vp_main_image);
        mLlGroupPoints = (LinearLayout) findViewById(R.id.ll_main_point);
    }

    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    class MyBannerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.e("TAG", "position : " + position);
            mLlGroupPoints.getChildAt((lastPosition - 1 + imageIds.length) % imageIds.length).setEnabled(false);
            mLlGroupPoints.getChildAt((position - 1 + imageIds.length) % imageIds.length).setEnabled(true);
            lastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            currentItem = mViewPager.getCurrentItem();
            switch (state) {
                case 0://No operation
                    if (currentItem == 0) {
                        mViewPager.setCurrentItem(imageIds.length, false);
                    } else if (currentItem == imageIds.length + 1) {
                        mViewPager.setCurrentItem(1, false);
                    }
                    break;
                case 1://start Sliding
                    if (currentItem == imageIds.length + 1) {
                        mViewPager.setCurrentItem(1, false);
                    } else if (currentItem == 0) {
                        mViewPager.setCurrentItem(imageIds.length, false);
                    }
                    break;
                case 2://end Sliding
                    break;
            }
        }
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (imageIds.length > 1) {
                Log.e("run", "currentItem : " + currentItem);
                currentItem = currentItem % (imageIds.length + 1) + 1;
                if (currentItem == 1) {
                    mViewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    mViewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {
            startAutoPlay();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stopAutoPlay();
        }
        return super.dispatchTouchEvent(event);
    }
}
