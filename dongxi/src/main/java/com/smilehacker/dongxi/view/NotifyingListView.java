package com.smilehacker.dongxi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by kleist on 13-12-29.
 */
public class NotifyingListView extends ListView {

    public interface OnScrollChangedListener {
        void onScrollChanged(ListView who, int l, int t, int oldl, int oldt);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public NotifyingListView(Context context) {
        super(context);
    }

    public NotifyingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
}
