package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.*;
import com.example.MarcApartment.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escola")
@CrossOrigin(origins = "http://localhost:5173")
public class SchoolRestController {

    @Autowired private SchoolRepository escolaRepo;
    @Autowired private ApartmentRepository aptRepo;

    @PostMapping("/afegir")
    @Transactional
    public ResponseEntity<School> novaEscola(@RequestBody School s) {
        try {if (s.getLogoUrl() == null || s.getLogoUrl().trim().isEmpty()) {s.setLogoUrl("/images/logo.webp");}
            java.util.Set<Apartment> aptsRebuts = s.getApartments(); 
            s.setApartments(null); 
            School escuelaGuardada = escolaRepo.save(s);

            if (aptsRebuts != null && !aptsRebuts.isEmpty()) {
                for (Apartment a : aptsRebuts) {
                    aptRepo.findById(a.getId()).ifPresent(aptReal -> {aptReal.addSchool(escuelaGuardada); aptRepo.save(aptReal);});
                }
            }
            return new ResponseEntity<>(escuelaGuardada, HttpStatus.CREATED);
        } catch (Exception e) {e.printStackTrace(); return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
    }

    @PutMapping("/modificar/{id}")
    @Transactional
    public ResponseEntity<School> modificarEscola(@PathVariable Long id, @RequestBody School d) {
        return escolaRepo.findById(id).map(a -> {a.setName(d.getName()); a.setAddress(d.getAddress());a.setEducationLevel(d.getEducationLevel());a.setSchoolType(d.getSchoolType()); a.setPublic(d.isPublic());a.setLat(d.getLat()); a.setLng(d.getLng());a.setLogoUrl((d.getLogoUrl() != null && !d.getLogoUrl().trim().isEmpty()) ? d.getLogoUrl() : "/images/logo.webp");
            if (d.getApartments() != null) {for (Apartment aptForm : d.getApartments()) {aptRepo.findById(aptForm.getId()).ifPresent(aptReal -> {if (!aptReal.getSchools().contains(a)) {aptReal.addSchool(a);aptRepo.save(aptReal);}});}}
            return ResponseEntity.ok(escolaRepo.save(a));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/llistar") public List<School> llistar() {return escolaRepo.findAll();}

    @Transactional
    @DeleteMapping("/esborrar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return escolaRepo.findById(id).map(escola -> {
            for (Apartment apt : aptRepo.findAll()) {if (apt.getSchools().contains(escola)) {apt.getSchools().remove(escola); aptRepo.save(apt);}}            
            escolaRepo.delete(escola);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}