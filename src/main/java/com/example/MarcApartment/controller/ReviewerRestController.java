package com.example.MarcApartment.controller;
import com.example.MarcApartment.model.Reviewer;
import com.example.MarcApartment.repository.ReviewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviewers")
@CrossOrigin(origins = "*")
public class ReviewerRestController {

    @Autowired private ReviewerRepository reviewerRepo;
    @Autowired private JdbcTemplate jdbcTemplate;

// http://localhost:8080/api/reviewers/run

    @GetMapping("/run") public String llistarCritics() {try {List<Reviewer> critics = reviewerRepo.findAll(); String items = critics.stream().map(this::renderReviewerCard).collect(Collectors.joining()); return buildReviewerPage(critics.size(), items);} catch (Exception e) {return "<h2>Error: " + e.getMessage() + "</h2>";}}

    private String renderReviewerCard(Reviewer r) {
        boolean isActive = r.getExperienceYears() >= 5 || r.getName().equalsIgnoreCase("Tomas");
        String statusClass = isActive ? "is-active" : "is-inactive";
        String statusLabel = isActive ? "ACTIU" : "INACTIU";
        String icona = isActive ? "✍️⭐" : "✍️";

        return String.format("""
            <div class='item-card %s'>
            <div class='item-main-content'>
            <div class='id-badge'>ID %d</div>
            <div class='item-text-wrapper'><div class='item-title'>%s %s</div><div class='item-subtitle'>%s</div>
            <div class='details-box'><div><b>Perfil:</b> %s</div><div><b>Experiència:</b> %d anys</div></div></div></div>
            <div class='status-container'><span class='status-text'>%s</span><div class='status-dot'></div>
            </div></div>""", statusClass, r.getId(), icona, r.getName(), r.getEmail(),r.getDescription() != null ? r.getDescription() : "Sense descripció.",r.getExperienceYears(), statusLabel);
    }

    private String buildReviewerPage(int size, String items) {
        return String.format("""
            <!DOCTYPE html><html><head><meta charset='UTF-8'><title>Reviewers | PINT APART®</title><link href='https://fonts.googleapis.com/css2?family=Inter:wght@bolder&display=swap' rel='stylesheet'>
            <link rel='icon' type='image/webp' href='/images/logo.webp'><link rel='stylesheet' href='/css/reviewer.css'></head><body>
            <div class='logo-fixed'><img src='/images/logo.webp'/></div>
            <div class='main-container'><div class='glass-card'><div class='header'><h1>REVIEWERS PANEL</h1><div class='unit-badge'>TOTAL : %d</div></div>
            <div class='btn-group'><a href='/api/reviewers/crear' class='btn btn-add'>➕ Nou Crític</a><a href='/api/reviewers/revertir' class='btn btn-revert'>🔄 Revertir</a></div>
            <div class='list-container'>%s</div></div>
            <p class='footer-brand'>✍️ <span>PINT APART</span> 2026 ✍️</p></div>
            </body></html>""",size, items.isEmpty() ? "<div class='empty-msg'>No hi ha crítics...</div>" : items);
    }

    @GetMapping("/crear")
    public String crear() {
        Reviewer r = new Reviewer();
        r.setName("Crític " + (reviewerRepo.count() + 1));
        r.setEmail("critic@pintapart.com");
        r.setExperienceYears(2);
        r.setDescription("Nou membre de l'equip.");
        reviewerRepo.save(r);
        return llistarCritics();
    }

    @GetMapping("/revertir") public String revertir() {reviewerRepo.deleteAll(); try { jdbcTemplate.execute("ALTER TABLE PERSONS ALTER COLUMN ID RESTART WITH 2"); } catch (Exception e) { System.out.println("Error en reset d'ID: " + e.getMessage());}
        Reviewer r1 = new Reviewer(); r1.setName("Tomas"); r1.setEmail("tomas@critics.com"); r1.setExperienceYears(10); r1.setDescription("Expert en luxe i anàlisi de mercat."); reviewerRepo.saveAll(List.of(r1));return llistarCritics();
    }

    @PostMapping("/nou") public ResponseEntity<Reviewer> registrarCritic(@RequestBody Reviewer nou) {return new ResponseEntity<>(reviewerRepo.save(nou), HttpStatus.CREATED);}
    @DeleteMapping("/eliminar/{id}") public ResponseEntity<Void> esborrar(@PathVariable String id) {if (reviewerRepo.existsById(id)) {reviewerRepo.deleteById(id);return new ResponseEntity<>(HttpStatus.NO_CONTENT);} return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
}