package com.testapp.hairsimulator;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by bharath.simha on 08/01/16.
 */
public class HairSimulator extends Fragment {

    public static Context context;

    public boolean isPhotoSelected = Boolean.FALSE;

    private FragmentHairStyles fragmentHairStyles;
    private FragmentPickAndCompare fragmentPickAndCompare;
    private FragmentImageEditor fragmentImageEditor;
    private FragmentImageControllers fragmentImageControllers;

    public FragmentManager fragmentManager;
    private LinearLayout firstLinearLayout, secondLinearLayout, thirdLinearLayout, fourthLinearLayout, headerStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.simulator, container,
                false);
        HairSimulator.context = HairSimulatorActivity.getContext();

        onInit(mView);

        fragmentManager = ((HairSimulatorActivity) getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.firstFragment, fragmentHairStyles).commit();
        fragmentManager.beginTransaction().replace(R.id.secondFragment, fragmentPickAndCompare).commit();
        fragmentManager.beginTransaction().replace(R.id.thirdFragment, fragmentImageEditor).commit();
        fragmentManager.beginTransaction().replace(R.id.imageOptionsFragment, fragmentImageControllers).commit();

        return mView;

    }

    private void onInit(View mView) {
        firstLinearLayout = (LinearLayout) mView.findViewById(R.id.firstLayoutLinear);
        secondLinearLayout = (LinearLayout) mView.findViewById(R.id.secondLinearLayout);
        thirdLinearLayout = (LinearLayout) mView.findViewById(R.id.thirdLinearLayout);
        fourthLinearLayout = (LinearLayout) mView.findViewById(R.id.fourthLinearLayout);

        headerStrip = (LinearLayout) mView.findViewById(R.id.headerStrip);

        firstLinearLayout.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(256), LinearLayout.LayoutParams.MATCH_PARENT));
        secondLinearLayout.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(175), LinearLayout.LayoutParams.MATCH_PARENT));
        thirdLinearLayout.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(375), LinearLayout.LayoutParams.MATCH_PARENT));
        fourthLinearLayout.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(165), LinearLayout.LayoutParams.MATCH_PARENT));

        onInitFragments();
    }

    public void onChangeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= 21) {
            if (!Utility.IS_MALE_THEME) {
                headerStrip.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
            } else {
                headerStrip.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
            }
        }
    }

    private void onInitFragments() {
        getFragmentHairStyles();
        getFragmentPickAndCompare();
        getFragmentImageEditor();
        getFragmentImageControllers();
    }

    public FragmentHairStyles getFragmentHairStyles() {
        if (fragmentHairStyles == null)
            fragmentHairStyles = new FragmentHairStyles();
        return fragmentHairStyles;

    }

    public FragmentPickAndCompare getFragmentPickAndCompare() {
        if (fragmentPickAndCompare == null)
            fragmentPickAndCompare = new FragmentPickAndCompare();
        return fragmentPickAndCompare;
    }

    public FragmentImageEditor getFragmentImageEditor() {
        if (fragmentImageEditor == null)
            fragmentImageEditor = new FragmentImageEditor();
        return fragmentImageEditor;
    }

    public FragmentImageControllers getFragmentImageControllers() {
        if (fragmentImageControllers == null)
            fragmentImageControllers = new FragmentImageControllers();
        return fragmentImageControllers;
    }

}
