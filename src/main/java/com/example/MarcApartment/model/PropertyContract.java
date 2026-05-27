package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete; 
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDate;

@Entity
@Table(name = "property_contracts")
public class PropertyContract {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH}) 
    @JoinColumn(name = "owner_id")  @JsonBackReference(value = "owner-contract")
    @OnDelete(action = OnDeleteAction.CASCADE) private Owner owner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH}) 
    @JoinColumn(name = "apartment_id") @JsonBackReference(value = "apartment-contract")
    @OnDelete(action = OnDeleteAction.CASCADE) private Apartment apartment;

    @Column(name = "contract_details") private String contractDetails; 
    @Column(name = "contract_date") private LocalDate contractDate; 
    @Column(name = "final_price") private Double finalPrice;

    public PropertyContract() {}

    public PropertyContract(Owner owner, Apartment apartment, String contractDetails, LocalDate contractDate, Double finalPrice) {this.owner = owner; this.apartment = apartment; this.contractDetails = contractDetails; this.contractDate = contractDate; this.finalPrice = finalPrice;}

    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public Owner getOwner() {return owner;} public void setOwner(Owner owner) {this.owner = owner;}
    public Apartment getApartment() {return apartment;} public void setApartment(Apartment apartment) {this.apartment = apartment;}
    public String getContractDetails() {return contractDetails;} public void setContractDetails(String contractDetails) {this.contractDetails = contractDetails;}
    public LocalDate getContractDate() {return contractDate;} public void setContractDate(LocalDate contractDate) {this.contractDate = contractDate;}
    public Double getFinalPrice() {return finalPrice;} public void setFinalPrice(Double finalPrice) {this.finalPrice = finalPrice;}
}