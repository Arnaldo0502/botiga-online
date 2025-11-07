# Botiga Online amb CRUD, Transaccions i Descomptes

## Descripció
Aplicació en Java amb JDBC per gestionar una botiga online sobre una base de dades MySQL. Inclou CRUD de productes i clients, creació de comandes amb gestió d'estoc mitjançant transaccions, i aplicació de descomptes amb savepoints.

## Funcionalitats
- **CRUD Bàsic**: Operacions completes per a productes i clients utilitzant PreparedStatement.
- **Creació de Comandes**: Amb transaccions per gestionar estoc, rollback en cas d'error.
- **Descomptes**: Aplicació amb savepoints, rollback si falla.
- **Consultes**: Llistats amb JOIN per comandes, línies i totals amb descomptes.

## Requisits
- Java (JDK)
- MySQL Server
- Connector/J de MySQL

## Configuració
1. Crear la base de dades executant `sql/schema.sql` a MySQL.
2. Configurar les credencials a `src/util/Connexio.java` (usuari i contrasenya).

## Execució
Compilar i executar `src/App.java`. El menú principal permet gestionar productes, clients, comandes i consultes.

## Estructura del Projecte
- `sql/schema.sql`: Script per crear la BD i dades de prova.
- `src/util/Connexio.java`: Connexió a la BD.
- `src/model/`: Classes POJO (Client, Producte, etc.).
- `src/dao/`: Classes DAO per persistència.
- `src/App.java`: Classe principal amb menú.
