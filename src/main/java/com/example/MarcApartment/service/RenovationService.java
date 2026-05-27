package com.example.MarcApartment.service;

import com.example.MarcApartment.model.Renovation;
import com.example.MarcApartment.repository.RenovationRepository; // Assegura't que aquest fitxer existeix
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RenovationService {

    private final RenovationRepository renovationRepository;

   @Autowired
public RenovationService(RenovationRepository renovationRepository) { // <--- JPA, no Dynamo
    this.renovationRepository = renovationRepository;
}

    // He canviat el nom a 'save' per coincidir amb el que crides des del Controller
    public Renovation save(Renovation renovation) {
        return renovationRepository.save(renovation); // Mètode estàndard de JPA
    }

    public List<Renovation> findAll() {
        return renovationRepository.findAll(); // Mètode estàndard de JPA
    }
}