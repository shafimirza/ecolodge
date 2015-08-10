package com.example.root.myapplication_eco;
/**
 * Created by root on 8/9/15.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FullScreenViewActivity extends Activity {

    private Utils utils;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);

        utils = new Utils(getApplicationContext());// I need replace it through the asynchronous task and do it respectively with the priorities///

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                utils.getFilePaths());

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }

    public class FullScreenImageAdapter extends PagerAdapter {

        private Activity _activity;
        private ArrayList<String> _imagePaths;
        private LayoutInflater inflater;

        // constructor
        public FullScreenImageAdapter(Activity activity,
                                      ArrayList<String> imagePaths) {
            this._activity = activity;
            this._imagePaths = imagePaths;
        }

        @Override
        public int getCount() {
            return this._imagePaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView imgDisplay;
            Button btnClose;

            inflater = (LayoutInflater) _activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                    false);

            imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
            btnClose = (Button) viewLayout.findViewById(R.id.btnClose);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
            imgDisplay.setImageBitmap(bitmap);

            // close button click event
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.finish();
                }
            });

            ((ViewPager) container).addView(viewLayout);

            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }
    }

    public class Utils {

        private Context _context;

        // constructor
        public Utils(Context context) {
            this._context = context;
        }

        /*
         * Reading file paths from SDCard
         */
        public ArrayList<String> getFilePaths() {
            ArrayList<String> filePaths = new ArrayList<String>();

            File directory = new File(
                    android.os.Environment.getExternalStorageDirectory()
                            + File.separator + AppConstant.PHOTO_ALBUM);

            // check for directory
            if (directory.isDirectory()) {
                // getting list of file paths
                File[] listFiles = directory.listFiles();

                // Check for count
                if (listFiles.length > 0) {

                    // loop through all files
                    for (int i = 0; i < listFiles.length; i++) {

                        // get file path
                        String filePath = listFiles[i].getAbsolutePath();

                        // check for supported file extension
                        if (IsSupportedFile(filePath)) {
                            // Add image path to array list
                            filePaths.add(filePath);
                        }
                    }
                } else {
                    // image directory is empty
                    Toast.makeText(
                            _context,
                            AppConstant.PHOTO_ALBUM
                                    + " is empty. Please load some images in it !",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Error!");
                alert.setMessage(AppConstant.PHOTO_ALBUM
                        + " directory path is not valid! Please set the image directory name AppConstant.java class");
                alert.setPositiveButton("OK", null);
                alert.show();
            }

            return filePaths;
        }


        /*
         * Check supported file extensions
         *
         * @returns boolean
         */
        private boolean IsSupportedFile(String filePath) {
            String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                    filePath.length());

            if (AppConstant.FILE_EXTN
                    .contains(ext.toLowerCase(Locale.getDefault())))
                return true;
            else
                return false;

        }

        /*
         * getting screen width
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        public int getScreenWidth() {
            int columnWidth;
            WindowManager wm = (WindowManager) _context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            final Point point = new Point();
            try {
                display.getSize(point);
            } catch (java.lang.NoSuchMethodError ignore) { // Older device
                point.x = display.getWidth();
                point.y = display.getHeight();
            }
            columnWidth = point.x;
            return columnWidth;
        }

    }

public static class AppConstant {

    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 3;

    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp

    // SD card image directory
    public static final String PHOTO_ALBUM = "androidhive";

    // supported file formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
            "png");
}
    public class TouchImageView extends ImageView {

        Matrix matrix;

        // We can be in one of these 3 states
        static final int NONE = 0;
        static final int DRAG = 1;
        static final int ZOOM = 2;
        int mode = NONE;

        // Remember some things for zooming
        PointF last = new PointF();
        PointF start = new PointF();
        float minScale = 1f;
        float maxScale = 3f;
        float[] m;

        int viewWidth, viewHeight;
        static final int CLICK = 3;
        float saveScale = 1f;
        protected float origWidth, origHeight;
        int oldMeasuredWidth, oldMeasuredHeight;

        ScaleGestureDetector mScaleDetector;

        Context context;

        public TouchImageView(Context context) {
            super(context);
            sharedConstructing(context);
        }

        public TouchImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
            sharedConstructing(context);
        }

        private void sharedConstructing(Context context) {
            super.setClickable(true);
            this.context = context;
            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
            matrix = new Matrix();
            m = new float[9];
            setImageMatrix(matrix);
            setScaleType(ScaleType.MATRIX);

            setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mScaleDetector.onTouchEvent(event);
                    PointF curr = new PointF(event.getX(), event.getY());

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            last.set(curr);
                            start.set(last);
                            mode = DRAG;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            if (mode == DRAG) {
                                float deltaX = curr.x - last.x;
                                float deltaY = curr.y - last.y;
                                float fixTransX = getFixDragTrans(deltaX, viewWidth,
                                        origWidth * saveScale);
                                float fixTransY = getFixDragTrans(deltaY, viewHeight,
                                        origHeight * saveScale);
                                matrix.postTranslate(fixTransX, fixTransY);
                                fixTrans();
                                last.set(curr.x, curr.y);
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            mode = NONE;
                            int xDiff = (int) Math.abs(curr.x - start.x);
                            int yDiff = (int) Math.abs(curr.y - start.y);
                            if (xDiff < CLICK && yDiff < CLICK)
                                performClick();
                            break;

                        case MotionEvent.ACTION_POINTER_UP:
                            mode = NONE;
                            break;
                    }

                    setImageMatrix(matrix);
                    invalidate();
                    return true; // indicate event was handled
                }

            });
        }

        public void setMaxZoom(float x) {
            maxScale = x;
        }

        private class ScaleListener extends
                ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                mode = ZOOM;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float mScaleFactor = detector.getScaleFactor();
                float origScale = saveScale;
                saveScale *= mScaleFactor;
                if (saveScale > maxScale) {
                    saveScale = maxScale;
                    mScaleFactor = maxScale / origScale;
                } else if (saveScale < minScale) {
                    saveScale = minScale;
                    mScaleFactor = minScale / origScale;
                }

                if (origWidth * saveScale <= viewWidth
                        || origHeight * saveScale <= viewHeight)
                    matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2,
                            viewHeight / 2);
                else
                    matrix.postScale(mScaleFactor, mScaleFactor,
                            detector.getFocusX(), detector.getFocusY());

                fixTrans();
                return true;
            }
        }

        void fixTrans() {
            matrix.getValues(m);
            float transX = m[Matrix.MTRANS_X];
            float transY = m[Matrix.MTRANS_Y];

            float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
            float fixTransY = getFixTrans(transY, viewHeight, origHeight
                    * saveScale);

            if (fixTransX != 0 || fixTransY != 0)
                matrix.postTranslate(fixTransX, fixTransY);
        }

        float getFixTrans(float trans, float viewSize, float contentSize) {
            float minTrans, maxTrans;

            if (contentSize <= viewSize) {
                minTrans = 0;
                maxTrans = viewSize - contentSize;
            } else {
                minTrans = viewSize - contentSize;
                maxTrans = 0;
            }

            if (trans < minTrans)
                return -trans + minTrans;
            if (trans > maxTrans)
                return -trans + maxTrans;
            return 0;
        }

        float getFixDragTrans(float delta, float viewSize, float contentSize) {
            if (contentSize <= viewSize) {
                return 0;
            }
            return delta;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);

            //
            // Rescales image on rotation
            //
            if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                    || viewWidth == 0 || viewHeight == 0)
                return;
            oldMeasuredHeight = viewHeight;
            oldMeasuredWidth = viewWidth;

            if (saveScale == 1) {
                // Fit to screen.
                float scale;

                Drawable drawable = getDrawable();
                if (drawable == null || drawable.getIntrinsicWidth() == 0
                        || drawable.getIntrinsicHeight() == 0)
                    return;
                int bmWidth = drawable.getIntrinsicWidth();
                int bmHeight = drawable.getIntrinsicHeight();

                Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

                float scaleX = (float) viewWidth / (float) bmWidth;
                float scaleY = (float) viewHeight / (float) bmHeight;
                scale = Math.min(scaleX, scaleY);
                matrix.setScale(scale, scale);

                // Center the image
                float redundantYSpace = (float) viewHeight
                        - (scale * (float) bmHeight);
                float redundantXSpace = (float) viewWidth
                        - (scale * (float) bmWidth);
                redundantYSpace /= (float) 2;
                redundantXSpace /= (float) 2;

                matrix.postTranslate(redundantXSpace, redundantYSpace);

                origWidth = viewWidth - 2 * redundantXSpace;
                origHeight = viewHeight - 2 * redundantYSpace;
                setImageMatrix(matrix);
            }
            fixTrans();
        }
    }
}

