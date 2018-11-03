package com.footballnukes.moreorlessfootballers.beautifiers;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by moshe on 23/05/2017.
 */

public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/baloo.ttf");
        this.setTypeface(typeface);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/baloo.ttf");
        this.setTypeface(typeface);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/baloo.ttf");
        this.setTypeface(typeface);
    }

}
