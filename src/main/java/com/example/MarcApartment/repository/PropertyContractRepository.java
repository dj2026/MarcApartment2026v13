package com.example.MarcApartment.repository;

import com.example.MarcApartment.model.PropertyContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropertyContractRepository extends JpaRepository<PropertyContract, Long> {@Modifying @Transactional void deleteByApartmentId(Long apartmentId);}