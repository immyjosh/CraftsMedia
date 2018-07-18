package com.ijp.app.craftmedia.Model.PicstaModel;

public class CategoryFragmentItem {
    public String image_url;
    public String category;

    public CategoryFragmentItem() {
    }

    public CategoryFragmentItem(String image_url, String category) {
        this.image_url = image_url;
        this.category = category;
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
