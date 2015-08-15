package com.ecomap.ukraine.activities;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ecomap.ukraine.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProblemPhotoSlidePager extends FragmentActivity {

    private static List<String> urls;

    public static void setContent(List<String> urls) {
        ProblemPhotoSlidePager.urls = urls;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<>();
        int position = getIntent().getIntExtra("position", 0);

        for (String url : urls) {
            View page = inflater.inflate(R.layout.activity_problem_photo_slide_pager, null);
            ImageView photoView = (ImageView) page.findViewById(R.id.problem_photo);
            Picasso.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.resolved2)
                    .error(R.drawable.photo_error1)
                    .into(photoView);

            pages.add(page);
        }

        PhotoPagerAdapter pagerAdapter = new PhotoPagerAdapter(pages);
        ViewPager viewPager = new ViewPager(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);

        setContentView(viewPager);
    }
}
