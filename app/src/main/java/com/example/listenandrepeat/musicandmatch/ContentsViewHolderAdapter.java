package com.example.listenandrepeat.musicandmatch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ListenAndRepeat on 2016. 3. 8..
 */
public class ContentsViewHolderAdapter extends RecyclerView.Adapter<ContentsViewHolder>
 implements ContentsViewHolder.OnImageClickListener{

    List<ContentsItem> items = new ArrayList<ContentsItem>();

    public void addAll(List<ContentsItem> items){
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    public void clearAll(){
        items.clear();
        notifyDataSetChanged();
    }
    public void clear(){
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

    @Override
    public ContentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_contents, parent, false);
        ContentsViewHolder holder = new ContentsViewHolder(view);
        holder.setOnImageClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentsViewHolder holder, int position) {
        holder.setContentsItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ContentsItem getItem(int position){
        if(position < 0 || position >= items.size()) return null;

        return items.get(position);
    }

    @Override
    public void onCommentImageClick(View view, ContentsItem contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);

            mAdapterListener.onAdapterItemCommentImageClick(this,view,contentsItem,index);
        }
    }

    @Override
    public void onLikeImageClick(View view, ContentsItem contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);
            mAdapterListener.onAdpaterItemLikeImageClick(this,view,contentsItem,index);
        }
    }

    @Override
    public void onEditImageClick(View view, ContentsItem contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);
            mAdapterListener.onAdapterItemEditImageClick(this,view,contentsItem,index);
        }
    }

    @Override
    public void onNickNameClick(View view, ContentsItem contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);
            mAdapterListener.onAdapterItemNickNameTextClick(this, view, contentsItem, index);
        }
    }

    @Override
    public void onDeleteImageClick(View view, ContentsItem contentsItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(contentsItem);
            mAdapterListener.onAdapterItemDeleteImageClick(this,view,contentsItem,index);
        }
    }

    @Override
    public void onMoreImageClick(View view, ContentsItem contentsItem) {

    }

    public interface OnViewHolderAdapterItemClickListener{

        public void onAdapterItemEditImageClick(ContentsViewHolderAdapter adapter,View view,ContentsItem item,int position);
        public void onAdpaterItemLikeImageClick(ContentsViewHolderAdapter adapter,View view,ContentsItem item,int position);
        public void onAdapterItemCommentImageClick(ContentsViewHolderAdapter adapter,View view,ContentsItem item,int position);
        public void onAdapterItemNickNameTextClick(ContentsViewHolderAdapter adapter,View view,ContentsItem item,int position);
        public void onAdapterItemDeleteImageClick(ContentsViewHolderAdapter adapter,View view,ContentsItem item,int position);
        public void onAdpaterItemMoreImageClick(ContentsViewHolderAdapter adapter,View view,ContentsItem item,int position);
    }
    OnViewHolderAdapterItemClickListener mAdapterListener;
    public void setOnAdapterItemClickListener(OnViewHolderAdapterItemClickListener listener){
        mAdapterListener = listener;
    }
}
