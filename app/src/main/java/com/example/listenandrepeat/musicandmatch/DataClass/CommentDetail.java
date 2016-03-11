package com.example.listenandrepeat.musicandmatch.DataClass;

import com.example.listenandrepeat.musicandmatch.CommentItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ListenAndRepeat on 2016. 3. 7..
 */
public class CommentDetail {
    public String message;
    public int page;
    public int pageLimit;
    @SerializedName("data")
    public ArrayList<CommentItem> items;
}
