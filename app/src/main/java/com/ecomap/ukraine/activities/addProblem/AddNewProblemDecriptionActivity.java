package com.ecomap.ukraine.activities.addProblem;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.activities.BitmapResizer;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.models.User;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddNewProblemDecriptionActivity extends AppCompatActivity {

    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;
    private static final int ADD_PHOTO_ITEM = 1;
    private static final int NUMBER_OF_TUBS = 2;
    private static final String DATE_TEMPLATE = "MMdd_HHmmss";
    private static final String PHOTO_FORMAT = ".jpg";
    private static final String FILE_NAME_BEGINNING = "JPEG_";
    private static final String DESCRIPTION_HINT = "Add description...";
    private static final String ADD_DESCRIPTION = "Add description";
    private static final String CAMERA_URI = "Camera Uri";
    private static final String USER_PHOTOS = "Number of photos";
    private static final String DESCRIPTION = "Description";
    protected final String TAG = getClass().getSimpleName();
    private Uri currentPhotoUri;
    private List<Uri> userPhotos;
    private TableLayout photoDescriptionLayout;
    private List<String> descriptions;
    private User user;
    private Toolbar toolbar;
    private ViewPager pager;
    private String Titles[] = getResources().getStringArray(R.array.tabs_in_posting_prpblem);

    public List<Bitmap> getBitmapsPhoto() {
        List<Bitmap> photoBitmaps = new ArrayList<>();
        if (userPhotos == null) {
            return null;
        }
        BitmapResizer bitmapResizer = new BitmapResizer(getApplicationContext());
        int userPhotoSize = (int) getResources().getDimension(R.dimen.edit_text_add_photo);
        for (Uri userPhoto : userPhotos) {
            String userPhotoPath = userPhoto.getPath();
            photoBitmaps.add(bitmapResizer.scalePhoto(userPhotoPath, userPhotoSize));
        }
        return photoBitmaps;
    }

    public void addPhoto(View v) {
        pager.setCurrentItem(ADD_PHOTO_ITEM);
    }

    public void getPhotoFromCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, "Camera photo file error");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra(ExtraFieldNames.USER);

        setContentView(R.layout.add_problem_description);
        setupToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, NUMBER_OF_TUBS);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(pager);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(CAMERA_URI)) {
            currentPhotoUri = Uri.parse(savedInstanceState.getString(CAMERA_URI));
        }
        if (isPhotosSaved(savedInstanceState)) {
            userPhotos = new ArrayList<>();
            ArrayList<String> userPhotoArrayList = savedInstanceState.getStringArrayList(USER_PHOTOS);
            for (int i = 0; i < userPhotoArrayList.size(); i++) {
                userPhotos.add(Uri.parse(userPhotoArrayList.get(i)));
            }
        }
        if (isDescriptionsSaved(savedInstanceState)) {
            descriptions = new ArrayList<>();
            descriptions = savedInstanceState.getStringArrayList(DESCRIPTION);
            addPhotosToView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_problem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confirm_problem) {
            AddProblemDescriptionFragment.getInstance(getBitmapsPhoto(), descriptions).sendProblem();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(this, ChooseProblemLocationActivity.class);
        mainIntent.putExtra(ExtraFieldNames.USER, user);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentPhotoUri != null) {
            outState.putString(CAMERA_URI, currentPhotoUri.toString());
        }
        if (userPhotos != null) {
            ArrayList<String> userPhotoArrayList = new ArrayList<>();
            for (int i = 0; i < userPhotos.size(); i++) {
                userPhotoArrayList.add(userPhotos.get(i).toString());
            }
            outState.putStringArrayList(USER_PHOTOS, userPhotoArrayList);
        }
        if (descriptions != null) {
            outState.putStringArrayList(DESCRIPTION, new ArrayList<>(descriptions));
        }
    }

    private boolean isDescriptionsSaved(Bundle savedInstanceState) {
        return (descriptions == null) && savedInstanceState.containsKey(DESCRIPTION);
    }

    private boolean isPhotosSaved(Bundle savedInstanceState) {
        return (userPhotos == null) && savedInstanceState.containsKey(USER_PHOTOS);
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(ADD_DESCRIPTION);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setClickable(true);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();

        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    }

    private void processCameraPhoto() {
        String photoPath = currentPhotoUri.getPath();
        savePhoto(photoPath);

        addPhotosToView();
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
            setDeleteButton(i);
            TableRow activityRow = new TableRow(getApplicationContext());
            activityRow.addView(buildUserPhoto(userPhotos.get(i).getPath()));
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
        photoDescription.setTextColor(getResources().getColor(R.color.primary_text));
        photoDescription.setTextSize(getResources().getDimension(R.dimen.comment_text_size));
        photoDescription.setLayoutParams(setPhotoDescriptionParams());
        photoDescription.setPadding(
                (int) getResources().getDimension(R.dimen.description_left_padding),
                (int) getResources().getDimension(R.dimen.description_top_padding),
                (int) getResources().getDimension(R.dimen.description_right_padding),
                (int) getResources().getDimension(R.dimen.description_bottom_padding)
        );
        AddPhotoFragment.getInstance(this).setOnFocusChangeListener(photoDescription);
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

    private ImageView buildUserPhoto(final String photoPath) {
        BitmapResizer bitmapResizer = new BitmapResizer(getApplicationContext());
        int photoSize = (int) getResources().getDimension(R.dimen.edit_text_add_photo);

        Bitmap photoBitmap = bitmapResizer.changePhotoOrientation(photoPath, photoSize);
        TableRow.LayoutParams imageParams =
                new TableRow.LayoutParams(photoBitmap.getWidth(),
                        photoBitmap.getHeight());
        ImageView photoView = new ImageView(getApplicationContext());
        photoView.setImageBitmap(photoBitmap);
        photoView.setLayoutParams(imageParams);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullSizePhoto(photoPath);
            }
        });

        return photoView;
    }

    private void showFullSizePhoto(String photoPath) {
        Intent intent = new Intent(this, UserPhotoFullScreen.class);
        intent.putExtra(ExtraFieldNames.PHOTO, photoPath);
        startActivity(intent);
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
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        return buttonParams;
    }

    private void addListenerOnDeleteButton(ImageButton deleteButton) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlock(v.getId());
            }
        });
    }

    private void deleteBlock(int buttonId) {
        userPhotos.remove(buttonId);
        descriptions.remove(buttonId);
        photoDescriptionLayout.removeAllViews();
        addPhotosToView();
    }

    private void savePhoto(String photoPath) {
        if (userPhotos == null) {
            userPhotos = new ArrayList<>();
        }
        userPhotos.add(Uri.fromFile(new File(photoPath)));
        if (descriptions == null) {
            descriptions = new ArrayList<>();
        }
        descriptions.add("");
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH).format(new Date());
        String imageFileName = FILE_NAME_BEGINNING + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFileName + PHOTO_FORMAT);
    }

}
