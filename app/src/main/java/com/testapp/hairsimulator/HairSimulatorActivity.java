package com.testapp.hairsimulator;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;

/**
 * Created by bharath.simha on 26/01/16.
 */
public class HairSimulatorActivity extends AppCompatActivity {

    public static Context context;

    public FragmentManager fragmentManager;
    private HairSimulator hairSimulator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_simulator);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        HairSimulatorActivity.context = getApplicationContext();

        fragmentManager = getSupportFragmentManager();

        hairSimulator = getHairSimulator();

        fragmentManager.beginTransaction().replace(R.id.mainContainer, hairSimulator).commit();
    }

    public static Context getContext() {
        return HairSimulatorActivity.context;
    }

    public HairSimulator getHairSimulator() {
        if (hairSimulator == null)
            hairSimulator = new HairSimulator();

        return hairSimulator;
    }

}
