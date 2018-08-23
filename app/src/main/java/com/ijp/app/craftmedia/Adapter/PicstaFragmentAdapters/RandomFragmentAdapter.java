package com.ijp.app.craftmedia.Adapter.PicstaFragmentAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.PicstaModel.RandomListItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.WallpaperDetailActivity;

import java.util.List;

public class RandomFragmentAdapter extends RecyclerView.Adapter<RandomFragmentAdapter.RandomFragmentViewHolder> {

    private Context mContext;
    private List<RandomListItem> randomListItems;

    public RandomFragmentAdapter(Context mContext, List<RandomListItem> randomListItems) {
        this.mContext = mContext;
        this.randomListItems = randomListItems;
    }

    @NonNull
    @Override
    public RandomFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.random_fragment_item,parent,false);

        return new RandomFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomFragmentViewHolder holder, final int position) {
        Glide.with(mContext).load(randomListItems.get(position).getImage_url())
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentRandomListItem=randomListItems.get(position);
                mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return randomListItems.size();
    }

    public class RandomFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }


        public RandomFragmentViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imgPics=itemView.findViewById(R.id.random_item_image);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
