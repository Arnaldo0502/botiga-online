# Botiga Online

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
<div style="border:1px solid #f39c12; padding:12px; border-radius:6px; background:#fff7e6;color:black;">
⚠️ <strong>Atención:</strong> Este programa está diseñado para ejecutarse en Visual Studio Code. Si utilizas otro entorno, verifica que cumpla los requisitos necesarios para su funcionamiento.
</div>


## Índex

- [Descripció](#descripció)
- [Característiques](#característiques)
- [Requisits previs](#requisits-previs)
- [Instal·lació](#instal·lació)
- [Ús](#ús)
- [Estructura del Projecte](#estructura-del-projecte)
- [Contribucions](#contribucions)
- [Llicència](#llicència)
- [Contacte](#contacte)

## Descripció

Botiga Online és una aplicació de consola desenvolupada en Java que permet gestionar una botiga en línia. Utilitza una base de dades MySQL per emmagatzemar informació sobre productes, clients i comandes. L'aplicació ofereix una interfície de menú interactiva per realitzar operacions CRUD (Crear, Llegir, Actualitzar, Eliminar) sobre les entitats principals.

## Característiques

- **Gestió de Productes**: Inserir, llistar, actualitzar i eliminar productes amb informació de nom, preu i estoc.
- **Gestió de Clients**: Administrar clients amb nom i correu electrònic.
- **Creació de Comandes**: Crear comandes associades a clients, amb línies de comanda per productes.
- **Consultes Avançades**: Llistar comandes per client i mostrar totals amb descomptes aplicats.
- **Interfície de Consola**: Menú interactiu fàcil d'utilitzar amb validació d'entrada.
- **Integració amb MySQL**: Connexió segura a la base de dades utilitzant JDBC.

## Requisits previs

- Java Development Kit (JDK) 17 o superior
- MySQL Server 8.0 o superior
- Connector JDBC per MySQL (inclòs en el projecte: `mysql-connector-j-9.4.0.jar`)

## Instal·lació

1. **Clona el repositori**:
   ```bash
   git clone https://github.com/usuari/botiga-online.git
   cd botiga-online
   ```

2. **Configura la base de dades**:
   - Instal·la i inicia MySQL Server.
   - Crea una base de dades nova:
     ```sql
     CREATE DATABASE botiga_online;
     ```
   - Executa l'script de l'esquema:
     ```bash
     mysql -u root -p botiga_online < sql/schema.sql
     ```

3. **Configura la connexió**:
   - Edita el fitxer `src/util/Connexio.java` per ajustar les credencials de la base de dades (usuari, contrasenya, URL).

4. **Compila el projecte**:
   ```bash
   javac -cp "lib/mysql-connector-j-9.4.0.jar" -d out/production/botiga-online src/**/*.java
   ```

5. **Executa l'aplicació**:
   ```bash
   java -cp "out/production/botiga-online:lib/mysql-connector-j-9.4.0.jar" App
   ```

## Ús

Un cop executada l'aplicació, es mostrarà un menú principal amb les opcions següents:

1. **Gestionar Productes**: Accedeix a submenús per inserir, llistar, actualitzar o eliminar productes.
2. **Gestionar Clients**: Gestiona clients amb operacions CRUD.
3. **Crear Comanda**: Crea una nova comanda associada a un client existent.
4. **Llistar Comandes**: Consulta comandes per client o mostra totals amb descomptes.

### Exemple d'ús

- Selecciona "1" per gestionar productes.
- Selecciona "2" per llistar tots els productes.
- La sortida mostrarà una llista amb ID, nom, preu i estoc de cada producte.

## Estructura del Projecte

```
📁 botiga-online/
├── 📁 lib/
│   └── 📄 mysql-connector-j-9.4.0.jar
├── 📁 out/
│   └── 📁 production/
│       └── 📁 botiga-online/
├── 📁 sql/
│   └── 📄 schema.sql
├── 📁 src/
│   ├── 📄 App.java
│   ├── 📁 dao/
│   │   ├── 📄 ClientDAO.java
│   │   ├── 📄 ComandaDAO.java
│   │   ├── 📄 ConsultesDAO.java
│   │   ├── 📄 DescompteDAO.java
│   │   └── 📄 ProducteDAO.java
│   ├── 📁 model/
│   │   ├── 📄 Client.java
│   │   ├── 📄 Comanda.java
│   │   ├── 📄 Descompte.java
│   │   ├── 📄 LiniaComanda.java
│   │   └── 📄 Producte.java
│   └── 📁 util/
│       └── 📄 Connexio.java
├── 📄 botiga-online.iml
└── 📄 README.md
```

## Contribucions

Les contribucions són benvingudes. Si vols contribuir:

1. Fork el projecte.
2. Crea una branca per la teva funcionalitat (`git checkout -b feature/nova-funcionalitat`).
3. Commit els canvis (`git commit -am 'Afegeix nova funcionalitat'`).
4. Push a la branca (`git push origin feature/nova-funcionalitat`).
5. Obre un Pull Request.

## Llicència

Aquest projecte està sota la Llicència MIT. Consulta el fitxer LICENSE per a més detalls.
