package com.example.listenandrepeat.musicandmatch.Setting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.ProfileOther;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Request;

public class ProfileOtherActivity extends AppCompatActivity {

    ImageView imageView;
//    TextView textMessage;
//    TextView textUsername;
//    TextView textPhoto;
    TextView textNickname;
    TextView textIntro;

    Button text_nickname;
    Button text_intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("nickname님의 프로필");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileOtherActivity.this, "center toolbar navigation click", Toast.LENGTH_SHORT).show();
            }
        });



        imageView = (ImageView)findViewById(R.id.imageView);
//        textMessage = (TextView)findViewById(R.id.text_message);
//        textUsername = (TextView)findViewById(R.id.text_username);
//        textPhoto = (TextView)findViewById(R.id.text_photo);
        textNickname = (TextView)findViewById(R.id.text_nickname);
        textIntro = (TextView)findViewById(R.id.text_intro);

        text_nickname = (Button)findViewById(R.id.text_nickname);
        text_intro = (Button)findViewById(R.id.text_intro);

        //getImage(imageView);



        try {
            NetworkManager.getInstance().getProfileOther(ProfileOtherActivity.this, 1, new NetworkManager.OnResultListener<ProfileOther>() {
                @Override
                public void onSuccess(Request request, ProfileOther result) {
                    Toast.makeText(ProfileOtherActivity.this, "success", Toast.LENGTH_SHORT).show();
//                    textMessage.setText(result.success.message);
////                    textUsername.setText(result.success.data.username);
//                    textPhoto.setText(result.success.data.photo);
                    textNickname.setText(result.success.data.nickname);
                    textIntro.setText(result.success.data.intro);

//                    text_nickname.setText(result.success.data.nickname);
//                    text_intro.setText(result.success.data.intro+"genre : " +result.success.data.genre +"position : "+result.success.data.position);
                    text_nickname.setText("닉네임");
                    text_intro.setText("프로필");
                    new LoadImagefromUrl( ).execute( imageView, result.success.data.photo );
                }


                @Override
                public void onFailure(Request request, int code, Throwable cause) {
                    Toast.makeText(ProfileOtherActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    private class LoadImagefromUrl extends AsyncTask< Object, Void, Bitmap > {
        ImageView ivPreview = null;

        @Override
        protected Bitmap doInBackground( Object... params ) {
            this.ivPreview = (ImageView) params[0];
            String url = (String) params[1];
            return loadBitmap( url );
        }


        @Override
        protected void onPostExecute( Bitmap result ) {
            super.onPostExecute( result );
            ivPreview.setImageBitmap( result );
        }
    }

    public Bitmap loadBitmap( String url ) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL( url );
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch ( MalformedURLException e ) {
            e.printStackTrace( );
        } catch ( IOException e ) {

            e.printStackTrace( );
        }
        return bitmap;
    }
}
