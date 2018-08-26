package com.ijp.app.craftmedia.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Favorite")
public class Favorites {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "category")
    public String Category;

    @ColumnInfo(name = "videoLink")
    public String video_link;

    @ColumnInfo(name = "hdVideoLink")
    public String hd_video_link;

    @ColumnInfo(name = "hdSize")
    public String hd_size;

    @ColumnInfo(name = "sdSize")
    public String sd_size;


}