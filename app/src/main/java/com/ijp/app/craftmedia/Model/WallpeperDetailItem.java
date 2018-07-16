package com.ijp.app.craftmedia.Model;

public class WallpeperDetailItem {
    public String image_link ;

    public WallpeperDetailItem() {
    }

    public WallpeperDetailItem(String link) {
        image_link = link;
    }

    public String getLink() {
        return image_link;
    }

    public void setLink(String link) {
        image_link = link;
    }
}
