package com.example.listenandrepeat.musicandmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.DataClass.CommentResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class CommentEditActivity extends AppCompatActivity {


    ImageView profileImage;
    EditText editComment;
    Button updateBtn,cancelBtn;
    String profile , content;
    int postId , replyId;
    public static final String POST_ID = "post_id";
    public static final String PRORFILE= "profile";
    public static final String CONTENT ="comment_content";
    public static final String REPLY_ID ="reply_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_edit);
        profileImage = (ImageView)findViewById(R.id.image_profile);
        editComment = (EditText) findViewById(R.id.edit_comment);
        updateBtn = (Button)findViewById(R.id.btn_update);
        cancelBtn = (Button)findViewById(R.id.btn_cancel);


        final Intent intent = getIntent();
        postId = intent.getIntExtra(POST_ID,0);
        profile = intent.getStringExtra(PRORFILE);
        content = intent.getStringExtra(CONTENT);
        replyId = intent.getIntExtra(REPLY_ID,1);
        editComment.setText(content);

        Glide.with(CommentEditActivity.this)
                .load(profile)
                .into(profileImage);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NetworkManager.getInstance().modifyComment(CommentEditActivity.this, postId, replyId, editComment.getText().toString(), new NetworkManager.OnResultListener<CommentResult>() {
                        @Override
                        public void onSuccess(Request request, CommentResult result) {
                            Intent intent1 = new Intent(CommentEditActivity.this,CommentActivity.class);
                            intent1.putExtra(CommentActivity.PARAM_POST_ID,postId);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                   .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent1);
                            finish();
                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(CommentEditActivity.this,"comment Edit Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
