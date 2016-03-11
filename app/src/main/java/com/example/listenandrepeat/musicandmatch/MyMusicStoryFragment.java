package com.example.listenandrepeat.musicandmatch;


import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.listenandrepeat.musicandmatch.R;

/**
 * A simple {@link Fragment} subclass.
 */


public class MyMusicStoryFragment extends Fragment {



    public MyMusicStoryFragment() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager pager;
    MyPagerAdapter2 mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_music_story, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.maintab_layout);
        pager = (ViewPager)view.findViewById(R.id.pager);
        mAdapter = new MyPagerAdapter2(getChildFragmentManager());
        pager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.removeAllTabs();
        for (int i = 0 ; i < 3 ; i ++){
            tabLayout.addTab(tabLayout.newTab().setText("tab" + i));
        }
        // Inflate the layout for this fragment
        return view;
    }


}
