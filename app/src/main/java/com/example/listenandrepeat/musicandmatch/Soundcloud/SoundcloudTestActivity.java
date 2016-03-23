package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Request;

public class SoundcloudTestActivity extends AppCompatActivity {

    ImageView imageIcon;
    TextView textUsername;
    TextView textTitle;
    TextView textLastModified;
    TextView textDuration;
    TextView textAvatarURL;
    TextView textStreamURL;
    TextView textWaveformURL;
    Uri uri;


    MediaPlayer mPlayer;
    enum PlayerState {
        STATE_IDLE,
        STATE_INITIALIZED,
        STATE_PREPARED,
        STATE_STARTED,
        STATE_PAUSED,
        STATE_STOPPED,
        STATE_END,
        STATE_ERROR,
    }

    PlayerState mState;
    SeekBar progressView;
    boolean isSeeking = false;
    AudioManager mAudioManager;
    SeekBar volumeView;
    CheckBox muteView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundcloud_test);

        imageIcon = (ImageView)findViewById(R.id.image_icon);
        textUsername = (TextView)findViewById(R.id.text_username);
        textTitle = (TextView)findViewById(R.id.text_last_modified);
        textLastModified = (TextView)findViewById(R.id.text_last_modified);
        textDuration = (TextView)findViewById(R.id.text_duration);
        textAvatarURL = (TextView)findViewById(R.id.text_avatar_url);
        textStreamURL = (TextView)findViewById(R.id.text_stream_url);
        textWaveformURL = (TextView)findViewById(R.id.text_waveform_url);

        Button btn = (Button)findViewById(R.id.btn_get);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NetworkManager.getInstance().getSCTrack(SoundcloudTestActivity.this, new NetworkManager.OnResultListener<SCTrackInfoData[]>() {
                        @Override
                        public void onSuccess(Request request, SCTrackInfoData[] result) {
                            Toast.makeText(SoundcloudTestActivity.this, "success", Toast.LENGTH_SHORT).show();

                            new LoadImagefromUrl( ).execute(imageIcon, result[0].user.avatar_url);

                            Uri fileUri = Uri.fromFile(new File(result[0].user.avatar_url));
                            uri = Uri.fromFile(new File(result[0].stream_url));
                            Glide.with(SoundcloudTestActivity.this).load(fileUri).into(imageIcon);

                            textUsername.setText(result[0].user.username);
                            textTitle.setText(result[0].title);
                            textLastModified.setText(result[0].last_modified);
                            textDuration.setText(result[0].duration);
                            textAvatarURL.setText(result[0].user.avatar_url);
                            textStreamURL.setText(result[0].stream_url);
                            textWaveformURL.setText(result[0].waveform_url);

                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(SoundcloudTestActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        });




////        volumeView = (SeekBar)findViewById(R.id.seek_volume);
//        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//
////        mPlayer = MediaPlayer.create(this, R.raw.winter_blues);
//        mState = PlayerState.STATE_PREPARED;
//
//        setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        volumeView.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
//        volumeView.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
//
//        progressView.setMax(mPlayer.getDuration());
//
//        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                mState = PlayerState.STATE_ERROR;
//                return false;
//            }
//        });
////        Handler mHadler = new Handler(Looper.getMainLooper());
//        btn = (Button)findViewById(R.id.btn_start);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mState == PlayerState.STATE_INITIALIZED || mState == PlayerState.STATE_STOPPED) {
//                    try {
//                        mPlayer.prepare();
//                        mState = PlayerState.STATE_PREPARED;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (mState == PlayerState.STATE_PREPARED || mState == PlayerState.STATE_PAUSED) {
//                    mPlayer.seekTo(progressView.getProgress());
//                    mPlayer.start();
//                    mState = PlayerState.STATE_STARTED;
////                    mHadler.post(updateProgress);
//                }
//            }
//        });
//
//        if (RESULT_OK == RESULT_OK) {
////            Uri uri = data.getData();
////            String displayName = data.getStringExtra("displayName");
////            String path = data.getStringExtra("file");
////            setTitle(displayName);
//            mPlayer.reset();
//            mState = PlayerState.STATE_IDLE;
//            try {
//                mPlayer.setDataSource(this, uri);
//                mState = PlayerState.STATE_INITIALIZED;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (mState == PlayerState.STATE_INITIALIZED) {
//                try {
//                    mPlayer.prepare();
//                    mState = PlayerState.STATE_PREPARED;
//                    progressView.setMax(mPlayer.getDuration());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }

    }


    private class LoadImagefromUrl extends AsyncTask< Object, Void, Bitmap > {
        ImageView ivPreview = null;

        @Override
        protected Bitmap doInBackground( Object... params ) {
            this.ivPreview = (ImageView) params[0];
            String url = (String) params[1];
            return loadBitmap( url );
        }


        @Override
        protected void onPostExecute( Bitmap result ) {
            super.onPostExecute( result );
            ivPreview.setImageBitmap( result );
        }
    }

    public Bitmap loadBitmap( String url ) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL( url );
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch ( MalformedURLException e ) {
            e.printStackTrace( );
        } catch ( IOException e ) {

            e.printStackTrace( );
        }
        return bitmap;
    }
}
