package com.ijp.app.craftmedia.Model;

public class NewPicsItem {
    private String ID;
    private String Name;
    private String Link;
    private String orig_image_link;
    private String Category ;

    public NewPicsItem() {
    }

    public NewPicsItem(String ID, String name, String link, String orig_image_link, String category) {
        this.ID = ID;
        Name = name;
        Link = link;
        this.orig_image_link = orig_image_link;
        Category = category;
    }


    public String getOrig_image_link() {
        return orig_image_link;
    }

    public void setOrig_image_link(String orig_image_link) {
        this.orig_image_link = orig_image_link;
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
}
