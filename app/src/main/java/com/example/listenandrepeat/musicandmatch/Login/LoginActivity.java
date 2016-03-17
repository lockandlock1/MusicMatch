package com.example.listenandrepeat.musicandmatch.Login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.listenandrepeat.musicandmatch.R;

public class LoginActivity extends AppCompatActivity {

    TextView textView;
    public static Activity LoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginActivity = LoginActivity.this;

        Button btn = (Button)findViewById(R.id.btn_login_email);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginEmailActivity.class));

            }
        });

        btn = (Button)findViewById(R.id.btn_login_soundcloud);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginSoundcloudActivity.class));
            }
        });

        btn = (Button)findViewById(R.id.btn_login_google);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginGoogleActivity.class));
            }
        });

        textView = (TextView)findViewById(R.id.text_join);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginJoinActivity.class));
            }
        });
    }
}
