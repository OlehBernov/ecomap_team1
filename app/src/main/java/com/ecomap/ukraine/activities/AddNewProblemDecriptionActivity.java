package com.ecomap.ukraine.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.io.ByteArrayOutputStream;
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
public class AddNewProblemDecriptionActivity extends AppCompatActivity  {

    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;
    private static final int DEGREE_90 = 90;

    private static final String DATE_TEMPLATE = "MMdd_HHmmss";
    private static final String PHOTO_FORMAT = ".jpg";
    private static final String FILE_NAME_BEGINNING = "JPEG_";
    private static final String DESCRIPTION_HINT = "Add description...";

    private static final String CAMERA_URI = "Camera Uri";
    private static final String NUMBER_OF_PHOTOS = "Number of photos";
    private static final String DESCRIPTION = "Description";

    private Uri currentPhotoUri;
    private List<String> userPhotos;
    private TableLayout photoDescriptionLayout;
    private List<String> descriptions;
    private ArrayList<Bitmap> bitmapsPhoto;

    //private DataManager dataManager;

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Description", "Photo"};
    int Numboftabs = 2;


    public ArrayList<Bitmap> getBitmapsPhoto () {
        return bitmapsPhoto;
    }


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


            }
        });


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

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

        if (savedInstanceState != null) {
            onSaveInstanceState(savedInstanceState);
        }

        Tab2.setContext(getApplicationContext());
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
            Tab1.getInstance(getBitmapsPhoto()).sendProblem();
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
        if (descriptions != null) {
            outState.putInt(DESCRIPTION, descriptions.size());
            for (int i = 0; i < descriptions.size(); i++) {
                outState.putString("" + i, descriptions.get(i));
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
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
        if (descriptions == null && savedInstanceState.containsKey(DESCRIPTION)) {
            int numberOfDescriptions = savedInstanceState.getInt(DESCRIPTION);
            descriptions = new ArrayList<>();
            for (int i = 0; i < numberOfDescriptions; i++) {
                if (savedInstanceState.containsKey("" + i)) {
                    descriptions.add(savedInstanceState.getString("" + i));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (isGalleryPhoto(requestCode, resultCode, data)) {
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

        assert ab != null;
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
        savePhoto(photoPath);
        cursor.close();

        addPhotosToView();
        addPhotoToGallery(photoPath);
    }

    private void processCameraPhoto() {
        String photoPath = currentPhotoUri.getPath();
        savePhoto(photoPath);
        addPhotosToView();
        addPhotoToGallery(photoPath);
    }

    private boolean isActivityLayoutHaveChild() {
        return photoDescriptionLayout != null && (photoDescriptionLayout.getChildCount() > 0);
    }

    private void addPhotosToView() {
        if (isActivityLayoutHaveChild()) {
            photoDescriptionLayout.removeAllViews();
        }

        photoDescriptionLayout = (TableLayout) findViewById(R.id.photo_descriptions);
        for (int i = 0; i < userPhotos.size(); i++) {

            Bitmap photoBitmap = changePhotoOrientation(userPhotos.get(i));
            setDeleteButton(i);
            TableRow activityRow = new TableRow(getApplicationContext());

            activityRow.addView(buildUserPhoto(photoBitmap));
            activityRow.addView(buildPhotoDescription(i));
            photoDescriptionLayout.addView(activityRow);
        }
    }

    private EditText buildPhotoDescription(int id) {
        EditText photoDescription = new EditText(getApplicationContext());
        photoDescription.setHint(DESCRIPTION_HINT);
        photoDescription.setBackgroundResource(R.drawable.edit_text_description_style);
        photoDescription.setGravity(Gravity.TOP);
        photoDescription.setVerticalScrollBarEnabled(true);
        photoDescription.setFocusableInTouchMode(true);
        photoDescription.setHintTextColor(getResources().getColor(R.color.calendar_header));
        photoDescription.setTextColor(getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        photoDescription.setTextSize(getResources().getDimension(R.dimen.comment_text_size));
        photoDescription.setLayoutParams(setPhotoDescriptionParams());
        photoDescription.setPadding(
                (int) getResources().getDimension(R.dimen.description_left_padding),
                (int) getResources().getDimension(R.dimen.description_top_padding),
                (int) getResources().getDimension(R.dimen.description_right_padding),
                (int) getResources().getDimension(R.dimen.description_bottom_padding)
        );
        if (!descriptions.get(id).equals("")) {
            photoDescription.setText(descriptions.get(id));
        }

        return photoDescription;
    }

    private TableRow.LayoutParams setPhotoDescriptionParams() {
        TableRow.LayoutParams photoDescriptionParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        photoDescriptionParams.leftMargin = (int) getResources().getDimension(R.dimen.slide_panel_items_margin);
        photoDescriptionParams.gravity = Gravity.CENTER_HORIZONTAL;

        return photoDescriptionParams;
    }

    private ImageView buildUserPhoto(final Bitmap photoBitmap) {
        TableRow.LayoutParams imageParams =
                new TableRow.LayoutParams(photoBitmap.getWidth(),
                                         photoBitmap.getHeight());
        ImageView photoView = new ImageView(getApplicationContext());
        photoView.setImageBitmap(photoBitmap);
        photoView.setLayoutParams(imageParams);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullSizePhoto(photoBitmap);
            }
        });

        return photoView;
    }

    private void showFullSizePhoto(Bitmap photoBitmap) {
        Intent intent = new Intent (this, UserPhotoFullScreen.class);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
        byte[] photoByteArray = blob.toByteArray();
        intent.putExtra("photo", photoByteArray);
        startActivity(intent);
    }

    private Bitmap changePhotoOrientation(String photoPath) {
        BitmapResizer bitmapResizer =  new BitmapResizer(getApplicationContext());
        int photoBounds = (int) getResources().getDimension(R.dimen.edit_text_add_photo);
        Bitmap photoBitmap = bitmapResizer.scalePhoto(photoPath, photoBounds);
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(photoPath);
        } catch (IOException e) {
            Log.e("IOException", "change photo orientation");
            return photoBitmap;
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                        ExifInterface.ORIENTATION_NORMAL);
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                Matrix matrix = new Matrix();
                matrix.postRotate(DEGREE_90);
                return Bitmap.createBitmap(photoBitmap, 0, 0, photoBitmap.getWidth(),
                                           photoBitmap.getHeight(), matrix, true);
        } else {
            return photoBitmap;
        }
    }

    private void setDeleteButton(int buttonId) {
        ImageButton deleteButton = new ImageButton(getApplicationContext());
        deleteButton.setLayoutParams(setDeleteButtonParams());
        deleteButton.setId(buttonId);
        deleteButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.delete));
        deleteButton.setBackgroundColor(getResources().getColor(R.color.white));
        deleteButton.setLayoutParams(setDeleteButtonParams());
        addListenerOnDeleteButton(deleteButton);

        RelativeLayout buttonLayout = new RelativeLayout(getApplicationContext());
        buttonLayout.addView(deleteButton);

        photoDescriptionLayout.addView(buttonLayout);
    }

    private RelativeLayout.LayoutParams setDeleteButtonParams() {
        RelativeLayout.LayoutParams buttonParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        return buttonParams;
    }

    private void addListenerOnDeleteButton(ImageButton deleteButton) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlock(photoDescriptionLayout.indexOfChild(v));
            }
        });
    }

    private void deleteBlock(int buttonId) {
/*        Log.e("i", "" + buttonId);
        photoDescriptionLayout.removeViews(buttonId, 2);
        userPhotos.remove(buttonId);
        descriptions.remove(buttonId);*/
    }

    private void savePhoto(String photoPath) {
        if (userPhotos == null) {
            userPhotos = new ArrayList<>();
        }
        userPhotos.add(photoPath);
        if (descriptions == null) {
            descriptions = new ArrayList<>();
        }
        descriptions.add("");
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
