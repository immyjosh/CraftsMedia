package com.ijp.app.craftmedia.Model;

public class TopPicsItem {
    public String Link;

    public TopPicsItem() {
    }

    public TopPicsItem(String topImageLink) {
        this.Link = topImageLink;
    }

    public String getTopImageLink() {
        return Link;
    }

    public void setTopImageLink(String topImageLink) {
        this.Link = topImageLink;
    }
}
