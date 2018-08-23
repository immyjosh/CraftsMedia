package com.ijp.app.craftmedia.Model.PicstaModel;

public class CategoryListItem {
    private String ID;
    private String image_url;
    private String orig_image_url;
    private String category_item_id;

    public CategoryListItem() {
    }



    public CategoryListItem(String ID, String image_url, String orig_image_url, String category_item_id) {
        this.ID = ID;
        this.image_url = image_url;
        this.orig_image_url = orig_image_url;
        this.category_item_id = category_item_id;
    }

    public String getOrig_image_url() {
        return orig_image_url;
    }

    public void setOrig_image_url(String orig_image_url) {
        this.orig_image_url = orig_image_url;
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
