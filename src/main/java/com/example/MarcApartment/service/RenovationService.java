package com.example.MarcApartment.service;

import com.example.MarcApartment.model.Renovation;
import com.example.MarcApartment.repository.RenovationRepository; // Assegura't que aquest fitxer existeix
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RenovationService {

    private final RenovationRepository renovationRepository;

   @Autowired
public RenovationService(RenovationRepository renovationRepository) { // <--- JPA, no Dynamo
    this.renovationRepository = renovationRepository;
}

    @Transactional
    public Renovation save(Renovation renovation) {
        return renovationRepository.save(renovation);
    }

    @Transactional(readOnly = true)
    public List<Renovation> findAll() {
        return renovationRepository.findAll();
    }
}