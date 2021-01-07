package com.midtrans.demo;

import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.core.app.FragmentPagerAdapter;

/**
 * Created by rakawm on 3/15/17.
 */

public class DemoImageAdapter extends FragmentPagerAdapter {

    public DemoImageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
