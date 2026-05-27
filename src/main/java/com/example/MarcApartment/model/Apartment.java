package com.example.MarcApartment.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="apartments")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="apartment_type")
@DiscriminatorValue("APARTMENT")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "propertyType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Apartment.class, name = "APARTMENT"), 
    @JsonSubTypes.Type(value = Duplex.class, name = "DUPLEX"), 
    @JsonSubTypes.Type(value = House.class, name = "HOUSE")
})
public class Apartment {

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String propertyType; 
    private Long price; 
    private Integer area; 
    private Integer bedrooms; 
    private Integer bathrooms; 
    private Integer stories; 
    private Integer parking; 
    private String mainroad; 
    private String guestroom; 
    private String basement; 
    private String hotwaterheating;
    private String airconditioning; 
    private String terrace; 
    private String prefarea; 
    private String furnishingstatus;
    private Double lat; 
    private Double lng;
    
    @Column(length=1000) 
    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id") 
    @JsonIgnoreProperties({"contracts", "qtyDaysAsOwner", "registrationDate", "isActive", "isBusiness", "idLegalOwner"}) 
    private Owner owner;

    @OneToMany(mappedBy="apartment", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER) 
    @JsonIgnoreProperties("apartment") 
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy="apartment", cascade=CascadeType.ALL, orphanRemoval=true) 
    @JsonIgnoreProperties("apartment") 
    private List<PropertyContract> propertyContracts = new ArrayList<>();

    // NOVA LLISTA DE REFORMES
    @OneToMany(mappedBy="apartment", cascade=CascadeType.ALL, orphanRemoval=true) 
    @JsonIgnoreProperties("apartment") 
    private List<Renovation> renovations = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER) 
    @JoinTable(name = "apartment_school", joinColumns = @JoinColumn(name = "apartment_id"), inverseJoinColumns = @JoinColumn(name = "school_id"))
    @JsonIgnoreProperties("apartments") 
    private Set<School> schools = new HashSet<>();

    public Apartment(){}
    
    public Apartment(Long id, String propertyType, Long price, Integer area, Integer bedrooms, Integer bathrooms, Integer stories, String mainroad, String guestroom, String basement, String hotwater, String aircon, String terrace, Integer parking, String prefarea, String furnish, String description, Owner owner){
        this.id = id; this.propertyType = propertyType; this.price = price; this.area = area; this.bedrooms = bedrooms; this.bathrooms = bathrooms; this.stories = stories; this.mainroad = mainroad; this.guestroom = guestroom; this.basement = basement; this.hotwaterheating = hotwater;this.airconditioning = aircon; this.terrace = terrace; this.parking = parking; this.prefarea = prefarea;this.furnishingstatus = furnish; this.description = description;this.owner = owner; 
    }

    // Getters i Setters Estàndard
    public Long getId(){return id;} public void setId(Long id){this.id = id;}
    public Owner getOwner(){return owner;} public void setOwner(Owner owner){this.owner = owner;}
    public List<Review> getReviews(){return reviews;}  public void setReviews(List<Review> r){this.reviews = (r != null) ? r : new ArrayList<>();}
    public List<PropertyContract> getPropertyContracts(){return propertyContracts;} public void setPropertyContracts(List<PropertyContract> pc){this.propertyContracts = (pc != null) ? pc : new ArrayList<>();}
    public Set<School> getSchools(){return schools;} public void setSchools(Set<School> s){this.schools = (s != null) ? s : new HashSet<>();}
    
    // Getter i Setter per Renovations
    public List<Renovation> getRenovations() { return renovations; }
    public void setRenovations(List<Renovation> r) { this.renovations = (r != null) ? r : new ArrayList<>(); }

    public String getDescription(){return description;}public void setDescription(String d){this.description = d;}
    public String getPropertyType(){return propertyType;} public void setPropertyType(String pt){this.propertyType = pt;}
    public Long getPrice(){return price;} public void setPrice(Long price){this.price = price;}
    public Integer getArea(){return area;} public void setArea(Integer area){this.area = area;}
    public Integer getBedrooms(){return bedrooms;} public void setBedrooms(Integer b){this.bedrooms = b;}
    public Integer getBathrooms(){return bathrooms;} public void setBathrooms(Integer b){this.bathrooms = b;}
    public Integer getStories(){return stories;} public void setStories(Integer s){this.stories = s;}
    public Integer getParking(){return parking;} public void setParking(Integer p){this.parking = p;}
    public String getMainroad(){return mainroad;} public void setMainroad(String m){this.mainroad = m;}
    public String getGuestroom(){return guestroom;} public void setGuestroom(String g){this.guestroom = g;}
    public String getBasement(){return basement;} public void setBasement(String b){this.basement = b;}    
    public String getHotwaterheating(){return hotwaterheating;} public void setHotwaterheating(String h){this.hotwaterheating = h;}
    public String getAirconditioning(){return airconditioning;} public void setAirconditioning(String a){this.airconditioning = a;}
    public String getTerrace(){return terrace;} public void setTerrace(String t){this.terrace = t;}
    public String getPrefarea(){return prefarea;} public void setPrefarea(String p){this.prefarea = p;}    
    public String getFurnishingstatus(){return furnishingstatus;} public void setFurnishingstatus(String f){this.furnishingstatus = f;}
    public Double getLat(){return lat;} public void setLat(Double lat){this.lat = lat;}
    public Double getLng(){return lng;} public void setLng(Double lng){this.lng = lng;}

    @PrePersist @PreUpdate
    public void validateAndShield(){
        if (this.propertyType == null || this.propertyType.isEmpty()) this.propertyType = "APARTMENT";
        if (this.area == null || this.area <= 0) this.area = 75;
        if (this.price == null || this.price <= 0L) this.calcularPreuAutomatic();
        if (this.description == null) this.description = "Nova propietat " + this.propertyType;
    }

    public void calcularPreuAutomatic(){
        int areaSegura = (this.area == null) ? 75 : this.area;
        long c = (long) areaSegura * 2000L;
        c += ((this.bedrooms != null ? this.bedrooms : 0) * 1500L);
        c += ((this.bathrooms != null ? this.bathrooms : 0) * 1000L);
        this.price = c;
    }

    @Transient @JsonProperty("reviewCount") public int getReviewCount(){return (this.reviews != null) ? this.reviews.size() : 0;}

    @Transient @JsonProperty("averageRating")
    public String getAverageRating(){
        if (this.reviews == null || this.reviews.isEmpty()) return "0/5";
        double sum = 0; 
        for (Review r : this.reviews) sum += (r.getRating() != null ? r.getRating() : 0);
        return String.format("%.1f/5", sum / this.reviews.size());
    }

    public void addSchool(School school){if (this.schools == null) this.schools = new HashSet<>(); this.schools.add(school);}
    public void addPropertyContract(PropertyContract contract) {if (this.propertyContracts == null) this.propertyContracts = new ArrayList<>(); this.propertyContracts.add(contract); contract.setApartment(this);}
    public void addRenovation(Renovation renovation) {
        if (this.renovations == null) this.renovations = new ArrayList<>();
        this.renovations.add(renovation);
        renovation.setApartment(this);
    }
}