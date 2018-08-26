package com.ijp.app.craftmedia.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ijp.app.craftmedia.Interface.IitemClickListner;
import com.ijp.app.craftmedia.Model.InfiniteListItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.VideoDetailsPage;
import com.ijp.app.craftmedia.WallpaperDetailActivity;

import java.util.List;

public class DiscreteScrollAdapter extends RecyclerView.Adapter<DiscreteScrollAdapter.DiscreteScrollViewHolder> {
    private Context mContext;
    private List<InfiniteListItem> infiniteListItemList;

    public DiscreteScrollAdapter(Context mContext, List<InfiniteListItem> infiniteListItemList) {
        this.mContext = mContext;
        this.infiniteListItemList = infiniteListItemList;
    }

    @NonNull
    @Override
    public DiscreteScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.discrete_scroll_item,parent,false);
        return new DiscreteScrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscreteScrollViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(mContext).load(infiniteListItemList.get(position).getImage_link())
                .into(holder.imgPics);

        Glide.with(mContext).load(infiniteListItemList.get(position).getImage_icon())
                .into(holder.discretePickerText);


        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        Common.infiniteListItems=infiniteListItemList.get(position);
                        mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));
                        ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
                        break;

                    case 1:
                        Common.infiniteListItems=infiniteListItemList.get(position);
                        mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));
                        ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
                        break;

                    case 2:

                        Common.infiniteListItems=infiniteListItemList.get(position);
                        mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
                        ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
                        break;

                    case 3:
                        Common.infiniteListItems=infiniteListItemList.get(position);
                        mContext.startActivity(new Intent(mContext, VideoDetailsPage.class));
                        ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return infiniteListItemList.size();
    }

    public class DiscreteScrollViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        private ImageView discretePickerText;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }

        private DiscreteScrollViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            imgPics=itemView.findViewById(R.id.discrete_image_pics_cv);
            discretePickerText=itemView.findViewById(R.id.discrete_text_cv);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
