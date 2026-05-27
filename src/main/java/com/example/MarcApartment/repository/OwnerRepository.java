package com.example.MarcApartment.repository;
import com.example.MarcApartment.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, String> {}