package com.ijp.app.craftmedia.Adapter;

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
import com.ijp.app.craftmedia.Model.NewPicsItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.WallpaperDetailActivity;

import java.util.List;

public class NewPicsAdapter extends RecyclerView.Adapter<NewPicsAdapter.NewPicsAdapterViewHolder> {

    private Context mContext;
    private List<NewPicsItem> newPicsItemList;

    public NewPicsAdapter(Context mContext, List<NewPicsItem> newPicsItemList) {
        this.mContext = mContext;
        this.newPicsItemList = newPicsItemList;
    }

    @NonNull
    @Override
    public NewPicsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.new_pics_item_cv,parent,false);

        return new NewPicsAdapter.NewPicsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewPicsAdapterViewHolder holder, final int position) {
        Glide.with(mContext).load(newPicsItemList.get(position).getLink())
                .into(holder.imgPics);

        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentNewPicsItem=newPicsItemList.get(position);
                mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));

                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newPicsItemList.size();
    }


    public class NewPicsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }


        private NewPicsAdapterViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imgPics=itemView.findViewById(R.id.new_image_pics_cv);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
