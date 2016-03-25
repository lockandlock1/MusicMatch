package com.example.listenandrepeat.musicandmatch;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.listenandrepeat.musicandmatch.Soundcloud.SoundcloudActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment {


    public SongFragment() {
        // Required empty public constructor
    }

    public static SongFragment newInstance(){
        SongFragment fragment = new SongFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SoundcloudActivity.class));
            }
        });

        return view;
    }


}
