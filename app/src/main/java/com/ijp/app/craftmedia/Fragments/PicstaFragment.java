package com.ijp.app.craftmedia.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ijp.app.craftmedia.Fragments.PicstaPageFragment.CategoryFragment;
import com.ijp.app.craftmedia.Fragments.PicstaPageFragment.RandomFragment;
import com.ijp.app.craftmedia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicstaFragment extends Fragment {


    String hexColorTransparent="#99000000";
    String hexColorBlack="#27253d";


    public PicstaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picsta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {

        Fragment categoryFragment = new CategoryFragment();
        FragmentTransaction categoryTransaction = getChildFragmentManager().beginTransaction();
        categoryTransaction.replace(R.id.child_fragment_container, categoryFragment).commit();



        final Button btn1=view.findViewById(R.id.btn1);
        final Button btn2=view.findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setBackgroundColor(Color.parseColor(hexColorBlack));
                btn2.setBackgroundColor(Color.parseColor(hexColorTransparent));

                Fragment categoryFragment = new CategoryFragment();
                FragmentTransaction categoryTransaction = getChildFragmentManager().beginTransaction();
                categoryTransaction.replace(R.id.child_fragment_container, categoryFragment).commit();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.setBackgroundColor(Color.parseColor(hexColorBlack));
                btn1.setBackgroundColor(Color.parseColor(hexColorTransparent));

                Fragment randomFragment = new RandomFragment();
                FragmentTransaction recentsTransaction = getChildFragmentManager().beginTransaction();
                recentsTransaction.replace(R.id.child_fragment_container, randomFragment).commit();

            }
        });


    }


}
