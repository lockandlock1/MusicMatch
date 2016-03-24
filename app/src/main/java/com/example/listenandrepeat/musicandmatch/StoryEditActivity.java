package com.example.listenandrepeat.musicandmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.DataClass.ListDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.StoryWriteResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class StoryEditActivity extends AppCompatActivity {


    Button photoDeleteBtn , editBtn;
    ImageView imageView;
    EditText textEdit;
    String photo = null , content = null;
    public static final String POST_ID = "post_id";
    public static final String PHOTO = "photo_content";
    public static final String CONTENT ="write_content";
    int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_edit);
        textEdit = (EditText)findViewById(R.id.text_edit);
        imageView = (ImageView)findViewById(R.id.image_gallery);
        editBtn = (Button)findViewById(R.id.btn_edit);
        photoDeleteBtn = (Button)findViewById(R.id.btn_photo_del);
        photoDeleteBtn.setVisibility(View.GONE);


        Intent intent = getIntent();
        postId = intent.getIntExtra(POST_ID, 0);
        photo = intent.getStringExtra(PHOTO);
        content = intent.getStringExtra(CONTENT);

        textEdit.setText(content);

      //  Toast.makeText(StoryEditActivity.this,"::::"+postId,Toast.LENGTH_SHORT).show();

        if(photo != null && !TextUtils.isEmpty(photo)){
            photoDeleteBtn.setVisibility(View.VISIBLE);
            Glide.with(StoryEditActivity.this)
                    .load(photo)
                    .into(imageView);
        }

        photoDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = null;
                Glide.with(StoryEditActivity.this)
                        .load(photo)
                        .into(imageView);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StoryEditActivity.this,textEdit.getText().toString(),Toast.LENGTH_SHORT).show();
                editStroy(postId, textEdit.getText().toString(), photo);
            }
        });


    }




    private void editStroy(int pid, String content, String photo){
        try {
            NetworkManager.getInstance().modifyStory(StoryEditActivity.this, pid, content, photo, new NetworkManager.OnResultListener<StoryWriteResult>() {
                @Override
                public void onSuccess(Request request, StoryWriteResult result) {
                    // 바로 메인 가기
                    Intent intent = new Intent(StoryEditActivity.this,MainActivity.class);

                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {
                    Toast.makeText(StoryEditActivity.this,"fail",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
