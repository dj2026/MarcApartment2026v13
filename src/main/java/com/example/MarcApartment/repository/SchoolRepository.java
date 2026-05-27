package com.example.MarcApartment.repository;

import com.example.MarcApartment.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    @Modifying @Transactional @Query(value = "ALTER TABLE schools AUTO_INCREMENT = 4", nativeQuery = true) void resetAutoIncrement();
}