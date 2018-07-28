package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoBannerItem {
    public String ID;
    public String Name;
    public String Link;
    public String video_link;


    public VideoBannerItem() {
    }

    public VideoBannerItem(String ID, String name, String link,String video_link) {
        this.ID = ID;
        Name = name;
        Link = link;
        this.video_link = video_link;
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

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
