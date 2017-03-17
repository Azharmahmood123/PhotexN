package com.example.ninesole.photexpro;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClipArtText extends RelativeLayout {
    final static int FLIP_VERTICAL = 1;
    final static int FLIP_HORIZONTAL = 2;
    public static ImageButton btndel;
    public static ImageButton btnrot;
    public static ImageButton btnscl;
    public static TextView canvas_text;
    public static ImageView imgring;
    public static LinearLayout llVertical;
    public boolean angelchange = false;
    public boolean angelhback = false;
    public LayoutInflater mInflater;
    public LinearLayout ln1;
    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    int baseh;
    int basew;
    int basex;
    int basey;
    RelativeLayout clip;
    Context cntx;
    boolean freeze = false;
    int h;
    int i;
    String imageUri;
    boolean isShadow;
    int iv;
    RelativeLayout layBg;
    RelativeLayout layGroup;
    LayoutParams layoutParams;
    int margl;
    int margt;
    // DisplayImageOptions op;
    float opacity = 1.0F;
    Bitmap originalBitmap;
    int pivx;
    int pivy;
    int pos;
    Bitmap shadowBitmap;
    float startDegree;
    String[] v;
    int ii = 0;
    private List<Data> data;

    public ClipArtText(Context paramContext) {
        super(paramContext);
        cntx = paramContext;
        layGroup = this;
        // this.clip = paramRelativeLayout;
        basex = 0;
        basey = 0;
        pivx = 0;
        pivy = 0;
        // .v = paramArrayOfString
        // this.op = paramDisplayImageOptions;
        mInflater = ((LayoutInflater) paramContext.getSystemService("layout_inflater"));
        mInflater.inflate(R.layout.clipart_text, this, true);
        btndel = ((ImageButton) findViewById(R.id.del));
        btnrot = ((ImageButton) findViewById(R.id.rotate));
        btnscl = ((ImageButton) findViewById(R.id.sacle));
        imgring = ((ImageView) findViewById(R.id.image));
        ln1 = (LinearLayout) findViewById(R.id.ln1);

        // imageUri = ("assets://Cliparts/" + paramArrayOfString[paramInt1]);
        layoutParams = new LayoutParams(250, 250);
        layGroup.setLayoutParams(layoutParams);
        canvas_text = (TextView) findViewById(R.id.canvas_text);

        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        ln1 = (LinearLayout) findViewById(R.id.ln1);
        ln1.setVisibility(View.GONE);
        data = fill_with_data();


        horizontalAdapter = new HorizontalAdapter(data, getContext());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


        //   image.setImageResource(R.drawable.ic_launcher);
        // ImageLoader.getInstance().displayImage(this.imageUri, this.image,
        // paramDisplayImageOptions);
        canvas_text.setTag(Integer.valueOf(1));
        canvas_text.setOnTouchListener(new OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(ClipArtText.this.cntx,
                    new GestureDetector.SimpleOnGestureListener() {
                        public boolean onDoubleTap(MotionEvent paramAnonymous2MotionEvent) {
                            return false;
                        }
                    });

            public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                ClipArtText.this.visiball();
                if (!ClipArtText.this.freeze) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("touch", "text");
                            layGroup.invalidate();
                            gestureDetector.onTouchEvent(event);
                            ln1.setVisibility(View.GONE);
                            layGroup.bringToFront();

                            layGroup.performClick();
                            basex = ((int) (event.getRawX() - layoutParams.leftMargin));
                            basey = ((int) (event.getRawY() - layoutParams.topMargin));
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int i = (int) event.getRawX();
                            int j = (int) event.getRawY();
                            layBg = (RelativeLayout) (getParent());
                            if ((i - basex > -(layGroup.getWidth() * 2 / 3))
                                    && (i - basex < layBg.getWidth() - layGroup.getWidth() / 3)) {
                                layoutParams.leftMargin = (i - basex);
                            }
                            if ((j - basey > -(layGroup.getHeight() * 2 / 3))
                                    && (j - basey < layBg.getHeight() - layGroup.getHeight() / 3)) {
                                layoutParams.topMargin = (j - basey);
                            }
                            layoutParams.rightMargin = -9999999;
                            layoutParams.bottomMargin = -9999999;
                            layGroup.setLayoutParams(layoutParams);
                            break;

                    }

                    return true;
                }
                return true;
                // freeze;
            }
        });

        btnscl.setOnTouchListener(new OnTouchListener() {
            @SuppressLint({"NewApi"})
            public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                if (!ClipArtText.this.freeze) {
                    ln1.setVisibility(View.GONE);
                    int j = (int) event.getRawX();
                    int i = (int) event.getRawY();
                    layoutParams = (LayoutParams) layGroup.getLayoutParams();
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            ClipArtText.this.layGroup.invalidate();
                            ClipArtText.this.basex = j;
                            ClipArtText.this.basey = i;
                            ClipArtText.this.basew = ClipArtText.this.layGroup.getWidth();
                            ClipArtText.this.baseh = ClipArtText.this.layGroup.getHeight();
                            int[] loaction = new int[2];
                            layGroup.getLocationOnScreen(loaction);
                            margl = layoutParams.leftMargin;
                            margt = layoutParams.topMargin;
                            break;
                        case MotionEvent.ACTION_MOVE:

                            float f2 = (float) Math.toDegrees(Math.atan2(i - ClipArtText.this.basey, j - ClipArtText.this.basex));
                            float f1 = f2;
                            if (f2 < 0.0F) {
                                f1 = f2 + 360.0F;
                            }
                            j -= ClipArtText.this.basex;
                            int k = i - ClipArtText.this.basey;
                            i = (int) (Math.sqrt(j * j + k * k) * Math.cos(Math.toRadians(f1
                                    - ClipArtText.this.layGroup.getRotation())));
                            j = (int) (Math.sqrt(i * i + k * k) * Math.sin(Math.toRadians(f1
                                    - ClipArtText.this.layGroup.getRotation())));
                            k = i * 2 + ClipArtText.this.basew;
                            int m = j * 2 + ClipArtText.this.baseh;
                            if (k > 150) {
                                layoutParams.width = k;
                                layoutParams.leftMargin = (ClipArtText.this.margl - i);
                            }
                            if (m > 150) {
                                layoutParams.height = m;
                                layoutParams.topMargin = (ClipArtText.this.margt - j);
                            }
                            ClipArtText.this.layGroup.setLayoutParams(layoutParams);
                            ClipArtText.this.layGroup.performLongClick();
                            break;
                    }
                    return true;

                }
                return ClipArtText.this.freeze;
            }
        });

        btnrot.setOnTouchListener(new OnTouchListener() {
            @SuppressLint({"NewApi"})
            public boolean onTouch(View paramAnonymousView, MotionEvent event) {
                if (!ClipArtText.this.freeze) {
                    ln1.setVisibility(View.GONE);
                    layoutParams = (LayoutParams) ClipArtText.this.layGroup.getLayoutParams();
                    ClipArtText.this.layBg = ((RelativeLayout) ClipArtText.this.getParent());
                    int[] arrayOfInt = new int[2];
                    layBg.getLocationOnScreen(arrayOfInt);
                    int i = (int) event.getRawX() - arrayOfInt[0];
                    int j = (int) event.getRawY() - arrayOfInt[1];
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            ClipArtText.this.layGroup.invalidate();
                            ClipArtText.this.startDegree = layGroup.getRotation();
                            ClipArtText.this.pivx = (layoutParams.leftMargin + ClipArtText.this.getWidth() / 2);
                            ClipArtText.this.pivy = (layoutParams.topMargin + ClipArtText.this.getHeight() / 2);
                            ClipArtText.this.basex = (i - ClipArtText.this.pivx);
                            ClipArtText.this.basey = (ClipArtText.this.pivy - j);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            int k = ClipArtText.this.pivx;
                            int m = ClipArtText.this.pivy;
                            j = (int) (Math.toDegrees(Math.atan2(ClipArtText.this.basey, ClipArtText.this.basex)) - Math
                                    .toDegrees(Math.atan2(m - j, i - k)));
                            i = j;
                            if (j < 0) {
                                i = j + 360;
                            }
                            ClipArtText.this.layGroup.setRotation((ClipArtText.this.startDegree + i) % 360.0F);
                            break;
                    }

                    return true;
                }
                return ClipArtText.this.freeze;
            }
        });
        btndel.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ln1.setVisibility(View.VISIBLE);
                MainCanvas.sblayout.setVisibility(View.INVISIBLE);
             /*   if (!ClipArt.this.freeze) {
                    layBg = ((RelativeLayout) ClipArt.this.getParent());
                    layBg.performClick();
                    layBg.removeView(ClipArt.this.layGroup);

                }*/
            }
        });
    }

    public static Bitmap flip(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (type == FLIP_VERTICAL) {
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if (type == FLIP_HORIZONTAL) {
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    //<----------------------->
    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data(R.drawable.rotation, "Edit"));
        data.add(new Data(R.drawable.rotation, "Color"));
        data.add(new Data(R.drawable.rotation, "Font"));
        data.add(new Data(R.drawable.rotation, "Size"));
        data.add(new Data(R.drawable.rotation, "Rotate"));
        data.add(new Data(R.drawable.rotation, "Space"));
        data.add(new Data(R.drawable.rotation, "Style"));
        data.add(new Data(R.drawable.rotation, "Align"));
        data.add(new Data(R.drawable.rotation, "Delete"));


        return data;
    }

    //  <------------------------->
    public void disableAll() {
        btndel.setVisibility(View.INVISIBLE);
        btnrot.setVisibility(View.INVISIBLE);
        btnscl.setVisibility(View.INVISIBLE);
        imgring.setVisibility(View.INVISIBLE);
        ln1.setVisibility(View.INVISIBLE);

    }

    public TextView getImageView() {
        return canvas_text;
    }

    public float getOpacity() {
        return canvas_text.getAlpha();
    }

    public void resetImage() {
        this.originalBitmap = null;
        this.layGroup.performLongClick();
    }

    public void setColor(int paramInt) {
//        canvas_text.setTextColor(null);
        ColorMatrixColorFilter localColorMatrixColorFilter = new ColorMatrixColorFilter(new float[]{0.33F, 0.33F,
                0.33F, 0.0F, Color.red(paramInt), 0.33F, 0.33F, 0.33F, 0.0F, Color.green(paramInt), 0.33F, 0.33F,
                0.33F, 0.0F, Color.blue(paramInt), 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});
//        canvas_text.getDrawable().setColorFilter(localColorMatrixColorFilter);
        canvas_text.setTag(Integer.valueOf(paramInt));
        this.layGroup.performLongClick();
    }

    public void setFreeze(boolean paramBoolean) {
        this.freeze = paramBoolean;
    }

    public void setImageId() {
        canvas_text.setId(this.layGroup.getId() + this.i);
        this.i += 1;
    }

    public void setLocation() {
        this.layBg = ((RelativeLayout) getParent());
        LayoutParams localLayoutParams = (LayoutParams) this.layGroup.getLayoutParams();
        localLayoutParams.topMargin = ((int) (Math.random() * (this.layBg.getHeight() - 400)));
        localLayoutParams.leftMargin = ((int) (Math.random() * (this.layBg.getWidth() - 400)));
        this.layGroup.setLayoutParams(localLayoutParams);
    }

    public void visiball() {
        btndel.setVisibility(View.VISIBLE);
        btnrot.setVisibility(View.VISIBLE);
        btnscl.setVisibility(View.VISIBLE);
        imgring.setVisibility(View.VISIBLE);

    }

    public interface DoubleTapListener {
        void onDoubleTap();
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<Data> horizontalList = Collections.emptyList();
        ArrayList<Integer> idsList = new ArrayList<Integer>();

        Context context;


        public HorizontalAdapter(List<Data> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);
            llVertical = (LinearLayout) itemView.findViewById(R.id.ll_vertical);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            // Change items for text class
            idsList.add(R.drawable.edit);
            idsList.add(R.drawable.color);
            idsList.add(R.drawable.font);
            idsList.add(R.drawable.size);
            idsList.add(R.drawable.rotate2);
            idsList.add(R.drawable.gap);
            idsList.add(R.drawable.style);
            idsList.add(R.drawable.align);
            idsList.add(R.drawable.del);

            holder.imageView.setImageResource(idsList.get(position));
            holder.txtview.setText(horizontalList.get(position).txt);

            holder.imageView.setOnClickListener(new OnClickListener() {
                @Override

                public void onClick(View v) {
                    String list = horizontalList.get(position).txt.toString();
                    if (position == 0) {
                        disableAll();
                        ln1.setVisibility(View.GONE);
                    } else if (position == 1) {
                        Log.i("Flip", "Flip");
                        canvas_text.buildDrawingCache();
                        Bitmap bmap = canvas_text.getDrawingCache();
                        Bitmap bOutput;
                        Matrix matrix = new Matrix();
//Clicks
                        if (ii == 0) {
                            ii = 1;
                            matrix.preScale(-1.0f, 1.0f);
                        } else {
                            ii = 0;
                            matrix.preScale(1.0f, 1.0f);
                        }

                        bOutput = Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(), bmap.getHeight(), matrix, true);

//                        canvas_text.setText(bOutput);
                        canvas_text.setRotation(canvas_text.getRotation() + 0);
                    } else if (position == 2) {


                        /////
                        canvas_text.buildDrawingCache();
                        Bitmap bmap = canvas_text.getDrawingCache();
                        Bitmap bOutput;
                        Matrix matrix = new Matrix();
//Clicks
                        if (ii == 0) {
                            ii = 1;
                            matrix.preScale(1.0f, -1.0f);
                        } else {
                            ii = 0;
                            matrix.preScale(1.0f, 1.0f);
                        }

                        bOutput = Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(), bmap.getHeight(), matrix, true);


                        canvas_text.setRotation(canvas_text.getRotation() + 0);
                    } else if (position == 3) {
                        canvas_text.setRotation(canvas_text.getRotation() + 90);


                    } else if (position == 4) {
                        MainCanvas.sblayout.setVisibility(View.VISIBLE);
                        disableAll();
                        ln1.setVisibility(View.GONE);
                    } else if (position == 5) {
                        canvas_text.setVisibility(View.INVISIBLE);
                        MainCanvas.sblayout.setVisibility(View.INVISIBLE);
                        disableAll();
                        ln1.setVisibility(View.GONE);
                    }
                }

            });

        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView txtview;

            public MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imageview);
                txtview = (TextView) view.findViewById(R.id.txtview);
            }
        }
    }
}
