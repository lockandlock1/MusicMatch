package com.example.listenandrepeat.musicandmatch.ManagerClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.listenandrepeat.musicandmatch.MyApplication;

/**
 * Created by ListenAndRepeat on 2016. 2. 22..
 */
public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance(){
        if(instance == null){
            instance = new PropertyManager();
        }
        return instance;
    }

    private static final String FIELD_USER_ID = "userid";
    private static final String FIELD_PASSWORD = "password";


    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager(){
        Context context = MyApplication.getContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    int mID = -1;
    public void  setMid(int mid){
        mID = mid;

    }
    public int  getMid() {
        return mID;
    }

    public void setUserId(String userId){
        mEditor.putString(FIELD_USER_ID, userId);
        mEditor.commit();
    }

    public String getUserId() {
        return mPrefs.getString(FIELD_USER_ID,"");
    }

    public void setPassword(String password) {
        mEditor.putString(FIELD_PASSWORD, password);
        mEditor.commit();
    }

    public String getPassword() {
        return mPrefs.getString(FIELD_PASSWORD, "");
    }

}
