package com.example.easipe_mobileapplicationdevelopment.view.auth;

public class ReadWriteUserDetails {
    public String firstname, lastname, email, username, password, location, description;

    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails(String firstname, String lastname, String email, String username, String password, String location, String description) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.location = location;
        this.description = description;
    }
}
