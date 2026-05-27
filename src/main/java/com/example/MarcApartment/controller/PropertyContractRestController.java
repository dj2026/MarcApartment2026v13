package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.PropertyContract;
import com.example.MarcApartment.repository.PropertyContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contracte")
@CrossOrigin(origins = "*")
public class PropertyContractRestController {

    @Autowired private PropertyContractRepository contracteRepo;

    // http://localhost:8080/api/contracte/run
    
    @GetMapping("/run")
    public String runPopulate() {
        try {
            List<PropertyContract> contracts = contracteRepo.findAll();
            String html = contracts.stream().map(this::renderCard).collect(Collectors.joining());
            return String.format("""
                <!DOCTYPE html><html><head><link rel='icon' type='image/webp' href='/images/logo.webp'><meta charset='UTF-8'><title>PropertyC. RC | PINT APART®</title><link href='https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;800&display=swap' rel='stylesheet'><link rel='stylesheet' href='/css/contract.css'></head>
                <body><div class='logo-fixed'><img src='/images/logo.webp'/></div><div class='main-container'><div class='glass-card'><div class='header'><h1>CONTRACTS RC</h1><div class='unit-badge'>TOTAL : %d</div></div><div class='btn-group'><a href='/api/populate/run' class='btn'>🏠 Tornar a Propietats</a></div><div class='list-container'>%s</div></div><p class='footer-brand'>📑 <span>PINT APART</span> 2026 📑</p></div></body></html>""", 
                contracts.size(), html.isEmpty() ? "<div class='empty-msg'>Sense contractes al sistema...</div>" : html);
        } catch (Exception e) { return "<h2>Error: " + e.getMessage() + "</h2>"; }
    }

   private String renderCard(PropertyContract c) {
    boolean active = c.getId() != null && c.getId() <= 3;
    String detallsRaw = c.getContractDetails() != null ? c.getContractDetails() : "Sense detalls";
    String detallsNets = detallsRaw.replace("Contracte: ", "").trim();
    String icona = detallsNets.toLowerCase().contains("duplex") ? "📑" : "📑";
    
    return String.format("""
        <div class='item-card %s'><div class='item-main-content'>
            <div class='id-badge'>ID %d</div><div class='item-text-wrapper'><div class='item-title'>%s %s</div><div class='item-subtitle'>Contracte al sistema</div></div></div>
            <div class='status-container'><span class='status-text'>%s</span><div class='status-dot'></div></div>
        </div>""", active ? "is-active" : "is-inactive", c.getId(), icona, detallsNets, active ? "VIGENT" : "FINALITZAT");
    }

    @GetMapping("/llistar") public List<PropertyContract> obtenirTots() {return contracteRepo.findAll();}
    @GetMapping("/detalls/{id}") public ResponseEntity<PropertyContract> buscarPerId(@PathVariable Long id) {return contracteRepo.findById(id).map(c -> new ResponseEntity<>(c, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));}
    @PostMapping("/nou") public ResponseEntity<PropertyContract> crearContracte(@RequestBody PropertyContract nouContracte) {try {PropertyContract guardat = contracteRepo.save(nouContracte); return new ResponseEntity<>(guardat, HttpStatus.CREATED);} catch (Exception e) {return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}}
    @DeleteMapping("/eliminar/{id}") public ResponseEntity<Void> eliminarContracte(@PathVariable Long id) {if (contracteRepo.existsById(id)) {contracteRepo.deleteById(id); return new ResponseEntity<>(HttpStatus.NO_CONTENT);}return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
}