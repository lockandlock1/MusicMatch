package com.example.listenandrepeat.musicandmatch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ListenAndRepeat on 2016. 2. 23..
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0) {
            return AllFragment.newInstance();
        }else {

            return MatchingFragment.newInstance();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "tab" + position;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
