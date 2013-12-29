package com.smilehacker.dongxi.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.adapter.DongxiListAdapter;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.dongxi.model.event.CategoryEvent;
import com.smilehacker.dongxi.network.SimpleVolleyTask;
import com.smilehacker.dongxi.network.task.DongxiTask;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by kleist on 13-12-24.
 */
public class HomeFragment extends Fragment implements OnRefreshListener {

    private final String TAG = HomeFragment.class.getName();

    private ListView mLvDongxi;
    private PullToRefreshLayout mPtrLayout;
    private View mVLoadMore;
    private ProgressBar mPbLoading;
    private View mActionBarContainer;

    private Drawable mActionBarBackgroundDrawble;

    private DongxiTask mDongxiTask;
    private DongxiListAdapter mAdapter;
    private List<Dongxi> mDongxiList;

    private EventBus mEventBus;

    private int mCategoryId;
    private String mUntilId;
    private enum LoadingStatus {
        refresh, loadingMore, stop
    }
    private LoadingStatus mLoadingStatus = LoadingStatus.stop;
    private int mActionBarHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDongxiList = new ArrayList<Dongxi>();
        mAdapter = new DongxiListAdapter(getActivity(), mDongxiList);
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

        mActionBarBackgroundDrawble = getResources().getDrawable(R.drawable.red_actionbar_bg);
        getActivity().getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawble);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDongxiTask != null) {
            mDongxiTask.cancel();
        }
        mEventBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_home, container, false);
        mLvDongxi = (ListView) view.findViewById(R.id.v_list);
        mPtrLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_layout);
        mVLoadMore = inflater.inflate(R.layout.footer_dongxi_list, null);
        mPbLoading = (ProgressBar) mVLoadMore.findViewById(R.id.pb_loading);
        mActionBarContainer = getActivity().findViewById(getResources().getIdentifier("action_bar_container", "id", "android"));

        mLvDongxi.setAdapter(mAdapter);
        mLvDongxi.addFooterView(mVLoadMore);
        addListViewHeader();
        mLvDongxi.setOnScrollListener(new DongxiListOnScrollListener());

        if (mActionBarContainer != null) {
            mActionBarContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    load(mCategoryId, null, LoadingStatus.refresh);
                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(mPtrLayout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        load(Constants.DONGXI_ALL, null, LoadingStatus.refresh);
    }

    public void onEvent(CategoryEvent event) {
        getActivity().setTitle(event.title);
        mCategoryId = event.id;
        load(event.id, null, LoadingStatus.refresh);
    }

    private void addListViewHeader() {
        View header = new View(getActivity());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, getActionHeight());
        header.setLayoutParams(lp);
        mLvDongxi.addHeaderView(header);
    }

    private int getActionHeight() {
        mActionBarHeight = getActivity().getActionBar().getHeight();
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            mActionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return mActionBarHeight;
    }

    private void load(int tag, String untilId, LoadingStatus status) {
        mLoadingStatus = status;
        if (mDongxiTask == null) {
            mDongxiTask = new DongxiTask(getActivity(), new SimpleVolleyTask.VolleyTaskCallBack<List<Dongxi>>() {
                @Override
                public void onStart() {
                    if (mLoadingStatus == LoadingStatus.refresh) {
                        startRefresh();
                        mLvDongxi.smoothScrollToPosition(0);
                        mPbLoading.setVisibility(View.GONE);
                    } else if (mLoadingStatus == LoadingStatus.loadingMore) {
                        stopRefresh();
                        mPbLoading.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onSuccess(List<Dongxi> result) {
                    stopRefresh();
                    mPbLoading.setVisibility(View.GONE);

                    if (mLoadingStatus == LoadingStatus.loadingMore) {
                        mDongxiList.addAll(result);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mDongxiList.clear();
                        mDongxiList.addAll(result);
                        mAdapter.notifyDataSetChanged();
                    }

                    mLoadingStatus = LoadingStatus.stop;
                }

                @Override
                public void onFail(Throwable e) {
                    mLoadingStatus = LoadingStatus.stop;
                    mPbLoading.setVisibility(View.GONE);
                    stopRefresh();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            mDongxiTask.cancel();
        }
        mDongxiTask.setParams(tag, untilId);
        mDongxiTask.execute();
    }

    @Override
    public void onRefreshStarted(View view) {
        if (mLoadingStatus != LoadingStatus.refresh) {
            load(mCategoryId, null, LoadingStatus.refresh);
        }
    }

    private void stopRefresh() {
        if (mPtrLayout.isRefreshing()) {
            mPtrLayout.setRefreshing(false);
        }
    }

    private void startRefresh() {
        if (!mPtrLayout.isRefreshing()) {
            mPtrLayout.setRefreshing(true);
        }
    }

    private class DongxiListOnScrollListener implements AbsListView.OnScrollListener {
        private int visibleLastIndex = 0;

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            int itemLastIndex = mAdapter.getCount() + 1;
            if (scrollState == SCROLL_STATE_IDLE && this.visibleLastIndex == itemLastIndex && mLoadingStatus != LoadingStatus.loadingMore) {
                load(mCategoryId, mDongxiList.get(mDongxiList.size() - 1).fid, LoadingStatus.loadingMore);
            }

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.visibleLastIndex = firstVisibleItem + visibleItemCount - 1;

            /**
             * 通过header滚动的距离来设置actionbar背景的alpha
             */
            if (firstVisibleItem == 0) {
                View firstView = absListView.getChildAt(0);
                if (firstView != null) {
                    double ratio =  firstView.getY() / mActionBarHeight + 1;
                    ratio = ratio < 0.3 ? 0.3 : ratio;
                    int alpha = (int) (ratio * 255);
                    mActionBarBackgroundDrawble.setAlpha(alpha);
                }
            }
              
        }

    }

}
