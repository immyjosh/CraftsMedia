package com.ijp.app.craftmedia.Model;


public class InfiniteListItem {
    private String ID,Name,image_link,video_link,hd_video_link,category,image_icon;
    private String size_SD;
    private String size_HD;
    private String portrait_img_url;
    private String landscape_img_url;
    public InfiniteListItem() {
    }

    public InfiniteListItem(String ID, String name, String image_link, String video_link, String hd_video_link, String category, String image_icon, String size_SD, String size_HD, String portrait_img_url, String landscape_img_url) {
        this.ID = ID;
        Name = name;
        this.image_link = image_link;
        this.video_link = video_link;
        this.hd_video_link = hd_video_link;
        this.category = category;
        this.image_icon = image_icon;
        this.size_SD = size_SD;
        this.size_HD = size_HD;
        this.portrait_img_url = portrait_img_url;
        this.landscape_img_url = landscape_img_url;
    }

    public String getImage_icon() {
        return image_icon;
    }

    public void setImage_icon(String image_icon) {
        this.image_icon = image_icon;
    }

    public String getHd_video_link() {
        return hd_video_link;
    }

    public void setHd_video_link(String hd_video_link) {
        this.hd_video_link = hd_video_link;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize_SD() {
        return size_SD;
    }

    public void setSize_SD(String size_SD) {
        this.size_SD = size_SD;
    }

    public String getSize_HD() {
        return size_HD;
    }

    public void setSize_HD(String size_HD) {
        this.size_HD = size_HD;
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
