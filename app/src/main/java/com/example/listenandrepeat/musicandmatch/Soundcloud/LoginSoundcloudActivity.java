package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.LoginAndSignUpResult;
import com.example.listenandrepeat.musicandmatch.Login.AccessToken;
import com.example.listenandrepeat.musicandmatch.Login.BrowserActivity;
import com.example.listenandrepeat.musicandmatch.Login.MeInfo;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.File;
import java.io.UnsupportedEncodingException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LoginSoundcloudActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "855fe8df184bf720b9d8e3a4bfb05caf";
    private static final String CLIENT_SECRET = "aede1edc37a86ede4606326274d1172c";
    private static final String LOGIN_URL = "https://soundcloud.com/connect";
    private static final String SCOPE = "*";
    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";

    private static final String TOKEN_URL ="https://api.soundcloud.com/oauth2/token";
    private static final int RC_LOGIN = 100;

    OkHttpClient mClient;

    String cloud_token;
    String nickname;
    int cloud_id;
    String photo_path;
    String profilePath = null;
    File file = new File("");

    AccessToken token = null;
    String tokenString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_soundcloud);

        mClient = new OkHttpClient.Builder().build();

        Button btn = (Button)findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = makeConnectionURL();
                Intent intent = new Intent(LoginSoundcloudActivity.this, BrowserActivity.class);
                intent.setData(Uri.parse(url));
                startActivityForResult(intent, RC_LOGIN);
            }
        });

        btn = (Button)findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), SoundcloudActivity.class));
//                NetworkManager.getInstance().getMeInfo();
            }
        });

        btn = (Button)findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSoundcloudActivity.this, SoundcloudTestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN) {
            String code = data.getStringExtra(BrowserActivity.PARAM_CODE);
            if (!TextUtils.isEmpty(code)) {
                try {
                    NetworkManager.getInstance().getAccessToken(code, new NetworkManager.OnResultListener<AccessToken>() {
                        @Override
                        public void onSuccess(Request request, AccessToken result) {
//                            Gson gson = new Gson();
//                            tokenString = result.accessToken;
//                            token = gson.fromJson(tokenString, AccessToken.class);
                            Toast.makeText(LoginSoundcloudActivity.this, "token OK", Toast.LENGTH_SHORT).show();
                            token = result;
                            tokenString = result.accessToken;

                            try {
                                NetworkManager.getInstance().getMeInfo(result, new NetworkManager.OnResultListener<MeInfo>() {
                                    @Override
                                    public void onSuccess(Request request, MeInfo result) {
                                        nickname = result.username;
                                        cloud_id = result.id;
                                        photo_path = result.avatar_url;
                                        Toast.makeText(LoginSoundcloudActivity.this, "MeInfo OK", Toast.LENGTH_SHORT).show();


                                        try {
                                            NetworkManager.getInstance().postLoginSC(LoginSoundcloudActivity.this, cloud_id, new NetworkManager.OnResultListener<LoginAndSignUpResult>(){
                                                @Override
                                                public void onSuccess(Request request, LoginAndSignUpResult result) {
                                                    Toast.makeText(LoginSoundcloudActivity.this, "postLoginSC OK", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Request request, int code, Throwable cause) {
                                                    Toast.makeText(LoginSoundcloudActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void onFailure(Request request, int code, Throwable cause) {
                                        Toast.makeText(LoginSoundcloudActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(LoginSoundcloudActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private String makeConnectionURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(LOGIN_URL).append("?");
        sb.append("client_id").append("=").append(CLIENT_ID).append("&");
        sb.append("redirect_uri").append("=").append(BrowserActivity.CALLBACK_URL).append("&");
        sb.append("response_type").append("=").append(RESPONSE_TYPE);
//        .append("&");
//        sb.append("scope").append("=").append(SCOPE);
        return sb.toString();
    }

    private String makeTokenURL(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append(TOKEN_URL).append("?");
        sb.append("client_id").append("=").append(CLIENT_ID).append("&");
        sb.append("client_secret").append("=").append(CLIENT_SECRET).append("&");
        sb.append("redirect_uri").append("=").append(BrowserActivity.CALLBACK_URL).append("&");
        sb.append("grant_type").append("=").append(GRANT_TYPE).append("&");
        sb.append("code").append("=").append(code).append("&");
        return sb.toString();
    }
}
