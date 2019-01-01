package com.pubak.econovation.amadium.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.pubak.econovation.amadium.fragment.MatchListFragment;
import com.pubak.econovation.amadium.fragment.ProfileFragment;
import com.pubak.econovation.amadium.fragment.RankListFragment;
import com.pubak.econovation.amadium.fragment.ResultListFragment;
import com.pubak.econovation.amadium.fragment.SearchUserFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public TabPagerAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ProfileFragment();
            case 1:
                return new MatchListFragment();
            case 2:
                return new RankListFragment();
            case 3:
                return new ResultListFragment();
            case 4:
                return new SearchUserFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
