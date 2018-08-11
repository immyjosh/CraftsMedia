package com.ijp.app.craftmedia.Model;


public class InfiniteListItem {
    public String ID,Name,image_link,video_link,category;

    public InfiniteListItem() {
    }

    public InfiniteListItem(String ID, String name, String image_link, String video_link, String category) {
        this.ID = ID;
        Name = name;
        this.image_link = image_link;
        this.video_link = video_link;
        this.category = category;
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

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
