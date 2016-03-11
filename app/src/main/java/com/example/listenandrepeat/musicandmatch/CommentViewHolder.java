package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by ListenAndRepeat on 2016. 3. 8..
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    ImageView profileImage , genreImage , positionImage;
    TextView nickText, contentsText,dateText;
    CommentItem item;
    Context mContext;

    public CommentViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        profileImage = (ImageView)itemView.findViewById(R.id.image_profile);
        genreImage = (ImageView)itemView.findViewById(R.id.image_genre);
        positionImage = (ImageView)itemView.findViewById(R.id.image_position);
        nickText = (TextView)itemView.findViewById(R.id.text_nick);
        contentsText = (TextView)itemView.findViewById(R.id.text_contents);
        dateText = (TextView)itemView.findViewById(R.id.text_date);


    }
    public void setCommentItem(CommentItem c){
        item = c;

        if(!TextUtils.isEmpty(c.photo)){
            Glide.with(mContext)
                    .load(c.photo)
                    .into(profileImage);
        }else {
            profileImage.setImageResource(R.mipmap.ic_launcher);
        }

        nickText.setText(c.nickname);
        contentsText.setText(c.content);
        dateText.setText(c.date);


    }

}
