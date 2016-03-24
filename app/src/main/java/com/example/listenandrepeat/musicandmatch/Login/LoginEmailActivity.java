package com.example.listenandrepeat.musicandmatch.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginEmailActivity extends AppCompatActivity {



    TextView textView;
    TextView textEmail;
    TextView textPwd;
    LoginActivity AActivity = (LoginActivity)LoginActivity.LoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

//        Button btn = (Button)findViewById(R.id.btn_pwforget);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        textView = (TextView)findViewById(R.id.text_pwforget);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), PasswordFindEmailActivity.class));
            }
        });

        textEmail = (EditText)findViewById(R.id.edit_email);
        textPwd = (EditText)findViewById(R.id.edit_pwd);


        Button btn = (Button)findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailId = textEmail.getText().toString();
                final String pwd = textPwd.getText().toString();
                try {
                    NetworkManager.getInstance().logIn(LoginEmailActivity.this, emailId, pwd, new NetworkManager.OnResultListener<LoginAndSignUpResult>() {
                        @Override
                        public void onSuccess(Request request, LoginAndSignUpResult result) {
                            PropertyManager.getInstance().setUserId(emailId);
                            PropertyManager.getInstance().setPassword(pwd);
                            getMyProfile();
                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(LoginEmailActivity.this,"Fail",Toast.LENGTH_SHORT).show();
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
            NetworkManager.getInstance().getProfileMe(LoginEmailActivity.this, new NetworkManager.OnResultListener<ProfileMe>() {
                @Override
                public void onSuccess(Request request, ProfileMe result) {
                    PropertyManager.getInstance().setMid(result.success.data.mid);
                    PropertyManager.getInstance().setPostion(result.success.data.position);
                    PropertyManager.getInstance().setGenre(result.success.data.genre);
                    PropertyManager.getInstance().setNickName(result.success.data.nickname);
                    PropertyManager.getInstance().setProfile(result.success.data.photo);
                    startActivity(new Intent(getApplication(), MainActivity.class));

                    LoginEmailActivity.this.finish();
                    AActivity.finish();
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
