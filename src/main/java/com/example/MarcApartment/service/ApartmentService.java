package com.example.MarcApartment.service;
import com.example.MarcApartment.model.*;
import com.example.MarcApartment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Service
public class ApartmentService {

    @Autowired private ApartmentRepository aptRepo;
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private SchoolRepository schoolRepo;
    @Autowired private PropertyContractRepository contractRepo;
    @Autowired private EntityManager em;
    @Autowired private RenovationRepository renovationRepo;

    @Transactional(readOnly = true)
    public Apartment findById(Long id) {return aptRepo.findById(id).orElse(null);}

    @Transactional(readOnly = true)
    public List<Apartment> filterApartments(Long minPrice, Integer minParking, Integer minArea, Integer minSchools, Integer minReviews, String reviewText, String reviewerName) {
        Specification<Apartment> spec = ApartmentSpecification.filterBy(minPrice, minParking, minArea, minSchools, minReviews, reviewText, reviewerName);
        return aptRepo.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Page<Apartment> findByTypePaginated(String type, Pageable pageable) {
        if (type == null || type.trim().isEmpty()) { return aptRepo.findAll(pageable);}
        return aptRepo.findByPropertyTypeContainingIgnoreCase(type, pageable);
    }

    @Transactional(readOnly = true)
    public List<Apartment> findAll() {return aptRepo.findAll();}

    @Transactional(readOnly = true)
    public List<Apartment> findAllWithSchools() {return aptRepo.findAllWithSchools();}

    @Transactional 
    public Apartment createApartment(Apartment apt) {
        if (apt == null) throw new IllegalArgumentException("L'apartament no pot ser nul");
        if (apt.getPrice() == null || apt.getPrice() == 0) { apt.calcularPreuAutomatic(); } 
        return aptRepo.saveAndFlush(apt);
    }

    @Transactional
    public Apartment updateApartment(Long id, Apartment dadesNoves) {
        if (id == null || dadesNoves == null) throw new IllegalArgumentException("ID o dades nules");
        return aptRepo.findById(id).map(apt -> {
            apt.setPropertyType(dadesNoves.getPropertyType()); 
            apt.setDescription(dadesNoves.getDescription());
            apt.setArea(dadesNoves.getArea()); 
            apt.setBedrooms(dadesNoves.getBedrooms());
            apt.setBathrooms(dadesNoves.getBathrooms()); 
            apt.setStories(dadesNoves.getStories());
            apt.setParking(dadesNoves.getParking()); 
            apt.setMainroad(dadesNoves.getMainroad());
            apt.setGuestroom(dadesNoves.getGuestroom());
            apt.setBasement(dadesNoves.getBasement()); 
            apt.setHotwaterheating(dadesNoves.getHotwaterheating());
            apt.setAirconditioning(dadesNoves.getAirconditioning());
            apt.setTerrace(dadesNoves.getTerrace()); 
            apt.setPrefarea(dadesNoves.getPrefarea());
            apt.setFurnishingstatus(dadesNoves.getFurnishingstatus());
            apt.setLat(dadesNoves.getLat());
            apt.setLng(dadesNoves.getLng());
            if (dadesNoves.getSchools() != null) {apt.getSchools().clear(); apt.getSchools().addAll(dadesNoves.getSchools());}
            if (apt instanceof House && dadesNoves instanceof House) {((House) apt).setYardSize(((House) dadesNoves).getYardSize()); ((House) apt).setPool(((House) dadesNoves).getPool());} 
            else if (apt instanceof Duplex && dadesNoves instanceof Duplex) {((Duplex) apt).setBalcony(((Duplex) dadesNoves).getBalcony()); ((Duplex) apt).setElevator(((Duplex) dadesNoves).getElevator());}
            if (dadesNoves.getPrice() != null && dadesNoves.getPrice() > 0) {apt.setPrice(dadesNoves.getPrice());} else {apt.calcularPreuAutomatic();}
            return aptRepo.saveAndFlush(apt); 
        }).orElseThrow(() -> new RuntimeException("No he trobat l'ID: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (id == null) return;
        Apartment a = aptRepo.findById(id).orElseThrow(() -> new RuntimeException("L'ID " + id + " no existeix."));
        em.createNativeQuery("DELETE FROM reviews WHERE apartment_id = ?1").setParameter(1, id).executeUpdate();
        em.createNativeQuery("DELETE FROM renovations WHERE apartment_id = ?1").setParameter(1, id).executeUpdate();
        
        contractRepo.deleteByApartmentId(id);
        if (a.getSchools() != null) {a.getSchools().clear(); aptRepo.saveAndFlush(a);}
        aptRepo.delete(a);
        aptRepo.flush();
    }

    @Transactional
    public Renovation saveRenovation(Renovation renovation) {
        if (renovation == null) throw new IllegalArgumentException("La reforma no pot ser nula");
        return renovationRepo.save(renovation);
    }

    @Transactional
    public void executeFullReset() {
        try {
            em.createNativeQuery("DELETE FROM renovations").executeUpdate();
            em.createNativeQuery("DELETE FROM reviews").executeUpdate();
            em.createNativeQuery("DELETE FROM property_contracts").executeUpdate();
            em.createNativeQuery("DELETE FROM apartment_school").executeUpdate();
            em.createNativeQuery("DELETE FROM apartments").executeUpdate();
            em.createNativeQuery("DELETE FROM schools").executeUpdate();
            em.createNativeQuery("DELETE FROM persons").executeUpdate();
            
            String[] tables = {"apartments", "schools", "persons", "reviews", "property_contracts", "renovations"};
            for (String table : tables) {
                try { em.createNativeQuery("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1").executeUpdate(); } catch (Exception e){}
            }
        } catch (Exception e) { 
            System.err.println("Error crític en el reset: " + e.getMessage()); 
        }
        em.flush(); em.clear();

        School s1 = schoolRepo.save(new School(null, "Escola Gravi", "Concertada", "C/Jerico, 5", "Tots els nivells", true, null, null, 41.4185, 2.1420));
        School s2 = schoolRepo.save(new School(null, "Escola Palcam", "Concertada", "C/Castillejos, 361", "Tots els nivells", true, null, null, 41.410943, 2.172281));
        School s3 = schoolRepo.save(new School(null, "Escola Paideia", "Privada", "C/Montnegre, 20", "Educació Especial", false, null, null, 41.387781, 2.139475));
        
        Owner marc = ownerRepo.save(new Owner("Marc", "marc@apartments.com","INITIAL_LOAD", 29, true, false, "12345678X", LocalDate.now(), 100));

        Apartment apt = new Apartment(null, "Apartment", 0L, 1100, 2, 2, 1, "yes", "no", "no", "no", "no", "yes", 2, "yes", "furnished", "Aquest apartament presenta un saló de luxe d'estètica contemporània i minimalista...", marc);
        apt.setLat(41.417685); apt.setLng(2.140027); apt.addSchool(s1); guardarTot(apt, marc);

        Duplex dup = new Duplex(null, "Duplex", 0L, 1820, 3, 2, 2, "yes", "yes", "no", "no", "yes", "yes", 2, "no", "semi-furnished", "Duplex amb saló de luxe d'estil industrial-modern...", "yes", "yes", marc);
        dup.setLat(41.41091); dup.setLng(2.168365); dup.addSchool(s2); guardarTot(dup, marc);

        House casa = new House(null, "House", 0L, 1530, 4, 3, 2, "yes", "no", "yes", "no", "yes", "no", 1, "no", "unfurnished", "Casa que connecta l'interior amb un jardí i piscina...", 160, "yes", marc);
        casa.setLat(41.38794); casa.setLng(2.136245); casa.addSchool(s3); guardarTot(casa, marc);
        System.out.println("🔄 DB Reset completada correctament.");
    }

    private void guardarTot(Apartment apt, Owner o) {
        if (apt == null || o == null) return;
        if (apt.getPrice() == null || apt.getPrice() <= 0) { apt.calcularPreuAutomatic(); }       
        Owner ownerManaged = em.merge(o);
        Apartment guardat = aptRepo.saveAndFlush(apt); 
        PropertyContract contracte = new PropertyContract(ownerManaged, guardat, "Contracte " + guardat.getPropertyType(), LocalDate.now(), (double)guardat.getPrice());
        contractRepo.saveAndFlush(contracte); 
    }
}