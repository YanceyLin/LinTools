package com.lxb.lintools.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxb.lintools.R;

import java.util.ArrayList;
import java.util.List;

public class ViewpageAdapter extends PagerAdapter {

    Context context;
    List<View> views = new ArrayList<>();

    public ViewpageAdapter(Context context) {
        this.context = context;


        views.add(LayoutInflater.from(context).inflate(R.layout.item1, null));
        views.add(LayoutInflater.from(context).inflate(R.layout.item2, null));
        views.add(LayoutInflater.from(context).inflate(R.layout.item3, null));
        views.add(LayoutInflater.from(context).inflate(R.layout.item4, null));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = views.get(position);
        container.removeView(view);
    }
}
