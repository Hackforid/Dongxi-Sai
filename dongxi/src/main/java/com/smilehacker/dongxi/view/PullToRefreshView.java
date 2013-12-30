package com.smilehacker.dongxi.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smilehacker.dongxi.R;

import java.util.zip.Inflater;

/**
 * Created by kleist on 13-12-30.
 */
public class PullToRefreshView extends LinearLayout implements View.OnTouchListener {

    private final static String TAG = PullToRefreshView.class.getName();

    public static final int STATUS_PULL_TO_REFRESH = 0;
    public static final int STATUS_RELEASE_TO_REFRESH = 1;
    public static final int STATUS_REFRESHING = 2;
    public static final int STATUS_REFRESH_FINISHED = 3;
    public static final int SCROLL_SPEED = -20;

    private View mHeader;
    private ListView mListView;
    private TextView mTvMsg;

    private MarginLayoutParams mHeaderLayoutParams;
    private int mHideHeaderHeight;
    private PullToRefreshListener mListener;

    private int mCurrentStatus = STATUS_REFRESH_FINISHED;
    private int mLastStatus = STATUS_REFRESH_FINISHED;

    private float mTouchY;
    private int mTouchSlop;
    private Boolean mHasLoad = false;
    private Boolean mAbleToPull;

    private Context mContext;

    public PullToRefreshView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setOnRefreshListener(PullToRefreshListener listener) {
        mListener = listener;
    }

    private void init(Context context) {
        mHeader = LayoutInflater.from(context).inflate(R.layout.pull_to_fresh, null);
        mTvMsg = (TextView) mHeader.findViewById(R.id.tv_msg);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOrientation(VERTICAL);
        addView(mHeader, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !mHasLoad) {
            mHideHeaderHeight = -mHeader.getHeight();
            mHeaderLayoutParams = (MarginLayoutParams) mHeader.getLayoutParams();
            mHeaderLayoutParams.topMargin = mHideHeaderHeight;
            mListView = (ListView) getChildAt(1);
            mListView.setOnTouchListener(this);
            mHasLoad = true;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        setIsAbleToPull(motionEvent);
        if (mAbleToPull) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchY = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveY = motionEvent.getRawY();
                    int distance = (int) (moveY - mTouchY);
                    if (distance <= 0 && mHeaderLayoutParams.topMargin <= mHideHeaderHeight) {
                        return false;
                    }
                    if (distance < mTouchSlop) {
                        return false;
                    }
                    if (mCurrentStatus != STATUS_REFRESHING) {
                        if (mHeaderLayoutParams.topMargin > 0) {
                            mCurrentStatus = STATUS_RELEASE_TO_REFRESH;
                        } else {
                            mCurrentStatus = STATUS_PULL_TO_REFRESH;
                        }
                        mHeaderLayoutParams.topMargin = (distance / 2) + mHideHeaderHeight;
                        mHeader.setLayoutParams(mHeaderLayoutParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (mCurrentStatus == STATUS_RELEASE_TO_REFRESH) {
                        new RefreshingTask().execute();
                    } else if (mCurrentStatus == STATUS_PULL_TO_REFRESH) {
                        new HideHeaderTask().execute();
                    }
                    break;
            }
            if (mCurrentStatus == STATUS_PULL_TO_REFRESH || mCurrentStatus == STATUS_RELEASE_TO_REFRESH) {
                updateHeaderView();
                mListView.setPressed(false);
                mListView.setFocusable(false);
                mListView.setFocusableInTouchMode(false);
                mLastStatus = mCurrentStatus;
                return true;
            }
        }
        return false;
    }

    private void setIsAbleToPull(MotionEvent event) {
        View firstChild = mListView.getChildAt(0);
        if (firstChild != null) {
            int firstVisiblePos = mListView.getFirstVisiblePosition();
            if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
                if (!mAbleToPull) {
                    mTouchY = event.getRawY();
                }
                mAbleToPull = true;
            } else {
                if (mHeaderLayoutParams.topMargin != mHideHeaderHeight) {
                    mHeaderLayoutParams.topMargin = mHideHeaderHeight;
                    mHeader.setLayoutParams(mHeaderLayoutParams);
                }
                mAbleToPull = false;
            }
        } else {
            mAbleToPull = false;
        }

    }

    private void updateHeaderView() {
        if (mLastStatus != mCurrentStatus) {
            if (mCurrentStatus == STATUS_PULL_TO_REFRESH) {
            } else if (mCurrentStatus == STATUS_RELEASE_TO_REFRESH) {
            }
            switch (mCurrentStatus) {
                case STATUS_RELEASE_TO_REFRESH:
                    mTvMsg.setText(R.string.pull_to_refresh);
                    break;
                case STATUS_PULL_TO_REFRESH:
                    mTvMsg.setText(R.string.release_to_refresh);
                    break;
                case STATUS_REFRESHING:
                    mTvMsg.setText(R.string.refreshing);
                    break;
                default:
            }
        }
    }

    public void finishRefreshing() {
        mCurrentStatus = STATUS_REFRESH_FINISHED;
        new HideHeaderTask().execute();
    }

    private class RefreshingTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int topMargin = mHeaderLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= 0) {
                    topMargin = 0;
                    break;
                }
                sleep(10);
                publishProgress(topMargin);
            }
            mCurrentStatus = STATUS_REFRESHING;
            publishProgress(0);
            if (mListener != null) {
                mListener.onRefresh();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            updateHeaderView();
            mHeaderLayoutParams.topMargin = values[0];
            mHeader.setLayoutParams(mHeaderLayoutParams);
        }
    }

    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int topMargin = mHeaderLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= mHideHeaderHeight) {
                    topMargin = mHideHeaderHeight;
                    break;
                }
                publishProgress(topMargin);
                sleep(10);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            mHeaderLayoutParams.topMargin = topMargin[0];
            mHeader.setLayoutParams(mHeaderLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer topMargin) {
            mHeaderLayoutParams.topMargin = topMargin;
            mHeader.setLayoutParams(mHeaderLayoutParams);
            mCurrentStatus = STATUS_REFRESH_FINISHED;
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface PullToRefreshListener {
        void onRefresh();
    }
}
