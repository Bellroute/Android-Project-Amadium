package com.pubak.econovation.amadium;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView userName;
    private static String userEmail;
    private static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.user_view);
        userName.setText(userEmail.substring(0, userEmail.lastIndexOf("@")));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
    }

    // LoginActivity 에서 사용자 정보 받아오는 메소드
    public static void getCurrentUser(FirebaseUser user) {
        userEmail = user.getEmail();
        userId = user.getUid();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.menuItem_bottomBar_profile:
                            selectedFragment = new ProfileFragment();
                            break;
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
                            selectedFragment = new SearchUserFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };
}
