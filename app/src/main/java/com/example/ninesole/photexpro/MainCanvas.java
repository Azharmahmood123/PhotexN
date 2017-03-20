package com.example.ninesole.photexpro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import adapters.ImageAdapter;
import yuku.ambilwarna.AmbilWarnaDialog;


public class MainCanvas extends AppCompatActivity {

    public static final int RequestPermissionCode = 1;
    public static RelativeLayout sblayout, sblayoutText;
    public static String canvas_text = "";
    public static SeekBar sb_value, rotateBar, sb_value_text;
    public static SeekBar seekBar, seekBarX;
    public static LinearLayout llStyles;
    //keep track of camera capture intent
    public final int CATEGORY_ID = 0;
    public final int Fonts = 4;
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    final int DONE_CROP = 3;
    public Bitmap bitmap;
    public ImageView imageView;
    public ClipArt ca;
    public ClipArtText ca_text;
    public RelativeLayout relativeLayout;
    public Button button2;
    public ImageView imgbg;
    public int DefaultColor;
    public int position;
    Context context;
    LinearLayout llGallery, llAddImage, llFrame, llSticker, llPlain, llDevice;
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
    boolean bg = false;
    int add_text = 0;
    SharedPreferences pref;
    SharedPreferences.Editor ed;
    Button bBold, bItalic, bUnderline, bItBold;
    int i = 0;
    boolean italic = false, bold = false, underline = false, italic_bold = false;
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

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public static void TextSet(String txt) {
        canvas_text = txt;
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
        sb_value_text = (SeekBar) findViewById(R.id.sb_value_text);

        layBg = (RelativeLayout) findViewById(R.id.laybg);
        imageView = (ImageView) findViewById(R.id.imageview);
        sblayout = (RelativeLayout) findViewById(R.id.seekbatlayout);
        sblayoutText = (RelativeLayout) findViewById(R.id.seekbatlayouttext);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        button2 = (Button) findViewById(R.id.button2);
        //   buttonCamera = (Button)findViewById(R.id.button2);
        buttonGallery = (Button) findViewById(R.id.button1);
        EnableRuntimePermission();
        DefaultColor = ContextCompat.getColor(context, R.color.abc_hint_foreground_material_light);

        llStyles = (LinearLayout) findViewById(R.id.ll_styles);
        bBold = (Button) findViewById(R.id.b_bold);
        bItalic = (Button) findViewById(R.id.b_italic);
        bUnderline = (Button) findViewById(R.id.b_underline);
        bItBold = (Button) findViewById(R.id.b_it_bold);
        seekBar = (SeekBar) findViewById(R.id.sb_value_y);
        seekBar.setMax(360);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // prevent seeking on app creation
                ClipArtText.canvas_text.setRotationY((float) progress);
            }
        });
        seekBarX = (SeekBar) findViewById(R.id.sb_value_x);
        seekBarX.setMax(360);
        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // prevent seeking on app creation
                ClipArtText.canvas_text.setRotationX((float) progress);
            }
        });
        /////////////////////
    }

    public void Actions() {

        bBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bold) {
                    ClipArtText.canvas_text.setTypeface(null, Typeface.NORMAL);
                    bold = false;
                } else {
                    ClipArtText.canvas_text.setTypeface(null, Typeface.NORMAL);
                    ClipArtText.canvas_text.setTypeface(null, Typeface.BOLD);
                    bold = true;
                }

//


            }
        });
        bItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (italic) {
                    ClipArtText.canvas_text.setTypeface(null, Typeface.NORMAL);
                    italic = false;
                } else {
                    ClipArtText.canvas_text.setTypeface(null, Typeface.NORMAL);
                    ClipArtText.canvas_text.setTypeface(null, Typeface.ITALIC);

                    italic = true;
                }

            }
        });
        bUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas_text = ClipArtText.canvas_text.getText().toString();
                if (underline) {
                    ClipArtText.canvas_text.setText(canvas_text);
                    underline = false;
                } else {
                    SpannableString content = new SpannableString(ClipArtText.canvas_text.getText());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                    ClipArtText.canvas_text.setText(content);

//                    ClipArtText.canvas_text.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    underline = true;
                }

            }
        });
        bItBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (italic_bold) {
                    ClipArtText.canvas_text.setTypeface(null, Typeface.NORMAL);
                    italic_bold = false;
                } else {
                    ClipArtText.canvas_text.setTypeface(null, Typeface.NORMAL);
                    ClipArtText.canvas_text.setTypeface(null, Typeface.BOLD_ITALIC);
                    italic_bold = true;
                }


            }
        });


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
//                Toast.makeText(context, "Selected Item Id : " + v.getId(), Toast.LENGTH_LONG).show();
            }
        });
        sb_value_text.setProgress(255);
        sb_value_text.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                ClipArt.image.setColorFilter(setBrightness(progress));
//                canvas.drawColor(Color.argb(alpha, red, green, blue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipArtText.canvas_text.setTextSize(progress);

                    Log.e("progress_text", progress + "");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_value.setProgress(255);
        sb_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                ClipArt.image.setColorFilter(setBrightness(progress));
//                canvas.drawColor(Color.argb(alpha, red, green, blue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipArt.image.setImageAlpha(progress);
                    Log.e("progress", progress + "");
                }

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
                startActivity(new Intent(context, WriteTextAct.class));
//                finish();
                break;
            case R.id.sticker:
                Toast.makeText(context, "Sticker Dialog", Toast.LENGTH_SHORT).show();
                stickerDialog();
                break;
            case R.id.background:
                Toast.makeText(context, "Background Dialog", Toast.LENGTH_SHORT).show();
                backgroundDialog();
                break;

            case R.id.save:
                ClipArt.llVertical.setVisibility(View.GONE);
                new AlertDialog.Builder(context)
                        .setTitle("Save Post")
                        .setMessage("Are you sure you want to save the current post")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                disableall();

                                sb_value.setVisibility(View.GONE);
                                Bitmap snap = loadBitmapFromView(relativeLayout);
                                createImage(snap);
                                Toast.makeText(context, "saved to storage", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.save)
                        .show();

                break;

        }
        return true;


    }

    public void screenShot() {
        Bitmap mbitmap = getBitmapOFRootView(imageView);
        imageView.setImageBitmap(mbitmap);
        createImage(mbitmap);
    }

    public Bitmap getBitmapOFRootView(View v) {
        View rootview = v.getRootView();
        rootview.setDrawingCacheEnabled(true);
        Bitmap bitmap1 = rootview.getDrawingCache();
        return bitmap1;
    }

    public void createImage(Bitmap bmp) {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int min = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String time = hour + ":" + min + ":" + seconds;
        Log.i("Time", time);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File photexSnaps = new File(Environment.getExternalStorageDirectory() + "/PhotexPro/");
// have the object build the directory structure, if needed.
        photexSnaps.mkdirs();
        File file = new File(photexSnaps, "photex_" + time + ".jpg");
//        File file = new File(Environment.getExternalStorageDirectory()
//                + "/photex_" + time + ".jpg");
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + photexSnaps)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stickerDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(context, R.style.dialog_animate);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sticker);
        llGallery = (LinearLayout) dialog.findViewById(R.id.ll_gallery);
        llAddImage = (LinearLayout) dialog.findViewById(R.id.ll_addimage);
        llFrame = (LinearLayout) dialog.findViewById(R.id.ll_frame);
        llSticker = (LinearLayout) dialog.findViewById(R.id.ll_sticker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView closeButton = (ImageView) dialog.findViewById(R.id.iv_close);
        // if button is clicked, close the custom dialog
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GetImageFromGallery();

            }
        });
        llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                bg = false;
                addImageDialog();

            }
        });
        llFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
        final Dialog dialog = new Dialog(context, R.style.dialog_animate);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_background);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView closeButton = (ImageView) dialog.findViewById(R.id.iv_close);
        llPlain = (LinearLayout) dialog.findViewById(R.id.ll_plain);
        llDevice = (LinearLayout) dialog.findViewById(R.id.ll_device);
        // if button is clicked, close the custom dialog
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llPlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                OpenColorPickerDialog(false);

            }
        });
        llDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bg = true;
                dialog.dismiss();
                addImageDialog();
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


                    BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                    if (bg) {
                        relativeLayout.setBackgroundDrawable(ob);
                    } else {
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
    }

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
//        ClipArt.llVertical.setVisibility(View.INVISIBLE);
        sblayout.setVisibility(View.GONE);


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
                ImageView tick = (ImageView) layout.findViewById(R.id.close);
                imgbg = (ImageView) layout.findViewById(R.id.imgbg);

                tick.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onPause() {
        super.onPause();
        ed = getSharedPreferences("my", MODE_PRIVATE).edit();
        ed.putString(WriteTextAct.MAIN_TEXT, null);
        ed.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pref = getSharedPreferences("my", MODE_PRIVATE);
        Intent c_text;
        c_text = getIntent();
//        canvas_text = c_text.getStringExtra(WriteTextAct.MAIN_TEXT);
        canvas_text = pref.getString(WriteTextAct.MAIN_TEXT, null);
        add_text = c_text.getIntExtra("add_text", 0);
        if (canvas_text != null) {
            if (canvas_text.length() > 0) {
                ca_text = new ClipArtText(context);
                layBg.addView(ca_text);
                ca_text.setId(count++);
                ca_text.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();
//                Toast.makeText(context, "Selected Item Id : " + v.getId(), Toast.LENGTH_LONG).show();
                    }
                });
                ClipArtText.imgring.setVisibility(View.VISIBLE);
                ClipArtText.btndel.setVisibility(View.VISIBLE);
                ClipArtText.btnrot.setVisibility(View.VISIBLE);
                ClipArtText.btnscl.setVisibility(View.VISIBLE);
                ClipArtText.canvas_text.setVisibility(View.VISIBLE);
                ClipArtText.canvas_text.setText(canvas_text);
                Log.i("canvastext", canvas_text);
            }
        }
    }
}
