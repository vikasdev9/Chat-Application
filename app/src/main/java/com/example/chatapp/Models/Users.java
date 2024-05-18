package com.example.chatapp.Models;

public class Users {
    String uid;
    String name;
    String email;

    String imageuris;
    String Status;

    public Users(String uid, String name, String email, String imageuris,String Status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageuris = imageuris;
        this.Status = Status;
    }

    public Users() {

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageuris() {
        return imageuris;
    }

    public void setImageuris(String imageuris) {
        this.imageuris = imageuris;
    }
}
