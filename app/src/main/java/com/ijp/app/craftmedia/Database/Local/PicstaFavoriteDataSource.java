package com.ijp.app.craftmedia.Database.Local;

import com.ijp.app.craftmedia.Database.DataSource.IPicstaFavoriteDataSource;
import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;

import java.util.List;

import io.reactivex.Flowable;

public class PicstaFavoriteDataSource implements IPicstaFavoriteDataSource {

    private PicstaFavoriteDAO picstaFavoriteDAO;
    private static PicstaFavoriteDataSource instance;

    public PicstaFavoriteDataSource(PicstaFavoriteDAO picstaFavoriteDAO) {
        this.picstaFavoriteDAO = picstaFavoriteDAO;
    }

    public static PicstaFavoriteDataSource getInstance(PicstaFavoriteDAO picstaFavoriteDAO){
        if(instance==null)
            instance=new PicstaFavoriteDataSource(picstaFavoriteDAO);
        return instance;
    }

    @Override
    public Flowable<List<PicstaFavorites>> getFavItem() {
        return picstaFavoriteDAO.getFavItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return picstaFavoriteDAO.isFavorite(itemId);
    }

    @Override
    public void insertFav(PicstaFavorites... picstaFavorites) {
        picstaFavoriteDAO.insertFav(picstaFavorites);
    }

    @Override
    public void delete(PicstaFavorites picstaFavorites) {
        picstaFavoriteDAO.delete(picstaFavorites);
    }
}
