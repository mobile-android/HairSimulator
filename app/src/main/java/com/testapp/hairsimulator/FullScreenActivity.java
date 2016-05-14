package com.testapp.hairsimulator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by bharath.simha on 26/01/16.
 */
public class FullScreenActivity extends AppCompatActivity {

    private Bitmap bm1, bm2, bm3;
    private ImageView im1, im2, im3;
    private Button b1, b2, b3;
    private ImageButton close;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.full_screen);

        bm1 = Utility.bm1;
        bm2 = Utility.bm2;
        bm3 = Utility.bm3;

        im1 = (ImageView) findViewById(R.id.compareIV1);
        im2 = (ImageView) findViewById(R.id.compareIV2);
        im3 = (ImageView) findViewById(R.id.compareIV3);

        b1 = (Button) findViewById(R.id.saveToMyStylesButton1);
        b2 = (Button) findViewById(R.id.saveToMyStylesButton2);
        b3 = (Button) findViewById(R.id.saveToMyStylesButton3);

        close = (ImageButton) findViewById(R.id.fullScreenClose);

        linearLayout = (LinearLayout) findViewById(R.id.headerStripFullScreen);

        if (!Utility.IS_MALE_THEME) {
            linearLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ladiesBrightColor));
        } else {
            linearLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gentsBrightColor));
        }

        Utility.onChangeStatusBarColor(getApplicationContext(), getWindow());

        if (bm1 != null)
            im1.setImageBitmap(bm1);
        if (bm2 != null)
            im2.setImageBitmap(bm2);
        if (bm3 != null)
            im3.setImageBitmap(bm3);

        b1.setTypeface(Utility.getFont(getApplicationContext()));
        b1.setTypeface(Utility.getFont(getApplicationContext()));
        b1.setTypeface(Utility.getFont(getApplicationContext()));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Preferences.instance().getButtonValue() != null) {
            int position = Integer.parseInt(Preferences.instance().getButtonValue());

            if (position == 1) {
                if (Utility.IS_MALE_THEME) {
                    b1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gentsBackgroundColor));
                    b1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gentsBrightColor));
                } else {
                    b1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ladiesBackgroundColor));
                    b1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ladiesBrightColor));
                }
            }

            if (position == 2) {
                if (Utility.IS_MALE_THEME) {
                    b2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gentsBackgroundColor));
                    b2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gentsBrightColor));
                } else {
                    b2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ladiesBackgroundColor));
                    b2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ladiesBrightColor));
                }
            }

            if (position == 3) {
                if (Utility.IS_MALE_THEME) {
                    b3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gentsBackgroundColor));
                    b3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gentsBrightColor));
                } else {
                    b3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.ladiesBackgroundColor));
                    b3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ladiesBrightColor));
                }
            }
        }
    }
}
