# 📑 Memòria de Pràctica: PRA02 — PINT APART 2026

## 1. 🎯 Descripció del Projecte
Aquest projecte consisteix en una plataforma de gestió immobiliària integral per a l'empresa **PINT APART**. S'ha dissenyat un ecosistema digital per administrar propietats, contractes i propietaris mitjançant:
* **Backend:** Arquitectura REST robusta amb **Spring Boot**.
* **Frontend:** Interfície dinàmica amb estètica **Glassmorphism** i disseny adaptatiu.
* **Dades:** Persistència amb **JPA** i base de dades **H2**.

---

## 💻 2. Implementació del Backend (Controllers)
S'ha prioritzat la **reutilització de codi** i la **unificació visual**. Mitjançant mètodes `renderCard`, el servidor genera components HTML coherents injectant dades dinàmiques de les entitats.

### 🏠 2.1 ApartmentRestController
El sistema realitza una detecció lògica del tipus d'immoble per assignar la icona corresponent:
* **Dúplex:** 🏘️
* **Apartament/Casa:** 🏠

```java
private String renderCard(Apartment a) {
    boolean m = a.getId() != null && a.getId() <= 3;
    String icona = a.getPropertyType().toLowerCase().contains("duplex") ? "🏘️" : "🏠";
    
    return String.format("""
        <div class='item-card %s'>
            <div class='item-main-content'>
                <div class='id-badge'>ID %d</div><div class='item-text-wrapper'><div class='item-title'>%s %s</div><div class='item-subtitle'>Unitat al sistema</div></div>
            </div>
            <div class='status-container'><span class='status-text'>%s</span><div class='status-dot'></div></div>
        </div>""", 
        m ? "is-active" : "is-inactive", a.getId(), icona, a.getPropertyType(), m ? "ACTIU" : "INACTIU");
}
```
###  📑 2.2  PropertyContractRestController
S'ha implementat una lògica de neteja de cadenes per eliminar el text "Contracte: " provinent de la base de dades, millorant la llegibilitat de la interfície.

```java
private String renderCard(PropertyContract c) {
    boolean active = c.getId() != null && c.getId() <= 3;
    String detallsNets = c.getContractDetails().replace("Contracte: ", "").trim();
    String icona = detallsNets.toLowerCase().contains("duplex") ? "🏘️" : "🏠";
    
    return String.format("""
        <div class='item-card %s'>
            <div class='item-main-content'>
                <div class='id-badge'>ID %d</div>
                <div class='item-text-wrapper'><div class='item-title'>%s %s</div>
                <div class='item-subtitle'>Contracte al sistema</div></div>
            </div>
            <div class='status-container'><span class='status-text'>%s</span><div class='status-dot'></div></div>
        </div>""", active ? "is-active" : "is-inactive", c.getId(), icona, detallsNets, active ? "VIGENT" : "FINALITZAT");
}
```
### 👤 2.3  OwnerRestController
S'utilitza Boolean.TRUE.equals() per garantir la seguretat enfront de valors nuls i es mostra l'email del propietari com a subtítol informatiu.

```java
private String renderOwnerCard(Owner o) {
    boolean active = Boolean.TRUE.equals(o.getIsActive());
    return String.format("""
        <div class='item-card %s'>
            <div class='item-main-content'>
                <div class='id-badge'>ID %d</div>
                <div class='item-text-wrapper'><div class='item-title'>👤 %s</div><div class='item-subtitle'>%s</div></div>
            </div>
            <div class='status-container'><span class='status-text'>%s</span><div class='status-dot'></div></div>
        </div>""", active ? "is-active" : "is-inactive", o.getId(), o.getName(), o.getEmail(), active ? "ACTIU" : "INACTIU");
}
```
### 🎨 3. Interfície d'Usuari (Frontend CSS)
- El disseny se centra en la claredat i el feedback visual. S'han eliminat ombres innecessàries per mantenir un estil net.

    **Estil de fons (Gradient Invertit):**
```CSS
body{margin:0;font-family:'Plus Jakarta Sans',sans-serif;background:#0f172a;display:flex;justify-content:center;align-items:center;min-height:100vh;padding:40px 0;box-sizing:border-box;position:relative;background-image:radial-gradient(at 100% 0%,rgba(141,39,205,0.85) 0,transparent 50%),radial-gradient(at 0% 100%,rgba(198,79,128,0.85) 0,transparent 50%),radial-gradient(at 50% 50%,rgba(161,53,172,0.85) 0,transparent 50%);background-attachment:fixed;}
```
### 💎 4. Components UI Clau

**🔲 .item-card**: Targetes amb `flexbox` per a una alineació perfecta dels elements (ID, contingut i estat).<br>
**🆔 .id-badge**: Bloc destacat per a l'identificador únic amb colors suaus que milloren la llegibilitat.<br>
**📝 .item-text-wrapper**: Jerarquia tipogràfica que separa clarament el títol principal del subtítol informatiu.<br>
**✨ Animacions**: L'estat actiu integra un efecte `pulse-border-only` que proporciona un feedback visual orgànic.<br>

### 🛠️ 5. Gestió de Dades
- Persistència: Ús de Spring Data JPA.
- Reset de Sistema: Funcionalitat per revertir canvis i reiniciar els comptadors d'ID en la base de dades H2 mitjançant JdbcTemplate.
- Rutes dinàmiques: Pàgines de "Populate" que generen el contingut HTML per a una ràpida previsualització.


## 🚀 6. Vista de Control Principal (L'experiència d'usuari)

Per a la verificació del correcte funcionament del backend i la gestió de dades, s'han habilitat els següents punts de control:

### 🔄 6.1 Reset i Restauració de la Base de Dades
La ruta principal per veure que tot funciona visualment és:
👉 `http://localhost:8080/api/populate/run`<br>
- **Afegir:** que crea un immoble nou i l'assigna automàticament a un Propietari (Marc) i li crea una Review (Tomas) mitjançant cascades.

- **Revertir** executa un reset complet de la base de dades, reiniciant els comptadors d'ID i deixant els 3 immobles originals.

### ➕ 6.2 Comprovar les Cascades (Postman / JSON)
Si vols veure com les entitats estan realment lligades al backend, pots mirar aquests Endpoints:

- **JSON d'Apartaments: GET /api/populate/json** Aquí veuràs que cada apartament ja ve amb el seu contracte i propietari.

- **Llista de Propietaris: GET /api/populate/owners** Podràs veure com el Marc gestiona tots els immobles que vas afegint.


### ➕ 6.3 Lògica implementada

- **Integritat referencial**: En esborrar o fer reset, no queden contractes "orfes".
- **Automatització:**: El preu dels nous apartaments es calcula automàticament segons els metres quadrats al Service."
























---
Desenvolupat per👤 Marc
PRA02 PINT APART - 2026