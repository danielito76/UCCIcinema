package com.example.cinemaprovafragment;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.cinemaprovafragment.Adapter.CustomPagerAdapter;
import com.example.cinemaprovafragment.Connection.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity implements BottomNavigationView
                   .OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    MenuItem previousMenuItem;
    ViewPager2 viewPager;
    TokenManager tokenManager;
    CustomPagerAdapter adapter;
    Toolbar topAppBar, loggedTopAppBar;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        initializeValues();
        configureHeader(tokenManager,constraintLayout);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                if (previousMenuItem != null) {
                    previousMenuItem.setChecked(false);
                }
                else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                previousMenuItem = bottomNavigationView.getMenu().getItem(position);

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void bindViews() {
        constraintLayout = findViewById(R.id.parent_id);
        topAppBar = findViewById(R.id.topAppBar);
        loggedTopAppBar = findViewById(R.id.topAppBar_logged);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager=findViewById(R.id.pager);
    }
    private void initializeValues() {
        tokenManager = new TokenManager(this);
        adapter=new CustomPagerAdapter(this, 5);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        viewPager.setLayoutDirection(ViewPager2.LAYOUT_DIRECTION_INHERIT);
        viewPager.setPageTransformer(new DepthPageTransformer());
        //viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.cinema:
                viewPager.setCurrentItem(1);
                break;

            case R.id.film:
                viewPager.setCurrentItem(2);
                break;

            case R.id.preferiti:
                viewPager.setCurrentItem(3);
                break;
            case R.id.gioco:
                viewPager.setCurrentItem(4);
                break;
        }
        return false;
    }

    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
    private class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                // Move it behind the left page
                view.setTranslationZ(-1f);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}
