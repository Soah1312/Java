# Farmers Produce Tracker (Spring Boot + H2)

Minimal Java 17+ Spring Boot web app with a monochrome HTML/CSS UI and an in-memory H2 database (via Spring Data JPA). Two routes:

- `/farmer` – Add produce
- `/home` – View all produce

## Requirements
- Java 17+ (JDK 17 or newer)

## Configure
Defaults are set for H2 in `src/main/resources/application.properties`.

## Run (local)
From `javaproj/Java`:

```
.\u200cgradlew.bat bootRun
```

Then open http://localhost:8080/home. H2 console at http://localhost:8080/h2-console (JDBC: `jdbc:h2:mem:farmersdb`, user `sa`, empty password).

## Build
```
./gradlew build
```

The Boot jar will be at `build/libs/farmersapp-0.0.1-SNAPSHOT.jar`.

## Deploy
- Build Command: `./gradlew build`
- Start Command: `java -jar build/libs/farmersapp-0.0.1-SNAPSHOT.jar`

## Notes
- Uses Spring Data JPA with H2 in-memory database.
- UI is grayscale, minimalist, emoji icons for produce.
- Gradle Wrapper included; no global Gradle install required.

