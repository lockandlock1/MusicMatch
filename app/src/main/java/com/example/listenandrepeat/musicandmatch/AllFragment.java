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

import com.example.listenandrepeat.musicandmatch.DataClass.AllListlResult;
import com.example.listenandrepeat.musicandmatch.ManagerClass.NetworkManager;

import java.io.UnsupportedEncodingException;

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
    RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                Toast.makeText(getContext(), "EditImage Click : " , Toast.LENGTH_SHORT).show();
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
        });


        try {
            NetworkManager.getInstance().getAllList(getContext(), 1,new NetworkManager.OnResultListener<AllListlResult>() {
                @Override
                public void onSuccess(Request request, AllListlResult result) {


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

        

        return view;



    }


}
