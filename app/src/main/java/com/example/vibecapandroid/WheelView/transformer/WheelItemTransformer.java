package com.example.vibecapandroid.WheelView.transformer;

import android.graphics.Rect;

import com.example.vibecapandroid.WheelView.WheelView;


public interface WheelItemTransformer {
    /**
     * You have control over the Items draw bounds. By supplying your own WheelItemTransformer
     * you must call set bounds on the itemBounds.
     */
    void transform(WheelView.ItemState itemState, Rect itemBounds);
}
