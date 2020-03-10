package project.news;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Customize ViewPager to disable swipe action that changes viewPager when has tabs
 *
 * Learn how to customize view pager and disable swipe change from Stackoverflow,
 * the answer proposed by Rajul
 * https://stackoverflow.com/a/13437997
 * @author Jing Qian
 *
 */

public class CustomViewPager extends ViewPager {

    private boolean enableSwipe;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enableSwipe = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//override the onTouchEvent  for the purpose of disable swipe tab change as it will block the swipe add and delete feature
        if (this.enableSwipe) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {//override the onInterceptTouchEvent
        if (this.enableSwipe) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    /**
     *A method to call in fragments and activities to determine whether swipe change view is allowed
     *
     * @author Jing Qian
     * @param enabled
     *
     */

    public void setSwipeToChangeEnabled(boolean enabled) {
        this.enableSwipe = enabled;
    } //a method to call, which defines whether swipe change viewpage is allowed
}
