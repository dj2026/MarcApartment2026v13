classDiagram
    direction TB

%% =====================================================
%% 🟦 PRA02: APPLICATION
%% =====================================================
namespace PRA02_Application {
    class ApartmentPredictorApplication {
        +main(args: String[])
        +run()
        +setupContext()
    }
}

%% =====================================================
%% 🟩 PRA02: CONTROLLERS
%% =====================================================
namespace PRA02_Controllers {
    class ApartmentRestController {
        -ApartmentService service
        +getPredictions()
        +getApartmentById(Long id)
        +createApartment(Apartment a)
        +updateApartment(Long id, Apartment a)
        +deleteApartment(Long id)
    }

    class ReviewRestController {
        -ReviewService service
        +getAllReviews()
        +getReviewByProperty(Long id)
        +postReview(Review r)
    }

    class PopulateDBController {
        -ApartmentService service
        -PopulateDB utils
        +populate()
        +resetDatabase()
        +checkStatus()
    }

    class OwnerRestController {
        -OwnerRepository repository
        +getOwners()
        +getOwnerById(Long id)
    }

    class PropertyContractRestController {
        -PropertyContractRepository repository
        +getContracts()
        +getContractByProperty(Long id)
    }

    class ReviewerRestController {
        -ReviewerRepository repository
        +getReviewers()
    }

    class SchoolRestController {
        -SchoolRepository repository
        +getSchools()
        +getSchoolsNear(double x, double y)
    }
}

%% =====================================================
%% 🟨 PRA02: SERVICES
%% =====================================================
namespace PRA02_Services {
    class ApartmentService {
        -ApartmentRepository repository
        +calculatePrice(Property)
        +saveProperty(Property)
        +validateProperty(Property)
        +findCheapApartments(double price)
        +calculateTax(Property)
    }

    class ReviewService {
        -ReviewRepository repository
        +addReview(Review)
        +getAverageRating(Long id)
        +moderateReview(Long id)
    }
}

%% =====================================================
%% 🟧 PRA01: PERSISTENCE
%% =====================================================
namespace PRA01_Persistence {
    class JpaRepository {
        <<interface>>
        +save(entity)
        +findById(id)
        +findAll()
        +deleteById(id)
    }

    class ApartmentRepository { <<interface>> +findByPriceLessThan(double p) }
    class OwnerRepository { <<interface>> +findByDni(String dni) }
    class PropertyContractRepository { <<interface>> +findByDateAfter(LocalDate d) }
    class ReviewerRepository { <<interface>> }
    class ReviewRepository { <<interface>> +findByRating(int r) }
    class SchoolRepository { <<interface>> +findByType(String t) }
}

%% =====================================================
%% 🟥 PRA01: DOMAIN
%% =====================================================
namespace PRA01_Domain {
    class Person {
        <<abstract>>
        #Long id
        #String name
        #String email
        #String dni
        +getIdentity()
    }

    class Owner {
        -String bankAccount
        +getAccountInfo()
        +validateIBAN()
    }

    class Reviewer {
        -int totalReviews
        -int yearsExperience
        +getRating()
        +incrementReviews()
    }

    class Property {
        <<abstract>>
        #Long id
        #double m2
        #double price
        +calculateM2Price()
        +getFormattedPrice()
    }

    class Apartment {
        - int ID
        - int price
        - int area
        - int bedrooms
        - int bathrooms
        - int stories
        - int mainroad
        - int guestroom
        - int basement
        - int hotwaterheating
        - int airconditioning
        - int parking
        - int prefarea
        - int furnishingstatus
        - boolean hasElevator
        + isPenthouse()
    }

    class Duplex {
        - string balcony
        - string airconditioning
        - string garden
        - int garageQty
        - int roofType
        - boolean elevator
        - boolean terrace
        + hasStairs()
    }

    class House {
        - int garageQty
        - string roofType
        - string garden
        - double gardenSize
        + hasLargeGarden()
    }

    class School {
        # String id
        # String name
        # String type
        # String location
        # int rating
        # boolean isPublic
        + getSchoolInfo()
    }

    class PropertyContract {
        -Long id
        -LocalDate contractdate
        -Double finalPrice
        -double amount
        +isValid()
    }

    class Review {
        -String id
        -String title
        -String content
        -int rating
        LocalDate reviewDate
        -String comment
        +isPositive()
    }
}

%% =====================================================
%% 🟪 UTILS
%% =====================================================
namespace Utils {
    class PopulateDB {
        +loadInitialData()
        +createMockOwners()
        +createMockProperties()
    }

    class PrintingUtils {
        +formatOutput()
        +printHeader()
        +generateReport(Property p)
    }
}

%% =====================================================
%% 🔵 RELACIONS I CONNEXIONS
%% =====================================================
Person <|-- Owner
Person <|-- Reviewer
Property <|-- Apartment
Property <|-- Duplex
Property <|-- House
JpaRepository <|-- ApartmentRepository
JpaRepository <|-- OwnerRepository
JpaRepository <|-- PropertyContractRepository
JpaRepository <|-- ReviewerRepository
JpaRepository <|-- ReviewRepository
JpaRepository <|-- SchoolRepository

Owner "1" -- "0..*" Property : posseeix
Property "1" -- "0..*" Review : rep
Reviewer "1" -- "0..*" Review : escriu
Property "1" -- "1" PropertyContract : posseeix
Apartment "m" -- "n" School : te aprop
Duplex "m" -- "n" School : te aprop
House "m" -- "n" School : te aprop

ApartmentPredictorApplication --> ApartmentRestController
ApartmentRestController --> ApartmentService
ReviewRestController --> ReviewService
PopulateDBController --> ApartmentService
PopulateDBController --> PopulateDB
ApartmentService --> ApartmentRepository
ReviewService --> ReviewRepository
PopulateDB --> ApartmentRepository
OwnerRestController --> OwnerRepository
PropertyContractRestController --> PropertyContractRepository
ReviewerRestController --> ReviewerRepository
SchoolRestController --> SchoolRepository
ApartmentService ..> Property : calculates
PrintingUtils ..> Property : prints

%% =====================================================
%% 🎨 COLORS (STROKE 5PX)
%% =====================================================
style ApartmentPredictorApplication fill:#bbdefb,stroke:#1565c0,stroke-width:5px
style ApartmentRestController fill:#c8e6c9,stroke:#2e7d32,stroke-width:5px
style ReviewRestController fill:#c8e6c9,stroke:#2e7d32,stroke-width:5px
style PopulateDBController fill:#c8e6c9,stroke:#2e7d32,stroke-width:5px
style OwnerRestController fill:#c8e6c9,stroke:#2e7d32,stroke-width:5px
style PropertyContractRestController fill:#c8e6c9,stroke:#2e7d32,stroke-width:5px
style ReviewerRestController fill:#c8e6c9,stroke:#2e7d32,stroke-width:5px
style SchoolRestController fill:#c8e6c9,stroke:#2e7d32,stroke-width:5px
style ApartmentService fill:#fff9c4,stroke:#fbc02d,stroke-width:5px
style ReviewService fill:#fff9c4,stroke:#fbc02d,stroke-width:5px
style JpaRepository fill:#ffe0b2,stroke:#ef6c00,stroke-width:5px
style ApartmentRepository fill:#ffe0b2,stroke:#ef6c00,stroke-width:5px
style OwnerRepository fill:#ffe0b2,stroke:#ef6c00,stroke-width:5px
style PropertyContractRepository fill:#ffe0b2,stroke:#ef6c00,stroke-width:5px
style ReviewerRepository fill:#ffe0b2,stroke:#ef6c00,stroke-width:5px
style ReviewRepository fill:#ffe0b2,stroke:#ef6c00,stroke-width:5px
style SchoolRepository fill:#ffe0b2,stroke:#ef6c00,stroke-width:5px
style Person fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style Owner fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style Reviewer fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style Property fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style Apartment fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style Duplex fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style House fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style School fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style PropertyContract fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style Review fill:#ffcdd2,stroke:#c62828,stroke-width:5px
style PopulateDB fill:#e1bee7,stroke:#6a1b9a,stroke-width:5px
style PrintingUtils fill:#e1bee7,stroke:#6a1b9a,stroke-width:5px