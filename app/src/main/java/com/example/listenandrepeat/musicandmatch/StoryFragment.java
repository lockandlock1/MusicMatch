package com.example.listenandrepeat.musicandmatch;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.listenandrepeat.musicandmatch.DataClass.ListDetailResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;
import com.example.listenandrepeat.musicandmatch.ManagerClass.PropertyManager;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment {



    public StoryFragment() {
        // Required empty public constructor
    }

    public static StoryFragment newInstance(int mid){
        StoryFragment fragment = new StoryFragment();
        Bundle b = new Bundle();
        b.putInt("mid", mid);
        fragment.setArguments(b);
        return fragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mid = getArguments().getInt("mid");
      //  Toast.makeText(getContext(),"Story"+mid,Toast.LENGTH_SHORT).show();


    }

    RecyclerView recyclerView;
    ContentsViewHolderAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button floatingBtn;
    int mid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        floatingBtn = (Button) view.findViewById(R.id.btn_edit);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter = new ContentsViewHolderAdapter();
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setOnAdapterItemClickListener(new ContentsViewHolderAdapter.OnViewHolderAdapterItemClickListener() {
            @Override
            public void onAdapterItemEditImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Toast.makeText(getContext(), "EditImage Click :", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdpaterItemLikeImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Toast.makeText(getContext(), "LikeImage Click :", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterItemCommentImageClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                int PostId = adapter.items.get(position).pid;
                intent.putExtra(CommentActivity.PARAM_POST_ID, PostId);
                startActivity(intent);
            }

            @Override
            public void onAdapterItemNickNameTextClick(ContentsViewHolderAdapter adapter, View view, ContentsItem item, int position) {

            }
        });

        try {
            NetworkManager.getInstance().getMyStroyList(getContext(), 1, "", "story",mid, new NetworkManager.OnResultListener<ListDetailResult>() {
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
        // Inflate the layout for this fragment
        return view;
    }


}
