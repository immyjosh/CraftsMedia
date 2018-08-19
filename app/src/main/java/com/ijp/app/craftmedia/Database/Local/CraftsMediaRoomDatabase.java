package com.ijp.app.craftmedia.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ijp.app.craftmedia.Database.ModelDB.Favorites;
import com.ijp.app.craftmedia.Database.ModelDB.PicstaFavorites;


@Database(entities = {Favorites.class, PicstaFavorites.class},version = 1)

public abstract class CraftsMediaRoomDatabase extends RoomDatabase {

    public abstract FavoriteDAO favoriteDAO();
    public abstract PicstaFavoriteDAO picstaFavoriteDAO();

    private static CraftsMediaRoomDatabase instance;

    public static CraftsMediaRoomDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context,CraftsMediaRoomDatabase.class,"CraftsMedia")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }


}
