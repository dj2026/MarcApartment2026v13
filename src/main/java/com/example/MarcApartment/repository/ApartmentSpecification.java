package com.example.MarcApartment.repository;

import com.example.MarcApartment.model.Apartment;
import com.example.MarcApartment.model.Review;
import com.example.MarcApartment.model.Reviewer;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class ApartmentSpecification {

    public static Specification<Apartment> filterBy(
        Long minPrice,Integer minParking, Integer minArea,Integer minSchools,Integer minReviews,String reviewText,String reviewerName) {
    return (Root<Apartment> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {Predicate p = cb.conjunction();
        if (minPrice != null && minPrice > 0) {p = cb.and(p, cb.greaterThanOrEqualTo(root.get("price"), minPrice));}
        if (minParking != null && minParking > 0) {p = cb.and(p, cb.greaterThanOrEqualTo(root.get("parking"), minParking));}
        if (minArea != null && minArea > 0) {p = cb.and(p, cb.greaterThanOrEqualTo(root.get("area"), minArea));}
        if (minSchools != null && minSchools > 0) {p = cb.and(p, cb.greaterThanOrEqualTo(cb.size(root.get("schools")), minSchools));}
        if (minReviews != null && minReviews > 0) {p = cb.and(p, cb.greaterThanOrEqualTo(cb.size(root.get("reviews")), minReviews));}
        if (reviewText != null && !reviewText.trim().isEmpty()) {Join<Object, Object> reviewJoin = root.join("reviews", JoinType.LEFT);
            p = cb.and(p,cb.like(cb.lower(reviewJoin.get("comment")),"%" + reviewText.toLowerCase() + "%")); query.distinct(true);
        if (reviewerName != null && !reviewerName.trim().isEmpty()) {Join<Review, Reviewer> reviewerJoin = reviewJoin.join("reviewer", JoinType.LEFT);p = cb.and(p, cb.like(cb.lower(reviewerJoin.get("name")), "%" + reviewerName.toLowerCase() + "%"));}}

        return p;
    };
}
}