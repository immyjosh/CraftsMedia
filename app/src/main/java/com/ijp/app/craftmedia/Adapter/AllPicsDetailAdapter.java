package com.ijp.app.craftmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.WallpeperDetailItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.WallpaperDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllPicsDetailAdapter extends RecyclerView.Adapter<AllPicsDetailAdapter.AllPicsDetailViewHolder> {
    private Context mContext;
    private List<WallpeperDetailItem> wallpeperDetailItemList;

    public AllPicsDetailAdapter(Context mContext, List<WallpeperDetailItem> wallpeperDetailItemList) {
        this.mContext = mContext;
        this.wallpeperDetailItemList = wallpeperDetailItemList;
    }

    @NonNull
    @Override
    public AllPicsDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.top_pics_item_cv,parent,false);

        return new AllPicsDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPicsDetailViewHolder holder, final int position) {
        Picasso.with(mContext).load(wallpeperDetailItemList.get(position).image_link)
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {

                Common.currentWallpaperDetailItem=wallpeperDetailItemList.get(position);
                mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpeperDetailItemList.size();
    }

    public class AllPicsDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        public AllPicsDetailViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imgPics=itemView.findViewById(R.id.top_image_pics_cv);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
