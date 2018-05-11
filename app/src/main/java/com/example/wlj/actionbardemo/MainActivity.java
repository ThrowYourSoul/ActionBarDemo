package com.example.wlj.actionbardemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.actionbar.ActionBarView;

public class MainActivity extends AppCompatActivity {

    private ActionBarView actionBar;
    private ViewPager pager;

    private String[] titles = new String[]{"第一个", "第二个", "第三个"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                actionBar.animateMask(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.requestLayout();
        Log.e("AAA-->" + "wlj", "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        actionBar = ((ActionBarView) findViewById(R.id.ts_action_bar));
        pager = ((ViewPager) findViewById(R.id.main_viewpager));

        for (int i = 0; i < titles.length; i++) {
            actionBar.addTab(new ActionBarView.ZTab(titles[i]));
        }
        ActionBarView.TopMenu menu = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            menu = new ActionBarView.TopMenu(getDrawable(R.drawable.actionbar_download));
        }
        actionBar.addTopMenu(menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "下载", Toast.LENGTH_SHORT).show();
            }
        });
        actionBar.setOnTabClickListener(new ActionBarView.TabClickListener() {
            @Override
            public void onTabClicked(ActionBarView.ZTab tab, int position) {
                pager.setCurrentItem(position);
            }
        });
        actionBar.setTitle("标题");
        actionBar.setSelectedTab(0);
        actionBar.setOnBackButtonClickListener(new ActionBarView.BackButtonClickListener() {
            @Override
            public void onBackBtnClicked(View paramView) {
                Toast.makeText(MainActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });
        pager.setAdapter(new ThemePagerAdapter());
    }


    public class ThemePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView iv = new ImageView(MainActivity.this);
            iv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            );
            iv.setImageResource(R.drawable.ic_launcher_background);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
