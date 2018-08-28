package com.ijp.app.craftmedia.Model;

public class WallpaperDetailItem {
    private String ID;
    private String Category;
    private String image_link ;
    private String portrait_img_url;
    private String landscape_img_url;
    private String top_pics_id;
    private String new_pics_id;
    private String picsta_category_list_id;


    public WallpaperDetailItem() {
    }

    public WallpaperDetailItem(String ID, String category, String image_link, String portrait_img_url, String landscape_img_url, String top_pics_id, String new_pics_id, String picsta_category_list_id) {
        this.ID = ID;
        Category = category;
        this.image_link = image_link;
        this.portrait_img_url = portrait_img_url;
        this.landscape_img_url = landscape_img_url;
        this.top_pics_id = top_pics_id;
        this.new_pics_id = new_pics_id;
        this.picsta_category_list_id = picsta_category_list_id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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

    public String getName() {
        return Category;
    }

    public void setName(String category) {
        Category = category;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getTop_pics_id() {
        return top_pics_id;
    }

    public void setTop_pics_id(String top_pics_id) {
        this.top_pics_id = top_pics_id;
    }

    public String getNew_pics_id() {
        return new_pics_id;
    }

    public void setNew_pics_id(String new_pics_id) {
        this.new_pics_id = new_pics_id;
    }

    public String getPicsta_category_list_id() {
        return picsta_category_list_id;
    }

    public void setPicsta_category_list_id(String picsta_category_list_is) {
        this.picsta_category_list_id = picsta_category_list_is;
    }
}
