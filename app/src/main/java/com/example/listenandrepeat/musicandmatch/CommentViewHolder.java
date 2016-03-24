package com.example.listenandrepeat.musicandmatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    ImageView btnDel,btnEdit;
    Spinner spinner;
    SpinnerAdapter mAdapter;

    public interface OnButtonClickListener{
        public void onEditBtnClick(View view,CommentItem commentItem);
        public void onDeleteBtnClick(View view,CommentItem commentItem);
    }

    OnButtonClickListener mButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener){
        mButtonClickListener = listener;
    }

    public CommentViewHolder(final View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        profileImage = (ImageView)itemView.findViewById(R.id.image_profile);
        genreImage = (ImageView)itemView.findViewById(R.id.image_genre);
        positionImage = (ImageView)itemView.findViewById(R.id.image_position);
        nickText = (TextView)itemView.findViewById(R.id.text_nickname);
        contentsText = (TextView)itemView.findViewById(R.id.text_contents);
        dateText = (TextView)itemView.findViewById(R.id.text_date);
        btnDel = (ImageView)itemView.findViewById(R.id.btn_del);
        btnEdit = (ImageView)itemView.findViewById(R.id.btn_edit);

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButtonClickListener != null){
                    mButtonClickListener.onDeleteBtnClick(itemView,item);
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonClickListener != null){
                    mButtonClickListener.onEditBtnClick(itemView,item);
                }
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
            profileImage.setImageResource(R.drawable.circle_profile);
        }

        if(PropertyManager.getInstance().getMid() != c.mid){

            btnDel.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
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

        nickText.setText(c.nickname);
        contentsText.setText(c.content);
        dateText.setText(c.date);


    }



}
