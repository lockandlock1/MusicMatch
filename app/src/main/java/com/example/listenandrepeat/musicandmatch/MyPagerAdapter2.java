package com.example.listenandrepeat.musicandmatch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ListenAndRepeat on 2016. 3. 2..
 */
public class MyPagerAdapter2 extends FragmentPagerAdapter {
    int mid;
    public MyPagerAdapter2(FragmentManager fm,int mid) {
        super(fm);
        this.mid  = mid;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return  StoryFragment.newInstance(mid);
        }else if(position == 1){
            return SongFragment.newInstance();
        } else{
            return VideoFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
