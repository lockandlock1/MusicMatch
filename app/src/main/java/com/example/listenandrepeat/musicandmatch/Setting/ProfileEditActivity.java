package com.example.listenandrepeat.musicandmatch.Setting;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileChange;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileMe;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;
import com.example.listenandrepeat.musicandmatch.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Request;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int BASE_POSITION = 10;
    private static final int GUITAR_POSITION= 11;
    private static final int DRUM_POSITION= 12;
    private static final int KEYBOARD_POSITION =13;
    private static final int VOCAL_POSITION =14;
    private static final int RAP_POSITION =15;
    private static final int COMPOSE_POSITION =16;
    private static final int _POSITION =17;

    private static final int BALLAD_GENRE = 0;
    private static final int RB_GENRE = 1;
    private static final int HIPHOP_GENRE = 2;
    private static final int ROCK_GENRE = 3;
    private static final int DANCE_GENRE = 4;
    private static final int INDI_GENRE = 5;
    private static final int ELEC_GENRE = 6;
    private static final int TROT_GENRE = 7;

    ImageView imagePhoto;
    ImageView imageGenre;
    ImageView imagePosition;
    //    TextView textMessage;
//    TextView textUsername;
//    TextView textPhoto;
    TextView textNickname;
    TextView textIntro;


    String profilePath = null;

    String username = "test3@exe.com";
    String password = "test3";

    String photo = "";
    String nickname ;
    String intro;
    int genre;
    int position;
    File file = new File("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("나의 프로필");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileEditActivity.this, "center toolbar navigation click", Toast.LENGTH_SHORT).show();
            }
        });


        imagePhoto = (ImageView)findViewById(R.id.image_photo);
        imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGallery();
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


        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable("selected_file");
        }

        btn = (Button) findViewById(R.id.btn_OK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    file = new File(profilePath);
                    textNickname.setText("inho");
                    textIntro.setText("hi~~~~~~~~~~~~");
                    nickname = textNickname.getText().toString();
                    intro = textIntro.getText().toString();

                    NetworkManager.getInstance().putProfileChange(ProfileEditActivity.this, username, password, photo, nickname, intro, genre, position, file, new NetworkManager.OnResultListener<ProfileChange>() {
                        @Override
                        public void onSuccess(Request request, ProfileChange result) {
                            Toast.makeText(ProfileEditActivity.this,"success",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Request request, int code, Throwable cause) {
                            Toast.makeText(ProfileEditActivity.this,"fail",Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        imagePhoto = (ImageView)findViewById(R.id.image_photo);
        imageGenre = (ImageView)findViewById(R.id.image_genre);
        imagePosition = (ImageView)findViewById(R.id.image_position);
        textNickname = (TextView)findViewById(R.id.text_nickname);
        textIntro = (TextView)findViewById(R.id.text_intro);

        imageGenre.setImageResource(R.drawable.genrebut);
        imagePosition.setImageResource(R.drawable.positionbut);


        try {
            NetworkManager.getInstance().getProfileMe(ProfileEditActivity.this, new NetworkManager.OnResultListener<ProfileMe>() {
                @Override
                public void onSuccess(Request request, ProfileMe result) {
                    Toast.makeText(ProfileEditActivity.this,"success",Toast.LENGTH_SHORT).show();
                    PropertyManager.getInstance().setPostion(result.success.data.position);
                    PropertyManager.getInstance().setGenre(result.success.data.genre);
                    PropertyManager.getInstance().setNickName(result.success.data.nickname);
                    photo = result.success.data.photo;
                    nickname = result.success.data.nickname ;
                    intro = result.success.data.intro;
                    genre = result.success.data.genre;
                    position = result.success.data.position;

                    setGenre(result.success.data.genre);
                    setPosition(result.success.data.position);
                    textNickname.setText(result.success.data.nickname);
                    textIntro.setText(result.success.data.intro);
                    new LoadImagefromUrl( ).execute(imagePhoto, result.success.data.photo);
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {
                    Toast.makeText(ProfileEditActivity.this,"fail",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }



    public void showDialogGenreFragment(View view) {
        DialogGenreFragment f = new DialogGenreFragment();
        f.setOnGenreSelectionListener(new DialogGenreFragment.OnGenreSelectionListener() {
            @Override
            public void onGenreSelected(int genre) {
                setGenre(genre);
                ProfileEditActivity.this.genre = genre;
            }
        });
        f.show(getSupportFragmentManager(), "dialog");
    }

    public void showDialogPositionFragment(View view) {
        DialogPositionFragment f = new DialogPositionFragment();
        f.setOnPositionSelectionListener(new DialogPositionFragment.OnPositionSelectionListener() {
            @Override
            public void onPositionSelected(int position) {
                setPosition(position);
                ProfileEditActivity.this.position = position;
            }
        });
        f.show(getSupportFragmentManager(), "dialog");
    }

    private void setGenre(int genre) {
        switch (genre) {
            case BALLAD_GENRE :
                imageGenre.setImageResource(R.drawable.balled2);
                break;
            case RB_GENRE :
                imageGenre.setImageResource(R.drawable.rb2);
                break;
            case HIPHOP_GENRE :
                imageGenre.setImageResource(R.drawable.hiphop2);
                break;
            case ROCK_GENRE :
                imageGenre.setImageResource(R.drawable.rock2);
                break;
            case DANCE_GENRE :
                imageGenre.setImageResource(R.drawable.dance2);
                break;
            case INDI_GENRE :
                imageGenre.setImageResource(R.drawable.indi2);
                break;
            case ELEC_GENRE :
                imageGenre.setImageResource(R.drawable.ellec2);
                break;
            case TROT_GENRE :
                imageGenre.setImageResource(R.drawable.trot2);
                break;
        }
    }

    private void setPosition(int position) {
        switch (position) {
            case BASE_POSITION :
                imagePosition.setImageResource(R.drawable.base2);
                break;
            case GUITAR_POSITION :
                imagePosition.setImageResource(R.drawable.guitar2);
                break;
            case DRUM_POSITION :
                imagePosition.setImageResource(R.drawable.drum2);
                break;
            case KEYBOARD_POSITION :
                imagePosition.setImageResource(R.drawable.keyboard2);
                break;
            case VOCAL_POSITION :
                imagePosition.setImageResource(R.drawable.vocal2);
                break;
            case RAP_POSITION :
                imagePosition.setImageResource(R.drawable.rap2);
                break;
            case COMPOSE_POSITION :
                imagePosition.setImageResource(R.drawable.compose2);
                break;
            case _POSITION :
                imagePosition.setImageResource(R.drawable.bboy2);
                break;
        }
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


    private static final int RC_GALLERY = 1;

    private void callGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri());
        startActivityForResult(intent, RC_GALLERY);
    }

    private static final int RC_GAMERA= 2;
    private void callCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri());
        startActivityForResult(intent, RC_GAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Cursor c = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    profilePath = path;
                    Uri fileUri = Uri.fromFile(new File(path));
                    Glide.with(this).load(fileUri).into(imagePhoto);
                }
//                Glide.with(this).load(mFileUri).into(photoView);
            }
        }
        if (requestCode == RC_GAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(mFileUri).into(imagePhoto);
            }
        }
    }

    Uri mFileUri;

    private Uri getFileUri() {
        File dir = getExternalFilesDir("myfile");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir,"my_image_" + System.currentTimeMillis() + ".jpeg");
        mFileUri = Uri.fromFile(file);
        return mFileUri;
    }
}
