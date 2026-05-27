package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.Review;
import com.example.MarcApartment.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})public class ReviewRestController {

    @Autowired private ReviewRepository reviewRepo;

    @GetMapping("/run")
    public String renderReviews() {
        try {
            List<Review> reviews = reviewRepo.findAll();
            String html = reviews.stream().map(this::renderReviewCard).collect(Collectors.joining());
            return String.format("""
                <!DOCTYPE html><html><head><meta charset='UTF-8'><title>Reviews | PINT APART®</title><link rel='icon' type='image/webp' href='/images/logo.webp'>
                <link href='https://fonts.googleapis.com/css2?family=Inter:wght@bolder&display=swap' rel='stylesheet'><link rel='stylesheet' href='/css/review.css'></head>
                <body>
                    <div class='logo-fixed'><img src='/images/logo.webp'/></div>
                    <div class='main-container'><div class='glass-card'><div class='header'><h1>VALORACIONS</h1><div class='unit-badge'>TOTAL : %d</div></div><div class='list-container'>%s</div></div>
                    <p class='footer-brand'>💬 <span>PINT APART</span> 2026 💬</p></div>
                </body></html>""", reviews.size(), html.isEmpty() ? "<div class='empty'>Sense valoracions encara...</div>" : html);
        } catch (Exception e) { return "Error: " + e.getMessage(); }
    }

    private String renderReviewCard(Review r) {
        int ratingSegur = (r.getRating() != null) ? r.getRating() : 0;
        String estrelles = "⭐".repeat(Math.max(0, ratingSegur));
        
        String reviewerName = (r.getReviewer() != null) ? r.getReviewer().getName() : "Anònim";
        String propertyName = (r.getApartment() != null) ? r.getApartment().getPropertyType() : "Propietat";

        return String.format("""
            <div class='item-card'>
                <div class='item-main-content'>
                    <div class='id-badge'>%d/5</div><div class='item-text-wrapper'><div class='item-title'>%s</div><div class='details-box'><div>👤 <b>Autor:</b> %s</div><div>🏠 <b>Propietat:</b> %s</div><div>📝 <b>"%s"</b></div></div>
                </div></div>
                <div class='status-container'><span class='status-text'>%s</span></div>
            </div>""", ratingSegur, r.getTitle(), reviewerName, propertyName, r.getComment(), estrelles);
    }

    @GetMapping("/all") public List<Review> llistarReviews() {return reviewRepo.findAll();}
    @PostMapping("/save") public ResponseEntity<?> guardarReview(@RequestBody Review r) {
        try {if (r.getDate() == null) {r.setDate(java.time.LocalDate.now());} if (r.getRating() == null) {r.setRating(5);}
            Review nova = reviewRepo.save(r); return new ResponseEntity<>(nova, HttpStatus.CREATED);
        } catch (Exception e) {e.printStackTrace(); return new ResponseEntity<>("Error en guardar: " + e.getMessage(), HttpStatus.BAD_REQUEST);}
    }
    @DeleteMapping("/del/{id}") public ResponseEntity<String> esborrar(@PathVariable Long id) {if (reviewRepo.existsById(id)) {reviewRepo.deleteById(id); return new ResponseEntity<>("Review eliminada", HttpStatus.OK);} return new ResponseEntity<>("No s'ha trobat la review", HttpStatus.NOT_FOUND);} 
}