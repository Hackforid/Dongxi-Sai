package com.smilehacker.dongxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.model.Dongxi;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kleist on 14-1-4.
 */
public class DetailStaggeredGridViewAdapter extends BaseAdapter{

    private final static String TAG = DetailStaggeredGridViewAdapter.class.getName();

    private List<Dongxi> mDongxiList;
    private Context mContext;
    private LayoutInflater mInflater;

    public DetailStaggeredGridViewAdapter(Context context, List<Dongxi> dongxiList) {
        mContext = context;
        mDongxiList = dongxiList;
        mInflater = LayoutInflater.from(context);
    }

    public void setDongxiList(List<Dongxi> list) {
        mDongxiList.clear();
        mDongxiList.addAll(list);
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mDongxiList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDongxiList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_detail_grid, viewGroup, false);
            holder = new ViewHolder();
            holder.picture = (ImageView) view.findViewById(R.id.iv_dongxi);
            holder.title = (TextView) view.findViewById(R.id.tv_dongxi_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Dongxi dongxi = mDongxiList.get(pos);
        holder.title.setText(dongxi.title);
        Picasso.with(mContext).load(dongxi.pictures.get(0).src).into(holder.picture);

        return view;
    }

    private static class ViewHolder {
        public ImageView picture;
        public TextView title;
    }
}