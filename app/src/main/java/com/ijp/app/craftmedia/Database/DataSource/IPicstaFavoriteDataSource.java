package com.ijp.app.craftmedia.Database.DataSource;

import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;

import java.util.List;

import io.reactivex.Flowable;

public interface IPicstaFavoriteDataSource {
    Flowable<List<PicstaFavorites>> getFavItem();

    int isFavorite(int itemId);

    void insertFav(PicstaFavorites... picstaFavorites);

    void delete(PicstaFavorites picstaFavorites);
}
