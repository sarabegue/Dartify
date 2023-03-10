package com.mentit.dartify.HelperClasses;

import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.view.MotionEvent;
import android.util.AttributeSet;

public class CustomViewPager   extends ViewPager {
    private boolean enabled;
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
