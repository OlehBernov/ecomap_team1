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

    private static Map<Photo, Bitmap> photos;

    public static PhotoSlidePagerActivity newInstance (Map<Photo, Bitmap> photos) {
        PhotoSlidePagerActivity.photos = photos;
        return new PhotoSlidePagerActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<>();

        for (Photo photo : photos.keySet()) {
            View page = inflater.inflate(R.layout.fragment_photo_viewer, null);

            ImageView photoView = (ImageView) page.findViewById(R.id.imageView4);
            photoView.setImageBitmap(photos.get(photo));

            TextView photoDescription = (TextView) page.findViewById(R.id.photo_description);
            photoDescription.setText(photo.getDescription());

            pages.add(page);
        }

        PhotoPagerAdapter pagerAdapter = new PhotoPagerAdapter(pages);
        ViewPager viewPager = new ViewPager(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);

        setContentView(viewPager);
    }

}
