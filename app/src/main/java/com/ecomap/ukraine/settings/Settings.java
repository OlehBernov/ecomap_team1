package com.ecomap.ukraine.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.activities.main.MainActivity;
import com.ecomap.ukraine.data.manager.DataManager;

public class Settings extends AppCompatActivity {

    private static final String SETTINGS = "Settings";

    private UpdateTime updateTime = UpdateTime.ONCE_A_WEEK;
    private MapType mapType = MapType.FIRST;

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
                    public boolean onSelection(MaterialDialog dialog, View view, int which,
                                               CharSequence text) {
                        updateTime = UpdateTime.values()[which];
                        saveUpdateTimeToSharedPreferences();
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .show();
    }

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
                    public boolean onSelection(MaterialDialog dialog, View view, int which,
                                               CharSequence text) {
                        mapType = MapType.values()[which];
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ExtraFieldNames.MAP_TYPE, mapType);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();
        SharedPreferences settings = getSharedPreferences(ExtraFieldNames.SETTINGS_PREFERENCES,
                                                          MODE_PRIVATE);
        int updateTimeValue = settings.getInt(ExtraFieldNames.UPDATE_TIME,
                                              UpdateTime.ONCE_A_WEEK.getId());
        updateTime = UpdateTime.values()[updateTimeValue];
        int mapTypeValue = settings.getInt(ExtraFieldNames.MAP_TYPE, MapType.FIRST.getId());
        mapType = MapType.values()[mapTypeValue];
    }

    private void saveUpdateTimeToSharedPreferences() {
        SharedPreferences settings =
                getSharedPreferences(ExtraFieldNames.SETTINGS_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(ExtraFieldNames.UPDATE_TIME, updateTime.getId());
        editor.apply();
    }

    private void saveMapTypeToSharedPreferences() {
        SharedPreferences settings =
                getSharedPreferences(ExtraFieldNames.SETTINGS_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(ExtraFieldNames.MAP_TYPE, mapType.getId());
        editor.apply();
    }

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

}


