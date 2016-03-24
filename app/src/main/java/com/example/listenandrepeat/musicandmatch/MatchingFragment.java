package com.example.listenandrepeat.musicandmatch;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.ListDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.StoryWriteResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchingFragment extends Fragment {


    public MatchingFragment() {
        // Required empty public constructor
    }

    public static MatchingFragment newInstance(){
        MatchingFragment fragment = new MatchingFragment();

        return fragment;
    }

    RecyclerView recyclerView;
    ContentsViewHolderAdapter mAdapter;
    LinearLayoutManager layoutManager;
    Button floatingBtn;

    boolean isLast = false;
    boolean isMoreData = false;
    int postId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matching,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        floatingBtn = (Button)view.findViewById(R.id.btn_edit);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MatchingWriteActivity.class);
                startActivity(intent);
            }
        });
        mAdapter = new ContentsViewHolderAdapter();
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setOnAdapterItemClickListener(new ContentsViewHolderAdapter.OnViewHolderAdapterItemClickListener() {
            @Override
            public void onAdapterItemEditImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Intent intent = new Intent(getActivity(), StoryEditActivity.class);

                int postId = adapter.items.get(position).pid;

                String content = adapter.items.get(position).content;
                String photo = null;

                if(adapter.items.get(position).photo != null && adapter.items.get(position).photo.size() > 0 && !TextUtils.isEmpty(adapter.items.get(position).photo.get(0))){
                    photo = adapter.items.get(position).photo.get(0);
                    intent.putExtra(StoryEditActivity.PHOTO,photo);
                }
                intent.putExtra(StoryEditActivity.CONTENT,content);
                intent.putExtra(StoryEditActivity.POST_ID, postId);

                startActivity(intent);
            }

            @Override
            public void onAdpaterItemLikeImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Toast.makeText(getContext(),"LikeImage Click : " + adapter.items.get(position).pid,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterItemCommentImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Intent intent = new Intent(getActivity(),CommentActivity.class);
                postId = adapter.items.get(position).pid;
                //Toast.makeText(getContext(),"CommentImage Click : " + PostId,Toast.LENGTH_SHORT).show();
                intent.putExtra(CommentActivity.PARAM_POST_ID, postId);
                startActivity(intent);

            }

            @Override
            public void onAdapterItemNickNameTextClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                ((MainActivity) getActivity()).changeMusicStory(adapter.items.get(position).mid);
            }

            @Override
            public void onAdapterItemDeleteImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                matchingDelete(adapter.items.get(position).pid);
            }

            @Override
            public void onAdpaterItemMoreImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {

            }


        });


        try {
            NetworkManager.getInstance().getMatchingList(getContext(), 1,"","people",new NetworkManager.OnResultListener<ListDetailResult>() {
                @Override
                public void onSuccess(Request request, ListDetailResult result) {
                    mAdapter.clearAll();
                    mAdapter.addAll(result.success.items);
                    mAdapter.setPageNumber(result.success.page);
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(isLast && newState == RecyclerView.SCROLL_STATE_IDLE){
                    getMoreItem();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                if(totalItemCount > 0 && lastVisibleItemPosition != RecyclerView.NO_POSITION && (totalItemCount - 1 <= lastVisibleItemPosition)){
                    isLast = true;
                } else {
                    isLast =false;
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    //************************************************************************************************************************
    ////Function
    private void matchingDelete(int pid){
        try {
            NetworkManager.getInstance().deleteStory(getContext(), pid, new NetworkManager.OnResultListener<StoryWriteResult>() {
                @Override
                public void onSuccess(Request request, StoryWriteResult result) {
                    try {
                        NetworkManager.getInstance().getMatchingList(getContext(), 1, "", "people", new NetworkManager.OnResultListener<ListDetailResult>() {
                            @Override
                            public void onSuccess(Request request, ListDetailResult result) {
                                mAdapter.clear();
                                mAdapter.addAll(result.success.items);
                                mAdapter.setPageNumber(result.success.page);;
                            }

                            @Override
                            public void onFailure(Request request, int code, Throwable cause) {

                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {
                    Toast.makeText(getContext(),"게시글 삭제실패",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            NetworkManager.getInstance().getMatchingList(getContext(), 1, "", "people", new NetworkManager.OnResultListener<ListDetailResult>() {
                @Override
                public void onSuccess(Request request, ListDetailResult result) {
                    mAdapter.clearAll();
                    mAdapter.addAll(result.success.items);
                    mAdapter.setPageNumber(result.success.page);
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getMoreItem(){

        if(isMoreData) return;

        isMoreData = true;

        if(0 < mAdapter.getPageNumber()){
            int start = mAdapter.getPageNumber() + 1;

            try {
                NetworkManager.getInstance().getMatchingList(getContext(), start, "", "people", new NetworkManager.OnResultListener<ListDetailResult>() {
                    @Override
                    public void onSuccess(Request request, ListDetailResult result) {
                        mAdapter.addAll(result.success.items);
                        isMoreData = false;
                    }

                    @Override
                    public void onFailure(Request request, int code, Throwable cause) {
                        isMoreData = false;
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                isMoreData = false;
            }
        }


    }
}
