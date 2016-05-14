package com.testapp.hairsimulator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView textView, tvExpired;

    public static final int DAY_DIFF = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);
        tvExpired = (TextView) findViewById(R.id.textViewExpired);

        Preferences.instance().initialize(getApplicationContext());

        if (Preferences.instance().isFirstTime()) {
            Preferences.instance().setLocalFirstTimeStamp(System.currentTimeMillis());
            Preferences.instance().setFirstTime(false);
            if (getResources().getBoolean(R.bool.isTab)) {
                startActivity(new Intent(getApplicationContext(), HairSimulatorActivity.class));
                finish();
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        } else {
            long timeDiff = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()
                    - Preferences.instance().getLocalFirstTimeStamp());
            if (DAY_DIFF > timeDiff)
            {
                if (getResources().getBoolean(R.bool.isTab)) {
                    startActivity(new Intent(getApplicationContext(), HairSimulatorActivity.class));
                    finish();
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }
            else
                tvExpired.setVisibility(View.VISIBLE);
            return;
        }
    }
}
