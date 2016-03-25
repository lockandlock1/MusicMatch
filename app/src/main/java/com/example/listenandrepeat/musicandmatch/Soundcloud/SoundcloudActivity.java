package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Request;

public class SoundcloudActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapterSC mAdapter;
    RecyclerView.LayoutManager layoutManager;
    int mid;

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
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(SoundcloudActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setOnAdapterItemClickListener(new MyAdapterSC.OnViewHolderAdapterItemClickListener() {
            @Override
            public void onAdapterItemLayoutClick(MyAdapterSC adapter, View view, SCTrackInfoData item, int position) {

                Intent intent = new Intent(getApplication(), SoundcloudPlayActivity.class);
                int PostId = adapter.items.get(position).id;
                intent.putExtra(SoundcloudPlayActivity.PARAM_POST_ID, PostId);
                startActivity(intent);

                Toast.makeText(SoundcloudActivity.this, "layout Click :", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplication(), SoundcloudPlayActivity.class));
            }

            @Override
            public void onAdpaterItemLikeImageClick(MyAdapterSC adapter, View view, SCTrackInfoData item, int position) {
                Toast.makeText(SoundcloudActivity.this, "like Click :", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterItemCommentImageClick(MyAdapterSC adapter, View view, SCTrackInfoData item, int position) {
                Toast.makeText(SoundcloudActivity.this, "comment Click :", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
//        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);



        try {
            NetworkManager.getInstance().getSCTrack(SoundcloudActivity.this, new NetworkManager.OnResultListener<SCTrackInfoData[]>() {
                @Override
                public void onSuccess(Request request, SCTrackInfoData[] result) {
                    Toast.makeText(SoundcloudActivity.this, "success", Toast.LENGTH_SHORT).show();

//                            new LoadImagefromUrl( ).execute(imageIcon, result[0].user.avatar_url);
//
//                            Uri fileUri = Uri.fromFile(new File(result[0].user.avatar_url));
//                            uri = Uri.fromFile(new File(result[0].stream_url));
//                            Glide.with(SoundcloudTestActivity.this).load(fileUri).into(imageIcon);



                    ArrayList<SCTrackInfoData> arrayList= new ArrayList<SCTrackInfoData>();
                    Collections.addAll(arrayList, result);



                    mAdapter.clearAll();
                    mAdapter.addAll(arrayList);
                    mAdapter.setPageNumber(1);
//
//                    if(!TextUtils.isEmpty(result[0].user.avatar_url))  {
//                        Glide.with(SoundcloudActivity.this)
//                                .load(result[0].user.avatar_url)
//                                .into(imageIcon);
//                    }
//
//                    textUsername.setText(result[0].user.username);
//                    textTitle.setText(result[0].title);
//                    textLastModified.setText(result[0].last_modified);
//                    textDuration.setText(""+result[0].duration);
//
////                            https://api.soundcloud.com/tracks/57047389/stream?client_id=855fe8df184bf720b9d8e3a4bfb05caf
//                    textAvatarURL.setText(result[0].user.avatar_url);
//                    textStreamURL.setText(result[0].stream_url+"?client_id=855fe8df184bf720b9d8e3a4bfb05caf");
//                    textWaveformURL.setText(result[0].waveform_url);
//
//
//
//                    String title;
//                    String last_modified = result[0].last_modified;
//                    int duration = result[0].duration;
//                    String stream_url = result[0].stream_url+"?client_id=855fe8df184bf720b9d8e3a4bfb05caf";
//                    String waveform_url;



                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {
                    Toast.makeText(SoundcloudActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


//        recyclerView.addItemDecoration(new MyDecoration(this));

//        initData();
    }

//    private void initData() {
//        Random r = new Random();
//        for (int i = 0; i < 100; i++) {
//            MyDataSC data = new MyDataSC();
//
//            data.icon_image = "item " + i;
//            data.like_image = "item " + i;
//            data.comment_image = "item " + i;
//            data.nickname = "item " + i;
//            data.title = "item " + i;
//            data.text= "item " + i;
//            data.duration = "item " + i;
//            data.like = "item " + i;
//            data.comment = "item " + i;
////            data.fontSize = 20 + r.nextInt(40);
//            mAdapter.add(data);
//        }
//    }
}
