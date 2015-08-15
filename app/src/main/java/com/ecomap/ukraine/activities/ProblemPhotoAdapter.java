package com.ecomap.ukraine.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ecomap.ukraine.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProblemPhotoAdapter extends BaseAdapter {

    private final Context context;
    private List<String> urls;

    public ProblemPhotoAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView squaredImageView = (ImageView) convertView;
        if (convertView == null) {
            squaredImageView = new ImageView(context);
        }

        Picasso.with(context)
                .load(urls.get(position))
                .placeholder(R.drawable.resolved2)
                .error(R.drawable.photo_error1)
                .into(squaredImageView);

        squaredImageView.setAdjustViewBounds(true);
        squaredImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return squaredImageView;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
