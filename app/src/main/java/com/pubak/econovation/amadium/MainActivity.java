package com.pubak.econovation.amadium;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MatchListFragment()).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.menuItem_bottomBar_list:
                            selectedFragment = new MatchListFragment();
                            break;
                        case R.id.menuItem_bottomBar_ranking:
                            selectedFragment = new RankListFragment();
                            break;
                        case R.id.menuItem_bottomBar_results:
                            selectedFragment = new ResultListFragment();
                            break;
                        case R.id.menuItem_bottomBar_search:
                            selectedFragment = new SearchUserFrament();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };
}
