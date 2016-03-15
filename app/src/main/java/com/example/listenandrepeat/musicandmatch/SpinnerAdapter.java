package com.example.listenandrepeat.musicandmatch;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by ListenAndRepeat on 2016. 3. 15..
 */
public class SpinnerAdapter extends BaseAdapter {

    ArrayList<SpinnerItem> items = new ArrayList<SpinnerItem>();

    public void add(SpinnerItem s){
        items.add(s);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public SpinnerItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerItemView view;
        if(convertView == null){
            view = new SpinnerItemView(parent.getContext());

        } else {
            view = (SpinnerItemView)convertView;
        }
        view.setSpinner(items.get(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        SpinnerItemView view;

        if(convertView == null){
            view = new SpinnerItemView(parent.getContext());

        } else {
            view = (SpinnerItemView)convertView;
        }
        view.setSpinner(items.get(position));

        return view;

    }
}
