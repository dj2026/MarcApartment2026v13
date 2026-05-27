package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.Apartment;
import com.example.MarcApartment.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")

public class ApartmentQueriesRestController {

    @Autowired  private ApartmentRepository apartmentRepository;

    @GetMapping("/price-range") public List<Apartment> findByPriceRange(@RequestParam Long minPrice, @RequestParam Long maxPrice) {return apartmentRepository.findByPriceBetween(minPrice, maxPrice);}
    @GetMapping("/bedrooms")  public List<Apartment> findByBedrooms(@RequestParam Integer bedrooms) {return apartmentRepository.findByBedrooms(bedrooms);}
    @GetMapping("/bedrooms-and-price-between-asc") public List<Apartment> findByBedroomsAndPriceBetween(@RequestParam Integer bedrooms, @RequestParam Long minPrice, @RequestParam Long maxPrice) {
        
        return apartmentRepository.findByBedroomsAndPriceBetweenOrderByPriceAsc(bedrooms, minPrice, maxPrice);
    }
}