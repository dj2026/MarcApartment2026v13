package com.example.MarcApartment.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String title;
    @Column(name = "comment", length = 1000) private String comment;
    private Integer rating; private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST}) @JoinColumn(name = "reviewer_id") @JsonIgnoreProperties({"reviews", "hibernateLazyInitializer", "handler"})  private Reviewer reviewer;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "apartment_id")  @JsonIgnoreProperties({"reviews", "propertyContracts", "renovations", "hibernateLazyInitializer", "handler"})  private Apartment apartment;
    public Review() {}
    
    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;} public void setTitle(String title) {this.title = title;}
    public String getComment() {return comment;} public void setComment(String comment) {this.comment = comment;}
    public Integer getRating() {return (rating == null) ? 0 : rating;} public void setRating(Integer rating) {this.rating = rating;}
    public LocalDate getDate() {return date;} public void setDate(LocalDate date) {this.date = date;}
    public Reviewer getReviewer() {return reviewer;} public void setReviewer(Reviewer reviewer) {this.reviewer = reviewer;}
    public Apartment getApartment() {return apartment;} public void setApartment(Apartment apartment) {this.apartment = apartment;}
}