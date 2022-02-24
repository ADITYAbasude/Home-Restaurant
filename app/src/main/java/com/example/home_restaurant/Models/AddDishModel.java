package com.example.home_restaurant.Models;

public class AddDishModel {
    public String DishTitle, DishDescription, DishImage, DishPrice, ProductId , HotelId , userName , lat , lng ;
    public AddDishModel(){}

    public AddDishModel(String HotelId, String DishTitle, String DishDescription, String DishPrice, String DishImage, String ProductId) {
        this.HotelId = HotelId;
        this.DishDescription = DishDescription;
        this.DishTitle = DishTitle;
        this.DishPrice = DishPrice;
        this.DishImage = DishImage;
        this.ProductId = ProductId;
    }

    public AddDishModel(String HotelId, String DishTitle, String DishDescription, String DishPrice,
                        String DishImage, String ProductId , String userName , String lat , String lng) {
        this.HotelId = HotelId;
        this.DishDescription = DishDescription;
        this.DishTitle = DishTitle;
        this.DishPrice = DishPrice;
        this.DishImage = DishImage;
        this.ProductId = ProductId;
        this.userName = userName;
        this.lat= lat;
        this.lng = lng;
    }

}
