package com.example.listenandrepeat.musicandmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.CommentDetailResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class CommentActivity extends AppCompatActivity {

    public static final String PARAM_POST_ID = "post_id" ;

    RecyclerView recyclerView;
    CommentViewHolderAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        mAdapter = new CommentViewHolderAdapter();
        recyclerView.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        int PostId = intent.getIntExtra(PARAM_POST_ID,0);
        Toast.makeText(CommentActivity.this, "Comment Id : " + PostId, Toast.LENGTH_SHORT).show();
        try {
            NetworkManager.getInstance().getCommentDetail(CommentActivity.this, PostId, 2, new NetworkManager.OnResultListener<CommentDetailResult>() {
                @Override
                public void onSuccess(Request request, CommentDetailResult result) {
                    mAdapter.clearAll();
                    mAdapter.addAll(result.success.items);
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
