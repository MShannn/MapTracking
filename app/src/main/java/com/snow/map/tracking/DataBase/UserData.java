package com.snow.map.tracking.DataBase;

import io.realm.RealmObject;

/**
 * Created by Muhammad Shan on 04/03/2017.
 */

public class UserData extends RealmObject {
    public UserData() {
    }


    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private  String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private  String password;

}
