package com.ecomap.ukraine.ui.activities;

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
import com.ecomap.ukraine.ui.SlidingTabLayout;
import com.ecomap.ukraine.ui.adapters.ViewPagerAdapter;
import com.ecomap.ukraine.ui.fragments.AddProblemDescriptionFragment;
import com.ecomap.ukraine.util.BitmapResizer;
import com.ecomap.ukraine.util.ExtraFieldNames;
import com.ecomap.ukraine.util.Keyboard;

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
    private ViewPager pager;

    /**
     * Gets list of bitmaps from list of uri for posting photos.
     *
     * @return list of bitmaps of user photos related to the problem.
     */
    public List<Bitmap> getBitmapsPhoto() {
        List<Bitmap> photoBitmaps = new ArrayList<>();
        if (userPhotos == null) {
            return null;
        }

        int userPhotoSize = (int) getResources().getDimension(R.dimen.edit_text_add_photo);
        for (Uri userPhoto : userPhotos) {
            String userPhotoPath = userPhoto.getPath();
            photoBitmaps.add(BitmapResizer.scalePhoto(userPhotoPath, userPhotoSize));
        }
        return photoBitmaps;
    }

    /**
     * Open tab with add photo functionality.
     */
    public void openAddPhotoPage(View v) {
        pager.setCurrentItem(ADD_PHOTO_ITEM);
    }

    /**
     * Starts Camera application to take the photo.
     *
     * @param view button, which related to this action.
     */
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

    /**
     * Starts Gallery application to get the photo.
     *
     * @param view button, which related to this action.
     */
    public void getPhotoFromGallery(View view) {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_PHOTO);
    }

    /**
     * Inflate the menu, this adds items to the action bar if it is present.
     *
     * @param menu activity menu
     * @return result of action
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_problem, menu);
        return true;
    }

    /**
     * Called when the user selects an item from the options menu
     *
     * @param item menu item
     * @return result of action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confirm_problem) {
            AddProblemDescriptionFragment.getInstance(getBitmapsPhoto(), descriptions)
                    .postProblemValidation(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(this, ChooseProblemLocationActivity.class);
        startActivity(mainIntent);
        finish();
    }

    /**
     * Initialize activity
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] titles = getResources().getStringArray(R.array.tabs_in_posting_problem);

        setContentView(R.layout.add_problem_description);
        setupToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles,
                NUMBER_OF_TUBS);
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

    /**
     * Called when the activity is being re-initialized from a previously saved state
     *
     * @param savedInstanceState the data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(CAMERA_URI)) {
            currentPhotoUri = Uri.parse(savedInstanceState.getString(CAMERA_URI));
        }
        if (isPhotosSaved(savedInstanceState)) {
            userPhotos = new ArrayList<>();
            List<String> userPhotoArrayList = savedInstanceState.getStringArrayList(USER_PHOTOS);
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

    /**
     * Called when an activity you launched exits.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller.
     */
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
     * Called to retrieve per-instance state from an activity before being killed.
     *
     * @param outState Bundle in which to place your saved state
     */
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

    /**
     * Check if description was saved in savedInstanceState.
     *
     * @param savedInstanceState the data most recently supplied in onSaveInstanceState(Bundle).
     * @return whether the description was saved.
     */
    private boolean isDescriptionsSaved(Bundle savedInstanceState) {
        return (descriptions == null) && savedInstanceState.containsKey(DESCRIPTION);
    }

    /**
     * Check if photo was saved in savedInstanceState.
     *
     * @param savedInstanceState the data most recently supplied in onSaveInstanceState(Bundle).
     * @return whether the photo was saved.
     */
    private boolean isPhotosSaved(Bundle savedInstanceState) {
        return (userPhotos == null) && savedInstanceState.containsKey(USER_PHOTOS);
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
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

    /**
     * Checks if in onActivityResult returned photo from Camera application.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @return whether returned photo from Camera.
     */
    private boolean isCameraPhoto(int requestCode, int resultCode) {
        return (requestCode == CAMERA_PHOTO)
                && (resultCode == RESULT_OK);
    }

    /**
     * Checks if in onActivityResult returned photo from Gallery.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        Intent which may contains photo from Gallery.
     * @return whether returned photo from Gallery.
     */
    private boolean isGalleryPhoto(int requestCode, int resultCode, Intent data) {
        return (requestCode == GALLERY_PHOTO)
                && (resultCode == RESULT_OK)
                && (data != null);
    }

    /**
     * Gets photo from Intent, which returned from Gallery.
     * Saves photo and adds it to other user photo on view.
     *
     * @param data Intent, which returned from Gallery.
     */
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

    /**
     * Saves photo, which returned from Camera application
     * and adds it to other user photo on view.
     */
    private void processCameraPhoto() {
        String photoPath = currentPhotoUri.getPath();
        savePhoto(photoPath);

        addPhotosToView();
    }

    /**
     * Checks existance of user photos on view.
     *
     * @return whether the user photos exists on view.
     */
    private boolean isPhotoLayoutHaveChild() {
        return (photoDescriptionLayout != null) && (photoDescriptionLayout.getChildCount() > 0);
    }

    /**
     * Sets all saved user photos on view.
     */
    private void addPhotosToView() {
        if (isPhotoLayoutHaveChild()) {
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

    /**
     * Creates EditText for photo description.
     *
     * @param id photo id.
     * @return EditText for photo description.
     */
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

        Keyboard.setOnFocusChangeListener(photoDescription);

        if (!descriptions.get(id).equals("")) {
            photoDescription.setText(descriptions.get(id));
        }

        return photoDescription;
    }

    /**
     * Set parameters to photo description EditText.
     */
    private TableRow.LayoutParams setPhotoDescriptionParams() {
        TableRow.LayoutParams photoDescriptionParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        photoDescriptionParams.leftMargin = (int) getResources().getDimension(R.dimen.slide_panel_items_margin);
        photoDescriptionParams.gravity = Gravity.CENTER_HORIZONTAL;

        return photoDescriptionParams;
    }

    /**
     * Builds user photo from photoPath
     *
     * @param photoPath path to user photo
     * @return user photo
     */
    private ImageView buildUserPhoto(final String photoPath) {
        int photoSize = (int) getResources().getDimension(R.dimen.edit_text_add_photo);

        Bitmap photoBitmap = BitmapResizer.changePhotoOrientation(photoPath, photoSize);
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

    /**
     * Show full size of photo
     *
     * @param photoPath path to photo
     */
    private void showFullSizePhoto(String photoPath) {
        Intent intent = new Intent(this, UserPhotoFullScreenActivity.class);
        intent.putExtra(ExtraFieldNames.PHOTO, photoPath);
        startActivity(intent);
    }

    /**
     * Creates button for deleting concrete user photo from view.
     *
     * @param buttonId id of concrete photo (button).
     */
    private void setDeleteButton(int buttonId) {
        ImageButton deleteButton = new ImageButton(getApplicationContext());
        deleteButton.setLayoutParams(setDeleteButtonParams());
        deleteButton.setId(buttonId);
        deleteButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.ic_clear_black_18dp));
        deleteButton.setBackgroundColor(getResources().getColor(R.color.white));
        deleteButton.setLayoutParams(setDeleteButtonParams());
        addListenerToDeleteButton(deleteButton);

        RelativeLayout buttonLayout = new RelativeLayout(getApplicationContext());
        buttonLayout.addView(deleteButton);

        photoDescriptionLayout.addView(buttonLayout);
    }

    /**
     * Sets parameters for delete button.
     *
     * @return parameters for delete button.
     */
    private RelativeLayout.LayoutParams setDeleteButtonParams() {
        RelativeLayout.LayoutParams buttonParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        return buttonParams;
    }

    /**
     * Add listener to deleteButton.
     *
     * @param deleteButton delete button.
     */
    private void addListenerToDeleteButton(ImageButton deleteButton) {
        deleteButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Begins user photo deleting.
             *
             * @param v delete button.
             */
            @Override
            public void onClick(View v) {
                deleteBlock(v.getId());
            }
        });
    }

    /**
     * Deletes block from user photos view.
     *
     * @param buttonId id of photo (button).
     */
    private void deleteBlock(int buttonId) {
        userPhotos.remove(buttonId);
        descriptions.remove(buttonId);
        photoDescriptionLayout.removeAllViews();
        addPhotosToView();
    }

    /**
     * Adds new user photo to uri list.
     *
     * @param photoPath path to photo.
     */
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

    /**
     * Creates file for saving photo from Camera application.
     *
     * @return created file.
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH).format(new Date());
        String imageFileName = FILE_NAME_BEGINNING + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFileName + PHOTO_FORMAT);
    }

}
