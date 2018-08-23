package com.ijp.app.craftmedia.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "PicstaFavorite")
public class PicstaFavorites {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "orig_link")
    public String origLink;

    @ColumnInfo(name = "category")
    public String Category;




}
