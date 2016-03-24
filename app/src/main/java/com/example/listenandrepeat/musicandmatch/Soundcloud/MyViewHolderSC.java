package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listenandrepeat.musicandmatch.R;

/**
 * Created by Tacademy on 2016-03-24.
 */
public class MyViewHolderSC extends RecyclerView.ViewHolder {

    ImageView iconView;
    TextView titleView;
    MyDataSC data;

    public OnItemClickListenerSC itemClickListener;
    public void setOnItemClickListener(OnItemClickListenerSC listener) {
        itemClickListener = listener;
    }

    public MyViewHolderSC(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        });
        iconView = (ImageView)itemView.findViewById(R.id.image_icon);
        titleView = (TextView)itemView.findViewById(R.id.text_title);
    }

    public void setData(MyDataSC data) {
        this.data = data;
        iconView.setImageDrawable(data.icon);
        titleView.setText(data.title);
        titleView.setTextSize(data.fontSize);
    }
}
