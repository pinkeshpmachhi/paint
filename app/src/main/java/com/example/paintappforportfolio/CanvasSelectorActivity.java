package com.example.paintappforportfolio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import com.example.paintappforportfolio.databinding.ActivityCanvasSelectorBinding;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class CanvasSelectorActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCanvasSelectorBinding binding;
    int widthInPx = 400;
    int heightInPx = 400;
    int pickedColor = Color.WHITE;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCanvasSelectorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.continueBtn.setOnClickListener(v -> {

            startActivity(new Intent(CanvasSelectorActivity.this, MainActivity.class).
                    putExtra("width", widthInPx).
                    putExtra("height", heightInPx).
                    putExtra("pickedColor", pickedColor));
        });

        binding.oneJemOneIv.setOnClickListener(this);
        binding.fourJemThree.setOnClickListener(this);
        binding.threeJemTwoIv.setOnClickListener(this);
        binding.sixteenJemNineIv.setOnClickListener(this);
        binding.nineJemSixteenIv.setOnClickListener(this);
        binding.ScreenFitIv.setOnClickListener(this);
        binding.customIv.setOnClickListener(v -> {
            forCustomChoice();
        });

        binding.colorIb.setOnClickListener(v -> {
            pickBackgroundColor();
        });
    }

    private void pickBackgroundColor() {
        new ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton("Select",
                        new ColorEnvelopeListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                pickedColor = envelope.getColor();
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
    }

    private void forCustomChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CanvasSelectorActivity.this);
        builder.setTitle("Enter custom canvas size.");
        View view = LayoutInflater.from(CanvasSelectorActivity.this).
                inflate(R.layout.for_custom_size_choice_dialog, binding.mainContainerForCanvasSelector, false);
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText widthEd = view.findViewById(R.id.widthEdCustomSizeChoiceDialog);
                EditText heightEd = view.findViewById(R.id.heightEdCustomSizeChoiceDialog);

                if (widthEd.getText().toString().isEmpty()) {
                    widthEd.setError("You can't set empty width!");
                }
                else if (heightEd.getText().toString().isEmpty()) {
                    widthEd.setError("You can't set empty height!");
                }
                else if (Integer.parseInt(widthEd.getText().toString())>400) {
                    widthEd.setError("You can't set width greater than 400px!");
                }
                else if (Integer.parseInt(heightEd.getText().toString())>400) {
                    heightEd.setError("You can't set height greater than 400px!");
                }
                else if (!widthEd.getText().toString().isEmpty() && !heightEd.getText().toString().isEmpty() &&
                !(Integer.parseInt(widthEd.getText().toString())>400 || Integer.parseInt(widthEd.getText().toString()) == 0) && !(Integer.parseInt(heightEd.getText().toString())>400 || Integer.parseInt(widthEd.getText().toString()) == 0)){
                    widthInPx = Integer.parseInt(widthEd.getText().toString());
                    heightInPx = Integer.parseInt(heightEd.getText().toString());

                    binding.customIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.ripple_effect_color, null));

                    //make other white
                    binding.ScreenFitIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
                    binding.oneJemOneIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
                    binding.fourJemThree.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
                    binding.threeJemTwoIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
                    binding.sixteenJemNineIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
                    binding.nineJemSixteenIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
                    dialog.cancel();
                }

            }
        });

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.oneJemOneIv) {
            widthInPx = 400;
            heightInPx = 400;
            binding.oneJemOneIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.ripple_effect_color, null));

            //make other white
            binding.ScreenFitIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.fourJemThree.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.threeJemTwoIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.sixteenJemNineIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.nineJemSixteenIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.customIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
        }
        if (id == R.id.fourJemThree) {
            widthInPx = 400;
            heightInPx = 300;
            binding.fourJemThree.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.ripple_effect_color, null));

            //make other white
            binding.ScreenFitIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.oneJemOneIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.threeJemTwoIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.sixteenJemNineIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.nineJemSixteenIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.customIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
        }
        if (id == R.id.threeJemTwoIv) {
            widthInPx = 400;
            heightInPx = 267;
            binding.threeJemTwoIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.ripple_effect_color, null));

            //make other white
            binding.ScreenFitIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.oneJemOneIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.fourJemThree.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.sixteenJemNineIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.nineJemSixteenIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.customIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
        }
        if (id == R.id.sixteenJemNineIv) {
            widthInPx = 400;
            heightInPx = 225;
            binding.sixteenJemNineIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.ripple_effect_color, null));

            //make other white
            binding.ScreenFitIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.oneJemOneIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.fourJemThree.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.threeJemTwoIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.nineJemSixteenIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.customIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
        }
        if (id == R.id.nineJemSixteenIv) {
            widthInPx = 225;
            heightInPx = 400;
            binding.nineJemSixteenIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.ripple_effect_color, null));

            //make other white
            binding.ScreenFitIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.oneJemOneIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.fourJemThree.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.threeJemTwoIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.sixteenJemNineIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.customIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
        }
        if (id == R.id.ScreenFitIv) {
            widthInPx = 0;
            heightInPx = 0;
            binding.ScreenFitIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.ripple_effect_color, null));

            //make other white
            binding.oneJemOneIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.fourJemThree.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.threeJemTwoIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.sixteenJemNineIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.nineJemSixteenIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
            binding.customIv.setForeground(ResourcesCompat.getDrawable(getResources(),R.color.white, null));
        }
    }

    public int getPickedColor() {
        return pickedColor;
    }

    public void setPickedColor(int pickedColor) {
        this.pickedColor = pickedColor;
    }
}















