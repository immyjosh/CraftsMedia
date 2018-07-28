package com.ijp.app.craftmedia.Model.VideoModel;

public class VideoRandomModel {
    public String ID;
    public String Description;
    public String Image_link;
    public String video_link;

    public VideoRandomModel() {
    }

    public VideoRandomModel(String ID, String description, String image_link, String video_link) {
        this.ID = ID;
        Description = description;
        Image_link = image_link;
        this.video_link = video_link;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage_link() {
        return Image_link;
    }

    public void setImage_link(String image_link) {
        Image_link = image_link;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }
}
