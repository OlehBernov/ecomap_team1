package com.ecomap.ukraine.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.data.manager.DataManager;

/**
* Activity for 
*/
public class SettingsActivity extends AppCompatActivity {

    /**
	* Title of the SettingsActivity activity.
	*/
    private static final String SETTINGS = "SettingsActivity";

    private static final String EVERY_TIME = "Updating time <br/><font color='grey'>Every time</font>";

    private static final String ONCE_A_DAY = "Updating time <br/><font color='grey'>Once a day</font>";

    private static final String ONCE_A_WEEK = "Updating time <br/><font color='grey'>Once a week</font>";

    private static final String ONCE_A_MONTH = "Updating time <br/><font color='grey'>Once a month</font>";

    private static final String NORMAL_MAP_TYPE = "Map type <br/><font color='grey'>Normal map type</font>";

    private static final String VIEW_FROM_SATELLITE = "Map type <br/><font color='grey'>View from satellite</font>";

    private Button updateTimeButton;
    private Button mapTypeButton;

    private UpdateTime updateTime;
    private MapType mapType = MapType.MAP_TYPE_NORMAL;

	/**
	* Opens window for choosing updating time for the application data.
	*
	* @param view view.
	*/
    public void openChoosingTimeWindow(View view) {
        new MaterialDialog.Builder(this)
                .title(R.string.updating_time)
                .items(R.array.time_update_items)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.settings_text)
                .titleColorRes(R.color.settings_title)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(updateTime.getId(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view1, int which, CharSequence text) {
                        updateTime = UpdateTime.values()[which];
                        updateTimeButton.setText(Html.fromHtml(getUpdateTimeTitle(which)));
                        saveUpdateTimeToSharedPreferences();
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .show();
    }

	/**
	* Opens window for choosing map time for the application data.
	*
	* @param view view.
	*/
    public void openChoosingMapTypeWindow(View view) {
        new MaterialDialog.Builder(this)
                .title(R.string.map_type)
                .items(R.array.map_type_items)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.settings_text)
                .titleColorRes(R.color.settings_title)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(mapType.getId(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view1, int which, CharSequence text) {
                        mapType = MapType.values()[which];
                        mapTypeButton.setText(Html.fromHtml(getMapTypeTitle(which)));
                        saveMapTypeToSharedPreferences();
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataManager.setUpdateTime(updateTime);
        SharedPreferences.Editor editor = getSharedPreferences(ExtraFieldNames.MAP_TYPE,
                Context.MODE_PRIVATE).edit();
        editor.putInt(ExtraFieldNames.MAP_TYPE_ID, mapType.getId());
        editor.apply();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();
        SharedPreferences settings = getSharedPreferences(ExtraFieldNames.SETTINGS_PREFERENCES,
                MODE_PRIVATE);
        setUpdateTimeTitle(settings);
        setMapTypeTitle(settings);
    }

	/**
	* Sets the hint about current updating time mode on updating time mode choosing button.
	*
	* @param settings shared preferences for gets saved current updating time mode.
	*/
    private void setUpdateTimeTitle(SharedPreferences settings) {
        int updateTimeValue = settings.getInt(ExtraFieldNames.UPDATE_TIME,
                UpdateTime.ONCE_A_WEEK.getId());
        updateTime = UpdateTime.values()[updateTimeValue];
        updateTimeButton = (Button) findViewById(R.id.update_time_button);
        updateTimeButton.setText(Html.fromHtml(getUpdateTimeTitle(updateTimeValue)));
    }

	/**
	* Sets the hint about current map type mode on map type choosing button.
	*
	* @param settings shared preferences for gets saved current map type.
	*/
    private void setMapTypeTitle(SharedPreferences settings) {
        int mapTypeValue = settings.getInt(ExtraFieldNames.MAP_TYPE, MapType.MAP_TYPE_NORMAL.getId());
        mapType = MapType.values()[mapTypeValue];
        mapTypeButton = (Button) findViewById(R.id.map_type_button);
        mapTypeButton.setText(Html.fromHtml(getMapTypeTitle(mapTypeValue)));
    }

	/**
	* Performs saving chose updating time mode to sharedPreferences.
	*/
    private void saveUpdateTimeToSharedPreferences() {
        SharedPreferences settings =
                getSharedPreferences(ExtraFieldNames.SETTINGS_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(ExtraFieldNames.UPDATE_TIME, updateTime.getId());
        editor.apply();
    }

	/**
	* Performs saving chose map type to sharedPreferences.
	*/
    private void saveMapTypeToSharedPreferences() {
        SharedPreferences settings =
                getSharedPreferences(ExtraFieldNames.SETTINGS_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(ExtraFieldNames.MAP_TYPE, mapType.getId());
        editor.apply();
    }

	/**
	* Sets the toolbar on activity.
	*/
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(SETTINGS);
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
	* Returns name of the updating time mode.
	*
	* @param id id of the updating time mode.
	* @return name of the updating time mode.
	*/
    private String getUpdateTimeTitle(int id) {
        switch (id) {
            case 0:
                return EVERY_TIME;
            case 1:
                return ONCE_A_DAY;
            case 2:
                return ONCE_A_WEEK;
            case 3:
                return ONCE_A_MONTH;
            default:
                return ONCE_A_WEEK;
        }
    }

	/**
	* Returns name of the map type.
	*
	* @param id id of the map type.
	* @return name of the map type.
	*/
    private String getMapTypeTitle(int id) {
        switch (id) {
            case 0:
                return NORMAL_MAP_TYPE;
            case 1:
                return VIEW_FROM_SATELLITE;
            default:
                return NORMAL_MAP_TYPE;
        }
    }

}


