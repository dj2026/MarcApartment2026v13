package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.*;
import com.example.MarcApartment.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/apartment")
public class ApartmentRestController {

    @Autowired private ApartmentService apartmentService;

    @GetMapping("/paginated")
    public ResponseEntity<?> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String type) {
        try {
            int sanitizedPage = Math.max(0, page);
            int sanitizedSize = (size <= 0) ? 6 : size;
            Pageable pageable = PageRequest.of(sanitizedPage, sanitizedSize, Sort.by("id").descending());
            Page<Apartment> resultat = apartmentService.findByTypePaginated(type, pageable);
            return ResponseEntity.ok(resultat);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en processar la paginació: " + e.getMessage());
        }
    }

    @GetMapping("/list") public ResponseEntity<?> getListJSON() {try {List<Apartment> apts = apartmentService.findAllWithSchools(); return ResponseEntity.ok(apts);} catch (Exception e) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());}}

    @GetMapping("/getAll")
    public String renderView() {
        try {
            List<Apartment> apts = apartmentService.findAll();
            String htmlContent = apts.stream().map(this::renderCard).collect(Collectors.joining());
            
            return String.format("""
                <!DOCTYPE html><html><head><meta charset='UTF-8'><title>ApartmentRC | PINT APART®</title>
                <link rel='icon' type='image/webp' href='/images/logo.webp'><link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;700;900&display=swap' rel='stylesheet'><link rel='stylesheet' href='/css/main.css'></head>
                <body>
                <div class='logo-fixed'><img src='/images/logo.webp'/></div><div class='main-container'><div class='glass-card'><div class='header'><h1>APARTMENT RC</h1><div class='unit-badge'>TOTAL : %d</div></div><div class='list-container'>%s</div></div><p class='footer-brand'>👤 <span>PINT APART</span> 2026 👤</p>
                </div>
                </body></html> """, apts.size(), htmlContent.isEmpty() ? "<div class='empty'>Sense dades al sistema</div>" : htmlContent);
        } catch (Exception e) {
            return "<html><body><h1>Error</h1><p>" + e.getMessage() + "</p></body></html>";
        }
    }

    private String renderCard(Apartment a) {
        if (a == null) return "";
        String icona = (a.getPropertyType() != null && a.getPropertyType().toLowerCase().contains("duplex")) ? "🏘️" : "🏠";
        String ownerName = "Sense propietari"; if (a.getPropertyContracts() != null && !a.getPropertyContracts().isEmpty()) {PropertyContract c = a.getPropertyContracts().get(0); if (c != null && c.getOwner() != null) {ownerName = c.getOwner().getName();}}
        String schools = (a.getSchools() != null && !a.getSchools().isEmpty()) ? a.getSchools().stream().map(School::getName).collect(Collectors.joining(", ")) : "Cap escola";
        
        return String.format("""
            <div class='item-card'><div class='item-main-content'><div class='id-badge'>ID %d</div><div class='item-text-wrapper'><div class='item-title'>%s %s</div><div class='details-box'><div>👤 <b>Propietari:</b> %s</div><div>🎓 <b>Escola:</b> %s</div>
                </div></div></div></div>""", a.getId(), icona, a.getPropertyType(), ownerName, schools);
    }

    @PostMapping("/nou") public ResponseEntity<Apartment> crearApartment(@RequestBody Apartment a) {Apartment nou = apartmentService.createApartment(a); return new ResponseEntity<>(nou, HttpStatus.CREATED);}
    @PutMapping("/actualitzar/{id}") public ResponseEntity<Apartment> actualitzar(@PathVariable Long id, @RequestBody Apartment a) {try {Apartment reformat = apartmentService.updateApartment(id, a); return new ResponseEntity<>(reformat, HttpStatus.OK);} catch (Exception e) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}}
    @DeleteMapping("/esborrar/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id) {try {apartmentService.deleteById(id); return new ResponseEntity<>(HttpStatus.NO_CONTENT);} catch (Exception e) {return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}}
    @DeleteMapping("/reiniciar") public ResponseEntity<String> reiniciarDades() {try {apartmentService.executeFullReset(); return ResponseEntity.ok("Dades reiniciades correctament");} catch (Exception e) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al fer el reset: " + e.getMessage());}}
    @GetMapping("/filter") public ResponseEntity<?> filter(@RequestParam(required = false) Long minPrice, @RequestParam(required = false) Integer minParking,@RequestParam(required = false) Integer minArea,@RequestParam(required = false) Integer minSchools, @RequestParam(required = false) Integer minReviews,@RequestParam(required = false) String reviewText,@RequestParam(required = false) String reviewerName) {try {List<Apartment> resultat = apartmentService.filterApartments(minPrice, minParking, minArea, minSchools, minReviews,reviewText,reviewerName); return ResponseEntity.ok(resultat);} catch (Exception e) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el filtre: " + e.getMessage());}}
    @PostMapping("/{id}/renovations") public ResponseEntity<?> addRenovation(@PathVariable Long id, @RequestBody Renovation renovation) {try {Apartment apartment = apartmentService.findById(id); if (apartment == null) return ResponseEntity.notFound().build(); renovation.setApartment(apartment); Renovation saved = apartmentService.saveRenovation(renovation);return new ResponseEntity<>(saved, HttpStatus.CREATED);} catch (Exception e) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());}}



}