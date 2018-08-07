package com.ijp.app.craftmedia.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PicstaFavoriteDAO {
    @Query("SELECT * FROM PicstaFavorite")
    Flowable<List<PicstaFavorites>> getFavItem();

    @Query("SELECT EXISTS (SELECT 1 FROM PicstaFavorite WHERE id=:itemId)")
    int isFavorite(int itemId);

    @Insert
    void insertFav(PicstaFavorites... picstaFavorites);

    @Delete
    void delete(PicstaFavorites picstaFavorites);
}
