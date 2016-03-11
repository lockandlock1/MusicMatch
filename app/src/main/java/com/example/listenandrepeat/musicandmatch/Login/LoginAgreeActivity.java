package com.example.listenandrepeat.musicandmatch.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.listenandrepeat.musicandmatch.R;

public class LoginAgreeActivity extends AppCompatActivity {
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_agree);

        Button btn = (Button)findViewById(R.id.btn_enter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplication(), LoginJoinActivity.class));
                LoginAgreeActivity.this.finish();
            }
        });

        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        checkBox.isChecked();
    }
}
