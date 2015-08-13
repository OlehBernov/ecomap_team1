package com.ecomap.ukraine.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhotoSlidePagerActivity extends FragmentActivity {

    private static List<Bitmap> photos;

    public static void setContent(List<Bitmap> photos) {
        PhotoSlidePagerActivity.photos = photos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<>();

        for (Bitmap photo: photos) {
            View page = inflater.inflate(R.layout.fragment_photo_viewer, null);

            ImageView photoView = (ImageView) page.findViewById(R.id.imageView4);
            photoView.setImageBitmap(photo);

            pages.add(page);
        }

        PhotoPagerAdapter pagerAdapter = new PhotoPagerAdapter(pages);
        ViewPager viewPager = new ViewPager(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);

        setContentView(viewPager);
    }

}
