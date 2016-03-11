package com.example.listenandrepeat.musicandmatch.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.listenandrepeat.musicandmatch.MainActivity;
import com.example.listenandrepeat.musicandmatch.R;

public class LoginEmailActivity extends AppCompatActivity {
    TextView textView;
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

        Button btn = (Button)findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), MainActivity.class));
                LoginEmailActivity.this.finish();
                AActivity.finish();
            }
        });
    }
}
