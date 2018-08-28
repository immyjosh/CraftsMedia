package com.ijp.app.craftmedia.Model.PicstaModel;

public class RandomListItem {
    private String ID;
    private String image_url;
    private String portrait_img_url;
    private String landscape_img_url;
    private String category;

    public RandomListItem() {
    }

    public RandomListItem(String ID, String image_url, String portrait_img_url, String landscape_img_url, String category) {
        this.ID = ID;
        this.image_url = image_url;
        this.portrait_img_url = portrait_img_url;
        this.landscape_img_url = landscape_img_url;
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

    public String getPortrait_img_url() {
        return portrait_img_url;
    }

    public void setPortrait_img_url(String portrait_img_url) {
        this.portrait_img_url = portrait_img_url;
    }

    public String getLandscape_img_url() {
        return landscape_img_url;
    }

    public void setLandscape_img_url(String landscape_img_url) {
        this.landscape_img_url = landscape_img_url;
    }
}
