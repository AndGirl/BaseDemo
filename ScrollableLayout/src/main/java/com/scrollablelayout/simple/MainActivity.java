package com.scrollablelayout.simple;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scrollablelayout.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, PtrHandler, View.OnClickListener {
    private ScrollableLayout sl_root;
    private ViewPager vp_scroll;
    private ImageView iv_title;
    private TextView tv_right;
    private ImageView iv_spit;
    private TextView tv_signature;
    private RelativeLayout ly_page1;
    private TextView tv_page1;
    private RelativeLayout ly_page2;
    private TextView tv_page2;
    private int [] ids = {R.drawable.finger_1,R.drawable.finger_2,R.drawable.finger_3};

    //ces
    public static final int MAX = Integer.MAX_VALUE;
    private ArrayList<ImageView> mImageViews;
    private NoPreloadViewPager mViewPager;
    private LinearLayout ll_group_point;
    //判断当前Activity是否销毁
    private boolean isActivityDestroy = false;
    //判断是否为拖拽状态
    private boolean isDragging = false;

    /**
     * 上一次被高亮显示的位置
     */
    private int lastIndex;
    //ces

    private final List<BaseFragment> fragmentList = new ArrayList<>();
    private FrameLayout rl_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initBanner();

    }

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int item = mViewPager.getCurrentItem()+1;
            mViewPager.setCurrentItem(item);

            if(!isActivityDestroy){
                handler.sendEmptyMessageDelayed(0, 3000);
            }

        };
    };

    private void initBanner() {
        mImageViews = new ArrayList<ImageView>();
        for (int i = 0 ; i < ids.length ; i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            mImageViews.add(imageView);
            //有多少个页面就创建多少个点
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i != 0){
                params.leftMargin = 10;
                point.setEnabled(false);
            }

            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point_selector);
            //添加指示点
            ll_group_point.addView(point);
        }

        MyAdapter adapter = new MyAdapter();
        mViewPager.setAdapter(adapter);
        int item = Integer.MAX_VALUE/2 - (Integer.MAX_VALUE/2)% mImageViews.size();
        mViewPager.setCurrentItem(item );
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        //开始循环滑动
        handler.sendEmptyMessageDelayed(0, 3000);

    }

    private class MyPageChangeListener implements NoPreloadViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int nowPosition = position % mImageViews.size();
            ll_group_point.getChildAt(nowPosition).setEnabled(true);
            ll_group_point.getChildAt(lastIndex).setEnabled(false);
            lastIndex = nowPosition;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //  如果当前是拖拽状态
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                handler.removeMessages(0);
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                //  如果当前是空闲状态
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
                //  如果当前是滑动状态
                isDragging = false;
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    }

    class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = mImageViews.get(position % mImageViews.size());
//            ViewGroup parent = (ViewGroup) imageView.getParent();
//            if(parent != null) {
//                parent.removeAllViews();
//            }
            container.addView(imageView);

            //  对imageview进行触摸和点击监听
            imageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeMessages(0);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.sendEmptyMessageDelayed(0, 3000);
                            break;
                    }
                    return false;
                }
            });

            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,position + "", Toast.LENGTH_SHORT).show();
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void initView() {
        sl_root = (ScrollableLayout) findViewById(R.id.sl_root);
        vp_scroll = (ViewPager) findViewById(R.id.vp_scroll);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        iv_spit = (ImageView) findViewById(R.id.iv_spit);
        tv_signature = (TextView) findViewById(R.id.tv_signature);

        rl_view = (FrameLayout) findViewById(R.id.rl_view);
        mViewPager = (NoPreloadViewPager)findViewById(R.id.viewPager);
        ll_group_point = (LinearLayout)findViewById(R.id.ll_group_point);

        ly_page1 = (RelativeLayout) findViewById(R.id.ly_page1);
        tv_page1 = (TextView) findViewById(R.id.tv_page1);
        ly_page2 = (RelativeLayout) findViewById(R.id.ly_page2);
        tv_page2 = (TextView) findViewById(R.id.tv_page2);

        iv_spit.setVisibility(View.GONE);
        sl_root.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @SuppressLint("NewApi")
            @Override
            public void onScroll(int translationY, int maxY) {
                translationY = -translationY;
                int alpha = 0;
                alpha = Math.min(255, (int) (Math.abs(translationY) * (255) / rl_view.getHeight()));
                if(alpha >= 250) {
                    iv_title.setBackground(getResources().getDrawable(R.mipmap.add_yellow));
                }else{
                    iv_title.setBackground(getResources().getDrawable(R.mipmap.add_white));
                }
            }
        });

        CommonFragementPagerAdapter commonFragementPagerAdapter = new CommonFragementPagerAdapter(getSupportFragmentManager());
        fragmentList.add(RecyclerViewSimpleFragment.newInstance());
        fragmentList.add(RecyclerViewGridSimpleFragment.newInstance());
        vp_scroll.setAdapter(commonFragementPagerAdapter);
        vp_scroll.addOnPageChangeListener(this);
        sl_root.getHelper().setCurrentScrollableContainer(fragmentList.get(0));

        tv_right.setOnClickListener(this);
//        tv_signature.setOnClickListener(this);
        ly_page1.setOnClickListener(this);
        ly_page2.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        sl_root.getHelper().setCurrentScrollableContainer(fragmentList.get(position));
        if (position == 0) {
            ly_page1.setBackgroundResource(R.drawable.rectangle_left_select);
            tv_page1.setTextColor(Color.parseColor("#ffffff"));
            ly_page2.setBackgroundResource(R.drawable.rectangle_right);
            tv_page2.setTextColor(Color.parseColor("#435356"));
        } else {
            ly_page1.setBackgroundResource(R.drawable.rectangle_left);
            tv_page1.setTextColor(Color.parseColor("#435356"));
            ly_page2.setBackgroundResource(R.drawable.rectangle_right_select);
            tv_page2.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (vp_scroll.getCurrentItem() == 0 && sl_root.isCanPullToRefresh()) {
            return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
        }
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (fragmentList.size() > vp_scroll.getCurrentItem()) {
            fragmentList.get(vp_scroll.getCurrentItem()).pullToRefresh();
        }
    }

    public void refreshComplete() {
//        if (pfl_root != null) {
//            pfl_root.refreshComplete();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signature:
                Uri uri = Uri.parse(tv_signature.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.tv_right:
                startActivity(new Intent(MainActivity.this, TestActivity.class));
                break;
            case R.id.ly_page1:
                vp_scroll.setCurrentItem(0);
                break;
            case R.id.ly_page2:
                vp_scroll.setCurrentItem(1);
                break;
        }
    }

    public class CommonFragementPagerAdapter extends FragmentPagerAdapter {

        public CommonFragementPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getCount() > position ? fragmentList.get(position) : null;
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

}
