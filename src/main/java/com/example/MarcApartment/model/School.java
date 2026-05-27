package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "schools")
@JsonIgnoreProperties(ignoreUnknown = true)
public class School {

    @Id
    @SequenceGenerator(name = "school_id_seq", sequenceName = "school_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_id_seq")
    private Long id;

    private String name; private String schoolType; private String address; private String educationLevel;

    @Column(name = "is_public") @JsonProperty("isPublic") private Boolean isPublic;

    private String logoUrl;private String web;private Double lat;private Double lng;

    @ManyToMany(mappedBy = "schools", fetch = FetchType.EAGER) @JsonIgnoreProperties("schools") private Set<Apartment> apartments = new HashSet<>();

    public School() {}
    public School(Long id, String name, String schoolType, String address, String educationLevel, Boolean isPublic, String logoUrl, String web, Double lat, Double lng) {this.id = id;this.name = name;this.schoolType = schoolType;this.address = address;this.educationLevel = educationLevel;this.isPublic = isPublic;this.logoUrl = logoUrl;this.web = web;this.lat = lat;this.lng = lng;}

    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getName() {return name;} public void setName(String name) {this.name = name;}
    public String getSchoolType() {return schoolType;} public void setSchoolType(String schoolType) {this.schoolType = schoolType;}
    public String getAddress() {return address;} public void setAddress(String address) {this.address = address;}
    public String getEducationLevel() {return educationLevel;} public void setEducationLevel(String educationLevel) {this.educationLevel = educationLevel;}
    public Boolean isPublic() {return isPublic;} public void setPublic(Boolean isPublic) {this.isPublic = isPublic;}
    public String getLogoUrl() {return logoUrl;} public void setLogoUrl(String logoUrl) {this.logoUrl = logoUrl;}
    public String getWeb() {return web;} public void setWeb(String web) {this.web = web;}
    public Double getLat() {return lat;} public void setLat(Double lat) {this.lat = lat;}
    public Double getLng() {return lng;} public void setLng(Double lng) {this.lng = lng;}
    public Set<Apartment> getApartments() {return apartments;} public void setApartments(Set<Apartment> apartments) {this.apartments = apartments;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return Objects.equals(id, school.id);
    }

    @Override public int hashCode() {return Objects.hash(id);}

    @PreRemove
    private void removeApartmentsFromSchool() {if (apartments != null) {for (Apartment apartment : apartments) {if (apartment.getSchools() != null) {apartment.getSchools().remove(this);}}}}
}