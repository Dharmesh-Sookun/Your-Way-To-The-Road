package com.example.yourwaytotheroad;

public class member {

    protected String name;
    protected String NIC;
    protected String address;
    protected String phone;
    protected String date;

    public member() {
    }

    public String getName() {
        return name;
    }

    public String getNIC() {
        return NIC;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
