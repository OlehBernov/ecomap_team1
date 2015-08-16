package com.ecomap.ukraine.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProblemPhotoSlidePager extends FragmentActivity {

    private static List<String> urls;
    private static List<String> descriptions;

    public static void setContent(List<String> urls, List<String> descriptions) {
        ProblemPhotoSlidePager.urls = urls;
        ProblemPhotoSlidePager.descriptions = descriptions;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<>();
        int position = getIntent().getIntExtra("position", 0);

        for (int i = 0; i < urls.size(); i++) {
            View page = inflater.inflate(R.layout.activity_problem_photo_slide_pager, null);
            ImageView photoView = (ImageView) page.findViewById(R.id.problem_photo);
            Picasso.with(getApplicationContext())
                    .load(urls.get(i))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.photo_error1)
                    .into(photoView);
            TextView photoDescription = (TextView) page.findViewById(R.id.photo_description);
            photoDescription.setText(descriptions.get(i));

            pages.add(page);
        }

        PhotoPagerAdapter pagerAdapter = new PhotoPagerAdapter(pages);
        ViewPager viewPager = new ViewPager(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);

        setContentView(viewPager);
    }
}
