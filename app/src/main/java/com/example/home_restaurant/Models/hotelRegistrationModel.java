package com.example.home_restaurant.Models;

public class hotelRegistrationModel {
    public String hotelName , SellerId , hotelDescription , hotelAddress , hotelTiming , hotelContact  , lat, lag , HotelId , rating;
    public hotelRegistrationModel(){}

    public hotelRegistrationModel(String HotelId , String SellerId , String hotelName, String hotelDescription, String hotelAddress, String hotelTiming, String hotelContact, String lat, String lag) {
        this.HotelId = HotelId;
        this.SellerId = SellerId;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.hotelAddress = hotelAddress;
        this.hotelTiming = hotelTiming;
        this.hotelContact = hotelContact;
        this.lat = lat;
        this.lag = lag;
    }

    public hotelRegistrationModel(String rating){
        this.rating = rating;
    }
}
