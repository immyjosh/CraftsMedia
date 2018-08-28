package com.ijp.app.craftmedia.Model.PicstaModel;

public class CategoryListItem {
    private String ID;
    private String image_url;
    private String category_item_id;
    private String portrait_img_url;
    private String landscape_img_url;

    public CategoryListItem() {
    }


    public CategoryListItem(String ID, String image_url, String category_item_id, String portrait_img_url, String landscape_img_url) {
        this.ID = ID;
        this.image_url = image_url;
        this.category_item_id = category_item_id;
        this.portrait_img_url = portrait_img_url;
        this.landscape_img_url = landscape_img_url;
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

    public String getCategory_item_id() {
        return category_item_id;
    }

    public void setCategory_item_id(String category_item_id) {
        this.category_item_id = category_item_id;
    }
}
