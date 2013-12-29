package com.smilehacker.dongxi.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
public class HomeFragment extends Fragment implements OnRefreshListener{

    private final String TAG = HomeFragment.class.getName();

    private ListView mLvDongxi;
    private PullToRefreshLayout mPtrLayout;
    private View mVLoadMore;
    private ProgressBar mPbLoading;

    private DongxiTask mDongxiTask;
    private DongxiListAdapter mAdapter;
    private List<Dongxi> mDongxiList;

    private EventBus mEventBus;

    private int mCategoryId;
    private String mUntilId;
    private Boolean mIsLoadingMore = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDongxiList = new ArrayList<Dongxi>();
        mAdapter = new DongxiListAdapter(getActivity(), mDongxiList);
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);
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
        mLvDongxi.setAdapter(mAdapter);
        mLvDongxi.addFooterView(mVLoadMore);
        mLvDongxi.setOnScrollListener(new DongxiListOnScrollListener());
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
        load(Constants.DONGXI_ALL, null);
    }

    public void onEvent(CategoryEvent event) {
        getActivity().setTitle(event.title);
        mCategoryId = event.id;
        load(event.id, null);
    }

    private void load(int tag, String untilId) {
        if (mDongxiTask == null) {
            mDongxiTask = new DongxiTask(getActivity(), new SimpleVolleyTask.VolleyTaskCallBack<List<Dongxi>>() {
                @Override
                public void onSuccess(List<Dongxi> result) {
                    stopRefresh();
                    if (mIsLoadingMore) {
                        mDongxiList.addAll(result);
                        mAdapter.notifyDataSetChanged();
                        mPbLoading.setVisibility(View.GONE);
                        mIsLoadingMore = false;
                    } else {
                        mLvDongxi.smoothScrollToPosition(0);
                        mDongxiList.clear();
                        mDongxiList.addAll(result);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    Log.d(TAG, "Error:" + e.getMessage());
                    if (mIsLoadingMore) {
                        mPbLoading.setVisibility(View.GONE);
                        mIsLoadingMore = false;
                    } else {
                        stopRefresh();
                    }
                }

                @Override
                public void onStart() {
                    if (!mIsLoadingMore) {
                        startRefresh();
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
        load(mCategoryId, null);
    }

    private void stopRefresh() {
        mPtrLayout.setRefreshing(false);
    }

    private void startRefresh() {
        mPtrLayout.setRefreshing(true);
    }

    private class DongxiListOnScrollListener implements AbsListView.OnScrollListener {
        private int visibleLastIndex = 0;

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            int itemLastIndex = mAdapter.getCount();
            if (scrollState == SCROLL_STATE_IDLE && this.visibleLastIndex == itemLastIndex && !mIsLoadingMore) {
                mIsLoadingMore = true;
                mPbLoading.setVisibility(View.VISIBLE);
                load(mCategoryId, mDongxiList.get(mDongxiList.size() - 1).fid);
            }

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }

    }
}
