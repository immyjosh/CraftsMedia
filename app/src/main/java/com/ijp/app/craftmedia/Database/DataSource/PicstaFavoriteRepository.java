package com.ijp.app.craftmedia.Database.DataSource;

import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;

import java.util.List;

import io.reactivex.Flowable;

public class PicstaFavoriteRepository implements IPicstaFavoriteDataSource {

    private IPicstaFavoriteDataSource picstaFavoriteDataSource;

    public PicstaFavoriteRepository(IPicstaFavoriteDataSource picstaFavoriteDataSource) {
        this.picstaFavoriteDataSource = picstaFavoriteDataSource;
    }

    public static PicstaFavoriteRepository instance;
    public static PicstaFavoriteRepository getInstance(IPicstaFavoriteDataSource picstaFavoriteDataSource){
        if(instance==null)
            instance=new PicstaFavoriteRepository(picstaFavoriteDataSource);
        return instance;
    }

    @Override
    public Flowable<List<PicstaFavorites>> getFavItem() {
        return picstaFavoriteDataSource.getFavItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return picstaFavoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void insertFav(PicstaFavorites... picstaFavorites) {
        picstaFavoriteDataSource.insertFav(picstaFavorites);
    }

    @Override
    public void delete(PicstaFavorites picstaFavorites) {
        picstaFavoriteDataSource.delete(picstaFavorites);
    }
}
