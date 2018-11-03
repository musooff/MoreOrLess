package com.footballnukes.moreorlessfootballers.beautifiers;

import android.annotation.TargetApi;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.footballnukes.moreorlessfootballers.R;

public class VSView extends RelativeLayout {
    FrameLayout fl_vs;
    FrameLayout fl_answer;
    ImageView iv_answer;

    public VSView(Context context) {
        super(context);
        viewInflator();
    }

    public VSView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        viewInflator();
    }

    public VSView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        viewInflator();
    }

    public void viewInflator() {
        inflate(getContext(), R.layout.vs, this);
        this.fl_vs = (FrameLayout) findViewById(R.id.fv1);
        this.fl_answer = (FrameLayout) findViewById(R.id.fv2);
        this.iv_answer = (ImageView) findViewById(R.id.iv_answer);
    }

    public void correct() {
        this.fl_answer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.correct_background));
        this.iv_answer.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tick));
        animator(this.fl_vs, 1.0f, 0.0f, 1.0f, 0.0f);
    }

    public void wrong() {
        this.fl_answer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.incorrect_background));
        this.iv_answer.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.cross));
        animator(this.fl_vs, 1.0f, 0.0f, 1.0f, 0.0f);
    }

    public void bring_answer() {
        animator(this.fl_answer, 0.0f, 1.0f, 0.0f, 1.0f);
    }

    public void animator(View view, float f, float f2, float f3, float f4) {
        Animation scaleAnimation = new ScaleAnimation(f, f2, f3, f4, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(300);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(scaleAnimation);
    }

    public void bring_vs(){
        animator(this.fl_answer, 1.0f, 0.0f, 1.0f, 0.0f);
        animator(this.fl_vs,0.0f, 1.0f, 0.0f, 1.0f);
    }

}
