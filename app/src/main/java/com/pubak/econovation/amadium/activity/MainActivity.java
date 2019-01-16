package com.pubak.econovation.amadium.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pubak.econovation.amadium.adapter.TabPagerAdapter;
import com.pubak.econovation.amadium.R;

public class MainActivity extends AppCompatActivity {
    private TextView pageName;
    private static FirebaseUser user;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView findPlayer;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageName = findViewById(R.id.textView_page_name);
        pageName.setText("프로필");

        user = FirebaseAuth.getInstance().getCurrentUser();

        findPlayer = findViewById(R.id.textView_find_user);
        findPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindUserActivity.class);
                startActivity(intent);
            }
        });

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
                switch (tab.getPosition()) {
                    case 0:
                        pageName.setText("프로필");
                        break;
                    case 1:
                        pageName.setText("매치 리스트");
                        break;
                    case 2:
                        pageName.setText("랭킹");
                        break;
                    case 3:
                        pageName.setText("근처 플레이어");
                        break;
                }
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
    public static FirebaseUser getCurrentUser() {
        return user;
    }
}
