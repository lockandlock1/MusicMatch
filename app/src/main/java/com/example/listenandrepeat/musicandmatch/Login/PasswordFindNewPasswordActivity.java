package com.example.listenandrepeat.musicandmatch.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.R;

public class PasswordFindNewPasswordActivity extends AppCompatActivity {

//    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_find_new_password);

//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
//        actionBar.setTitle("비밀번호 찾기");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("비밀번호 찾기");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PasswordFindNewPasswordActivity.this, "center toolbar navigation click", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_enter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplication(), LoginEmailActivity.class));
                PasswordFindNewPasswordActivity.this.finish();
//                LoginEmailActivity.this.finish();
            }
        });
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            startActivity(new Intent(getApplication(), PasswordFindEmailActivity.class));
//            PasswordFindNewPasswordActivity.this.finish();
//            Toast.makeText(this, "home as up click", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
