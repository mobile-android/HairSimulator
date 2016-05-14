package com.testapp.hairsimulator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by bharath.simha on 09/01/16.
 */
public class Utility {

    public static boolean IS_MALE_THEME = false;
    public static int TEXT_SIZE = 12;
    private static Typeface typeface;

    public static Bitmap bm1, bm2, bm3;

    public static int dpToPx(int dp) {
        DisplayMetrics metrics = HairSimulator.context.getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        if (px < 1.0f) {
            px = 1;
        }
        return (int) px;
    }

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = HairSimulator.context.getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        if (px < 1.0f) {
            px = 1;
        }
        return (int) px;
    }

    public static LinearLayout.LayoutParams setCustomLinearLayoutParams(int weight, int height) {
        return new LinearLayout.LayoutParams(weight, height);
    }

    public static RelativeLayout.LayoutParams setCustomRelativeLayoutParams(int weight, int height, boolean hasRule) {
        RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(weight, height);
        if (hasRule)
            relativeLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);

        return relativeLayout;
    }

    public static Typeface getFont(Context context) {
        if (typeface == null) {
            String font = "robotregular.ttf";
            typeface = FontCache.getFont(font);
        }
        return typeface;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null");
    }

    public static void onChangeStatusBarColor(Context context, Window window) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (!Utility.IS_MALE_THEME) {
                window.setStatusBarColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
            } else {
                window.setStatusBarColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
            }
        }
    }
}
