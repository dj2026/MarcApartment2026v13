package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.Renovation;
import com.example.MarcApartment.service.RenovationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/renovation")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class RenovationRestController {

    private final RenovationService service;

    @Autowired
    public RenovationRestController(RenovationService service) {
        this.service = service;
    }

    @PostMapping("/save") 
    public Renovation create(@RequestBody Renovation renovation) {
        try {
            
            System.out.println("Guardant reforma pel pis ID: " + 
                (renovation.getApartment() != null ? renovation.getApartment().getId() : "N/A"));
            
            return service.save(renovation); 
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping
    public List<Renovation> getAll() {
        return service.findAll();
    }
}