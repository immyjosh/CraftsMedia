package com.ijp.app.craftmedia.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ijp.app.craftmedia.Adapter.ViewPagerAdapter;
import com.ijp.app.craftmedia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicstaFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;



    public PicstaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picsta, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {

        viewPager=view.findViewById(R.id.view_pager);

        ViewPagerAdapter adapter=new ViewPagerAdapter(getFragmentManager(),getContext());
        viewPager.setAdapter(adapter);

        tabLayout=view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
