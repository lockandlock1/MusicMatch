package com.example.listenandrepeat.musicandmatch.DataClass;

import com.example.listenandrepeat.musicandmatch.ContentsItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ListenAndRepeat on 2016. 3. 4..
 */
public class AllList {
    public String message;
    public int page;
    public int pageLimit;
    @SerializedName("data")
    public ArrayList<ContentsItem> items;


}
