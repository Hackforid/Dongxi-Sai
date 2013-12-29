package com.smilehacker.dongxi.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.model.event.CategoryEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by kleist on 13-12-28.
 */
public class MenuFragment extends ListFragment{

    private SparseArray<String> mCategoryArray;
    private LayoutInflater mLayoutInflater;
    private MenuAdapter mAdapter;
    private EventBus mEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = EventBus.getDefault();
        readCategoryArray();
        mAdapter = new MenuAdapter();
        setListAdapter(mAdapter);
    }

    private void readCategoryArray() {
        mCategoryArray = new SparseArray<String>();
        String[] titles = getResources().getStringArray(R.array.category_array);
        int[] ids = getResources().getIntArray(R.array.category_id_array);

        for (int i = 0; i < titles.length; i++) {
            mCategoryArray.append(ids[i], titles[i]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_menu, container, false);
        mLayoutInflater = inflater;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCategoryArray.size();
        }

        @Override
        public Object getItem(int i) {
            return mCategoryArray.keyAt(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_menu_list, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.tv_category_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(mCategoryArray.valueAt(position));
            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mEventBus.post(new CategoryEvent(mCategoryArray.keyAt(position), mCategoryArray.valueAt(position)));
    }

    private static class ViewHolder {
        public TextView title;
    }
}
