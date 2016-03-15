package com.example.listenandrepeat.musicandmatch.Setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.R;

public class SettingPasswordActivity extends AppCompatActivity {

//    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);

//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
//        actionBar.setTitle("비밀번호 변경");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("비밀번호 변경");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingPasswordActivity.this, "center toolbar navigation click", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_OK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingPasswordActivity.this.finish();
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            SettingPasswordActivity.this.finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
