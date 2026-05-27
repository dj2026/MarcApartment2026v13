package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("HOUSE")
@JsonPropertyOrder({"id", "propertyType", "owner", "price", "area", "bedrooms", "bathrooms", "stories", "mainroad", "guestroom", "basement", "hotwaterheating", "airconditioning", "terrace", "parking", "prefarea", "furnishingstatus", "yardSize", "pool", "description", "reviewCount", "averageRating", "reviews", "schools", "propertyContracts"})
public class House extends Apartment {

    @Column(name = "yard_size") private Integer yardSize;
    @Column(name = "pool") private String pool;

    public House() {super();}

    public House(Long id, String propertyType, Long price, Integer area, Integer bedrooms, Integer bathrooms, Integer stories, String mainroad, String guestroom, String basement, String hotwater, String aircon, String terrace, Integer parking, String prefarea, String furnishingstatus, String description, Integer yardSize, String pool, Owner owner) {
        super(id, propertyType, price, area, bedrooms, bathrooms, stories, mainroad, guestroom, basement, hotwater, aircon, terrace, parking, prefarea, furnishingstatus, description, owner); 
        this.yardSize = yardSize; this.pool = pool;}

    public Integer getYardSize() {return yardSize;} public void setYardSize(Integer yardSize) {this.yardSize = yardSize;}
    public String getPool() {return pool;} public void setPool(String pool) {this.pool = pool;}
}