package com.testapp.hairsimulator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.File;

/**
 * Created by bharath.simha on 10/01/16.
 */
public class FragmentImageEditor extends Fragment {

    public static final int TAKE_PICTURE = 0;
    public static final int SELECT_PICTURE = 1;
    public static final int CROP_TAKEN_PICCTURE = 2;
    public static final int CROP_SELECT_PICTURE = 3;

    private HairSimulator hairSimulator;

    public RelativeLayout mergeImageContainer;

    public LinearLayout linearLayout;
    public Button photoButton, galleryButton, stylesButton;

    public ImageView mainImage;

    public ImageView hairStyle = null;

    private Context context;

    private Bitmap selectedBitmap;

    private ProgressDialog progressDialog;

    private Handler handler = new Handler();

    public Bitmap finalImageBitmap = null;

    public boolean isContainerImages = false, isMainImage = false, isHairStyleImage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_image_editor, container,
                false);

        context = HairSimulator.context;
        hairSimulator = ((HairSimulatorActivity) getActivity()).getHairSimulator();

        linearLayout = (LinearLayout) mView.findViewById(R.id.thirdLayout);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(Utility.dpToPx(375), ViewGroup.LayoutParams.MATCH_PARENT));

        photoButton = (Button) mView.findViewById(R.id.photoButton);
        galleryButton = (Button) mView.findViewById(R.id.galleryButton);
        stylesButton = (Button) mView.findViewById(R.id.stylesButton);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mergeImageContainer = (RelativeLayout) mView.findViewById(R.id.mergeImageContainer);

        photoButton.setLayoutParams(Utility.setCustomLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utility.dpToPx(35)));
        galleryButton.setLayoutParams(Utility.setCustomLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utility.dpToPx(35)));
        stylesButton.setLayoutParams(Utility.setCustomLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utility.dpToPx(35)));

        photoButton.setTextSize(Utility.dpToPx(9.0f));
        galleryButton.setTextSize(Utility.dpToPx(9.0f));
        stylesButton.setTextSize(Utility.dpToPx(9.0f));

        galleryButton.setTypeface(Utility.getFont(context));
        photoButton.setTypeface(Utility.getFont(context));
        stylesButton.setTypeface(Utility.getFont(context));

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                intent.putExtra("return-data", true);
                startActivityForResult(Intent.createChooser(intent, "Take Picture"), TAKE_PICTURE);

                Toast.makeText(hairSimulator.getContext(), "Please take Picture in Portrait mode for better experience", Toast.LENGTH_LONG).show();
            }
        });

        stylesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Will show My Styles later", Toast.LENGTH_SHORT).show();
            }
        });

        applyLadiesTheme();

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            Intent intent = new Intent(context, PictureEditActivity.class);
            if (resultCode == Activity.RESULT_OK) {
                hairSimulator.isPhotoSelected = Boolean.TRUE;
                if (requestCode == SELECT_PICTURE && null != data) {
                    Uri selectedImage = data.getData();
                    intent.putExtra("uri", selectedImage.toString());
                    startActivityForResult(intent, CROP_SELECT_PICTURE);
                }
                if (requestCode == TAKE_PICTURE && null != data) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    intent.putExtra("bitmap", bitmap);
                    startActivityForResult(intent, CROP_TAKEN_PICCTURE);
                }
            }
        } catch (Exception e) {
            Crashlytics.getInstance().core.logException(new Exception(e.toString()));
        }

        try {

            if (requestCode == resultCode && null != data) {

                onClearSimulator();

                if (data.getExtras() != null) {

                    String filePath = data.getExtras().getString("filePath");

                    File imgFile = new File(filePath);
                    if (imgFile.exists()) {
                        selectedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        mainImage = new ImageView(context);
                        mainImage.setId(Integer.valueOf(64467));
                        RelativeLayout.LayoutParams layoutParams;

                        selectedBitmap.createScaledBitmap(selectedBitmap, Utility.dpToPx(325), Utility.dpToPx(linearLayout.getHeight() - 50), true);
                        layoutParams = new RelativeLayout.LayoutParams(mergeImageContainer.getWidth(), mergeImageContainer.getHeight());

                   /* if (requestCode == CROP_SELECT_PICTURE) {
                        selectedBitmap.createScaledBitmap(selectedBitmap, Utility.dpToPx(325), Utility.dpToPx(linearLayout.getHeight() - 50), true);
                        layoutParams = new RelativeLayout.LayoutParams(mergeImageContainer.getWidth(), mergeImageContainer.getHeight());
                    } else {
                        layoutParams = new RelativeLayout.LayoutParams(selectedBitmap.getWidth(), selectedBitmap.getHeight());
                    }*/
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        mainImage.setLayoutParams(layoutParams);
                        mainImage.setImageBitmap(selectedBitmap);
                        onAddImageToContainer(mainImage);
                        isMainImage = true;

                        hairSimulator.getFragmentPickAndCompare().onAddPicture(selectedBitmap);

                        hairSimulator.isPhotoSelected = true;

                    }
                }
            }
        } catch (Exception e) {
            Crashlytics.getInstance().core.logException(new Exception(e.toString()));
        }
    }

    public void addImagetoContainer(int image) {
        if (mergeImageContainer != null) {
            if (hairStyle != null) {
                mergeImageContainer.removeView(hairStyle);
                manipulateImages(image);
            } else
                manipulateImages(image);
        }
    }

    private void manipulateImages(int imageId) {
        hairStyle = new ImageView(context);
        hairStyle.setId(Integer.valueOf(98777));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mergeImageContainer.getWidth(), mergeImageContainer.getHeight());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        hairStyle.setLayoutParams(layoutParams);
        hairStyle.setImageResource(imageId);

        onAddImageToContainer(hairStyle);

        isHairStyleImage = true;
    }

    public void onClearSimulator() {
        if (mergeImageContainer != null) {
            mergeImageContainer.removeAllViews();
        }
    }

    public void applyLadiesTheme() {

        galleryButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        galleryButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
        photoButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        photoButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));
        stylesButton.setBackgroundColor(ContextCompat.getColor(context, R.color.ladiesBackgroundColor));
        stylesButton.setTextColor(ContextCompat.getColor(context, R.color.ladiesBrightColor));

    }

    public void applyGentsTheme() {

        galleryButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        galleryButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
        photoButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        photoButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
        stylesButton.setBackgroundColor(ContextCompat.getColor(context, R.color.gentsBackgroundColor));
        stylesButton.setTextColor(ContextCompat.getColor(context, R.color.gentsBrightColor));
    }

    public void zoomIn(ImageView hairStyle) {

        float x = hairStyle.getScaleX();
        float y = hairStyle.getScaleY();

        hairStyle.setScaleX(x + 0.1f);
        hairStyle.setScaleY(y + 0.1f);

        hairStyle.buildDrawingCache();

    }

    public void zoomOut(ImageView hairStyle) {

        float x = hairStyle.getScaleX();
        float y = hairStyle.getScaleY();

        if (x > 0.1 && y > 0.1) {
            hairStyle.setScaleX(x - 0.1f);
            hairStyle.setScaleY(y - 0.1f);
        } else
            Toast.makeText(context, "Maximum Zoom Out reached!!!", Toast.LENGTH_LONG).show();

        hairStyle.buildDrawingCache();

    }

    public void onMoveLeft(ImageView hairStyle) {
        hairStyle.setX(hairStyle.getX() - 10);
        hairStyle.buildDrawingCache();
    }

    public void onMoveRight(ImageView hairStyle) {
        hairStyle.setX(hairStyle.getX() + 10);
        hairStyle.buildDrawingCache();
    }

    public void onMoveTop(ImageView hairStyle) {
        hairStyle.setY(hairStyle.getY() - 10);
        hairStyle.buildDrawingCache();
    }

    public void onMoveBottom(ImageView hairStyle) {
        hairStyle.setY(hairStyle.getY() + 10);
        hairStyle.buildDrawingCache();
    }

    public void onMergeImages() {

        if (progressDialog != null)
            progressDialog.show();
        else {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Saving.....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        handler.postDelayed(new Runnable() {
            public void run() {
                finalImageBitmap = Bitmap.createBitmap(mergeImageContainer.getWidth(), mergeImageContainer.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(finalImageBitmap);
                mergeImageContainer.draw(canvas);

                ImageView ivFinalImage = new ImageView(context);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mergeImageContainer.getWidth(), mergeImageContainer.getHeight());
                ivFinalImage.setLayoutParams(layoutParams);
                ivFinalImage.setImageBitmap(finalImageBitmap);

                onClearSimulator();
                onAddImageToContainer(ivFinalImage);

                hairSimulator.getFragmentImageControllers().isSaved = true;

                progressDialog.dismiss();

                Toast.makeText(context, "Successfully Saved!!!", Toast.LENGTH_LONG).show();

            }
        }, 3000);
    }

    public void onAddImageToContainer(ImageView imageView) {
        mergeImageContainer.addView(imageView);
        isContainerImages = true;
    }

}
