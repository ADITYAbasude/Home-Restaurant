package com.example.home_restaurant.Models;

public class UserAuthenticationModel {
    public String UserName , UserImage ,lat , lag  ;
    public UserAuthenticationModel(){};
    public UserAuthenticationModel(String UserName){
        this.UserName = UserName;
    }
    public UserAuthenticationModel(String UserImage , String no){
        this.UserImage = UserImage;
    }
    public UserAuthenticationModel(String lat , String lag , String nuLL){this.lat = lat;this.lag = lag;}
}
