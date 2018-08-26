package com.ijp.app.craftmedia.Model;

public class VideoDetailItem {
    public String ID;
    public String video_link ;
    public String hd_video_link ;
    public String thumb_image_link ;
    public String Category;
    public String Name ;
    public String size_SD;
    public String size_HD;
    public String no_of_downloads;


    public VideoDetailItem() {
    }

    public VideoDetailItem(String ID, String video_link, String hd_video_link, String thumb_image_link, String category, String name, String size_SD, String size_HD, String no_of_downloads) {
        this.ID = ID;
        this.video_link = video_link;
        this.hd_video_link = hd_video_link;
        this.thumb_image_link = thumb_image_link;
        Category = category;
        Name = name;
        this.size_SD = size_SD;
        this.size_HD = size_HD;
        this.no_of_downloads = no_of_downloads;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getThumb_image_link() {
        return thumb_image_link;
    }

    public void setThumb_image_link(String thumb_image_link) {
        this.thumb_image_link = thumb_image_link;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getNo_of_downloads() {
        return no_of_downloads;
    }

    public void setNo_of_downloads(String no_of_downloads) {
        this.no_of_downloads = no_of_downloads;
    }
}
