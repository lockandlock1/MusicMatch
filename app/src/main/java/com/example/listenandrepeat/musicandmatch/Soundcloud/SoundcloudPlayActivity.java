package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Request;

public class SoundcloudPlayActivity extends AppCompatActivity {

    public static final String PARAM_POST_ID = "post_id";
    int postId;
    Intent intent;


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

    float currentVolume = 1.0f;
    Runnable volumeUp = new Runnable() {
        @Override
        public void run() {
            if (currentVolume < 1.0f) {
                mPlayer.setVolume(currentVolume, currentVolume);
                currentVolume += 0.2f;
                mHadler.postDelayed(this, 200);
            } else {
                currentVolume = 1.0f;
                mPlayer.setVolume(currentVolume, currentVolume);
            }
        }
    };

    Runnable volumeDown = new Runnable() {
        @Override
        public void run() {
            if (currentVolume > 0) {
                mPlayer.setVolume(currentVolume, currentVolume);
                currentVolume -= 0.2f;
                mHadler.postDelayed(this, 200);
            } else {
                currentVolume = 0;
                mPlayer.setVolume(currentVolume, currentVolume);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundcloud_test);

        imageIcon = (ImageView)findViewById(R.id.image_icon);
        textUsername = (TextView)findViewById(R.id.text_username);
        textTitle = (TextView)findViewById(R.id.text_title);
        textLastModified = (TextView)findViewById(R.id.text_last_modified);
        textDuration = (TextView)findViewById(R.id.text_duration);
        textAvatarURL = (TextView)findViewById(R.id.text_avatar_url);
        textStreamURL = (TextView)findViewById(R.id.text_stream_url);
        textWaveformURL = (TextView)findViewById(R.id.text_waveform_url);

        intent = getIntent();
        postId = intent.getIntExtra(PARAM_POST_ID, 0);




        try {
            NetworkManager.getInstance().getSCTrack(SoundcloudPlayActivity.this, new NetworkManager.OnResultListener<SCTrackInfoData[]>() {
                @Override
                public void onSuccess(Request request, SCTrackInfoData[] result) {
                    Toast.makeText(SoundcloudPlayActivity.this, "success" +postId, Toast.LENGTH_SHORT).show();

//                            new LoadImagefromUrl( ).execute(imageIcon, result[0].user.avatar_url);
//
//                            Uri fileUri = Uri.fromFile(new File(result[0].user.avatar_url));
//                            uri = Uri.fromFile(new File(result[0].stream_url));
//                            Glide.with(SoundcloudTestActivity.this).load(fileUri).into(imageIcon);

                    if(!TextUtils.isEmpty(result[0].user.avatar_url))  {
                        Glide.with(SoundcloudPlayActivity.this)
                                .load(result[0].user.avatar_url)
                                .into(imageIcon);
                    }

//                    textUsername.setText(result[0].user.username);
//                    textTitle.setText(result[0].title);
                    textLastModified.setText(""+postId);
//                    textDuration.setText(""+result[0].duration);
//
////                            https://api.soundcloud.com/tracks/57047389/stream?client_id=855fe8df184bf720b9d8e3a4bfb05caf
//                    textAvatarURL.setText(result[0].user.avatar_url);
//                    textStreamURL.setText("https://api.soundcloud.com/tracks/"+postId+"/stream?client_id=855fe8df184bf720b9d8e3a4bfb05caf");
//                    textWaveformURL.setText(result[0].waveform_url);



                    String title;
                    String last_modified = result[0].last_modified;
                    int duration = result[0].duration;
                    String stream_url = "https://api.soundcloud.com/tracks/"+""+postId+"/stream?client_id=855fe8df184bf720b9d8e3a4bfb05caf";
                    String waveform_url;



                    uri = Uri.parse(stream_url);
                    mPlayer.reset();
                    mState = PlayerState.STATE_IDLE;
                    try {
//                                mPlayer.setDataSource(this, uri);
                        mPlayer.setDataSource(SoundcloudPlayActivity.this, uri);
                        mState = PlayerState.STATE_INITIALIZED;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (mState == PlayerState.STATE_INITIALIZED) {
                        try {
                            mPlayer.prepare();
                            mState = PlayerState.STATE_PREPARED;
//                                    progressView.setMax(mPlayer.getDuration());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {
                    Toast.makeText(SoundcloudPlayActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        muteView = (CheckBox)findViewById(R.id.check_mute);
        muteView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mHadler.removeCallbacks(volumeUp);
                    mHadler.post(volumeDown);
//                    mPlayer.setVolume(0,0);
                } else {
                    mHadler.removeCallbacks(volumeDown);
                    mHadler.post(volumeUp);
//                    mPlayer.setVolume(1, 1);
                }
            }
        });

        progressView = (SeekBar)findViewById(R.id.seek_progress);
        progressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;
            private static final int PROGRESS_NOT_CHANGED = -1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    this.progress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progress = PROGRESS_NOT_CHANGED;
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progress != PROGRESS_NOT_CHANGED) {
                    if (mState == PlayerState.STATE_STARTED) {
                        mPlayer.seekTo(progress);
                    }
                }
                isSeeking = false;
            }
        });
        volumeView = (SeekBar)findViewById(R.id.seek_volume);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        mPlayer = MediaPlayer.create(this, R.raw.weingweing);
        mState = PlayerState.STATE_PREPARED;

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        volumeView.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeView.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        progressView.setMax(mPlayer.getDuration());

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mState = PlayerState.STATE_ERROR;
                return false;
            }
        });

        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mState == PlayerState.STATE_INITIALIZED || mState == PlayerState.STATE_STOPPED) {
                    try {
                        mPlayer.prepare();
                        mState = PlayerState.STATE_PREPARED;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mState == PlayerState.STATE_PREPARED || mState == PlayerState.STATE_PAUSED) {
                    mPlayer.seekTo(progressView.getProgress());
                    mPlayer.start();
                    mState = PlayerState.STATE_STARTED;
                    mHadler.post(updateProgress);
                }
            }
        });

        btn = (Button)findViewById(R.id.btn_pause);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == PlayerState.STATE_STARTED) {
                    mPlayer.pause();
                    mState = PlayerState.STATE_PAUSED;
                }
            }
        });

        btn = (Button)findViewById(R.id.btn_stop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == PlayerState.STATE_PREPARED ||
                        mState == PlayerState.STATE_STARTED ||
                        mState == PlayerState.STATE_PAUSED) {
                    mPlayer.stop();
                    mState = PlayerState.STATE_STOPPED;
                    progressView.setProgress(0);
                }
            }
        });

        btn = (Button)findViewById(R.id.btn_list);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(SoundcloudTestActivity.this, MusicListActivity.class), RC_LIST);
            }
        });

    }

    private static final int RC_LIST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LIST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String displayName = data.getStringExtra("displayName");
                String path = data.getStringExtra("file");
                setTitle(displayName);
                mPlayer.reset();
                mState = PlayerState.STATE_IDLE;
                try {
                    mPlayer.setDataSource(this, uri);
                    mState = PlayerState.STATE_INITIALIZED;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (mState == PlayerState.STATE_INITIALIZED) {
                    try {
                        mPlayer.prepare();
                        mState = PlayerState.STATE_PREPARED;
                        progressView.setMax(mPlayer.getDuration());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    Handler mHadler = new Handler(Looper.getMainLooper());
    private static final int INTERVAL = 50;

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if (mState == PlayerState.STATE_STARTED) {
                if (!isSeeking) {
                    progressView.setProgress(mPlayer.getCurrentPosition());
                }
                mHadler.postDelayed(this, INTERVAL);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        mState = PlayerState.STATE_END;
        mPlayer = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
