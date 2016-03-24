package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.MainActivity;
import com.example.listenandrepeat.musicandmatch.R;

import java.util.Random;

public class SoundcloudActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapterSC mAdapter;
    RecyclerView.LayoutManager layoutManager;

    int[] IDS = { R.drawable.mark_genre,
            R.drawable.mark_genre_balled,
            R.drawable.mark_genre_dance,
            R.drawable.mark_genre_ellec,
            R.drawable.mark_genre_hiphop,
            R.drawable.mark_genre_indi,
            R.drawable.mark_genre_rb,
            R.drawable.mark_genre_rock,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundcloud);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        mAdapter = new MyAdapterSC();
        mAdapter.setOnItemClickListener(new OnItemClickListenerSC() {
            @Override
            public void onItemClick(View view, int position) {
                MyDataSC data = mAdapter.getItem(position);
                Toast.makeText(SoundcloudActivity.this, "data : " + data.title, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
//        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);


//        recyclerView.addItemDecoration(new MyDecoration(this));

        initData();
    }

    private void initData() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            MyDataSC data = new MyDataSC();
            data.icon = getResources().getDrawable(IDS[i%IDS.length]);
            data.title = "item " + i;
            data.fontSize = 20 + r.nextInt(40);
            mAdapter.add(data);
        }
    }
}
