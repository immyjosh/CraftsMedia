package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoCategoriesItem {
    public String ID;
    public String image_url;
    public String category;

    public VideoCategoriesItem() {
    }

    public VideoCategoriesItem(String ID, String image_url, String category) {
        this.ID = ID;
        this.image_url = image_url;
        this.category = category;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
