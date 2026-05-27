package com.example.MarcApartment.repository;

import com.example.MarcApartment.model.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository public interface ReviewerRepository extends JpaRepository<Reviewer, String> {}