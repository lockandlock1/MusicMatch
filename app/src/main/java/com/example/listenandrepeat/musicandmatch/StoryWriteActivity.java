package com.example.listenandrepeat.musicandmatch;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.DataClass.ListDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.StoryWrite;
import com.example.listenandrepeat.musicandmatch.DataClass.StoryWriteResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;

import java.io.File;
import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class StoryWriteActivity extends AppCompatActivity {



    EditText writeContents;
    Button btn;
    ImageView photoView;
    String filePath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroy_write);
        writeContents = (EditText)findViewById(R.id.text_wrtie);
        photoView = (ImageView)findViewById(R.id.image_gallery);



        btn = (Button)findViewById(R.id.btn_post);
        btn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                File file = null;
                if (filePath != null) {
                    file = new File(filePath);
                }

                try {
                    NetworkManager.getInstance().postStoryWrite(StoryWriteActivity.this, "title", writeContents.getText().toString(), 1, 0, file, new NetworkManager.OnResultListener<StoryWriteResult>() {
                        @Override
                        public void onSuccess(Request request, StoryWriteResult result) {
                            Toast.makeText(StoryWriteActivity.this, result.success.message, Toast.LENGTH_LONG).show();

                            finish();
                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(StoryWriteActivity.this,cause.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                   // Toast.makeText(StoryWriteActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });



        btn = (Button)findViewById(R.id.btn_plus);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn = (Button)findViewById(R.id.btn_photo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGallery();
            }
        });

    }
    Uri mFileUri;
    private static final int RC_GALLERY = 1;

    private void callGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");


        startActivityForResult(intent,RC_GALLERY);

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("selected_file",mFileUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GALLERY){
            if (resultCode == Activity.RESULT_OK){
                Uri uri = data.getData();
                Cursor c= getContentResolver().query(uri,new String[]{MediaStore.Images.Media.DATA},null,null,null);
                if(c.moveToNext()){
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    filePath = path;
                    Uri fileUri = Uri.fromFile(new File(path));
                    Glide.with(this).load(fileUri).into(photoView);
                }
            }
        }
    }



}
