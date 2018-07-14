package com.ijp.app.craftmedia.Utils;

import com.ijp.app.craftmedia.Adapter.TopPicsAdapter;
import com.ijp.app.craftmedia.Model.InfiniteListItem;
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
    public static final String BASE_URL = "http://www.thingspeakapi.tk/DrinkShop/";
    public static final String URL = "http://www.thingspeakapi.tk/DrinkShop/getdrink.php?menuid=";
    public static TopPicsItem currentPicsItem = null;
    public static TopVideosItem currentVideosItem = null;

    public static ICraftsMediaApi getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(ICraftsMediaApi.class);
    }
}
