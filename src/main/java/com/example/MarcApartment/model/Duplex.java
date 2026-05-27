package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("DUPLEX")
@JsonPropertyOrder({"id", "propertyType", "owner", "price", "area", "bedrooms", "bathrooms", "stories", "mainroad", "guestroom", "basement", "hotwaterheating", "airconditioning", "terrace", "parking", "prefarea", "furnishingstatus", "balcony", "elevator", "description", "reviewCount", "averageRating", "reviews", "schools", "propertyContracts"})
public class Duplex extends Apartment {

    @Column(name = "balcony") private String balcony;
    @Column(name = "elevator")private String elevator;
    public Duplex() {super();}
    public Duplex(Long id, String propertyType, Long price, Integer area, Integer bedrooms, Integer bathrooms, Integer stories, String mainroad, String guestroom,  String basement, String hotwater, String aircon, String terrace,  Integer parking, String prefarea, String furnishingstatus, String description, String balcony, String elevator, Owner owner) {
        super(id, propertyType, price, area, bedrooms, bathrooms, stories, mainroad, guestroom, basement, hotwater, aircon, terrace, parking, prefarea, furnishingstatus, description, owner);
        this.balcony = balcony; this.elevator = elevator;}

    public String getBalcony() {return balcony;} public void setBalcony(String balcony) {this.balcony = balcony;}
    public String getElevator() {return elevator;} public void setElevator(String elevator) {this.elevator = elevator;}
}