package com.smilehacker.dongxi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by kleist on 14-1-4.
 */
public class ScrollCompactGridView extends GridView {
    public ScrollCompactGridView(Context context) {
        super(context);
    }

    public ScrollCompactGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollCompactGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
