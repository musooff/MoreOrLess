package com.footballnukes.moreorlessfootballers.beautifiers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by moshe on 18/04/2017.
 */

public class FontTextViewTest extends TextView {
    public FontTextViewTest(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        m4652a(context, attributeSet);
    }

    public FontTextViewTest(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        m4652a(context, attributeSet);
    }

    private void m4652a(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, new int[]{2130771971, 2130772183}, 0, 0);
        if (Build.VERSION.SDK_INT >= 21) {
            setFontFeatureSettings("lnum");
        }
        int i = 1;
        try {
            Object obj = obtainStyledAttributes.getBoolean(getTypeface().getStyle(),false);
            i = obtainStyledAttributes.getInt(0, 0);
            setTypeface(m4651a(context, i));
            if (getResources().getDisplayMetrics().density >= 2.0f && obj != null) {
                setLineSpacing(0.0f, 1.4f);
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private Typeface m4651a(Context context, int i) {
        switch (i) {
            case 1 :
                return Typeface.createFromAsset(context.getAssets(), "fonts/baloo.ttf");
            default:
                return Typeface.createFromAsset(context.getAssets(), "fonts/baloo.ttf");
        }
    }
}
