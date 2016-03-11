package com.example.listenandrepeat.musicandmatch.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.R;

public class PasswordFindEmailActivity extends AppCompatActivity {

//    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_find_email);

//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
//        actionBar.setTitle("비밀번호 찾기");

        Button btn = (Button)findViewById(R.id.btn_auth_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PasswordFindEmailActivity.this, "인증번호 전송", Toast.LENGTH_SHORT).show();
            }
        });

        btn = (Button)findViewById(R.id.btn_enter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), PasswordFindNewPasswordActivity.class));
                PasswordFindEmailActivity.this.finish();
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            PasswordFindEmailActivity.this.finish();
//            Toast.makeText(this, "home as up click", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
