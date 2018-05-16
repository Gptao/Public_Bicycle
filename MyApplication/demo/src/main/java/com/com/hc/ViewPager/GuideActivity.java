package com.com.hc.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinasoft.activity.LoginActivity;
import com.chinasoft.activity.MenuActivity;
import com.example.demo.R;

import java.util.ArrayList;

/**
 * @Filename GuideActivity.java
 * @Package cn.staray.guidetest
 * @Project Guidetest
 * @Create 2014-6-12 下午2:44:23
 * @author Staray
 * @Description 引导界面
 */

public class GuideActivity extends Activity {

    // 显示导航页面的viewpager
    private ViewPager guideViewPager;

    // 页面适配器
    private ViewPagerAdapter guideViewAdapter;

    // 页面图片列表
    private ArrayList<View> mViews;

    // 图片资源，这里我们放入了3张图片，因为第四张图片，我们已经在guide_content_view.xml中加载好了
    // 一会直接添加这个文件就可以了。
    private final int images[] = {
            R.drawable.guide_page1, R.drawable.guide_page2, R.drawable.guide_page3
    };

    // 底部导航的小点
    private ImageView[] guideDots;

    // 记录当前选中的图片
    private int currentIndex;

    // 还记得我们的开始按钮吗？
    private Button startBtn;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view);
        //如果已经登陆则直接进入主界面
        sp=getSharedPreferences("msg",MODE_PRIVATE);
        if(sp.getBoolean("logined",false)){
            Intent alreadylogined=new Intent(GuideActivity.this,MenuActivity.class);
            alreadylogined.putExtra("flag","login");
            startActivity(alreadylogined);
        }
        initView();
        initDot();

        // 添加页面更换监听事件，来更新导航小点的状态。
        guideViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setCurrentDot(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // 开始按钮的点击事件监听
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 我们随便跳转一个页面
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(intent);
                GuideActivity.this.finish();
            }
        });
    }

    // 初始化页面
    private void initView() {
        guideViewPager = (ViewPager)findViewById(R.id.guide_view_pager);
        mViews = new ArrayList<View>();

        for (int i = 0; i < images.length; i++) {
            // 新建一个ImageView容器来放置我们的图片。
            ImageView iv = new ImageView(GuideActivity.this);
            iv.setBackgroundResource(images[i]);

            // 将容器添加到图片列表中
            mViews.add(iv);
        }

        // 上面添加了三张图片了，还有一张放在guide_content_view.xml中，我们把这个页面也添加进来。
        View view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.guide_content_view,
                null);
        mViews.add(view);

        // 现在为我们的开始按钮找到对应的控件
        startBtn = (Button)view.findViewById(R.id.start_btn);

        // 现在用到我们的页面适配器了
        guideViewAdapter = new ViewPagerAdapter(mViews);
        guideViewPager.setAdapter(guideViewAdapter);
    }

    // 初始化导航小点
    private void initDot() {
        // 找到放置小点的布局
        LinearLayout layout = (LinearLayout)findViewById(R.id.guide_dots);

        // 初始化小点数组
        guideDots = new ImageView[mViews.size()];

        // 循环取得小点图片，让每个小点都处于正常状态
        for (int i = 0; i < mViews.size(); i++) {
            guideDots[i] = (ImageView)layout.getChildAt(i);
            guideDots[i].setSelected(false);
        }

        // 初始化第一个小点为选中状态
        currentIndex = 0;
        guideDots[currentIndex].setSelected(true);
    }

    // 页面更换时，更新小点状态
    private void setCurrentDot(int position) {
        if (position < 0 || position > mViews.size() - 1 || currentIndex == position) {
            return;
        }
        guideDots[position].setSelected(true);
        guideDots[currentIndex].setSelected(false);
        currentIndex = position;
    }
}
