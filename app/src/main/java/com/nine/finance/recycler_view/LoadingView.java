package com.nine.finance.recycler_view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by benbennest on 16/8/24.
 */
public class LoadingView extends ImageView {
    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoading();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    public void startLoading() {
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
    }

    public void stopLoading() {
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.stop();
    }
}