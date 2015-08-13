package com.ecomap.ukraine.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecomap.ukraine.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andriy on 11.08.2015.
 */
public class AddNewProblemDecriptionActivity extends AppCompatActivity {

    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;
    private static final int PHOTO_BOUNDS = 300;

    private static final String DATE_TEMPLATE = "MMdd_HHmmss";
    private static final String PHOTO_FORMAT = ".jpg";
    private static final String FILE_NAME_BEGINNING = "JPEG_";

    private static final String CAMERA_URI = "Camera Uri";
    private static final String NUMBER_OF_PHOTOS = "Number of photos";

    private Uri currentPhotoUri;
    private List<String> userPhotos;

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Description","Photo"};
    int Numboftabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_problem_description);
        setupToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //System.exit(0);

            }
        });


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_problem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_confirm_problem) {
            Tab1.getInstance().sendProblem();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void getPhotoFromCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e("camera", "file error");
            }

            currentPhotoUri = Uri.fromFile(photoFile);
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        currentPhotoUri);
                startActivityForResult(takePictureIntent, CAMERA_PHOTO);
            }
        }
    }

    public void getPhotoFromGallery(View view) {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY_PHOTO);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentPhotoUri != null) {
            outState.putString(CAMERA_URI, currentPhotoUri.toString());
        }
        if (userPhotos != null) {
            outState.putInt(NUMBER_OF_PHOTOS, userPhotos.size());
            for (int i = 0; i < userPhotos.size(); i++) {
                outState.putString("" + i, userPhotos.get(i));
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(CAMERA_URI)) {
            currentPhotoUri = Uri.parse(savedInstanceState.getString(CAMERA_URI));
        }
        if (userPhotos == null && savedInstanceState.containsKey(NUMBER_OF_PHOTOS)) {
            int numberOfPhotos = savedInstanceState.getInt(NUMBER_OF_PHOTOS);
            userPhotos = new ArrayList<>();
            for (int i = 0; i < numberOfPhotos; i++) {
                if (savedInstanceState.containsKey("" + i)) {
                    userPhotos.add(savedInstanceState.getString("" + i));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (isGalleryPhoto(requestCode, resultCode, data) ) {
            processGalleryPhoto(data);
        } else if (isCameraPhoto(requestCode, resultCode)) {
            processCameraPhoto();
        }
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Add description");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setClickable(true);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private boolean isCameraPhoto(int requestCode, int resultCode) {
        return (requestCode == CAMERA_PHOTO)
                && (resultCode == RESULT_OK);
    }

    private boolean isGalleryPhoto(int requestCode, int resultCode, Intent data) {
        return (requestCode == GALLERY_PHOTO)
                && (resultCode == RESULT_OK)
                && (data != null);
    }

    private void processGalleryPhoto(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String photoPath = cursor.getString(columnIndex);
        cursor.close();

        BitmapResizer bitmapResizer = new BitmapResizer(getApplicationContext());
        Bitmap photo = bitmapResizer.scalePhoto(photoPath, PHOTO_BOUNDS);
        addPhotoToView(new BitmapResizer(getApplicationContext()).resizeBitmap(photo, 150));
        savePhoto(photoPath);
        addPhotoToGallery(photoPath);
    }

    private void processCameraPhoto() {
        BitmapResizer bitmapResizer = new BitmapResizer(getApplicationContext());
        if (userPhotos != null) {
            for (String userPhotoPath: userPhotos) {
                Bitmap photo = bitmapResizer.scalePhoto(userPhotoPath, PHOTO_BOUNDS);
                addPhotoToView(bitmapResizer.resizeBitmap(photo, PHOTO_BOUNDS / 2));
            }
        }

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageURI(currentPhotoUri);
        String photoPath = currentPhotoUri.getPath();
        Bitmap photo = bitmapResizer.scalePhoto(photoPath, PHOTO_BOUNDS);
        addPhotoToView(bitmapResizer.resizeBitmap(photo, PHOTO_BOUNDS / 2));
        savePhoto(photoPath);
        addPhotoToGallery(photoPath);
    }

    private void addPhotoToView(Bitmap photo) {
        LinearLayout photoContainer = (LinearLayout) findViewById(R.id.photo_container);
        BasicContentLayout photoLayout = new BasicContentLayout(photoContainer, getApplicationContext());
        ImageView photoView = new ImageView(getApplicationContext());
        photoView.setImageBitmap(photo);
        photoView.setAdjustViewBounds(true);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Bitmap> bitmaps = getBitmapsFromPath(userPhotos);
                PhotoSlidePagerActivity.setContent(bitmaps);
                Intent intent = new Intent(AddNewProblemDecriptionActivity.this,
                        PhotoSlidePagerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        photoLayout.addHorizontalBlock(photoView);
    }

    private List<Bitmap> getBitmapsFromPath(List<String> userPhotos) {
        List<Bitmap> bitmaps = new ArrayList<>();
        BitmapResizer bitmapResizer = new BitmapResizer(getApplicationContext());
        for (String path: userPhotos) {
            bitmaps.add(bitmapResizer.scalePhoto(path,
                    getResources().getDisplayMetrics().widthPixels));
        }
        return bitmaps;
    }

    private void savePhoto(String photoPath) {
        if (userPhotos == null) {
            userPhotos = new ArrayList<>();
        }
        userPhotos.add(photoPath);
    }

    private void addPhotoToGallery(String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH).format(new Date());
        String imageFileName = FILE_NAME_BEGINNING + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, PHOTO_FORMAT, storageDir);
    }

}
