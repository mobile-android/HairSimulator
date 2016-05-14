package com.testapp.hairsimulator;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

/**
 * Created by bharath.simha on 09/01/16.
 */
public class FragmentHairStyles extends Fragment {

    public RelativeLayout relativeLayout;
    public Button male, female;
    public View maleView, femaleView;
    public Button shortButton, mediumButton, longButton;
    public RecyclerView recyclerView;
    public LinearLayout recyclerViewLayout;
    public LinearLayout lastImageInstance = null;
    private Context context;
    private HairSimulator hairSimulator;
    private HairStyleImagesAdapter hairStyleImagesAdapter;

    private int[] longImages = {R.drawable.long_1, R.drawable.long_2, R.drawable.long_3, R.drawable.long_4, R.drawable.long_5, R.drawable.long_6};
    private int[] mediumImages = {R.drawable.medium_1, R.drawable.medium_2, R.drawable.medium_3, R.drawable.medium_4, R.drawable.medium_5, R.drawable.medium_6};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_hair_styles, container,
                false);

        context = HairSimulator.context;
        hairSimulator = ((HairSimulatorActivity) getActivity()).getHairSimulator();

        relativeLayout = (RelativeLayout) mView.findViewById(R.id.firstLayout);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(Utility.dpToPx(256), ViewGroup.LayoutParams.MATCH_PARENT));

        male = (Button) mView.findViewById(R.id.maleButton);
        female = (Button) mView.findViewById(R.id.femaleButton);

        shortButton = (Button) mView.findViewById(R.id.shortTypeButton);
        mediumButton = (Button) mView.findViewById(R.id.mediumTypeButton);
        longButton = (Button) mView.findViewById(R.id.largeTypeButton);

        maleView = (View) mView.findViewById(R.id.maleButtonView);
        femaleView = (View) mView.findViewById(R.id.femaleButtonView);

        recyclerViewLayout = (LinearLayout) mView.findViewById(R.id.recyclerview_grid_layout);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview_grid);

        doViewManipulations();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(llm);

        hairStyleImagesAdapter = new HairStyleImagesAdapter();
        hairStyleImagesAdapter.imagesArray(mediumImages);
        recyclerView.setAdapter(hairStyleImagesAdapter);

        setSelectedButtonValues(mediumButton);
        setUnselectedSelectedButtonValues(shortButton);
        setUnselectedSelectedButtonValues(longButton);

        male.setTypeface(Utility.getFont(context));
        female.setTypeface(Utility.getFont(context));
        shortButton.setTypeface(Utility.getFont(context));
        mediumButton.setTypeface(Utility.getFont(context));
        longButton.setTypeface(Utility.getFont(context));

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utility.IS_MALE_THEME)
                    onThemeButtonsClicked(true);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.IS_MALE_THEME)
                    onThemeButtonsClicked(false);
            }
        });

        shortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hairSimulator.getFragmentImageEditor().isContainerImages) {
                    if (!hairSimulator.getFragmentImageControllers().isSaved) {
                        Toast.makeText(context, "Please save your current Style", Toast.LENGTH_LONG).show();
                    } else {
                        hairStyleImagesAdapter.imagesArray(longImages);
                        hairStyleImagesAdapter.notifyDataSetChanged();
                        setSelectedButtonValues(shortButton);
                        setUnselectedSelectedButtonValues(mediumButton);
                        setUnselectedSelectedButtonValues(longButton);
                    }
                } else {
                    hairStyleImagesAdapter.imagesArray(longImages);
                    hairStyleImagesAdapter.notifyDataSetChanged();
                    setSelectedButtonValues(shortButton);
                    setUnselectedSelectedButtonValues(mediumButton);
                    setUnselectedSelectedButtonValues(longButton);
                }
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hairSimulator.getFragmentImageEditor().isContainerImages) {
                    if (!hairSimulator.getFragmentImageControllers().isSaved) {
                        Toast.makeText(context, "Please save your current Style", Toast.LENGTH_LONG).show();
                    } else {
                        hairStyleImagesAdapter.imagesArray(mediumImages);
                        hairStyleImagesAdapter.notifyDataSetChanged();
                        setSelectedButtonValues(mediumButton);
                        setUnselectedSelectedButtonValues(shortButton);
                        setUnselectedSelectedButtonValues(longButton);
                    }
                } else {
                    hairStyleImagesAdapter.imagesArray(mediumImages);
                    hairStyleImagesAdapter.notifyDataSetChanged();
                    setSelectedButtonValues(mediumButton);
                    setUnselectedSelectedButtonValues(shortButton);
                    setUnselectedSelectedButtonValues(longButton);
                }
            }
        });

        longButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hairSimulator.getFragmentImageEditor().isContainerImages) {
                    if (!hairSimulator.getFragmentImageControllers().isSaved) {
                        Toast.makeText(context, "Please save your current Style", Toast.LENGTH_LONG).show();
                    } else {
                        hairStyleImagesAdapter.imagesArray(longImages);
                        hairStyleImagesAdapter.notifyDataSetChanged();
                        setSelectedButtonValues(longButton);
                        setUnselectedSelectedButtonValues(shortButton);
                        setUnselectedSelectedButtonValues(mediumButton);
                    }
                } else {
                    hairStyleImagesAdapter.imagesArray(longImages);
                    hairStyleImagesAdapter.notifyDataSetChanged();
                    setSelectedButtonValues(longButton);
                    setUnselectedSelectedButtonValues(shortButton);
                    setUnselectedSelectedButtonValues(mediumButton);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (hairSimulator.isPhotoSelected) {
                            if (lastImageInstance != null && lastImageInstance != (LinearLayout) view)
                                ((GradientDrawable) lastImageInstance.getBackground()).setStroke(2, Color.WHITE);
                            applyViewItemBorder((GradientDrawable) view.getBackground());
                            lastImageInstance = (LinearLayout) view;
                            hairSimulator.getFragmentImageEditor().addImagetoContainer(R.drawable.hairstyle);
                        } else
                            Toast.makeText(context, "Please pick/take you picture for HairStyle manipulation", Toast.LENGTH_LONG).show();
                    }
                }
        ));
        applyLadiesTheme();

        Utility.IS_MALE_THEME = false;

        return mView;
    }

    private void onThemeButtonsClicked(final boolean isMale) {
        if (hairSimulator.getFragmentImageEditor().isContainerImages) {
            if (!hairSimulator.getFragmentImageControllers().isSaved) {
                AlertDialog.Builder builder = new AlertDialog.Builder(hairSimulator.getActivity());
                builder.setTitle("Change Theme");
                builder.setMessage("Your work will not be Saved..");

                builder.setPositiveButton("Continue Anyway", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do do my action here
                        hairSimulator.getFragmentImageEditor().onClearSimulator();
                        hairSimulator.getFragmentPickAndCompare().onClearAddedPicture();
                        onsetThemesBasedOnType(isMale);
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("Save and Change Theme", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // I do not need any action here you might
                        FragmentImageEditor fragmentImageEditor = hairSimulator.getFragmentImageEditor();
                        if (fragmentImageEditor.isMainImage || fragmentImageEditor.isHairStyleImage) {
                            fragmentImageEditor.onMergeImages();
                            fragmentImageEditor.isMainImage = false;
                            fragmentImageEditor.isHairStyleImage = false;
                        }
                        hairSimulator.getFragmentPickAndCompare().onClearAddedPicture();
                        onsetThemesBasedOnType(isMale);
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            } else onsetThemesBasedOnType(isMale);
        } else onsetThemesBasedOnType(isMale);
    }

    private void onsetThemesBasedOnType(boolean isMale) {
        if (isMale)
            onMaleThemeClicked();
        else
            onFemaleThemeClicked();
    }

    private void onMaleThemeClicked() {
        Utility.IS_MALE_THEME = Boolean.TRUE;
        applyGentsTheme();
        hairSimulator.getFragmentPickAndCompare().applyGentsTheme();
        hairSimulator.getFragmentImageEditor().applyGentsTheme();
        hairSimulator.getFragmentImageControllers().applyGentsTheme();
        onChnageLastInstancebackground();
        hairSimulator.onChangeStatusBarColor();
        Utility.onChangeStatusBarColor(context, ((HairSimulatorActivity) getActivity()).getWindow());
        hairSimulator.getFragmentImageEditor().onClearSimulator();
    }

    private void onFemaleThemeClicked() {
        Utility.IS_MALE_THEME = Boolean.FALSE;
        applyLadiesTheme();
        hairSimulator.getFragmentPickAndCompare().applyLadiesTheme();
        hairSimulator.getFragmentImageEditor().applyLadiesTheme();
        hairSimulator.getFragmentImageControllers().applyLadiesTheme();
        onChnageLastInstancebackground();
        hairSimulator.onChangeStatusBarColor();
        Utility.onChangeStatusBarColor(context, ((HairSimulatorActivity) getActivity()).getWindow());
        hairSimulator.getFragmentImageEditor().onClearSimulator();
    }

    private void onChnageLastInstancebackground() {
        if (lastImageInstance != null) {
            GradientDrawable gradientDrawable = (GradientDrawable) lastImageInstance.getBackground();
            applyViewItemBorder(gradientDrawable);
        }
    }

    private void applyViewItemBorder(GradientDrawable gradientDrawable) {
        if (Utility.IS_MALE_THEME)
            gradientDrawable.setStroke(2, ContextCompat.getColor(context, R.color.gentsBrightColor));
        else
            gradientDrawable.setStroke(2, ContextCompat.getColor(context, R.color.ladiesBrightColor));
    }

    private void doViewManipulations() {

        male.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(128), Utility.dpToPx(60)));
        female.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(128), Utility.dpToPx(60)));

        shortButton.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(((int) 256 / 3)), Utility.dpToPx(38)));
        mediumButton.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(((int) 256 / 3)), Utility.dpToPx(38)));
        longButton.setLayoutParams(Utility.setCustomLinearLayoutParams(Utility.dpToPx(((int) 256 / 3)), Utility.dpToPx(38)));

        maleView.setLayoutParams(Utility.setCustomLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (Utility.dpToPx(2))));
        femaleView.setLayoutParams(Utility.setCustomLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (Utility.dpToPx(2))));

        male.setPadding(10, 0, 0, 0);
        female.setPadding(10, 0, 0, 0);

        male.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));
        female.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));
        shortButton.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));
        mediumButton.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));
        longButton.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));

    }

    private void setSelectedButtonValues(Button button) {
        button.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));
        button.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedHairStyleTypeBackground));
        button.setTextColor(ContextCompat.getColor(context, R.color.selectedHairStyleTypeTextColor));
    }

    private void setUnselectedSelectedButtonValues(Button button) {
        button.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));
        button.setBackgroundColor(ContextCompat.getColor(context, R.color.unselectedHairStyleTypeBackground));
        button.setTextColor(ContextCompat.getColor(context, R.color.unselectedHairStyleTypeTextColor));
    }

    public void applyGentsTheme() {

        recyclerView.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));

        male.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        male.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
        male.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gents_en, 0, 0, 0);
        maleView.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBrightColor));

        female.setBackgroundColor(Color.WHITE);
        female.setTextColor(ContextCompat.getColor(context, R.color.notSelectedTextColor));
        female.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ladies_dis, 0, 0, 0);
        femaleView.setBackgroundColor(Color.WHITE);
    }

    public void applyLadiesTheme() {

        recyclerView.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));

        female.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        female.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
        female.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ladies_en, 0, 0, 0);
        femaleView.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));

        male.setBackgroundColor(Color.WHITE);
        male.setTextColor(ContextCompat.getColor(context, R.color.notSelectedTextColor));
        male.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gents_dis, 0, 0, 0);
        maleView.setBackgroundColor(Color.WHITE);
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods

    }

}
