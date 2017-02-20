package com.example.ninesole.photexpro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import adapters.ImageAdapter;
import yuku.ambilwarna.AmbilWarnaDialog;


public class MainCanvas extends AppCompatActivity {

    public static final int RequestPermissionCode = 1;
    public static RelativeLayout sblayout;
    //keep track of camera capture intent
    public final int CATEGORY_ID = 0;
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    final int DONE_CROP = 3;
    public Bitmap bitmap;
    public ImageView imageView;
    public ClipArt ca;
    public SeekBar sb_value, rotateBar;
    public RelativeLayout relativeLayout;
    public Button button2;
    public ImageView imgbg;
    public int DefaultColor;
    public int position;
    Context context;
    LinearLayout llGallery, llAddImage, llFrame, llSticker, llPlain;
    ImageView iv;
    /////
    RelativeLayout layBg;
    int count = 1000;
    Button buttonCamera, buttonGallery;
    File file;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent;
    DisplayMetrics displayMetrics;
    int width, height;
    Dialog dialog;
    //captured picture uri
    private Uri picUri;

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >= 100) {
            int value = (progress - 100) * 255 / 100;

            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);

        } else {
            int value = (100 - progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);


        }
    }

    /////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainCanvas.this;
        Init();
        Actions();
    }

    public void Init() {
        iv = (ImageView) findViewById(R.id.iv_main);

        //////////////////////
        sb_value = (SeekBar) findViewById(R.id.sb_value);
        layBg = (RelativeLayout) findViewById(R.id.laybg);
        imageView = (ImageView) findViewById(R.id.imageview);
        sblayout = (RelativeLayout) findViewById(R.id.seekbatlayout);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        button2 = (Button) findViewById(R.id.button2);
        //   buttonCamera = (Button)findViewById(R.id.button2);
        buttonGallery = (Button) findViewById(R.id.button1);
        EnableRuntimePermission();
        DefaultColor = ContextCompat.getColor(context, R.color.abc_hint_foreground_material_light);
        /////////////////////
    }

    public void Actions() {

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                OpenColorPickerDialog(false);

            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageFromGallery();

            }
        });
        layBg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                disableall();
            }
        });

        ca = new ClipArt(context);
        layBg.addView(ca);
        ca.setId(count++);


        ca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                disableall();
                Toast.makeText(context, "Selected Item Id : " + v.getId(), Toast.LENGTH_LONG).show();
            }
        });
        sb_value.setProgress(125);
        sb_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                ClipArt.image.setColorFilter(setBrightness(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {

            Toast.makeText(context, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
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
                GetImageFromGallery();
            }
        });
        llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageDialog();
                dialog.dismiss();
            }
        });
        llFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CATEGORY_ID);
                imgbg.setBackgroundResource(0);
            }
        });
        llSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    public void GetImageFromGallery() {

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), PIC_CROP);

    }


    public void backgroundDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_background);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.iv_close);
        llPlain = (LinearLayout) dialog.findViewById(R.id.ll_plain);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llPlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenColorPickerDialog(false);
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
                GetImageFromGallery();
                dialog.dismiss();

            }
        });
        llCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Open Cam", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

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
//                performCropImage(picUri);
                imageCropFunction(picUri);

            }
            if (requestCode == PIC_CROP) {
                if (data != null) {
                    uri = data.getData();
                    imageCropFunction(uri);

                }
            }
            if (requestCode == DONE_CROP) {
                if (data != null) {

                    Bundle bundle = data.getExtras();

                    bitmap = bundle.getParcelable("data");

                    ClipArt.image.setVisibility(View.VISIBLE);
                    ClipArt.imgring.setVisibility(View.VISIBLE);
                    ClipArt.btndel.setVisibility(View.VISIBLE);
                    ClipArt.btnrot.setVisibility(View.VISIBLE);
                    ClipArt.btnscl.setVisibility(View.VISIBLE);
                    ClipArt.image.setImageBitmap(bitmap);
                }
            }
        }
    }

//    private boolean performCropImage(Uri mFinalImageUri) {
//        try {
//            if (mFinalImageUri != null) {
//                //call the standard crop action intent (the user device may not support it)
//                Intent cropIntent = new Intent("com.android.camera.action.CROP");
//                //indicate image type and Uri
//                cropIntent.setDataAndType(mFinalImageUri, "image/*");
//                //set crop properties
//                cropIntent.putExtra("crop", "true");
//                //indicate aspect of desired crop
//                cropIntent.putExtra("aspectX", 1);
//                cropIntent.putExtra("aspectY", 1);
//                cropIntent.putExtra("scale", true);
//                //indicate output X and Y
//                cropIntent.putExtra("outputX", 500);
//                cropIntent.putExtra("outputY", 500);
//                //retrieve data on return
//                cropIntent.putExtra("return-data", false);
//
//                File f = createNewFile("CROP_");
//                try {
//                    f.createNewFile();
//                } catch (IOException ex) {
//                    Log.e("io", ex.getMessage());
//                }
//
//                Uri mCropImagedUri = Uri.fromFile(f);
//                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
//                //start the activity - we handle returning in onActivityResult
//                startActivityForResult(cropIntent, PIC_CROP);
//                return true;
//            }
//        } catch (ActivityNotFoundException anfe) {
//            //display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//            return false;
//        }
//        return false;
//    }

    public void imageCropFunction(Uri uri) {

        // Image Crop Code
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, DONE_CROP);

        } catch (ActivityNotFoundException e) {

        }
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

    public void disableall() {

        for (int i = 0; i < layBg.getChildCount(); i++) {

            if (layBg.getChildAt(i) instanceof ClipArt) {

                ((ClipArt) layBg.getChildAt(i)).disableAll();

            }

        }

    }

    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(context, DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;

                relativeLayout.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

                Toast.makeText(context, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }

    //Calling Dialog
    protected Dialog onCreateDialog(int id) {

        switch (id) {

            case CATEGORY_ID:

                AlertDialog.Builder builder;
                Context mContext = this;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.backgrounds, (ViewGroup) findViewById(R.id.layout_root));
                GridView gridview = (GridView) layout.findViewById(R.id.gridview);
                gridview.setAdapter(new ImageAdapter(this));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View arg1,
                                            int pos, long arg3) {
                        // relativeLayout.setBackgroundResource((mThumbIds[pos]));
                        position = pos;
                        setbg();

                        // TODO Auto-generated method stub
                        //     Toast.makeText(arg1.getContext(), "Position is "+arg2, 3000).show();
                    }
                });
                ImageView close = (ImageView) layout.findViewById(R.id.close);
                imgbg = (ImageView) layout.findViewById(R.id.imgbg);

                close.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if ((dialog.isShowing())) {
                            relativeLayout.setBackgroundResource((ImageAdapter.mThumbIds[position]));
                        }

                        //  dialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(mContext);

                builder.setView(layout);
                dialog = builder.create();

                break;
            default:

                dialog = null;
        }
        return dialog;
    }

    public void setbg() {
        imgbg.setBackgroundResource((ImageAdapter.mThumbIds[position]));
        imgbg.setOnTouchListener(new View.OnTouchListener() {
            float xCoOrdinate, yCoOrdinate;

            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xCoOrdinate = view.getX() - event.getRawX();
                        yCoOrdinate = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
