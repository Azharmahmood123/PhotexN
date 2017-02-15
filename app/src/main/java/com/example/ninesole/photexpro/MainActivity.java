package com.example.ninesole.photexpro;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    Context context;
    LinearLayout llGallery, llAddImage, llFrame, llSticker;
    ImageView iv;
    //captured picture uri
    private Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        Init();
        Actions();
    }

    public void Init() {
        iv = (ImageView) findViewById(R.id.iv_main);
    }

    public void Actions() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.keyboard:
                Toast.makeText(context, "Open KeyBoard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sticker:
                Toast.makeText(context, "Sticker Dialog", Toast.LENGTH_SHORT).show();
                stickerDialog();
                break;
            case R.id.background:
                Toast.makeText(context, "Backgroung Dialog", Toast.LENGTH_SHORT).show();
                backgroundDialog();
                break;

        }
        return true;


    }

    public void stickerDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sticker);
        llGallery = (LinearLayout) dialog.findViewById(R.id.ll_gallery);
        llAddImage = (LinearLayout) dialog.findViewById(R.id.ll_addimage);
        llFrame = (LinearLayout) dialog.findViewById(R.id.ll_frame);
        llSticker = (LinearLayout) dialog.findViewById(R.id.ll_sticker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.iv_close);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageDialog();
            }
        });
        llFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    public void backgroundDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_background);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.iv_close);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addImageDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_image_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final LinearLayout llCam;
        llGallery = (LinearLayout) dialog.findViewById(R.id.ll_gallery);
        llCam = (LinearLayout) dialog.findViewById(R.id.ll_cam);
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Open Gallery", Toast.LENGTH_SHORT).show();

            }
        });
        llCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Open Cam", Toast.LENGTH_SHORT).show();


                try {
                    //use standard intent to capture an image
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //we will handle the returned data in onActivityResult
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


        dialog.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
//user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                iv.setImageBitmap(photo);


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                picUri = getImageUri(context, photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                File finalFile = new File(getRealPathFromURI(tempUri));

                System.out.println(picUri);
                performCropImage(picUri);

            }
            if (requestCode == PIC_CROP) {
                if (data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    iv.setImageBitmap(photo);

                }
            }
        }
    }

    private boolean performCropImage(Uri mFinalImageUri) {
        try {
            if (mFinalImageUri != null) {
                //call the standard crop action intent (the user device may not support it)
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                //indicate image type and Uri
                cropIntent.setDataAndType(mFinalImageUri, "image/*");
                //set crop properties
                cropIntent.putExtra("crop", "true");
                //indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("scale", true);
                //indicate output X and Y
                cropIntent.putExtra("outputX", 500);
                cropIntent.putExtra("outputY", 500);
                //retrieve data on return
                cropIntent.putExtra("return-data", false);

                File f = createNewFile("CROP_");
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    Log.e("io", ex.getMessage());
                }

                Uri mCropImagedUri = Uri.fromFile(f);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                //start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, PIC_CROP);
                return true;
            }
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return false;
    }

    private File createNewFile(String prefix) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory() + "/mypics/");
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                Log.d(context.getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
            }
        }
        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));
        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}
