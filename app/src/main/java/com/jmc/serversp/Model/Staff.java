package com.jmc.serversp.Model;

public class Staff {
    private String Name;
    private String Post;
    private String Degree;
    private Long Phone;
    private String Email;
    private String Image;
    private String Address;
    private String DeptId;

    public Staff(String name, String post, String degree, Long phone, String email, String image, String address, String deptId) {
        Name = name;
        Post = post;
        Degree = degree;
        Phone = phone;
        Email = email;
        Image = image;
        Address = address;
        DeptId = deptId;
    }

    public String getDeptId() {
        return DeptId;
    }

    public void setDeptId(String deptId) {
        DeptId = deptId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Staff() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public Long getPhone() {
        return Phone;
    }

    public void setPhone(Long phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
