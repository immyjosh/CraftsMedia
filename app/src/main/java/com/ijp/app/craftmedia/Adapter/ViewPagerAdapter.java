package com.ijp.app.craftmedia.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ijp.app.craftmedia.Fragments.PicstaFragment;
import com.ijp.app.craftmedia.Fragments.PicstaPageFragment.CategoryFragment;
import com.ijp.app.craftmedia.Fragments.PicstaPageFragment.RecentsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return CategoryFragment.getInstance();
        else if (position==1)
            return RecentsFragment.getInstance(context);
        else
            return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:
                return "Category";

            case 1:
                return "Recents";

        }
        return "";
    }
}
