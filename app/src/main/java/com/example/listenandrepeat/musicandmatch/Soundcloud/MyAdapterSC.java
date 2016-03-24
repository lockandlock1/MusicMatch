package com.example.listenandrepeat.musicandmatch.Soundcloud;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.listenandrepeat.musicandmatch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-03-24.
 */
public class MyAdapterSC extends RecyclerView.Adapter<MyViewHolderSC> implements OnItemClickListenerSC {

    List<MyDataSC> items = new ArrayList<MyDataSC>();

    public void add(MyDataSC data) {
        items.add(data);
        notifyDataSetChanged();
    }

    OnItemClickListenerSC itemClickListener;
    public void setOnItemClickListener(OnItemClickListenerSC listener) {
        itemClickListener = listener;
    }

    @Override
    public MyViewHolderSC onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_sc_item, parent, false);
        MyViewHolderSC holder = new MyViewHolderSC(view);
        holder.setOnItemClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolderSC holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(view, position);
        }
    }

    public MyDataSC getItem(int position) {
        if (position < 0 || position >= items.size()) return null;

        return items.get(position);
    }
}
