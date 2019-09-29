package com.jmc.serversp.Model;

public class Department {
    private String Name;
    private String Image;

    public Department() {
    }

    public Department(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
