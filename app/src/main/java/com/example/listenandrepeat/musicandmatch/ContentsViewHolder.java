package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;

/**
 * Created by ListenAndRepeat on 2016. 3. 8..
 */
public class ContentsViewHolder extends RecyclerView.ViewHolder {
    ImageView profileImage , genreImage , positionImage  , likeImage , commentImage ,editImage , contentImage ,btnDel , moreImage;
    TextView nickText , contentsText , dateText ,likeNum , commentNum ;

    Context mContext;

    public interface OnImageClickListener{

        public void onCommentImageClick(View view,ContentsItem contentsItem);
        public void onLikeImageClick(View view,ContentsItem contentsItem);
        public void onEditImageClick(View view,ContentsItem contentsItem);
        public void onNickNameClick(View view,ContentsItem contentsItem);
        public void onDeleteImageClick(View view,ContentsItem contentsItem);
        public void onMoreImageClick(View view,ContentsItem contentsItem);
    }
    OnImageClickListener mImageClickListener;
    public void setOnImageClickListener(OnImageClickListener listener){
        mImageClickListener = listener;
    }



    public ContentsViewHolder(final View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        btnDel =(ImageView)itemView.findViewById(R.id.btn_del);
        contentImage = (ImageView)itemView.findViewById(R.id.image_content);
        profileImage = (ImageView)itemView.findViewById(R.id.image_profile);
        genreImage = (ImageView)itemView.findViewById(R.id.image_genre);
        positionImage = (ImageView)itemView.findViewById(R.id.image_position);
        commentImage = (ImageView)itemView.findViewById(R.id.image_comment);
        likeImage = (ImageView)itemView.findViewById(R.id.image_like);
        editImage = (ImageView)itemView.findViewById(R.id.image_edit);
        nickText = (TextView)itemView.findViewById(R.id.text_nickname);
        contentsText = (TextView)itemView.findViewById(R.id.text_contents);
        dateText = (TextView)itemView.findViewById(R.id.text_date);
        likeNum = (TextView)itemView.findViewById(R.id.text_like);
        commentNum = (TextView)itemView.findViewById(R.id.text_comment);
        moreImage = (ImageView)itemView.findViewById(R.id.image_more);


        moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onMoreImageClick(itemView,item);
                }
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onDeleteImageClick(itemView,item);
                }
            }
        });
        commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onCommentImageClick(itemView, item);
                }
            }
        });
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onLikeImageClick(itemView, item);

                }

            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageClickListener != null){
                    mImageClickListener.onEditImageClick(itemView, item);

                }

            }
        });


        nickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageClickListener != null){
                    mImageClickListener.onNickNameClick(itemView, item);
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
            contentImage.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(c.content)){
            contentsText.setText(c.content);
        } else{
            contentsText.setVisibility(View.GONE);
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



        }
//



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

        }






        if(PropertyManager.getInstance().getMid() == c.mid ){

            editImage.setVisibility(View.VISIBLE);
            btnDel.setVisibility(View.VISIBLE);
            moreImage.setVisibility(View.GONE);

        } else {

            editImage.setVisibility(View.GONE);
            btnDel.setVisibility(View.GONE);

            moreImage.setVisibility(View.VISIBLE);

        }
        //contents

        likeImage.setImageResource(R.drawable.ic_favorite);
        commentImage.setImageResource(R.drawable.ic_chat);



        dateText.setText(c.date);
        nickText.setText(c.nickname);

    }

}
