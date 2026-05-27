package com.example.MarcApartment.repository;

import com.example.MarcApartment.model.Apartment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long>, JpaSpecificationExecutor<Apartment> {

    Page<Apartment> findByPropertyTypeContainingIgnoreCase(String type, Pageable pageable);
    List<Apartment> findByPriceBetween(Long minPrice, Long maxPrice);
    List<Apartment> findByBedrooms(Integer bedrooms);
    List<Apartment> findByBedroomsAndPriceBetweenOrderByPriceAsc(Integer bedrooms, Long minPrice, Long maxPrice);

    @Query("SELECT DISTINCT a FROM Apartment a LEFT JOIN FETCH a.schools") List<Apartment> findAllWithSchools();

    @Modifying @Transactional @Query(value = "DELETE FROM apartments", nativeQuery = true) void cleanTable();
    @Modifying @Transactional @Query(value = "ALTER TABLE apartments ALTER COLUMN id RESTART WITH 1", nativeQuery = true) void resetIdCounter();
}