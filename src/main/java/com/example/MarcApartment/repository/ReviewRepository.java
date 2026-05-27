package com.example.MarcApartment.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MarcApartment.model.Review;
public interface ReviewRepository extends JpaRepository<Review, Long> {}