package com.smilehacker.dongxi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.smilehacker.dongxi.R;

/**
 * Created by kleist on 14-1-1.
 */
public class DetailScrollView extends ScrollView {

    /**
     * scrollview上方透明区域的高度
     */
    private float mHeaderHeight;
    private int mScrollY;

    public DetailScrollView(Context context) {
        super(context);
        init();
    }

    public DetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetailScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mHeaderHeight = getResources().getDimension(R.dimen.detail_scroll_header);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float touchY = ev.getY() + mScrollY;
        return touchY <= mHeaderHeight ? false : super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollY = t;
    }
}
