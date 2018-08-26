package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoBannerItem {
    private String ID;
    private String Link;
    private String hd_video_link;
    private String sd_video_link;
    private String size_SD;
    private String size_HD;


    public VideoBannerItem() {
    }

    public VideoBannerItem(String ID, String link, String hd_video_link, String sd_video_link, String size_SD, String size_HD) {
        this.ID = ID;
        Link = link;
        this.hd_video_link = hd_video_link;
        this.sd_video_link = sd_video_link;
        this.size_SD = size_SD;
        this.size_HD = size_HD;
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
}
