package com.testapp.hairsimulator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;

/**
 * Created by bharath.simha on 09/01/16.
 */
public class FragmentPickAndCompare extends Fragment {

    public RelativeLayout relativeLayout, borderLayout, compareLayout1, compareLayout2, compareLayout3;
    public ImageView choosePicture, imageCompareIV3, imageCompareIV2, imageCompareIV1;
    private TextView compareImagesLabel;
    public Button addPictureButton, fullScreenButton, choosePicture1, choosePicture2, choosePicture3;
    public ImageButton close1, close2, close3;

    private HairSimulator hairSimulator;

    private FragmentManager fragmentManager;
    private FragmentImageEditor fragmentImageEditor;

    private Context context;

    private Button buttonSelected;

    public boolean isCompare1 = false, isCompare2 = false, isCompare3 = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_pick_compare, container,
                false);

        if (getArguments() == null) {

            context = HairSimulator.context;
            hairSimulator = ((HairSimulatorActivity) getActivity()).getHairSimulator();

            fragmentManager = hairSimulator.fragmentManager;
            fragmentImageEditor = hairSimulator.getFragmentImageEditor();

            relativeLayout = (RelativeLayout) mView.findViewById(R.id.choosePictureLayout);
            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(Utility.dpToPx(175), RelativeLayout.LayoutParams.MATCH_PARENT));

            borderLayout = (RelativeLayout) mView.findViewById(R.id.borderLayout);
            borderLayout.setLayoutParams(setCustomRelativeLayoutParamsHorizontal(Utility.dpToPx(154), RelativeLayout.LayoutParams.WRAP_CONTENT));

            choosePicture = (ImageView) mView.findViewById(R.id.choosePicture);
            compareImagesLabel = (TextView) mView.findViewById(R.id.compareImagesLabel);

            addPictureButton = (Button) mView.findViewById(R.id.addPictureButton);
            addPictureButton.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));

            fullScreenButton = (Button) mView.findViewById(R.id.fullScreenButton);
            fullScreenButton.setLayoutParams(setCustomRelativeLayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utility.dpToPx(38)));
            fullScreenButton.setTextSize(Utility.dpToPx(Utility.TEXT_SIZE));

            compareLayout1 = (RelativeLayout) mView.findViewById(R.id.borderCompareImagesItemLayout1);
            compareLayout2 = (RelativeLayout) mView.findViewById(R.id.borderCompareImagesItemLayout2);
            compareLayout3 = (RelativeLayout) mView.findViewById(R.id.borderCompareImagesItemLayout3);

            compareLayout1.setLayoutParams(setCustomLinearLayoutParams(Utility.dpToPx(124), Utility.dpToPx(155)));
            compareLayout2.setLayoutParams(setCustomLinearLayoutParams(Utility.dpToPx(124), Utility.dpToPx(155)));
            compareLayout3.setLayoutParams(setCustomLinearLayoutParams(Utility.dpToPx(124), Utility.dpToPx(155)));

            choosePicture1 = (Button) mView.findViewById(R.id.chooseImageButton1);
            choosePicture2 = (Button) mView.findViewById(R.id.chooseImageButton2);
            choosePicture3 = (Button) mView.findViewById(R.id.chooseImageButton3);

            imageCompareIV1 = (ImageView) mView.findViewById(R.id.imageCompareIV1);
            imageCompareIV2 = (ImageView) mView.findViewById(R.id.imageCompareIV2);
            imageCompareIV3 = (ImageView) mView.findViewById(R.id.imageCompareIV3);

            close1 = (ImageButton) mView.findViewById(R.id.close1);
            close2 = (ImageButton) mView.findViewById(R.id.close2);
            close3 = (ImageButton) mView.findViewById(R.id.close3);

            choosePicture1.setTypeface(Utility.getFont(context));
            choosePicture2.setTypeface(Utility.getFont(context));
            choosePicture3.setTypeface(Utility.getFont(context));
            fullScreenButton.setTypeface(Utility.getFont(context));
            addPictureButton.setTypeface(Utility.getFont(context));

            applyLadiesTheme();

            addPictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOpenDialog(v, new String[]{"Take Photo", "Pick from Gallery"});
                }
            });

            choosePicture1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOpenDialog(v, new String[]{"Take Photo", "Pick from Gallery", "Pick from My Styles"});
                }
            });

            choosePicture2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOpenDialog(v, new String[]{"Take Photo", "Pick from Gallery", "Pick from My Styles"});
                }
            });

            choosePicture3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOpenDialog(v, new String[]{"Take Photo", "Pick from Gallery", "Pick from My Styles"});
                }
            });

            fullScreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCompare1 || isCompare2 || isCompare3) {
                        Intent intent = new Intent(context, FullScreenActivity.class);
                        if ((imageCompareIV1.getDrawable()) != null)
                            Utility.bm1 = ((BitmapDrawable) imageCompareIV1.getDrawable()).getBitmap();
                        if ((imageCompareIV2.getDrawable()) != null)
                            Utility.bm2 = ((BitmapDrawable) imageCompareIV2.getDrawable()).getBitmap();
                        if ((imageCompareIV3.getDrawable()) != null)
                            Utility.bm3 = ((BitmapDrawable) imageCompareIV3.getDrawable()).getBitmap();
                        startActivity(intent);
                    } else
                        Toast.makeText(context, "Please select three pictures for FullScreen mode", Toast.LENGTH_LONG).show();
                }
            });

            close1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageCompareIV1.setImageBitmap(null);
                    choosePicture1.setVisibility(View.VISIBLE);
                    isCompare1 = false;
                }
            });

            close2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageCompareIV2.setImageBitmap(null);
                    choosePicture2.setVisibility(View.VISIBLE);
                    isCompare2 = false;
                }
            });

            close3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageCompareIV3.setImageBitmap(null);
                    choosePicture3.setVisibility(View.VISIBLE);
                    isCompare3 = false;
                }
            });
        }

        return mView;
    }

    private void onOpenDialog(View view, String[] arrayOptions) {
        buttonSelected = (Button) view;
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        Bundle b = new Bundle();
        b.putStringArray("dialogValues", arrayOptions);
        customDialogFragment.setArguments(b);
        customDialogFragment.show(fragmentManager, "dialog " + buttonSelected.getText());
    }

    public static RelativeLayout.LayoutParams setCustomRelativeLayoutParams(int weight, int height) {
        RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(weight, height);
        relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        return relativeLayout;
    }

    public static LinearLayout.LayoutParams setCustomLinearLayoutParams(int weight, int height) {
        LinearLayout.LayoutParams relativeLayout = new LinearLayout.LayoutParams(weight, height);
        relativeLayout.setMargins(0, Utility.dpToPx(10), 0, 0);

        return relativeLayout;
    }

    public void onAddPicture(Bitmap selectedBitmap) {
        if (selectedBitmap != null) {
            choosePicture.setImageBitmap(selectedBitmap);
            addPictureButton.setText("Change Picture");
        }
    }

    public void onClearAddedPicture() {
        choosePicture.setImageBitmap(null);
        addPictureButton.setText("Add Picture");
    }

    public void applyGentsTheme() {

        addPictureButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        addPictureButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
        choosePicture1.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        choosePicture1.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
        choosePicture2.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        choosePicture2.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
        choosePicture3.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        choosePicture3.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
        fullScreenButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        fullScreenButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));

    }

    public void applyLadiesTheme() {

        addPictureButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        addPictureButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
        choosePicture1.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        choosePicture1.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
        choosePicture2.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        choosePicture2.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
        choosePicture3.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        choosePicture3.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
        fullScreenButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        fullScreenButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));

    }

    private RelativeLayout.LayoutParams setCustomRelativeLayoutParamsHorizontal(int weight, int height) {
        RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(weight, height);
        relativeLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);

        return relativeLayout;
    }

    public void onAction(int position) {

        if (position == fragmentImageEditor.TAKE_PICTURE) {

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            startActivityForResult(Intent.createChooser(intent, "Take Picture"), fragmentImageEditor.TAKE_PICTURE);

            Toast.makeText(hairSimulator.getContext(), "Please take Picture in Protrait mode for better experience", Toast.LENGTH_LONG).show();

        } else if (position == fragmentImageEditor.SELECT_PICTURE) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), fragmentImageEditor.SELECT_PICTURE);

        } else {
            Toast.makeText(context, "My Styles", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            Bitmap bitmap;
            try {
                if (requestCode == fragmentImageEditor.SELECT_PICTURE && null != data) {
                    selectedImage = data.getData();
                    if (buttonSelected == addPictureButton) {
                        Intent intent = new Intent(context, PictureEditActivity.class);
                        intent.putExtra("uri", selectedImage.toString());
                        startActivityForResult(intent, fragmentImageEditor.CROP_SELECT_PICTURE);
                    } else {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                            onLoadCompareImages(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                Crashlytics.getInstance().core.logException(new Exception(e.toString()));
            }
            try {
                if (requestCode == fragmentImageEditor.TAKE_PICTURE && null != data) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    if (buttonSelected == addPictureButton) {
                        Intent intent = new Intent(context, PictureEditActivity.class);
                        intent.putExtra("bitmap", bitmap);
                        startActivityForResult(intent, fragmentImageEditor.CROP_TAKEN_PICCTURE);
                    } else {
                        try {
                            bitmap = (Bitmap) data.getExtras().get("data");
                            onLoadCompareImages(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                Crashlytics.getInstance().core.logException(new Exception(e.toString()));
            }
        }

        try {
            if (requestCode == resultCode) {
                fragmentImageEditor.onClearSimulator();

                if (data.getExtras() != null) {

                    String filePath = data.getExtras().getString("filePath");

                    File imgFile = new File(filePath);
                    if (imgFile.exists()) {
                        Bitmap selectedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ImageView mainImage = new ImageView(context);
                        RelativeLayout.LayoutParams layoutParams;

                        if (requestCode == fragmentImageEditor.CROP_SELECT_PICTURE) {
                            selectedBitmap.createScaledBitmap(selectedBitmap, Utility.dpToPx(325), Utility.dpToPx(fragmentImageEditor.linearLayout.getHeight() - 50), true);
                            layoutParams = new RelativeLayout.LayoutParams(fragmentImageEditor.mergeImageContainer.getWidth(), fragmentImageEditor.mergeImageContainer.getHeight());
                        } else {
                            layoutParams = new RelativeLayout.LayoutParams(selectedBitmap.getWidth(), selectedBitmap.getHeight());
                        }
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        mainImage.setLayoutParams(layoutParams);
                        mainImage.setImageBitmap(selectedBitmap);
                        fragmentImageEditor.onAddImageToContainer(mainImage);

                        onAddPicture(selectedBitmap);
                        hairSimulator.isPhotoSelected = true;
                    }
                }
            }
        } catch (Exception e) {
            Crashlytics.getInstance().core.logException(new Exception(e.toString()));
        }
    }

    public void onAddFirstChoosePicture(Bitmap bitmap) {
        if (bitmap != null) {
            imageCompareIV1.setImageBitmap(bitmap);
            choosePicture1.setVisibility(View.GONE);
            isCompare1 = true;
        }
    }

    public void onAddSecondChoosePicture(Bitmap bitmap) {
        if (bitmap != null) {
            imageCompareIV2.setImageBitmap(bitmap);
            choosePicture2.setVisibility(View.GONE);
            isCompare2 = true;
        }
    }

    public void onAddThirdChoosePicture(Bitmap bitmap) {
        if (bitmap != null) {
            imageCompareIV3.setImageBitmap(bitmap);
            choosePicture3.setVisibility(View.GONE);
            isCompare3 = true;
        }
    }

    private void onLoadCompareImages(Bitmap bitmap) {
        if (buttonSelected == choosePicture1) {
            imageCompareIV1.setImageBitmap(bitmap);
            choosePicture1.setVisibility(View.GONE);
            isCompare1 = true;
        } else if (buttonSelected == choosePicture2) {
            imageCompareIV2.setImageBitmap(bitmap);
            choosePicture2.setVisibility(View.GONE);
            isCompare2 = true;
        } else if (buttonSelected == choosePicture3) {
            imageCompareIV3.setImageBitmap(bitmap);
            choosePicture3.setVisibility(View.GONE);
            isCompare3 = true;
        }
    }
}
