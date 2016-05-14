package com.testapp.hairsimulator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by bharath.simha on 09/01/16.
 */
public class FragmentImageControllers extends Fragment implements View.OnClickListener {

    private LinearLayout linearLayout;
    private Button shareButton, saveToMyStylesButton, compareButton;
    public ImageButton zoomInButton, zoomOutButton, upArrow, downArrow, leftArrow, rightArrow;

    public boolean isSaved = false;

    private Context context;
    private HairSimulator hairSimulator;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_image_controllers, container,
                false);

        context = HairSimulator.context;
        hairSimulator = ((HairSimulatorActivity) getActivity()).getHairSimulator();

        linearLayout = (LinearLayout) mView.findViewById(R.id.finalLayout);
        linearLayout.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(155), ViewGroup.LayoutParams.MATCH_PARENT));

        shareButton = (Button) mView.findViewById(R.id.shareButton);
        saveToMyStylesButton = (Button) mView.findViewById(R.id.saveToMyStylesButton);
        compareButton = (Button) mView.findViewById(R.id.compareButton);

        shareButton.setLayoutParams(setLinearLayoutParams(Utility.dpToPx(140), Utility.dpToPx(30)));
        saveToMyStylesButton.setLayoutParams(setLinearLayoutParams(Utility.dpToPx(140), Utility.dpToPx(30)));
        compareButton.setLayoutParams(setLinearLayoutParams(Utility.dpToPx(140), Utility.dpToPx(30)));

        zoomInButton = (ImageButton) mView.findViewById(R.id.zoomInButton);
        zoomOutButton = (ImageButton) mView.findViewById(R.id.zoomOutButton);
        upArrow = (ImageButton) mView.findViewById(R.id.upArrow);
        downArrow = (ImageButton) mView.findViewById(R.id.downArrow);
        leftArrow = (ImageButton) mView.findViewById(R.id.leftArrow);
        rightArrow = (ImageButton) mView.findViewById(R.id.rightArrow);

        shareButton.setTextSize(Utility.dpToPx(9.0f));
        saveToMyStylesButton.setTextSize(Utility.dpToPx(9.0f));
        compareButton.setTextSize(Utility.dpToPx(9.0f));

        shareButton.setPadding(3, 3, 3, 3);
        saveToMyStylesButton.setPadding(3, 3, 3, 3);
        compareButton.setPadding(3, 3, 3, 3);

        shareButton.setTypeface(Utility.getFont(context));
        saveToMyStylesButton.setTypeface(Utility.getFont(context));
        compareButton.setTypeface(Utility.getFont(context));

        applyLadiesTheme();

        saveToMyStylesButton.setOnClickListener(this);
        zoomInButton.setOnClickListener(this);
        zoomOutButton.setOnClickListener(this);
        upArrow.setOnClickListener(this);
        downArrow.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);
        compareButton.setOnClickListener(this);

        return mView;
    }

    private LinearLayout.LayoutParams setLinearLayoutParams(int weight, int height) {
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(weight, height);
        linearLayout.setMargins(0, 10, 0, 10);

        return linearLayout;

    }

    public void applyGentsTheme() {

        shareButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        shareButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));

        saveToMyStylesButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        saveToMyStylesButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));

        compareButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        compareButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));

        zoomInButton.setImageResource(R.drawable.merrun_zoom_in);
        zoomOutButton.setImageResource(R.drawable.merrun_zoom_out);
        upArrow.setImageResource(R.drawable.merrun_arrow_top);
        downArrow.setImageResource(R.drawable.merrun_arrow_top);
        leftArrow.setImageResource(R.drawable.merrun_arrow_top);
        rightArrow.setImageResource(R.drawable.merrun_arrow_top);

    }

    public void applyLadiesTheme() {

        shareButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        shareButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));

        saveToMyStylesButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        saveToMyStylesButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));

        compareButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        compareButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));

        zoomInButton.setImageResource(R.drawable.pink_zoom_in);
        zoomOutButton.setImageResource(R.drawable.pink_zoom_out);
        upArrow.setImageResource(R.drawable.pink_arrow_top);
        downArrow.setImageResource(R.drawable.pink_arrow_top);
        leftArrow.setImageResource(R.drawable.pink_arrow_top);
        rightArrow.setImageResource(R.drawable.pink_arrow_top);
    }

    @Override
    public void onClick(View v) {
        FragmentImageEditor fragmentImageEditor = hairSimulator.getFragmentImageEditor();
        switch (v.getId()) {
            case R.id.saveToMyStylesButton:
                if (fragmentImageEditor.isMainImage && fragmentImageEditor.isHairStyleImage) {
                    fragmentImageEditor.onMergeImages();
                    fragmentImageEditor.isMainImage = false;
                    fragmentImageEditor.isHairStyleImage = false;
                } else
                    Toast.makeText(context, "Images not ready!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.zoomInButton:
                onApplyZoomControls(true);
                break;
            case R.id.zoomOutButton:
                onApplyZoomControls(false);
                break;
            case R.id.upArrow:
                if (hairSimulator.getFragmentImageEditor().hairStyle != null)
                    hairSimulator.getFragmentImageEditor().onMoveTop(fragmentImageEditor.hairStyle);
                break;
            case R.id.downArrow:
                if (hairSimulator.getFragmentImageEditor().hairStyle != null)
                    hairSimulator.getFragmentImageEditor().onMoveBottom(fragmentImageEditor.hairStyle);
                break;
            case R.id.leftArrow:
                if (hairSimulator.getFragmentImageEditor().hairStyle != null)
                    hairSimulator.getFragmentImageEditor().onMoveLeft(fragmentImageEditor.hairStyle);
                break;
            case R.id.rightArrow:
                if (hairSimulator.getFragmentImageEditor().hairStyle != null)
                    hairSimulator.getFragmentImageEditor().onMoveRight(fragmentImageEditor.hairStyle);
                break;
            case R.id.compareButton:
                if (!isSaved)
                    Toast.makeText(context, "please save your Image before Comparision", Toast.LENGTH_LONG).show();
                else {
                    if (hairSimulator.getFragmentImageEditor().finalImageBitmap != null) {
                        if (!hairSimulator.getFragmentPickAndCompare().isCompare1) {
                            hairSimulator.getFragmentPickAndCompare().onAddFirstChoosePicture(hairSimulator.getFragmentImageEditor().finalImageBitmap);
                            Preferences.instance().setButtonValue("1");
                        } else if (!hairSimulator.getFragmentPickAndCompare().isCompare2) {
                            hairSimulator.getFragmentPickAndCompare().onAddSecondChoosePicture(hairSimulator.getFragmentImageEditor().finalImageBitmap);
                            Preferences.instance().setButtonValue("2");
                        } else if (!hairSimulator.getFragmentPickAndCompare().isCompare3) {
                            hairSimulator.getFragmentPickAndCompare().onAddThirdChoosePicture(hairSimulator.getFragmentImageEditor().finalImageBitmap);
                            Preferences.instance().setButtonValue("3");
                        }
                    }
                }
        }
    }

    private void onApplyZoomControls(boolean isZoomIn) {
        if (hairSimulator.getFragmentImageEditor().hairStyle != null) {
            if (!isZoomIn)
                hairSimulator.getFragmentImageEditor().zoomOut(hairSimulator.getFragmentImageEditor().hairStyle);
            else
                hairSimulator.getFragmentImageEditor().zoomIn(hairSimulator.getFragmentImageEditor().hairStyle);
        } else
            Toast.makeText(context, "Please select an Image first", Toast.LENGTH_LONG).show();
    }

}
