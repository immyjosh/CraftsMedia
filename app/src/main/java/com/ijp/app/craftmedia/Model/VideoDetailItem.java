package com.ijp.app.craftmedia.Model;

public class VideoDetailItem {
    public String video_link ;
    public String thumb_image_link ;
    public String Name ;

    public VideoDetailItem() {
    }
    public VideoDetailItem(String video_link, String thumb_image_link, String name) {
        this.video_link = video_link;
        this.thumb_image_link = thumb_image_link;
        Name = name;
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
}
