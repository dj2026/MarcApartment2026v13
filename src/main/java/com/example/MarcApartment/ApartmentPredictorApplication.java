package com.example.MarcApartment;

import com.example.MarcApartment.model.*;
import com.example.MarcApartment.repository.*;
import com.example.MarcApartment.service.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@SpringBootApplication
public class ApartmentPredictorApplication implements CommandLineRunner {

    @Autowired private ApartmentRepository aptRepo; 
    @Autowired private SchoolRepository schoolRepo;
    @Autowired private PropertyContractRepository contractRepo;
    
    @Autowired private PersonService personService;

    public static void main(String[] args) {SpringApplication.run(ApartmentPredictorApplication.class, args);}

    @Override
    public void run(String... args) throws Exception {
        long totalApartments = aptRepo.count();
        if (totalApartments == 0) {System.out.println("⚠️ Base de dades buida. Carregant dades per defecte...");
            carregarDadesInicials();} else {System.out.println("✅ S'han trobat " + totalApartments + " Apartments a H2. No esborrem res.");}
    }

    @Transactional
    public void carregarDadesInicials() {
        School s1 = schoolRepo.save(new School(null, "Escola Gravi", "Concertada", "C/Jerico, 5", "Tots els nivells", true, null, null, 41.4185, 2.1420));
        School s2 = schoolRepo.save(new School(null, "Escola Palcam", "Concertada", "C/Castillejos, 361", "Tots els nivells", true, null, null, 41.410943, 2.172281));
        School s3 = schoolRepo.save(new School(null, "Escola Paideia", "Privada", "C/Montnegre, 20", "Educació Especial", false, null, null, 41.387781, 2.139475));

        Owner marc = new Owner("OWNER-MARC-001", "marc@apartments.com", "INITIAL_LOAD", 30, true, false, "12345678X", LocalDate.now(), 100);
        marc.setId("OWNER-MARC-001");
        marc = personService.saveOwner(marc); 

        Apartment apt = new Apartment(null, "Apartment", 0L, 1010, 2, 1, 1, "yes", "no", "no", "no", "no", "no", 1, "no", "furnished", "Aquest apartament presenta un saló de luxe d'estètica contemporània i minimalista, que comparteix la vista panoràmica de la ciutat al capvespre però amb un disseny més lluminós i suau que l'anterior.", marc);
        apt.setLat(41.417685); apt.setLng(2.140027); 
        prepararISalvar(apt, s1, marc);

        Duplex dup = new Duplex(null, "Duplex", 0L, 1820, 3, 2, 2, "yes", "yes", "no", "no", "yes", "yes", 2, "no", "semi-furnished", "Duplex amb saló de luxe d'estil industrial-modern situat en un àtic amb unes vistes espectaculars. L'espai destaca per la seva gran amplitud, sostres de doble alçada i una connexió total amb l'exterior.", "yes", "yes", marc);
        dup.setLat(41.41091); dup.setLng(2.168365);
        prepararISalvar(dup, s2, marc);

        House hou = new House(null, "House", 0L, 1530, 4, 3, 2, "yes", "no", "yes", "no", "yes", "no", 1, "no", "unfurnished", "Casa que connecta l'interior amb un jardí i piscina, destacant pel seu sostre de fusta amb línies LED i parets de pedra natural.", 160, "yes", marc);
        hou.setLat(41.38794); hou.setLng(2.136245); 
        prepararISalvar(hou, s3, marc);

        imprimirResumFinal();
    }

    private void prepararISalvar(Apartment apt, School school, Owner owner) {
        apt.addSchool(school);
        apt.calcularPreuAutomatic();
        Apartment savedApt = aptRepo.save(apt); 
        PropertyContract contracte = new PropertyContract(owner, savedApt, "Contracte " + savedApt.getPropertyType(), LocalDate.now(), (double)savedApt.getPrice());
        contractRepo.save(contracte);
    }

    private void imprimirResumFinal() {
        System.out.println("\n========================================================== ");
        System.out.println("                DADES CARREGADES AMB ÈXIT                   ");
        System.out.println("============================================================ ");
    }
}