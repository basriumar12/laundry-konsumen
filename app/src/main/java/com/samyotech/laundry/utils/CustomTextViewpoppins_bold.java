package com.samyotech.laundry.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Sandeep on 12-04-2016.
 */
public class CustomTextViewpoppins_bold extends AppCompatTextView {

    public CustomTextViewpoppins_bold(Context context) {

        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewpoppins_bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewpoppins_bold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

//    public CustomTextViewpoppins_bold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        applyCustomFont(context);
//    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("segoe_ui.ttf", context);
        setTypeface(customFont, Typeface.BOLD);
    }
}
