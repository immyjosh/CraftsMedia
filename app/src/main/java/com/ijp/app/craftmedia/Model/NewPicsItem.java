package com.ijp.app.craftmedia.Model;

public class NewPicsItem {
    public String ID;
    public String Name;
    public String Link;
    public String Category ;
    public NewPicsItem() {
    }

    public NewPicsItem(String ID, String name, String link, String category) {
        this.ID = ID;
        Name = name;
        Link = link;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
