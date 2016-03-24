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
public class MyAdapterSC extends RecyclerView.Adapter<MyViewHolderSC> implements MyViewHolderSC.OnImageClickListenerSC {

    List<MyDataSC> items = new ArrayList<MyDataSC>();

    public void addAll(List<MyDataSC> items){
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    public void clearAll(){
        items.clear();
        notifyDataSetChanged();
    }


    private int pageNumber;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }


    public void add(MyDataSC data) {
        items.add(data);
        notifyDataSetChanged();
    }

//    OnItemClickListenerSC itemClickListener;
//    public void setOnItemClickListener(OnItemClickListenerSC listener) {
//        itemClickListener = listener;
//    }

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
        holder.setContentsItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public MyDataSC getItem(int position) {
        if (position < 0 || position >= items.size()) return null;

        return items.get(position);
    }

    @Override
    public void onCommentImageClick(View view, MyDataSC contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);
            mAdapterListener.onAdapterItemCommentImageClick(this,view,contentsItem,index);
        }
    }

    @Override
    public void onLikeImageClick(View view, MyDataSC contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);
            mAdapterListener.onAdpaterItemLikeImageClick(this,view,contentsItem,index);
        }

    }

    @Override
    public void onLayoutImageClick(View view, MyDataSC contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);
            mAdapterListener.onAdapterItemLayoutClick(this,view,contentsItem,index);
        }

    }

//    public OnViewHolderAdapterItemClickListener getmAdapterListener() {
//        if(mAdapterListener != null){
//            int index = items.indexOf(contentsItem);
//            mAdapterListener.onAdpaterItemLikeImageClick(this,view,contentsItem,index);
//        }
//    }


    public interface OnViewHolderAdapterItemClickListener{

        public void onAdapterItemLayoutClick(MyAdapterSC adapter,View view,MyDataSC item,int position);
        public void onAdpaterItemLikeImageClick(MyAdapterSC adapter,View view,MyDataSC item,int position);
        public void onAdapterItemCommentImageClick(MyAdapterSC adapter,View view,MyDataSC item,int position);

    }

    OnViewHolderAdapterItemClickListener mAdapterListener;
    public void setOnAdapterItemClickListener(OnViewHolderAdapterItemClickListener listener){
        mAdapterListener = listener;
    }
}
