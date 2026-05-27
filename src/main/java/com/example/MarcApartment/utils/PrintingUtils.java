package com.example.MarcApartment.utils;
import com.example.MarcApartment.model.Apartment;
import com.example.MarcApartment.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PrintingUtils {

    @Autowired private ApartmentRepository apartmentRepository;

    public static void printList(List<?> list) {if (list == null) return;list.forEach(System.out::println);}
    public static void printList(List<?> list, String title) {System.out.println("\n=== " + title + " ===");printList(list);}
    public static void printObjectsList(Iterable<?> list, String title) {System.out.println("\n=== " + title + " ==="); int index = 0; for (Object obj : list) {index++;System.out.println("#" + index + ": " + obj);}}

    public void printApartmentsFromDb() {
        System.out.println("\n=== Apartments in the Database ===");
        List<Apartment> apartments = apartmentRepository.findAll();
        if (apartments.isEmpty()) {System.out.println("La base de dades està buida.");} else {int index = 0;
            for (Apartment apt : apartments) {index++;System.out.println("#" + index + " [ID: " + apt.getId() + "] -> " + apt.getPropertyType() + " - Preu: " + apt.getPrice());}
        }
    }
}