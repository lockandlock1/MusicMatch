package com.example.listenandrepeat.musicandmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.Comment;
import com.example.listenandrepeat.musicandmatch.DataClass.CommentDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.CommentResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class CommentActivity extends AppCompatActivity {

    public static final String PARAM_POST_ID = "post_id";

    RecyclerView recyclerView;
    CommentViewHolderAdapter mAdapter;
    LinearLayoutManager layoutManager;
    EditText editText;
    Button btn;
    int postId;
    Intent intent;

    boolean isLast = false;
    boolean isMoreData = false;

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
        intent = getIntent();
        postId = intent.getIntExtra(PARAM_POST_ID, 0);

        editText = (EditText)findViewById(R.id.text_comment);


        initComment();

        mAdapter.setOnAdapterItemClickListener(new CommentViewHolderAdapter.OnCommnetViewHolderAdapterItemClickListener() {
            @Override
            public void onAdapterItemDeleteBtnClick(CommentViewHolderAdapter adapter, View view, CommentItem commentItem, int position) {


                try {
                    NetworkManager.getInstance().deleteComment(CommentActivity.this, postId, adapter.items.get(position).rid, new NetworkManager.OnResultListener<CommentResult>() {
                        @Override
                        public void onSuccess(Request request, CommentResult result) {
                            Toast.makeText(CommentActivity.this, "suc", Toast.LENGTH_SHORT).show();
                            initComment();
                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(CommentActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onAdapterItemEditBtnClick(CommentViewHolderAdapter adapter, View view, CommentItem commentItem, int position) {
                Intent intent1 = new Intent(CommentActivity.this, CommentEditActivity.class);
                String content = adapter.items.get(position).content;
                String profile = adapter.items.get(position).profile;
                int replyId = adapter.items.get(position).rid;
                intent1.putExtra(CommentEditActivity.REPLY_ID, replyId);
                intent1.putExtra(CommentEditActivity.POST_ID, postId);
                intent1.putExtra(CommentEditActivity.PRORFILE, profile);
                intent1.putExtra(CommentEditActivity.CONTENT, content);
                startActivity(intent1);
                finish();
            }
        });

        btn = (Button)findViewById(R.id.btn_post);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    NetworkManager.getInstance().postCommentWrite(CommentActivity.this, postId, editText.getText().toString(), new NetworkManager.OnResultListener<CommentResult>() {
                        @Override
                        public void onSuccess(Request request, CommentResult result) {
                            editText.setText("");
                            Toast.makeText(CommentActivity.this, result.success.message, Toast.LENGTH_SHORT).show();

                            initComment();

                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {

                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(isLast && newState == RecyclerView.SCROLL_STATE_IDLE){
                    getMoreItem();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                if(totalItemCount > 0 && lastVisibleItemPosition != RecyclerView.NO_POSITION && (totalItemCount - 1 <= lastVisibleItemPosition)){
                    isLast = true;
                } else {
                    isLast =false;
                }
            }
        });

    }

    private void initComment() {


      //  Toast.makeText(CommentActivity.this, "Comment Id : " + postId, Toast.LENGTH_SHORT).show();
        try {
            NetworkManager.getInstance().getCommentDetail(CommentActivity.this, postId, 1, new NetworkManager.OnResultListener<CommentDetailResult>() {
                @Override
                public void onSuccess(Request request, CommentDetailResult result) {
                  //  Toast.makeText(CommentActivity.this, "Confirm", Toast.LENGTH_SHORT).show();
                    mAdapter.clearAll();
                    mAdapter.addAll(result.success.items);
                    mAdapter.setPageNumber(result.success.page);
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getMoreItem(){
        if(isMoreData) return;

        isMoreData = true;

        if(0 < mAdapter.getPageNumber()){
           int start = mAdapter.getPageNumber() + 1;

            mAdapter.setPageNumber(start);
            try {
                NetworkManager.getInstance().getCommentDetail(CommentActivity.this, postId, start, new NetworkManager.OnResultListener<CommentDetailResult>() {
                    @Override
                    public void onSuccess(Request request, CommentDetailResult result) {
                        mAdapter.addAll(result.success.items);
                        isMoreData = false;
                    }

                    @Override
                    public void onFailure(Request request, int code, Throwable cause) {
                        isMoreData = false;
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                isMoreData = false;
            }
        }
    }
}
