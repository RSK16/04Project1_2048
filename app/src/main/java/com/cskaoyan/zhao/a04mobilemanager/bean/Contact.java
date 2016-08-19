package com.cskaoyan.zhao.a04mobilemanager.bean;

/**
 * Created by zhao on 2016/8/8.
 */
public class Contact {

    String name;
    String phonenumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Contact(String name, String phonenumber) {
        this.name = name;
        this.phonenumber = phonenumber;
    }

    public Contact() {
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                '}';
    }
}
