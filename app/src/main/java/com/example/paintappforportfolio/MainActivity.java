package com.example.paintappforportfolio;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.paintappforportfolio.databinding.ActivityMainBinding;
import com.google.android.material.slider.RangeSlider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CustomViewPaint customViewPaint;
    ActivityMainBinding binding;
    int widthInPx, heightInPx, pickedColorByUser;
    public static final String TAG = "Pinkessh";
    ScaleGestureDetector scaleGestureDetector;
    public float mScaleFactor = 1.0f;
    public String shape = "STROKE";
    public int forBugInEaragerColor=0;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 101;
    public static final int STORAGE_PERMISSION_REQUEST_CODEE = 1011;
    private int storagePermissionInteger=0;

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        onlyForZoom();


        binding.linearforstrock.setVisibility(View.GONE);
        binding.linearForEarager.setVisibility(View.GONE);

        widthInPx = getIntent().getIntExtra("width", Resources.getSystem().getDisplayMetrics().widthPixels) * 2;
        heightInPx = getIntent().getIntExtra("height", Resources.getSystem().getDisplayMetrics().heightPixels) * 2;
        pickedColorByUser = getIntent().getIntExtra("pickedColor", Color.WHITE);


        if (widthInPx == 0 && heightInPx == 0) {
            getWidthAndHeightForScreenSelectionOption();
        }

        customViewPaint = findViewById(R.id.draw_view);

        binding.undoIb.setOnClickListener(v -> {
            binding.linearforstrock.setVisibility(View.GONE);
            binding.linearForEarager.setVisibility(View.GONE);
            customViewPaint.undo();
        });

        binding.saveIb.setOnClickListener(v -> {
            binding.linearforstrock.setVisibility(View.GONE);
            binding.linearForEarager.setVisibility(View.GONE);

            try {
                saveMethod();
            }catch (Exception e){
                e.printStackTrace();
            }

//            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R) {
//
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
//                    showingToast();
//                }
//
//            }
//            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.R){
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODEE);
//                    showingToast();
//                }
//            }

        });

        binding.colorIb.setOnClickListener(v -> {
            binding.linearforstrock.setVisibility(View.GONE);
            binding.linearForEarager.setVisibility(View.GONE);
            new ColorPickerDialog.Builder(this)
                    .setTitle("ColorPicker Dialog")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton("Confirm",
                            new ColorEnvelopeListener() {
                                @Override
                                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                    customViewPaint.setCurrentColor(envelope.getColor());
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                    .attachAlphaSlideBar(true) // the default value is true.
                    .attachBrightnessSlideBar(true)  // the default value is true.
                    .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                    .show();
        });

        binding.strokeIb.setOnClickListener(v ->  {
            binding.linearForEarager.setVisibility(View.GONE);
            if (binding.linearforstrock.getVisibility() == View.VISIBLE) {
                binding.linearforstrock.setVisibility(View.GONE);
            }else
                binding.linearforstrock.setVisibility(View.VISIBLE);

            customViewPaint.setCurrentColor(Color.GREEN);
            customViewPaint.setSHAPE("STROKE");
            customViewPaint.init(widthInPx,heightInPx,customViewPaint.getShape());
            if (forBugInEaragerColor == 1){
                customViewPaint.setCurrentColor(Color.GREEN);
                forBugInEaragerColor = 0;
            }
            customViewPaint.invalidate();
        });

        binding.rangeBarForStroke.setValueFrom(0.0f);
        binding.rangeBarForStroke.setValueTo(100.0f);
        binding.rangeBarForStroke.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                customViewPaint.setStrokeWidth((int) value);
            }
        });

        ViewTreeObserver viewTreeObserver = customViewPaint.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                customViewPaint.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                customViewPaint.init(widthInPx, heightInPx, shape);
                customViewPaint.setLayoutParams(new LinearLayout.LayoutParams(widthInPx, heightInPx));

                customViewPaint.setColorForCanvas(pickedColorByUser);

            }
        });

        binding.redoIb.setOnClickListener(v -> {
            binding.linearforstrock.setVisibility(View.GONE);
            binding.linearForEarager.setVisibility(View.GONE);
            customViewPaint.redo();
        });

        binding.eregarIb.setOnClickListener(v -> {
            binding.linearforstrock.setVisibility(View.GONE);
            if (binding.linearForEarager.getVisibility() == View.VISIBLE){
                binding.linearForEarager.setVisibility(View.GONE);
            }else
                binding.linearForEarager.setVisibility(View.VISIBLE);

            customViewPaint.setCurrentColor(Color.WHITE);
            customViewPaint.setSHAPE("EARASER");
            forBugInEaragerColor = 1;
            customViewPaint.invalidate();
        });

        binding.rangeBarForErager.setValueFrom(0.0f);
        binding.rangeBarForErager.setValueTo(100.0f);
        binding.rangeBarForErager.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                customViewPaint.setStrokeWidth((int) value);
            }
        });

        binding.clearAll.setOnClickListener(v -> {
            binding.linearforstrock.setVisibility(View.GONE);
            binding.linearForEarager.setVisibility(View.GONE);
            alertDialogForDeletion();
        });
        customViewPaint.setStrokeWidth(20);

        binding.drawShapes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.linearLayForShapes.getVisibility() == View.VISIBLE){
                    binding.linearLayForShapes.setVisibility(View.GONE);
                }else {
                    binding.linearLayForShapes.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.rectangleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customViewPaint.setSHAPE("RECT");
                customViewPaint.init(widthInPx,heightInPx,customViewPaint.getShape());
                binding.linearLayForShapes.setVisibility(View.GONE);

                if (forBugInEaragerColor == 1){
                    customViewPaint.setCurrentColor(Color.GREEN);
                    forBugInEaragerColor = 0;
                }
                customViewPaint.invalidate();
            }
        });
    }

    private void showingToast() {
        if (storagePermissionInteger == 0 || storagePermissionInteger ==1){
            storagePermissionInteger++;
        }
        else {
            Toast.makeText(this, "Give storage permission manually if you want to save this canvas!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMethod() {
        Bitmap bmp = customViewPaint.save();

        OutputStream imageOutStream = null;

        ContentValues cv = new ContentValues();

        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        try {
            imageOutStream = getContentResolver().openOutputStream(uri);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
            Toast.makeText(this, "Image saved at "+uri.getPath(), Toast.LENGTH_SHORT).show();
            imageOutStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onlyForZoom() {
        scaleGestureDetector = new ScaleGestureDetector(MainActivity.this,new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private void getWidthAndHeightForScreenSelectionOption() {
        LinearLayout linearLayout = findViewById(R.id.linearforCustom);
        ViewTreeObserver vtoForScreen = linearLayout.getViewTreeObserver();
        vtoForScreen.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                widthInPx = linearLayout.getMeasuredWidth();
                heightInPx = linearLayout.getMeasuredHeight();
            }
        });
    }

    private void alertDialogForDeletion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Clear canvas");
        builder.setMessage("After clearing this canvas you won't be able to restore your drawing which has been done on this canvas!");
        builder.setPositiveButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customViewPaint.clearCanvas();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private  class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));

            binding.linearforCustom.setScaleX(mScaleFactor);
            binding.linearforCustom.setScaleY(mScaleFactor);

            return true;
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                saveMethod();
//            }else {
//                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R) {
//
//                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
//                    }
//
//                }else {
//                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
//                    }
//                }
//            }
//        }
//    }
}
































