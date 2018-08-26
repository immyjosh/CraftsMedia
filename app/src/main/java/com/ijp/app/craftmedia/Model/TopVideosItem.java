package com.ijp.app.craftmedia.Model;

import com.google.gson.annotations.SerializedName;

public class TopVideosItem {
    private String ID ;
    private String Name ;
    private String Link ;
    private String Category ;
    private String video_link;
    private String hd_video_link;
    private String size_SD;
    private String size_HD;

    public TopVideosItem() {
    }


    public TopVideosItem(String ID, String name, String link, String category, String video_link, String hd_video_link, String size_SD, String size_HD) {
        this.ID = ID;
        Name = name;
        Link = link;
        Category = category;
        this.video_link = video_link;
        this.hd_video_link = hd_video_link;
        this.size_SD = size_SD;
        this.size_HD = size_HD;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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
