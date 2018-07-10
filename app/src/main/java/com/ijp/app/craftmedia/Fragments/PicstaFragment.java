package com.ijp.app.craftmedia.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ijp.app.craftmedia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicstaFragment extends Fragment {


    public PicstaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picsta, container, false);
    }

}
