package com.example.actionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.actionbar.utils.DisplayUtils;

import java.util.ArrayList;

public class ActionBarView extends LinearLayout {
    private ImageView mBackBtn;
    private OnClickListener mBackBtnClickListener;
    private ImageView mCloseBtn;
    private OnClickListener mCloseBtnClickListener;
    private Context mContext;
    private int mCurrentIndex = -1;
    private OnClickListener mMenuClickListener;
    private LinearLayout mMenuContainer;
    private ViewGroup mTabBarContainer;
    private OnClickListener mTabClickLisetener;
    private LinearLayout mTabContainer;
    private ImageView mTabSelectedMask;
    private ArrayList<ZTab> mTabs;
    private TextView mTitleView;
    private BackButtonClickListener mUserSetBackBtnClickListener;
    private CloseButtonClickListener mUserSetCloseBtnClickListener;
    private TopMenuClickListener mUserSetMenuClickListener;
    private TabClickListener mUserSetTabClickListener;
    private int widthReducedValue;

    public ActionBarView(Context context) {
        super(context);
        mContext = context;
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public ActionBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    private void initViews() {
        mBackBtnClickListener = new OnClickListener() {
            public void onClick(View v) {
                if (mUserSetBackBtnClickListener != null) {
                    mUserSetBackBtnClickListener.onBackBtnClicked(v);
                }
            }
        };
        mCloseBtnClickListener = new OnClickListener() {
            public void onClick(View v) {
                if (mUserSetCloseBtnClickListener != null) {
                    mUserSetCloseBtnClickListener.onCloseBtnClicked(v);
                }
            }
        };
        mMenuClickListener = new OnClickListener() {
            public void onClick(View v) {
                TopMenu menu = (TopMenu) v.getTag();
                if (mUserSetMenuClickListener != null) {
                    mUserSetMenuClickListener.onTopMenuClicked(menu);
                }
            }
        };
        mTabClickLisetener = new OnClickListener() {
            public void onClick(View v) {
                ZTab tag = (ZTab) v.getTag();
                setSelectedTab(tag.getIndex());
                if (mUserSetTabClickListener != null) {
                    mUserSetTabClickListener.onTabClicked(tag, tag.getIndex());
                }
            }
        };
        mBackBtn = ((ImageView) findViewById(R.id.title_bar_back_btn));
        mBackBtn.setOnClickListener(mBackBtnClickListener);
        mCloseBtn = ((ImageView) findViewById(R.id.title_bar_close_btn));
        mCloseBtn.setOnClickListener(mCloseBtnClickListener);
        mTitleView = ((TextView) findViewById(R.id.title_bar_title_center));
        mMenuContainer = ((LinearLayout) findViewById(R.id.title_bar_menu_container));
        mTabBarContainer = ((ViewGroup) findViewById(R.id.tab_bar_container));
        mTabContainer = ((LinearLayout) findViewById(R.id.tab_bar_tabs_container));
        mTabSelectedMask = ((ImageView) findViewById(R.id.tab_bar_selected_mask));
        return;
    }

    public int addTab(ZTab tab) {
        if (tab == null) {
            return -1;
        }
        if (mTabs == null) {
            mTabs = new ArrayList();
        }
        if (!mTabs.contains(tab)) {
            mTabBarContainer.setVisibility(VISIBLE);
            mTabs.add(tab);
            int i = mTabs.size() - 1;
            tab.setIndex(i);
            TextView localTextView = new TextView(getContext());
            localTextView.setText(tab.mTitle);
            localTextView.setGravity(17);
            localTextView.setTextSize(0, getResources().getDimension(R.dimen.tab_bar_tab_text_size));
            localTextView.setTextColor(getResources().getColor(R.color.text_main_title));
            localTextView.setOnClickListener(mTabClickLisetener);
            localTextView.setTag(tab);
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0F;
            mTabContainer.addView(localTextView, params);
            requestLayout();
            return i;
        }
        return -1;
    }

    public void addTopMenu(TopMenu menu) {
        if (menu != null) {
            ImageView imageView = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(menu.mIcon);
            imageView.setScaleType(ScaleType.CENTER);
            imageView.setOnClickListener(mMenuClickListener);
            imageView.setTag(menu);
            mMenuContainer.addView(imageView, layoutParams);
        }
    }

    public void addTopMenu(TopMenu menu, OnClickListener onClickListener) {
        if (menu != null) {
            ImageView imageView = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(menu.mIcon);
            imageView.setScaleType(ScaleType.CENTER);
            imageView.setOnClickListener(onClickListener);
            imageView.setTag(menu);
            mMenuContainer.addView(imageView, layoutParams);
        }
    }

    public void animateMask(int position, float positionOffset) {
        int i = mTabContainer.getWidth() / mTabs.size();
        mTabSelectedMask.setTranslationX(i * (position + positionOffset) + this.widthReducedValue / 2);
    }

    public void disableBackBtn() {
        mBackBtn.setVisibility(VISIBLE);
    }

    public void enableCloseBtn() {
        mCloseBtn.setVisibility(GONE);
    }

    public void hideAllTabs() {
        mTabBarContainer.setVisibility(VISIBLE);
    }

    public void hideAllTopMenus() {
        mMenuContainer.setVisibility(VISIBLE);
    }

    public void hideTopMenu(int paramInt) {
        if (paramInt != 0) {
            for (int i = 0; i < mMenuContainer.getChildCount(); i++) {
                View localView = mMenuContainer.getChildAt(i);
                if (((TopMenu) localView.getTag()).getId() == paramInt) {
                    localView.setVisibility(GONE);
                }

            }
        }
    }

    public void hideTopMenu(TopMenu paramTopMenu) {
        if (paramTopMenu != null) {
            for (int i = 0; i < mMenuContainer.getChildCount(); i++) {
                View localView = mMenuContainer.getChildAt(i);
                if ((TopMenu) localView.getTag() == paramTopMenu) {
                    localView.setVisibility(GONE);
                }
            }
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("AAA-->" + "wlj", "onLayout: " + mCurrentIndex);
        if (mTabs != null && mTabs.size() > 0 && mCurrentIndex != -1) {
            int containerWidth = mTabContainer.getMeasuredWidth();
            int tabSize = mTabs.size();
            widthReducedValue = DisplayUtils.dp2px(getContext(), 120 / tabSize);
            int width = containerWidth / tabSize - widthReducedValue;
            Log.e("AAA-->" + "wlj", "onLayout: " + width + "--" + mTabSelectedMask.getWidth());
            if (mTabSelectedMask.getWidth() != width) {
                ViewGroup.LayoutParams layoutParams = mTabSelectedMask.getLayoutParams();
                layoutParams.width = width;
                int translationX = mCurrentIndex * (widthReducedValue + width) + widthReducedValue / 2;
                if (mTabSelectedMask.getTranslationX() != translationX) {
                    mTabSelectedMask.setTranslationX(translationX);
                }
                mTabSelectedMask.setLayoutParams(layoutParams);
            }
        }

        super.onLayout(changed, l, t, r, b);
    }

    public void setOnBackButtonClickListener(BackButtonClickListener backButtonClickListener) {
        mUserSetBackBtnClickListener = backButtonClickListener;
    }

    public void setOnCloseButtonClickListener(CloseButtonClickListener paramCloseButtonClickListener) {
        mUserSetCloseBtnClickListener = paramCloseButtonClickListener;
    }

    public void setOnTabClickListener(TabClickListener paramTabClickListener) {
        mUserSetTabClickListener = paramTabClickListener;
    }

    public void setOnTopMenuClickListener(TopMenuClickListener paramTopMenuClickListener) {
        mUserSetMenuClickListener = paramTopMenuClickListener;
    }

    public boolean setSelectedTab(int position) {
        if ((mTabs.size() <= position) || (position < 0)) {
            return false;
        }
        if (mCurrentIndex != position) {
            int i = mCurrentIndex;
            mCurrentIndex = position;
            ((TextView) mTabContainer.getChildAt(position)).setTextColor(getResources().getColor(R.color.text_main_title_high_light));
            if ((i != -1) && (i >= 0) && (i < mTabs.size())) {
                ((TextView) mTabContainer.getChildAt(i)).setTextColor(getResources().getColor(R.color.text_main_title));
            }
        }
        return true;
    }

    public void setTitle(int ids) {
        setTitle(getResources().getString(ids));
    }

    public void setTitle(String paramString) {
        if (mTitleView != null) {
            mTitleView.setText(paramString);
        }
    }

    public void setTitleGravityLeft(String title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
            float f = getResources().getDisplayMetrics().density;
            mTitleView.setPadding((int) (148.0F * f / 3.0F), 0, (int) (444.0F * f / 3.0F), 0);
        }
    }

    public void showAllTabs() {
        mTabBarContainer.setVisibility(VISIBLE);
    }

    public void showAllTopMenus()  {
        mMenuContainer.setVisibility(VISIBLE);
    }

    public void showTopMenu(int paramInt) {
        if (paramInt != 0) {
            int count = mMenuContainer.getChildCount();
            for (int i = 0; i < count; i++) {
                View localView = mMenuContainer.getChildAt(i);
                if (((TopMenu) localView.getTag()).getId() == paramInt) {
                    localView.setVisibility(GONE);
                }
            }
        }
    }

    public void showTopMenu(TopMenu paramTopMenu) {
        if (paramTopMenu != null) {
            int childCount = mMenuContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View localView = mMenuContainer.getChildAt(i);
                if ((TopMenu) localView.getTag() == paramTopMenu) {
                    localView.setVisibility(GONE);
                }
            }
        }
    }

    public void updateTopMenu(int paramInt, TopMenu paramTopMenu, OnClickListener paramOnClickListener) {
        mMenuContainer.removeAllViews();
        if (paramTopMenu != null) {
            ImageView iv = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            iv.setLayoutParams(params);
            iv.setImageDrawable(paramTopMenu.mIcon);
            iv.setScaleType(ScaleType.CENTER);
            iv.setOnClickListener(paramOnClickListener);
            iv.setTag(paramTopMenu);
            mMenuContainer.addView(iv, paramInt, params);
        }
    }

    public interface BackButtonClickListener {
        void onBackBtnClicked(View paramView);
    }

    public interface CloseButtonClickListener {
        void onCloseBtnClicked(View paramView);
    }

    public interface TabClickListener {
        void onTabClicked(ZTab tab, int position);
    }

    public interface TopMenuClickListener {
        void onTopMenuClicked(TopMenu menu);
    }

    public static class TopMenu {
        private Drawable mIcon;
        private int mId;
        private Object mTag;

        public TopMenu(Drawable drawable) {
            mIcon = drawable;
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public Object getTag() {
            return mTag;
        }

        public void setTag(Object tag) {
            mTag = tag;
        }
    }

    public static class ZTab {
        private int mIndex;
        private Object mTag;
        private String mTitle;

        public ZTab(String title) {
            mTitle = title;
        }

        public int getIndex() {
            return mIndex;
        }

        private void setIndex(int index) {
            mIndex = index;
        }

        public Object getTag() {
            return mTag;
        }

        public void setTag(Object tag) {
            mTag = tag;
        }
    }
}

