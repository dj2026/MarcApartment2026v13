package com.example.MarcApartment.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reviewers")
public class Reviewer extends Person {

    @Column(name = "description", length = 1000) private String description;
    @Column(name = "experience_years") private Integer experienceYears;
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL) @JsonIgnore  private List<Review> reviews = new ArrayList<>();

    public Reviewer() {super();}
    public Reviewer(String name, String email, String operation, String description, Integer experienceYears) {super(name, email, operation); this.description = description; this.experienceYears = (experienceYears != null) ? experienceYears : 0;}

    public String getDescription() {return description;}  public void setDescription(String description) {this.description = description;}
    public Integer getExperienceYears() {return experienceYears;} public void setExperienceYears(Integer experienceYears) {this.experienceYears = (experienceYears != null) ? experienceYears : 0;}
    public List<Review> getReviews() {return reviews;} public void setReviews(List<Review> reviews) {this.reviews = reviews;} 
    public void addReview(Review review) {this.reviews.add(review); review.setReviewer(this);}
}