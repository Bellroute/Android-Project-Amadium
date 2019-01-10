package com.pubak.econovation.amadium.activity;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.pubak.econovation.amadium.adapter.TabPagerAdapter;
import com.pubak.econovation.amadium.R;

public class MainActivity extends AppCompatActivity {
    private TextView pageName;
    private static String userEmail;
    private static String userId;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageName = findViewById(R.id.user_view);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        buildTabLayout(tabLayout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void buildTabLayout(TabLayout tabLayout) {
        this.tabLayout.addTab(this.tabLayout.newTab().setIcon(R.drawable.ic_user));
        this.tabLayout.addTab(this.tabLayout.newTab().setIcon(R.drawable.ic_list));
        this.tabLayout.addTab(this.tabLayout.newTab().setIcon(R.drawable.ic_ranking));
        this.tabLayout.addTab(this.tabLayout.newTab().setIcon(R.drawable.ic_search));
        this.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    // LoginActivity 에서 사용자 정보 받아오는 메소드
    public static void getCurrentUser(FirebaseUser user) {
        userEmail = user.getEmail();
        userId = user.getUid();
    }
}
