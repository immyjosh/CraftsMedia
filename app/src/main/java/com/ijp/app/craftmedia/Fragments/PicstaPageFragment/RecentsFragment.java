package com.ijp.app.craftmedia.Fragments.PicstaPageFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ijp.app.craftmedia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentsFragment extends Fragment {

    private static RecentsFragment INSTANCE=null;

    Context context;

    public RecentsFragment() {
    }

    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context) {
        // Required empty public constructor
        this.context= this.context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

    public static RecentsFragment getInstance(Context context)
    {
        if(INSTANCE==null)
            INSTANCE=new RecentsFragment(context);
        return INSTANCE;
    }

}
