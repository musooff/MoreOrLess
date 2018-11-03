package com.footballnukes.moreorlessfootballers.beautifiers;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;

import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.footballnukes.moreorlessfootballers.R;

public class GameButton extends Button {
    int randomNumber;

    public GameButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupButton(attributeSet);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/baloo.ttf");
        this.setTypeface(typeface);
    }

    public GameButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setupButton(attributeSet);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/baloo.ttf");
        this.setTypeface(typeface);
    }

    protected void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        if (z) {
            whenClicked();
            animateClick(0.9f, 1.0f, 0.9f, 1.0f);
            return;
        }
        whenReleased();
        animateClick(1.0f, 0.9f, 1.0f, 0.9f);
    }

    private void whenReleased() {
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background));
        try {
            setTextColor(ContextCompat.getColor(getContext(), this.randomNumber));
        } catch (NotFoundException e) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.cardview_dark_background));
        }
    }

    private void whenClicked() {
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_touch));
        setTextColor(ContextCompat.getColor(getContext(), R.color.cardview_dark_background));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return super.onTouchEvent(motionEvent);
        }
        switch (motionEvent.getAction()) {
            case 0:
                whenClicked();
                animateClick(1.0f, 0.9f, 1.0f, 0.9f);
                break;
            case 1 :
            case 3 :
            case 10 :
                whenReleased();
                animateClick(0.9f, 1.0f, 0.9f, 1.0f);
                break;
        }
        invalidate();
        return super.onTouchEvent(motionEvent);
    }

    private void setupButton(AttributeSet attributeSet) {
        if (Build.MODEL.contains("AFT")) {
            animateClick(1.0f, 0.9f, 1.0f, 0.9f);
        }
        this.randomNumber = attributeSet.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColor", 2131427399);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background));
        setTransformationMethod(null);
    }

    public void animateClick(float f, float f2, float f3, float f4) {
        Animation scaleAnimation = new ScaleAnimation(f, f2, f3, f4, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(50);
        startAnimation(scaleAnimation);
    }
}
