package com.smilehacker.dongxi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.activity.DetailActivity;
import com.smilehacker.dongxi.app.App;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.dongxi.view.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kleist on 14-1-4.
 */
public class UserCreatedAdapter extends BaseAdapter{

    private final static String TAG = UserCreatedAdapter.class.getName();

    private List<Dongxi> mDongxiList;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mPicHeight;
    private int mPicWidth;

    private double mRadio = 0.9;

    public UserCreatedAdapter(Context context, List<Dongxi> dongxiList) {
        mContext = context;
        mDongxiList = dongxiList;
        mInflater = LayoutInflater.from(context);

        computerPictureSize();
    }

    private void computerPictureSize() {
        App app = (App) mContext.getApplicationContext();
        mPicWidth = app.deviceInfo.screenWidth / 2;
        mPicHeight = (int) (mPicWidth * mRadio);
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
            view = mInflater.inflate(R.layout.item_created_grid, viewGroup, false);
            holder = new ViewHolder();
            holder.picture = (DynamicHeightImageView) view.findViewById(R.id.iv_dongxi);
            holder.picture.setHeightRatio(mRadio);
            holder.title = (TextView) view.findViewById(R.id.tv_dongxi_title);
            holder.card = view.findViewById(R.id.v_card);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Dongxi dongxi = mDongxiList.get(pos);
        holder.title.setText(dongxi.title);
        Picasso.with(mContext).load(dongxi.pictures.get(0).src).fit().centerCrop().into(holder.picture);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Constants.KEY_DONGXI, dongxi);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    private static class ViewHolder {
        public View card;
        public DynamicHeightImageView picture;
        public TextView title;
    }
}
