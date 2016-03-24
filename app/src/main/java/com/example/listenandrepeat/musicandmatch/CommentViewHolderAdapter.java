package com.example.listenandrepeat.musicandmatch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ListenAndRepeat on 2016. 3. 8..
 */
public class CommentViewHolderAdapter extends RecyclerView.Adapter<CommentViewHolder>
 implements CommentViewHolder.OnButtonClickListener{
    List<CommentItem>items = new ArrayList<CommentItem>();
    public void addAll(List<CommentItem> items){
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
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        holder.setOnButtonClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.setCommentItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public CommentItem getItem(int position){
        if(position < 0 || position >= items.size()) return null;

        return items.get(position);
    }

    @Override
    public void onEditBtnClick(View view, CommentItem commentItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(commentItem);
            mAdapterListener.onAdapterItemEditBtnClick(this,view,commentItem,index);
        }
    }

    @Override
    public void onDeleteBtnClick(View view, CommentItem commentItem) {
        if(mAdapterListener != null){
            int index = items.indexOf(commentItem);
            mAdapterListener.onAdapterItemDeleteBtnClick(this,view,commentItem,index);
        }
    }

    public interface OnCommnetViewHolderAdapterItemClickListener{
        public void onAdapterItemDeleteBtnClick(CommentViewHolderAdapter adapter,View view,CommentItem commentItem,int position);
        public void onAdapterItemEditBtnClick(CommentViewHolderAdapter adapter,View view,CommentItem commentItem,int position);

    }

    OnCommnetViewHolderAdapterItemClickListener mAdapterListener;

    public void setOnAdapterItemClickListener(OnCommnetViewHolderAdapterItemClickListener listener){
        mAdapterListener = listener;
    }
}
