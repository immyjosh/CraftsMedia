package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoCategoryDataItem {

    private String ID;
    private String video_thumbnail;
    private String hd_video_link;
    private String sd_video_link;
    private String video_category_item_id;
    private String size_SD;
    private String size_HD;
    private int no_of_downloads;


    public VideoCategoryDataItem() {
    }

    public VideoCategoryDataItem(String ID, String video_thumbnail, String hd_video_link, String sd_video_link, String video_category_item_id, String size_SD, String size_HD, int no_of_downloads) {
        this.ID = ID;
        this.video_thumbnail = video_thumbnail;
        this.hd_video_link = hd_video_link;
        this.sd_video_link = sd_video_link;
        this.video_category_item_id = video_category_item_id;
        this.size_SD = size_SD;
        this.size_HD = size_HD;
        this.no_of_downloads = no_of_downloads;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getHd_video_link() {
        return hd_video_link;
    }

    public void setHd_video_link(String hd_video_link) {
        this.hd_video_link = hd_video_link;
    }

    public String getSd_video_link() {
        return sd_video_link;
    }

    public void setSd_video_link(String sd_video_link) {
        this.sd_video_link = sd_video_link;
    }

    public String getVideo_category_item_id() {
        return video_category_item_id;
    }

    public void setVideo_category_item_id(String video_category_item_id) {
        this.video_category_item_id = video_category_item_id;
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

    public int getNo_of_downloads() {
        return no_of_downloads;
    }

    public void setNo_of_downloads(int no_of_downloads) {
        this.no_of_downloads = no_of_downloads;
    }
}
