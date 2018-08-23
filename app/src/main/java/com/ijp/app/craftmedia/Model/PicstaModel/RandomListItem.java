package com.ijp.app.craftmedia.Model.PicstaModel;

public class RandomListItem {
    private String ID;
    private String image_url;
    private String orig_image_link;
    private String category;

    public RandomListItem() {
    }

    public RandomListItem(String ID, String image_url, String orig_image_link, String category) {
        this.ID = ID;
        this.image_url = image_url;
        this.orig_image_link = orig_image_link;
        this.category = category;
    }

    public String getOrig_image_link() {
        return orig_image_link;
    }

    public void setOrig_image_link(String orig_image_link) {
        this.orig_image_link = orig_image_link;
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
