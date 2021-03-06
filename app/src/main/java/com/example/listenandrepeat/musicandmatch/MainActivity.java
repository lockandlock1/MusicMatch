package com.example.listenandrepeat.musicandmatch;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.LoginAndSignUpResult;
import com.example.listenandrepeat.musicandmatch.Login.LoginActivity;
import com.example.listenandrepeat.musicandmatch.Login.SplashActivity;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;
import com.example.listenandrepeat.musicandmatch.Setting.SettingFragment;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    // inhghogogoogogogogog
    ///hh
    NavigationView menuView;
    DrawerLayout drawer;
   static int numPeople;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        menuView = (NavigationView)findViewById(R.id.navigation_menu);
        HeaderView view = (HeaderView)menuView.getHeaderView(0);

        view.setHeader(PropertyManager.getInstance().getNickName(),PropertyManager.getInstance().getProfile(),PropertyManager.getInstance().getPostion(),PropertyManager.getInstance().getGenre());
        menuView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            MainFragment fragment = new MainFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container,fragment);
            ft.commit();
        }


    }


    public void changeMusicStory(int mid) {
        MyMusicStoryFragment fragment = new MyMusicStoryFragment();
      //  Toast.makeText(MainActivity.this,""+mid,Toast.LENGTH_SHORT).show();
        Bundle b = new Bundle();
        b.putInt("mid",mid);
        fragment.setArguments(b);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){

            return true;
        }
        ////
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.Home:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.MyMusicStroy:
                getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,new MyMusicStoryFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.HallofFame:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new HallofFameFragment())
                .addToBackStack(null)
                .commit();
                break;
            case R.id.Settings:
                getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new SettingFragment())
                .addToBackStack(null)
                .commit();
                break;
            case R.id.LogOut:

                try {
                    NetworkManager.getInstance().logOut(this, new NetworkManager.OnResultListener<LoginAndSignUpResult>() {
                        @Override
                        public void onSuccess(Request request, LoginAndSignUpResult result) {

                            PropertyManager.getInstance().setMid(-1);
                            PropertyManager.getInstance().clear();
                            startActivity(new Intent(getApplication(), LoginActivity.class));
                            MainActivity.this.finish();
                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {

                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //////////////////////Dialog 추가/////////////////////////

    public void showDialogNickname(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("닉네임 변경");
//        builder.setMessage("Dialog message....");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Yes Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Cancel Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    public void showDialogEmail(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("이메일 변경");
//        builder.setMessage("Dialog message....");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Yes Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Cancel Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    public void showDialogLeave(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("비밀번호 입력");
        builder.setMessage("탈퇴시 모든 회원 정보가 삭제됩니다. 정말 탈퇴하시겠습니까?");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Yes Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Cancel Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }


}
