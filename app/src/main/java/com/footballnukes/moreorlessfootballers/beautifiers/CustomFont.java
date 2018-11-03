package com.footballnukes.moreorlessfootballers.beautifiers;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by moshe on 06/05/2017.
 */

public class CustomFont extends TextView {
    public CustomFont(Context context) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/baloo.ttf"));
    }

    public CustomFont(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/baloo.ttf"));
    }

    public CustomFont(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/baloo.ttf"));
    }
}
