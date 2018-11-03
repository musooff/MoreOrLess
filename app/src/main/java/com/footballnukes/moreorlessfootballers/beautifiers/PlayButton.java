package com.footballnukes.moreorlessfootballers.beautifiers;

/**
 * Created by moshe on 18/04/2017.
 */


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PlayButton extends GameButton {
    public PlayButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PlayButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                m4658b();
                break;
            case 1 :
            case 3 :
            case 10 :
                m4657a();
                break;
        }
        invalidate();
        return super.onTouchEvent(motionEvent);
    }

    protected void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        if (z) {
            m4658b();
        } else {
            m4657a();
        }
    }

    public void m4657a() {
        //setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_drawable, 0, 0, 0);
    }

    public void m4658b() {
        //setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_getplus_drawable, 0, 0, 0);

    }
}
