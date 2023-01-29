package com.example.vibecapandroid.WheelView.transformer;

import android.graphics.Rect;

import com.example.vibecapandroid.WheelView.Circle;
import com.example.vibecapandroid.WheelView.WheelView;


public class SimpleItemTransformer implements WheelItemTransformer {
    @Override
    public void transform(WheelView.ItemState itemState, Rect itemBounds) {
        Circle bounds = itemState.getBounds();
        float radius = bounds.getRadius();
        float x = bounds.getCenterX();
        float y = bounds.getCenterY();
        itemBounds.set(Math.round(x - radius), Math.round(y - radius), Math.round(x + radius), Math.round(y + radius));
    }
}
