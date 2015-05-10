package com.relianceit.relianceorder.models;

/**
 * Created by sura on 4/27/15.
 */
public class ROSUser {

    private String username = null;
    private String password = null;
    private String deviceToken = null;
    private String accessToken = null;

    private static ROSUser instance = null;

    private ROSUser() {}

    public static ROSUser getInstance(){
        if (instance == null) {
            instance = new ROSUser();
        }

        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
