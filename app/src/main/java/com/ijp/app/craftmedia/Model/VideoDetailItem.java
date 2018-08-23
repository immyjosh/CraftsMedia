package com.ijp.app.craftmedia.Model;

public class VideoDetailItem {
    public String ID;
    public String video_link ;
    public String hd_video_link ;
    public String thumb_image_link ;
    public String Category;
    public String Name ;


    public VideoDetailItem() {
    }

    public VideoDetailItem(String ID, String video_link, String hd_video_link, String thumb_image_link, String category, String name) {
        this.ID = ID;
        this.video_link = video_link;
        this.hd_video_link = hd_video_link;
        this.thumb_image_link = thumb_image_link;
        Category = category;
        Name = name;
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


}
