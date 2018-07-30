package com.ijp.app.craftmedia.Model;

public class WallpeperDetailItem {
    public String ID;
    public String Category;
    public String image_link ;
    private String top_pics_id;
    private String new_pics_id;
    private String picsta_category_list_id;


    public WallpeperDetailItem() {
    }

    public WallpeperDetailItem(String category, String image_link, String top_pics_id, String new_pics_id, String picsta_category_list_id) {
        Category = category;
        this.image_link = image_link;
        this.top_pics_id = top_pics_id;
        this.new_pics_id = new_pics_id;
        this.picsta_category_list_id = picsta_category_list_id;
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
