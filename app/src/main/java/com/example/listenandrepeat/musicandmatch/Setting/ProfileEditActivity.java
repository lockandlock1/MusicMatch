package com.example.listenandrepeat.musicandmatch.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.R;

public class ProfileEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("nickname님의 프로필");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileEditActivity.this, "center toolbar navigation click", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn = (Button) findViewById(R.id.btn_nickname);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ProfileEditNameActivity.class);
                startActivity(intent);
            }
        });

        btn = (Button) findViewById(R.id.btn_intro);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ProfileEditIntroActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showDialogGenreFragment(View view) {
        DialogGenreFragment f = new DialogGenreFragment();
        f.show(getSupportFragmentManager(), "dialog");
    }

    public void showDialogPositionFragment(View view) {
        DialogPositionFragment f = new DialogPositionFragment();
        f.show(getSupportFragmentManager(), "dialog");
    }
}
