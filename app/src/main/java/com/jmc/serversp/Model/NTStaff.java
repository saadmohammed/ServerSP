package com.jmc.serversp.Model;

public class NTStaff {

    private String Name, Designation;
    private Long Phone;

    public NTStaff() {
    }

    public NTStaff(String name, String designation, Long phone) {
        Name = name;
        Designation = designation;
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public Long getPhone() {
        return Phone;
    }

    public void setPhone(Long phone) {
        Phone = phone;
    }
}
