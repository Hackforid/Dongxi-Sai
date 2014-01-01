package com.smilehacker.dongxi.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.fragment.DetailFragment;
import com.smilehacker.dongxi.model.Dongxi;


/**
 * Created by kleist on 13-12-29.
 */
public class DetailActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);

        Dongxi dongxi = getIntent().getParcelableExtra(Constants.KEY_DONGXI);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_DONGXI, dongxi);
        fragment.setArguments(bundle);
        ft.replace(R.id.content, fragment);
        ft.commit();
    }
}
