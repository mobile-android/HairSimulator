package com.testapp.hairsimulator;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by bharath.simha on 16/01/16.
 */
public class PictureEditActivity extends FragmentActivity {

    private CropImageView cropImageView;
    private RelativeLayout cropActionLayout;
    private ProgressBar imageProgressBar;
    private Uri selectedImage;
    public Bitmap croppedImageBitmap;
    private String picturePath;
    private File pictureFile;
    private boolean isFromGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_fragment);

        cropImageView = (CropImageView) findViewById(R.id.image_preview);
        cropActionLayout = (RelativeLayout) findViewById(R.id.crop_action_layout);
        cropActionLayout.setEnabled(false);

        imageProgressBar = (ProgressBar) findViewById(R.id.image_progress_bar);
        imageProgressBar.setVisibility(View.GONE);

        Utility.onChangeStatusBarColor(getApplicationContext(), getWindow());

        try {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().get("uri") != null) {
                    isFromGallery = true;
                    selectedImage = Uri.parse(getIntent().getExtras().getString("uri"));
                    BitmapWorkerTask task = new BitmapWorkerTask(cropImageView, null, selectedImage);
                    task.execute();
                } else {
                    isFromGallery = false;
                    Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("bitmap");
                    cropImageView.setImageBitmap(bitmap);
                    cropActionLayout.setEnabled(true);
                }
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }

        cropActionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageProgressBar.setVisibility(View.VISIBLE);
                imageProgressBar.bringToFront();
                croppedImageBitmap = cropImageView.getCroppedImage();
                cropActionLayout.setEnabled(false);
                Bundle bundle = new Bundle();
                try {
                    String filePath = createFileBitmap(croppedImageBitmap);
                    bundle.putString("filePath", filePath);
                    Intent intent = getIntent().putExtras(bundle);
                    if (isFromGallery)
                        setResult(FragmentImageEditor.CROP_SELECT_PICTURE, intent);
                    else
                        setResult(FragmentImageEditor.CROP_TAKEN_PICCTURE, intent);

                    imageProgressBar.setVisibility(View.GONE);
                    finish();

                    croppedImageBitmap.recycle();


                } catch (Exception e) {

                }
            }
        });
    }

    @Nullable
    private String getAbsolutePath(Uri uri) {
        if (Build.VERSION.SDK_INT >= 19) {
            String id = "";
            if (uri.getLastPathSegment().split(":").length > 1)
                id = uri.getLastPathSegment().split(":")[1];
            else if (uri.getLastPathSegment().split(":").length > 0)
                id = uri.getLastPathSegment().split(":")[0];
            if (id.length() > 0) {
                final String[] imageColumns = {MediaStore.Images.Media.DATA};
                final String imageOrderBy = null;
                Uri tempUri = getUri();
                try {
                    Cursor imageCursor = getApplicationContext().getContentResolver().query(tempUri, imageColumns, MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
                    if (imageCursor != null) {
                        String path = null;
                        if (imageCursor.getCount() > 0 && imageCursor.moveToFirst()) {
                            path = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        imageCursor.close();
                        return path;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            String[] projection = {MediaStore.MediaColumns.DATA};
            try {
                Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
                String path = null;
                if (cursor != null) {
                    if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        path = cursor.getString(column_index);
                    }
                    cursor.close();
                    return path;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<CropImageView> imageViewReference;
        byte[] data;
        Uri picturePath;
        int orientatation = 0;

        public BitmapWorkerTask(CropImageView imageView, byte[] imageData, Uri picturePath) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<CropImageView>(imageView);
            this.data = imageData;
            this.picturePath = picturePath;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {

            Bitmap bitmap = null;
            int targetW = 800;//imagePlace.getWidth();
            int targetH = 800;//imagePlace.getHeight();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            try {
                if (data != null) {
                    BitmapFactory.decodeByteArray(data, 0, data.length, bmOptions);
                } else {
                    String absolutePath = getAbsolutePath(picturePath);
                    if (!Utility.isNullOrEmpty(absolutePath)) {
                        ExifInterface ei = new ExifInterface(absolutePath);
                        orientatation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    }
                    AssetFileDescriptor fileDescriptor;

                    Log.d("Picking picture ", String.valueOf(picturePath));

                    fileDescriptor =
                            getApplicationContext().getContentResolver().openAssetFileDescriptor(picturePath, "r");
                    BitmapFactory.decodeFileDescriptor(
                            fileDescriptor.getFileDescriptor(), null, bmOptions);
                }
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                Log.d("Photo Width and height", "photoW  " + photoW + " photoH " + photoH);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && scaleFactor <= 1) {
                    scaleFactor = 2;
                }
                bmOptions.inJustDecodeBounds = false;
                //    bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
                if (data != null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bmOptions);// BitmapFactory.decodeFile(pictureFile.getAbsolutePath(), bmOptions);
                } else {
                    InputStream iStream = getApplicationContext().getContentResolver().openInputStream(picturePath);
                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];

                    if (iStream != null) {
                        int len = 0;
                        Log.d("PEA", "Istream NOT null");
                        while ((len = iStream.read(buffer)) != -1) {
                            byteBuffer.write(buffer, 0, len);
                        }
                    } else {
                        Log.d("PEA", "Istream null");
                    }
                    byte[] inputData = byteBuffer.toByteArray();
                    bitmap = BitmapFactory.decodeByteArray(inputData, 0, inputData.length, bmOptions);
                    if (iStream != null)
                        try {
                            iStream.close();
                        } catch (IOException e) {
                            Log.d("PEA", e.getMessage());
                        }
                }
                Log.d("PEA", "SCALE FACTOR " + scaleFactor + " WIDTH  " + bitmap.getWidth() + " HEIGHT " + bitmap.getHeight());
            } catch (Exception e) {
                bitmap = null;
                Log.d("Error", e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                cropImageView = imageViewReference.get();
                if (cropImageView != null) {
                    if (bitmap.getHeight() > 2400) {
                        // this is the case when the bitmap fails to load
                        float aspect_ratio = ((float) bitmap.getHeight()) / ((float) bitmap.getWidth());
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                (int) (2400 * 0.7),
                                (int) ((2400 * 0.7) * aspect_ratio));
                    }
                    if (bitmap.getWidth() > 2400) {
                        float aspect_ratio = ((float) bitmap.getHeight()) / ((float) bitmap.getWidth());
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) ((2400 * 0.7) * aspect_ratio),
                                (int) (2400 * 0.7)
                        );
                    }
                    cropImageView.setImageBitmap(bitmap);

                }
                cropActionLayout.setEnabled(true);
                imageProgressBar.setVisibility(View.GONE);
            } else {
                showPictureLoadErrorDialog();
            }
        }
    }

    private String createFileBitmap(Bitmap bitmap) {
        String file = null;
        File dir;

        Bitmap.CompressFormat imageFormat = Bitmap.CompressFormat.JPEG;
        String fileExtension = "%d.jpg";

        dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String fileName = String.format(fileExtension, System.currentTimeMillis());
        pictureFile = new File(dir, fileName);
        if (pictureFile == null) {
            return null;
        }
        ByteArrayOutputStream internal_output_stream = new ByteArrayOutputStream();
        bitmap.compress(imageFormat, 90, internal_output_stream);

        FileOutputStream fos = null;
        try {
            picturePath = pictureFile.getAbsolutePath();
            fos = new FileOutputStream(pictureFile);
            fos.write(internal_output_stream.toByteArray());
            if (!isFromGallery) {
                addImageToGallery(picturePath, getApplicationContext());
            }

        } catch (Exception e) {
            Log.d("Exception caused ", e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.d("Exception caused ", e.getMessage());
                }
            }
        }
        file = picturePath;
        return file;
    }

    private void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    private void showPictureLoadErrorDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Picture Error");
        alertDialogBuilder
                .setMessage("Error loading image")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}

