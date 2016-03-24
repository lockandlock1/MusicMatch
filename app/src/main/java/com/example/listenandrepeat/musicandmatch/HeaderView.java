package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.DataClass.ProfileMeData;

/**
 * Created by ListenAndRepeat on 2016. 3. 24..
 */
public class HeaderView extends LinearLayout {


    ImageView profileImage , genreImage, posImage;
    TextView nickName;

    public HeaderView(Context context) {
        super(context);
        init();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
            inflate(getContext(),R.layout.ham_menu_header,this);
            profileImage = (ImageView)findViewById(R.id.image_photo);
            genreImage = (ImageView)findViewById(R.id.image_genre);
            posImage = (ImageView)findViewById(R.id.image_position);
            nickName = (TextView)findViewById(R.id.text_nickname);

    }

    void setHeader(String nickname,String profile,int pos,int genre){

        nickName.setText(nickname);

        if(!TextUtils.isEmpty(profile)){
            Glide.with(getContext())
                    .load(profile)
                    .into(profileImage);
        }else {
            profileImage.setImageResource(R.drawable.circle_profile);
        }

        //pos

        switch (pos){

            case 10:
                posImage.setImageResource(R.drawable.mark_position_base);
                break;
            case 11:
                posImage.setImageResource(R.drawable.mark_position_guitar);
                break;
            case 12:
                posImage.setImageResource(R.drawable.mark_position_drum);
                break;
            case 13:
                posImage.setImageResource(R.drawable.mark_position_keyboard);
                break;
            case 14:
                posImage.setImageResource(R.drawable.mark_position_vocal);
                break;
            case 15:
                posImage.setImageResource(R.drawable.mark_position_rap);
                break;
            case 16:
                posImage.setImageResource(R.drawable.mark_position_compose);
                break;



        }




        //genre

        switch (genre){
            case 0:
                genreImage.setImageResource(R.drawable.mark_genre_balled);
                break;
            case 1:
                genreImage.setImageResource(R.drawable.mark_genre_rb);
                break;
            case 2:
                genreImage.setImageResource(R.drawable.mark_genre_hiphop);
                break;
            case 3:
                genreImage.setImageResource(R.drawable.mark_genre_rock);
                break;
            case 4:
                genreImage.setImageResource(R.drawable.mark_genre_dance);
                break;
            case 5:
                genreImage.setImageResource(R.drawable.mark_genre_indi);
                break;
            case 6:
                genreImage.setImageResource(R.drawable.mark_genre_ellec);
                break;
            case 7:
                genreImage.setImageResource(R.drawable.mark_genre_trot);
                break;

        }

    }
}
