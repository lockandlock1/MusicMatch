package com.example.listenandrepeat.musicandmatch;


import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;
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
    ImageView imageView;

    int mid = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();

        if (b != null) {
            mid = b.getInt("mid", -1);

        }
        if (mid == -1) {
            mid = PropertyManager.getInstance().getMid();

        }


       // Toast.makeText(getContext(),"MymusicFrag"+mid,Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_music_story, container, false);



        tabLayout = (TabLayout)view.findViewById(R.id.maintab_layout);
        pager = (ViewPager)view.findViewById(R.id.pager);
        mAdapter = new MyPagerAdapter2(getChildFragmentManager(),mid);
        pager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.removeAllTabs();
        for (int i = 0 ; i < 3 ; i ++){
            if(i == 0){
                imageView = (ImageView)inflater.inflate(R.layout.my_story_tab1,null);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.my_story_tab1));

            } else if(i == 1){
                imageView = (ImageView)inflater.inflate(R.layout.my_story_tab2,null);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.my_story_tab2));
            } else {
                imageView = (ImageView)inflater.inflate(R.layout.my_story_tab3,null);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.my_story_tab3));
            }

        }
        // Inflate the layout for this fragment
        return view;
    }


}
