package com.example.listenandrepeat.musicandmatch;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.sql.Struct;
import java.util.ArrayList;

/**
 * Created by ListenAndRepeat on 2016. 2. 22..
 */
public class ContentsItem {
    String title;
    String date;
    int genre;
    int position;
    String nickname;
    public ArrayList<String> photo;
    String profile;
    String content;
    int pid;
    int mid;

    @SerializedName("limit_people")
    int limitPeople;
    @SerializedName("decide_people")
    int decidePeople;






}
