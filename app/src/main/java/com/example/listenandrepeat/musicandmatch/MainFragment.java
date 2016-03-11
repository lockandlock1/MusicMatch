package com.example.listenandrepeat.musicandmatch;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager pager;
    MyPagerAdapter mAdapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.maintab_layout);
        pager = (ViewPager)view.findViewById(R.id.pager);
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(mAdapter);

        tabLayout.setupWithViewPager(pager);
        tabLayout.removeAllTabs();
        for (int i = 0 ; i < 2 ; i ++){
            tabLayout.addTab(tabLayout.newTab().setText("tab" + i));
        }
        return view;
    }


}
