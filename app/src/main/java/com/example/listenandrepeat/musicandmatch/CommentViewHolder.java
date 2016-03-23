package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;

/**
 * Created by ListenAndRepeat on 2016. 3. 8..
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    ImageView profileImage , genreImage , positionImage;
    TextView nickText, contentsText,dateText;
    CommentItem item;
    Context mContext;

    Spinner spinner;
    SpinnerAdapter mAdapter;
    public CommentViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        profileImage = (ImageView)itemView.findViewById(R.id.image_profile);
        genreImage = (ImageView)itemView.findViewById(R.id.image_genre);
        positionImage = (ImageView)itemView.findViewById(R.id.image_position);
        nickText = (TextView)itemView.findViewById(R.id.text_nick);
        contentsText = (TextView)itemView.findViewById(R.id.text_contents);
        dateText = (TextView)itemView.findViewById(R.id.text_date);

        spinner = (Spinner)itemView.findViewById(R.id.spinner);
        mAdapter = new SpinnerAdapter();
        spinner.setAdapter(mAdapter);
        SpinnerItem s1 = new SpinnerItem(R.drawable.ic_create_button,"Edit");

        SpinnerItem s2 = new SpinnerItem(R.drawable.ic_delete_button,"Delete");

        mAdapter.add(s1);
        mAdapter.add(s2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext,"select " + position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void setCommentItem(CommentItem c){
        item = c;

        if(!TextUtils.isEmpty(c.profile)){
            Glide.with(mContext)
                    .load(c.profile)
                    .into(profileImage);
        }else {
            profileImage.setImageResource(R.mipmap.ic_launcher);
        }

        if(PropertyManager.getInstance().getMid() != c.mid){

            spinner.setVisibility(View.GONE);
        }

        //pos

        switch (c.position){

            case 10:
                positionImage.setImageResource(R.drawable.mark_position_base);
                break;
            case 11:
                positionImage.setImageResource(R.drawable.mark_position_guitar);
                break;
            case 12:
                positionImage.setImageResource(R.drawable.mark_position_drum);
                break;
            case 13:
                positionImage.setImageResource(R.drawable.mark_position_keyboard);
                break;
            case 14:
                positionImage.setImageResource(R.drawable.mark_position_vocal);
                break;
            case 15:
                positionImage.setImageResource(R.drawable.mark_position_rap);
                break;
            case 16:
                positionImage.setImageResource(R.drawable.mark_position_compose);
                break;

            default:
                positionImage.setImageResource(R.mipmap.ic_launcher);

        }




        //genre

        switch (c.genre){
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
            default:
                genreImage.setImageResource(R.mipmap.ic_launcher);
        }

        nickText.setText(c.nickname);
        contentsText.setText(c.content);
        dateText.setText(c.date);


    }



}
