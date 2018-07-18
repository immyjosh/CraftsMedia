package com.ijp.app.craftmedia.Utils;

import com.ijp.app.craftmedia.Adapter.TopPicsAdapter;
import com.ijp.app.craftmedia.Model.InfiniteListItem;
import com.ijp.app.craftmedia.Model.NewPicsItem;
import com.ijp.app.craftmedia.Model.NewVideosItem;
import com.ijp.app.craftmedia.Model.TopPicsItem;
import com.ijp.app.craftmedia.Model.TopVideosItem;
import com.ijp.app.craftmedia.Retrofit.ICraftsMediaApi;
import com.ijp.app.craftmedia.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static List<InfiniteListItem> infiniteListItems=new ArrayList<>();

    //10.0.2.2 - Emulator localhost
    //http://www.thingspeakapi.tk/DrinkShop/
    public static final String BASE_URL = "http://www.thingspeakapi.tk/CraftsMedia/";
    public static final String UNSPLASH_URL="https://api.unsplash.com/photos/?client_id=996c5bdaf4d519f049b6927722528645e266455d921f4a1b7bf84626c4f83783";

    public static TopPicsItem currentPicsItem = null;
    public static TopVideosItem currentVideosItem = null;
    public static NewPicsItem currentNewPicsItem = null;
    public static NewVideosItem currentNewVideosItem = null;

    public static ICraftsMediaApi getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(ICraftsMediaApi.class);
    }
}
