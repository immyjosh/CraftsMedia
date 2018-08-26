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
import com.ijp.app.craftmedia.Model.PicstaModel.CategoryListItem;
import com.ijp.app.craftmedia.R;
import com.ijp.app.craftmedia.Utils.Common;
import com.ijp.app.craftmedia.WallpaperDetailActivity;

import java.util.List;

public class CategoryListItemAdapter extends RecyclerView.Adapter<CategoryListItemAdapter.CategoryListViewHolder> {
    private Context mContext;
    private List<CategoryListItem> categoryListItems;

    public CategoryListItemAdapter(Context mContext, List<CategoryListItem> categoryListItems) {
        this.mContext = mContext;
        this.categoryListItems = categoryListItems;
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.category_list_item,parent,false);

        return new CategoryListItemAdapter.CategoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder holder, final int position) {
        Glide.with(mContext).load(categoryListItems.get(position).getImage_url())
                .into(holder.imgPics);


        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {

                Common.currentCategoryListItem=categoryListItems.get(position);
                mContext.startActivity(new Intent(mContext, WallpaperDetailActivity.class));
                ((Activity) mContext).overridePendingTransition(R.anim.fadein,R.anim.fade_out);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryListItems.size();
    }

    public class CategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPics;
        IitemClickListner iitemClickListner;

        public void setItemClickListner(IitemClickListner itemClickListner) {
            this.iitemClickListner = itemClickListner;
        }


        public CategoryListViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imgPics=itemView.findViewById(R.id.category_item_image);
        }

        @Override
        public void onClick(View v) {
            iitemClickListner.onClick(v);
        }
    }
}
