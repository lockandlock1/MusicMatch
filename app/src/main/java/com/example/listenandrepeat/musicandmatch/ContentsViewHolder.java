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
public class ContentsViewHolder extends RecyclerView.ViewHolder {
    ImageView profileImage , genreImage , positionImage  , likeImage , commentImage ,editImage , contentImage;
    TextView nickText , contentsText , dateText ,likeNum , commentNum ;

    Context mContext;

    public interface OnImageClickListener{

        public void onCommentImageClick(View view,ContentsItem contentsItem);
        public void onLikeImageClick(View view,ContentsItem contentsItem);
        public void onEditImageClick(View view,ContentsItem contentsItem);

    }
    OnImageClickListener mImageClickListener;
    public void setOnImageClickListener(OnImageClickListener listener){
        mImageClickListener = listener;
    }



    public ContentsViewHolder(final View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        contentImage = (ImageView)itemView.findViewById(R.id.image_content);
        profileImage = (ImageView)itemView.findViewById(R.id.image_profile);
        genreImage = (ImageView)itemView.findViewById(R.id.image_genre);
        positionImage = (ImageView)itemView.findViewById(R.id.image_position);
        commentImage = (ImageView)itemView.findViewById(R.id.image_comment);
        likeImage = (ImageView)itemView.findViewById(R.id.image_like);
        editImage = (ImageView)itemView.findViewById(R.id.image_edit);
        nickText = (TextView)itemView.findViewById(R.id.text_nick);
        contentsText = (TextView)itemView.findViewById(R.id.text_contents);
        dateText = (TextView)itemView.findViewById(R.id.text_date);
        likeNum = (TextView)itemView.findViewById(R.id.text_like);
        commentNum = (TextView)itemView.findViewById(R.id.text_comment);

        commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onCommentImageClick(itemView,item);
                }
            }
        });
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onLikeImageClick(itemView,item);
                }
            }
        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageClickListener != null){
                    mImageClickListener.onEditImageClick(itemView,item);
                }
            }
        });
    }

    ContentsItem item;
    public void setContentsItem(ContentsItem c){
        item = c;
        // profile

        if(!TextUtils.isEmpty(c.profile)){
            Glide.with(mContext)
                    .load(c.profile)
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.mipmap.ic_launcher);
        }

        if(c.photo != null && c.photo.size() > 0 && !TextUtils.isEmpty(c.photo.get(0))){
            Glide.with(mContext)
                    .load(c.photo.get(0))
                    .into(contentImage);
        } else{
            contentImage.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(c.content)){
            contentsText.setText(c.content);
        } else{
            contentsText.setVisibility(View.INVISIBLE);
        }
      /*
        //pos

        switch (c.position){
            default:
                positionImage.setImageResource(R.mipmap.ic_launcher);

        }




        //genre

        switch (c.genre){
            default:
                genreImage.setImageResource(R.mipmap.ic_launcher);
        }

*/





        //contents

        likeImage.setImageResource(R.mipmap.ic_launcher);
        commentImage.setImageResource(R.mipmap.ic_launcher);


        editImage.setImageResource(R.mipmap.ic_launcher);
        dateText.setText(c.date);
        nickText.setText(c.nickname);

    }

}
