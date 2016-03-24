package com.example.listenandrepeat.musicandmatch;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.ListDetailResult;
import com.example.listenandrepeat.musicandmatch.DataClass.StoryWriteResult;
import com.example.listenandrepeat.musicandmatch.Login.PasswordFindNewPasswordActivity;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import okhttp3.Request;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";

    private String message;

    public AllFragment() {
        // Required empty public constructor
    }
    public static AllFragment newInstance() {
        AllFragment fragment = new AllFragment();
//        Bundle args = new Bundle();
 //       args.putString(ARG_MESSAGE, message);
  //      fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    RecyclerView recyclerView;
    Button floatingBtn;
    ContentsViewHolderAdapter mAdapter;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    boolean isLast = false;



    int pid;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        floatingBtn = (Button)view.findViewById(R.id.btn_edit);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoryWriteActivity.class);
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
               // Toast.makeText(getContext(), "postmid : " + adapter.items.get(position).mid + ", myID : " + PropertyManager.getInstance().getMid() , Toast.LENGTH_SHORT).show();


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


                Toast.makeText(getContext(), "LikeImage Click : " , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterItemCommentImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                int postId = adapter.items.get(position).pid;

             //   Toast.makeText(getContext(),"CommentImage Click : " + PostId,Toast.LENGTH_SHORT).show();
                intent.putExtra(CommentActivity.PARAM_POST_ID, postId);
                startActivity(intent);
            }

            @Override
            public void onAdapterItemNickNameTextClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {


                        ((MainActivity) getActivity()).changeMusicStory(adapter.items.get(position).mid);
            }

            @Override
            public void onAdapterItemDeleteImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {


                storyDelete(adapter.items.get(position).pid);
            }

            @Override
            public void onAdpaterItemMoreImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Toast.makeText(getContext(), "MoreImage Click : " , Toast.LENGTH_SHORT).show();
            }
        });


        try {
            NetworkManager.getInstance().getAllList(getContext(),1,new NetworkManager.OnResultListener<ListDetailResult>() {
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


        

        return view;



    }
    boolean isMoreData =  false;

 ///   ProgressDialog dialog = null;
    private void getMoreItem(){
        if(isMoreData) return;

        isMoreData = true;

        if(0 < mAdapter.getPageNumber()){
            int start = mAdapter.getPageNumber() + 1;

            mAdapter.setPageNumber(start);

            try {
                NetworkManager.getInstance().getAllList(getContext(), start, new NetworkManager.OnResultListener<ListDetailResult>() {
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

    private void storyDelete(int pid){
        try {
            NetworkManager.getInstance().deleteStory(getContext(), pid, new NetworkManager.OnResultListener<StoryWriteResult>() {
                @Override
                public void onSuccess(Request request, StoryWriteResult result) {
                    Toast.makeText(getContext(),"게시글 삭제 되었습니다.",Toast.LENGTH_SHORT).show();
                    try {
                        NetworkManager.getInstance().getAllList(getContext(), 1, new NetworkManager.OnResultListener<ListDetailResult>() {
                            @Override
                            public void onSuccess(Request request, ListDetailResult result) {
                                mAdapter.clearAll();
                                mAdapter.addAll(result.success.items);

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
                    Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();
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
            NetworkManager.getInstance().getAllList(getContext(), 1, new NetworkManager.OnResultListener<ListDetailResult>() {
                @Override
                public void onSuccess(Request request, ListDetailResult result) {
                    mAdapter.clearAll();
                    mAdapter.addAll(result.success.items);
                }

                @Override
                public void onFailure(Request request, int code, Throwable cause) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
