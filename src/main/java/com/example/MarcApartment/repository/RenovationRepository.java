package com.example.MarcApartment.repository;

import com.example.MarcApartment.model.Renovation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenovationRepository extends JpaRepository<Renovation, Long> {
}