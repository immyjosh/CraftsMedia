package com.ijp.app.craftmedia.Model;

public class NewPicsItem {
    private String ID;
    private String Name;
    private String Link;
    private String portrait_img_url;
    private String landscape_img_url;
    private String Category ;

    public NewPicsItem() {
    }

    public NewPicsItem(String ID, String name, String link, String portrait_img_url, String landscape_img_url, String category) {
        this.ID = ID;
        Name = name;
        Link = link;
        this.portrait_img_url = portrait_img_url;
        this.landscape_img_url = landscape_img_url;
        Category = category;
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

    public String getPortrait_img_url() {
        return portrait_img_url;
    }

    public void setPortrait_img_url(String portrait_img_url) {
        this.portrait_img_url = portrait_img_url;
    }

    public String getLandscape_img_url() {
        return landscape_img_url;
    }

    public void setLandscape_img_url(String landscape_img_url) {
        this.landscape_img_url = landscape_img_url;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
