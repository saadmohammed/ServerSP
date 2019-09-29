package com.jmc.serversp.Model;

public class Banner {
    private String Id, Image;

    public Banner(String id, String image) {
        Id = id;
        Image = image;
    }





    public Banner() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
