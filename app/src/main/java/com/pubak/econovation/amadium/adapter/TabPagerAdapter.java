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
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                MatchListFragment matchListFragment = new MatchListFragment();
                return matchListFragment;
            case 2:
                RankListFragment rankListFragment = new RankListFragment();
                return rankListFragment;
            case 3:
                ResultListFragment resultListFragment = new ResultListFragment();
                return resultListFragment;
            case 4:
                SearchUserFragment searchUserFragment = new SearchUserFragment();
                return searchUserFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
