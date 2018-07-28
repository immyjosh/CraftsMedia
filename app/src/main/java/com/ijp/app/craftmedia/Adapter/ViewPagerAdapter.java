package com.ijp.app.craftmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ijp.app.craftmedia.Model.VideoModel.VideoBannerItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter{

    private Context context;

    private List<VideoBannerItem> videoBannerItemList;

    public ViewPagerAdapter(Context context, List<VideoBannerItem> videoBannerItemList) {
        this.context = context;
        this.videoBannerItemList = videoBannerItemList;
    }

    @Override
    public int getCount() {
        return videoBannerItemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {



        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater inflater=LayoutInflater.from(context);

        View view=inflater.inflate(R.layout.video_slider,container,false);

        ImageView imageView=view.findViewById(R.id.slider_image);

        Picasso.with(context).load(videoBannerItemList.get(position).Link)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TESTING",videoBannerItemList.get(position)+"position");
                Common.currentVideoBannerItem=videoBannerItemList.get(position);
                context.startActivity(new Intent(context, VideoDetailsPage.class));
            }
        });

        container.addView(view);

        return view;


    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
