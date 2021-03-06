package com.example.listenandrepeat.musicandmatch.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.listenandrepeat.musicandmatch.DataClass.LoginAndSignUpResult;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileMe;
import com.example.listenandrepeat.musicandmatch.MainActivity;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    int id = R.drawable.splash;

    String id_inho = "test3@exe.com";
    String pw_inho = "test3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String name = PropertyManager.getInstance().getUserId();

        if(!TextUtils.isEmpty(name)){
            String password = PropertyManager.getInstance().getPassword();

            try {
                NetworkManager.getInstance().logIn(SplashActivity.this, id_inho, pw_inho, new NetworkManager.OnResultListener<LoginAndSignUpResult>() {
                    @Override
                    public void onSuccess(Request request, LoginAndSignUpResult result) {

                        getMyProfile();

                    }

                    @Override
                    public void onFailure(Request request, int code, Throwable cause) {

                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            Handler hd = new Handler();
            hd.postDelayed(new splashHandler(), 1000);
        }
    }

    private class splashHandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), LoginActivity.class));
            SplashActivity.this.finish();
        }
    }
    private void getMyProfile(){
        try {
            NetworkManager.getInstance().getProfileMe(this, new NetworkManager.OnResultListener<ProfileMe>() {
                @Override
                public void onSuccess(Request request, ProfileMe result) {
                    PropertyManager.getInstance().setMid(result.success.data.mid);
                    PropertyManager.getInstance().setPostion(result.success.data.position);
                    PropertyManager.getInstance().setGenre(result.success.data.genre);
                    PropertyManager.getInstance().setNickName(result.success.data.nickname);
                    PropertyManager.getInstance().setProfile(result.success.data.photo);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
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
