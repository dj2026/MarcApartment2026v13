package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owners")
public class Owner extends Person {

    @Column(name = "is_active") private Boolean isActive; 
    @Column(name = "is_business") private Boolean isBusiness;  
    @Column(name = "id_legal_owner")private String idLegalOwner; 
    @Column(name = "registration_date")private LocalDate registrationDate; 
    @Column(name = "qty_days_owner")private Integer qtyDaysAsOwner; 
   
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("owner")
    private List<PropertyContract> contracts = new ArrayList<>();

    public Owner() {super();}

    public Owner(String name, String email, String operation, Integer age, Boolean isActive, Boolean isBusiness, String idLegal, LocalDate regDate, Integer days) {
        super(name, email, operation); 
        this.setAge(age); this.isActive = isActive; this.isBusiness = isBusiness; this.idLegalOwner = idLegal; this.registrationDate = regDate; this.qtyDaysAsOwner = days;}

    public Boolean getIsActive() {return isActive;} public void setIsActive(Boolean isActive) {this.isActive = isActive;}
    public Boolean getIsBusiness() {return isBusiness;} public void setIsBusiness(Boolean isBusiness) {this.isBusiness = isBusiness;}
    public String getIdLegalOwner() {return idLegalOwner;} public void setIdLegalOwner(String idLegalOwner) {this.idLegalOwner = idLegalOwner;}
    public LocalDate getRegistrationDate() {return registrationDate;} public void setRegistrationDate(LocalDate registrationDate) {this.registrationDate = registrationDate;}
    public Integer getQtyDaysAsOwner() {return qtyDaysAsOwner;} public void setQtyDaysAsOwner(Integer qtyDaysAsOwner) {this.qtyDaysAsOwner = qtyDaysAsOwner;}
    public List<PropertyContract> getContracts() {return contracts;} public void setContracts(List<PropertyContract> contracts) {this.contracts = contracts;}
    public void addContract(PropertyContract contract) {this.contracts.add(contract); contract.setOwner(this);}

    @Override public String toString() {return "Owner{id=" + getId() + ", name='" + getName() + "', email='" + getEmail() + "', age=" + getAge() + "}";}
}