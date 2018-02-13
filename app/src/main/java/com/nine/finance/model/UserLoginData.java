package com.nine.finance.model;

import java.io.Serializable;

public class UserLoginData implements Serializable {
    /**
     * username : admin
     * password : 123456
     * token : F4EA8B5BEEC14DC8872116195CFDFBB2
     */

    private String username;
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
