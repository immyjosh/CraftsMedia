package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoBannerItem {
    private String ID;
    private String Link;
    private String hd_video_link;
    private String sd_video_link;


    public VideoBannerItem() {
    }

    public VideoBannerItem(String ID, String link, String hd_video_link, String sd_video_link) {
        this.ID = ID;
        Link = link;
        this.hd_video_link = hd_video_link;
        this.sd_video_link = sd_video_link;
    }

    public String getSd_video_link() {
        return sd_video_link;
    }

    public void setSd_video_link(String sd_video_link) {
        this.sd_video_link = sd_video_link;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getHd_video_link() {
        return hd_video_link;
    }

    public void setHd_video_link(String hd_video_link) {
        this.hd_video_link = hd_video_link;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


}
