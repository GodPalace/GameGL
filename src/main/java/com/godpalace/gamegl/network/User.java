package com.godpalace.gamegl.network;

import com.godpalace.gamegl.network.exception.WrongPasswordException;

public class User {
    public String username;
    public String password;
    public String email;
    public boolean isLoggedIn;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = "";
        this.isLoggedIn = false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void login(String password) throws WrongPasswordException {
        if (this.password.equals(password)) {
            this.isLoggedIn = true;
        } else {
            throw new WrongPasswordException("Invalid password");
        }
    }

    public void logout() {
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
