package com.example.listenandrepeat.musicandmatch.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.LoginAndSignUpResult;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileMe;
import com.example.listenandrepeat.musicandmatch.MainActivity;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class LoginJoinActivity extends AppCompatActivity {

    EditText inputEmail , inputUserName , inputPwd, pwdConfirm;
    Button btnJoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_join);
        /*
        textView = (TextView)findViewById(R.id.textView1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginAgreeActivity.class));
            }
        });

        textView = (TextView)findViewById(R.id.textView2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginAgreeActivity.class));
            }
        });


        Button btn = (Button)findViewById(R.id.btn_join);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginJoinActivity.this.finish();
            }
        });
        */
        btnJoin = (Button)findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NetworkManager.getInstance().signUp(LoginJoinActivity.this, "music2@exe.com", "9dragon", "123", new NetworkManager.OnResultListener<LoginAndSignUpResult>() {
                        @Override
                        public void onSuccess(Request request, LoginAndSignUpResult result) {
                            PropertyManager.getInstance().setUserId("music2@exe.com");
                            PropertyManager.getInstance().setPassword("123");

                            startActivity(new Intent(LoginJoinActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(LoginJoinActivity.this,"Join Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getMyProfile(){
        try {
            NetworkManager.getInstance().getProfileMe(LoginJoinActivity.this, new NetworkManager.OnResultListener<ProfileMe>() {
                @Override
                public void onSuccess(Request request, ProfileMe result) {
                    PropertyManager.getInstance().setMid(result.success.data.mid);
                    PropertyManager.getInstance().setPostion(result.success.data.position);
                    PropertyManager.getInstance().setGenre(result.success.data.genre);
                    PropertyManager.getInstance().setNickName(result.success.data.nickname);
                    startActivity(new Intent(getApplication(), MainActivity.class));
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
