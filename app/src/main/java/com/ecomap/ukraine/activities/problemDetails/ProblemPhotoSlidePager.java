package com.ecomap.ukraine.activities.problemDetails;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProblemPhotoSlidePager extends FragmentActivity {

    private static List<Photo> photos;

    private final String EMPTY_DESCRIPTION = "null";
    private final String POSITION = "position";

    public static void setContent(final List<Photo> photos) {
        ProblemPhotoSlidePager.photos = photos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<>();
        int position = getIntent().getIntExtra(POSITION, 0);

        for (Photo photo: photos) {
            View page = inflater.inflate(R.layout.activity_problem_photo_slide_pager, null);
            ImageView photoView = (ImageView) page.findViewById(R.id.problem_photo);
            Picasso.with(getApplicationContext())
                    .load(photo.getLink())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.photo_error1)
                    .into(photoView);

            String photoDescription = photo.getDescription();
            if (photoDescription != EMPTY_DESCRIPTION) {
                TextView photoDescriptionView = (TextView) page.findViewById(R.id.photo_description);
                photoDescriptionView.setText(photoDescription);
            }

            pages.add(page);
        }

        PhotoPagerAdapter pagerAdapter = new PhotoPagerAdapter(pages);
        ViewPager viewPager = new ViewPager(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);

        setContentView(viewPager);
    }
}
