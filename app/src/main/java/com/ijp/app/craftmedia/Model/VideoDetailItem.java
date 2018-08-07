package com.ijp.app.craftmedia.Model;

public class VideoDetailItem {
    public String ID;
    public String video_link ;
    public String thumb_image_link ;
    public String Category;
    public String Name ;
    public String video_banner_id;

    public VideoDetailItem() {
    }

    public VideoDetailItem(String ID, String video_link, String thumb_image_link, String category, String name, String video_banner_id) {
        this.ID = ID;
        this.video_link = video_link;
        this.thumb_image_link = thumb_image_link;
        Category = category;
        Name = name;
        this.video_banner_id = video_banner_id;
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

    public String getVideo_banner_id() {
        return video_banner_id;
    }

    public void setVideo_banner_id(String video_banner_id) {
        this.video_banner_id = video_banner_id;
    }
}
