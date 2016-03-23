package com.example.listenandrepeat.musicandmatch.Setting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    private static final int BASE_POSITION = 10;
    private static final int GUITAR_POSITION= 11;
    private static final int DRUM_POSITION= 12;
    private static final int KEYBOARD_POSITION =13;
    private static final int VOCAL_POSITION =14;
    private static final int RAP_POSITION =15;
    private static final int COMPOSE_POSITION =16;
    private static final int _POSITION =17;

    private static final int BALLAD_GENRE = 0;
    private static final int RB_GENRE = 1;
    private static final int HIPHOP_GENRE = 2;
    private static final int ROCK_GENRE = 3;
    private static final int DANCE_GENRE = 4;
    private static final int INDI_GENRE = 5;
    private static final int ELEC_GENRE = 6;
    private static final int TROT_GENRE = 7;





    ImageView imagePhoto;
    ImageView imageGenre;
    ImageView imagePosition;
    //    TextView textMessage;
//    TextView textUsername;
//    TextView textPhoto;
    TextView textNickname;
    TextView textIntro;


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



        imagePhoto = (ImageView)findViewById(R.id.image_photo);
        imageGenre = (ImageView)findViewById(R.id.image_genre);
        imagePosition = (ImageView)findViewById(R.id.image_position);
//        textMessage = (TextView)findViewById(R.id.text_message);
//        textUsername = (TextView)findViewById(R.id.text_username);
//        textPhoto = (TextView)findViewById(R.id.text_photo);
        textNickname = (TextView)findViewById(R.id.text_nickname);
        textIntro = (TextView)findViewById(R.id.text_intro);

        imageGenre.setImageResource(R.drawable.genrebut);
        imagePosition.setImageResource(R.drawable.positionbut);



        //getImage(imageView);



        try {
            NetworkManager.getInstance().getProfileOther(ProfileOtherActivity.this, 4, new NetworkManager.OnResultListener<ProfileOther>() {
                @Override
                public void onSuccess(Request request, ProfileOther result) {
                    Toast.makeText(ProfileOtherActivity.this, "success" + result.success.data.genre + " " + result.success.data.position, Toast.LENGTH_SHORT).show();

                    setGenre(result.success.data.genre);
                    setPosition(result.success.data.position);
                    textNickname.setText(result.success.data.nickname);
                    textIntro.setText(result.success.data.intro);
                    new LoadImagefromUrl( ).execute( imagePhoto, result.success.data.photo );
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


    private void setGenre(int genre) {
        switch (genre) {
            case BALLAD_GENRE :
                imageGenre.setImageResource(R.drawable.balled2);
                break;
            case RB_GENRE :
                imageGenre.setImageResource(R.drawable.rb2);
                break;
            case HIPHOP_GENRE :
                imageGenre.setImageResource(R.drawable.hiphop2);
                break;
            case ROCK_GENRE :
                imageGenre.setImageResource(R.drawable.rock2);
                break;
            case DANCE_GENRE :
                imageGenre.setImageResource(R.drawable.dance2);
                break;
            case INDI_GENRE :
                imageGenre.setImageResource(R.drawable.indi2);
                break;
            case ELEC_GENRE :
                imageGenre.setImageResource(R.drawable.ellec2);
                break;
            case TROT_GENRE :
                imageGenre.setImageResource(R.drawable.trot2);
                break;
        }
    }

    private void setPosition(int position) {
        switch (position) {
            case BASE_POSITION :
                imagePosition.setImageResource(R.drawable.base2);
                break;
            case GUITAR_POSITION :
                imagePosition.setImageResource(R.drawable.guitar2);
                break;
            case DRUM_POSITION :
                imagePosition.setImageResource(R.drawable.drum2);
                break;
            case KEYBOARD_POSITION :
                imagePosition.setImageResource(R.drawable.keyboard2);
                break;
            case VOCAL_POSITION :
                imagePosition.setImageResource(R.drawable.vocal2);
                break;
            case RAP_POSITION :
                imagePosition.setImageResource(R.drawable.rap2);
                break;
            case COMPOSE_POSITION :
                imagePosition.setImageResource(R.drawable.compose2);
                break;
            case _POSITION :
                imagePosition.setImageResource(R.drawable.position);
                break;
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
