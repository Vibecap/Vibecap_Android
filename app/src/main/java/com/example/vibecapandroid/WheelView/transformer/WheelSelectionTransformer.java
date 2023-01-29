package com.example.vibecapandroid.WheelView.transformer;

import android.graphics.drawable.Drawable;

import com.example.vibecapandroid.WheelView.WheelView;


public interface WheelSelectionTransformer {
    void transform(Drawable drawable, WheelView.ItemState itemState);
}
