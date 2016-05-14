package com.testapp.hairsimulator;

import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bharath.simha on 15/01/16.
 */
public class FontCache {

    private static Map<String , Typeface> cache = new HashMap<String, Typeface>();

    public static Typeface getFont(String font){

        Typeface typeface = cache.get(font);
        if(typeface != null)
            return typeface;

        typeface = Typeface.createFromAsset(HairSimulator.context.getAssets(), font);
        cache.put(font, typeface);
        return typeface;
    }
}
