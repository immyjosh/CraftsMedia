package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoCategoryDataItem {

    private String ID;
    private String video_thumbnail;
    private String video_link;
    private String video_category_item_id;

    public VideoCategoryDataItem() {
    }

    public VideoCategoryDataItem(String ID, String video_thumbnail, String video_link, String video_category_item_id) {
        this.ID = ID;
        this.video_thumbnail = video_thumbnail;
        this.video_link = video_link;
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

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getVideo_category_item_id() {
        return video_category_item_id;
    }

    public void setVideo_category_item_id(String video_category_item_id) {
        this.video_category_item_id = video_category_item_id;
    }
}
