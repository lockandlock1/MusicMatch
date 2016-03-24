package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.listenandrepeat.musicandmatch.R;

/**
 * Created by Tacademy on 2016-03-24.
 */
public class MyViewHolderSC extends RecyclerView.ViewHolder {

    ImageView iconView;
    ImageView likeImageView;
    ImageView commentImageView;
    TextView nicknameView;
    TextView titleView;
    TextView textView;
    TextView durationView;
    TextView likeView;
    TextView commentView;

    RelativeLayout relativeLayout;

    Context mContext;

    MyDataSC data;

    public interface OnImageClickListenerSC{

        public void onCommentImageClick(View view,MyDataSC contentsItem);
        public void onLikeImageClick(View view,MyDataSC contentsItem);
        public void onLayoutImageClick(View view, MyDataSC contentsItem);
    }

    OnImageClickListenerSC mImageClickListener;
    public void setOnItemClickListener(OnImageClickListenerSC listener) {
        mImageClickListener = listener;
    }

    public MyViewHolderSC(final View itemView) {
        super(itemView);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClickListener != null) {
//                    itemClickListener.onItemClick(v, getAdapterPosition());
//                }
//            }
//        });
        mContext = itemView.getContext();
        iconView = (ImageView) itemView.findViewById(R.id.image_icon);
        likeImageView = (ImageView) itemView.findViewById(R.id.image_like);
        commentImageView = (ImageView) itemView.findViewById(R.id.image_comment);
        nicknameView = (TextView) itemView.findViewById(R.id.text_nickname);
        titleView = (TextView) itemView.findViewById(R.id.text_title);
        textView = (TextView) itemView.findViewById(R.id.text_view);
        durationView = (TextView) itemView.findViewById(R.id.text_duration);
        likeView = (TextView) itemView.findViewById(R.id.text_like);
        commentView = (TextView) itemView.findViewById(R.id.text_comment);
        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onLayoutImageClick(itemView, item);
                }
            }
        });

        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onLikeImageClick(itemView, item);
                }
            }
        });

        commentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickListener != null){
                    mImageClickListener.onCommentImageClick(itemView, item);
                }
            }
        });

    }



    MyDataSC item;

    public void setContentsItem(MyDataSC c){
        item = c;
        // profile

        iconView.setImageResource(R.mipmap.ic_launcher);
        if(!TextUtils.isEmpty(c.icon_image)){
            Glide.with(mContext)
                    .load(c.icon_image)
                    .into(iconView);

        } else {
            iconView.setImageResource(R.mipmap.ic_launcher);
        }

        likeImageView.setImageResource(R.drawable.ic_favorite);
        commentImageView.setImageResource(R.drawable.ic_chat);

        nicknameView.setText(c.nickname);
        titleView.setText(c.title);
        durationView.setText(c.duration);

    }


//    public void setData(MyDataSC data) {
//        this.data = data;
//        iconView.setImageDrawable(data.icon_image);
//        likeImageView.setImageDrawable(data.like_image);
//        commentImageView.setImageDrawable(data.comment_image);
//        nicknameView.setText(data.nickname);
//        titleView.setText(data.title);
//        textView.setText(data.text);
//        durationView.setText(data.duration);
//        likeView.setText(data.like);
//        commentView.setText(data.comment);
//    }
}
