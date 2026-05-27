package com.example.MarcApartment;

import com.example.MarcApartment.model.Owner;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne; 
import jakarta.persistence.JoinColumn; 

@Entity
public class ApartmentApplicationTest{ @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id; private String address; private double price;
   
    @ManyToOne @JoinColumn(name = "owner_id") private Owner owner;

    public void Apartment() {}
    public void Apartment(String address, double price, Owner owner) {this.address = address; this.price = price; this.owner = owner;}
    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getAddress() {return address;}  public void setAddress(String address) {this.address = address;}
    public double getPrice() {return price;} public void setPrice(double price) {this.price = price;}
    public Owner getOwner() {return owner;} public void setOwner(Owner owner) {this.owner = owner;}
}