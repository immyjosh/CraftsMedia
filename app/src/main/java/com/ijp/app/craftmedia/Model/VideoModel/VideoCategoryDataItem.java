package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoCategoryDataItem {

    private String ID;
    private String video_thumbnail;
    private String hd_video_link;
    private String sd_video_link;
    private String video_category_item_id;

    public VideoCategoryDataItem() {
    }

    public VideoCategoryDataItem(String ID, String video_thumbnail, String hd_video_link, String sd_video_link, String video_category_item_id) {
        this.ID = ID;
        this.video_thumbnail = video_thumbnail;
        this.hd_video_link = hd_video_link;
        this.sd_video_link = sd_video_link;
        this.video_category_item_id = video_category_item_id;
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
}
