# Product Service

This README explains how to run the Product Service application locally, how to start the database (Postgres) using the included docker-compose, how to run the app, and how to import the provided CSV (`src/main/resources/TestExampleFile.csv`).

## Prerequisites
- Java 17+ (the project sets `java.version` 17 in `pom.xml`) or Java 21 installed
- Maven (wrapper `./mvnw` is provided, you can use it without installing Maven)
- Docker and Docker Compose (optional but recommended for Postgres)

## Quick start (recommended using Docker Compose)
A `docker-compose.yml` is included in this repo. It maps a Postgres data directory to `./postgres_data`.

1. Start Postgres via docker-compose:

```powershell
# from project root
docker compose up -d
```

2. Verify Postgres is running (ps and logs):

```powershell
docker compose ps
docker compose logs -f postgres
```

3. Build and run the application (use the Maven wrapper):

```powershell
# build
./mvnw -DskipTests package

# run (dev)
./mvnw spring-boot:run

# or run the packaged jar
java -Duser.timezone=UTC -jar target/product-service-0.0.1-SNAPSHOT.jar
```

The application starts on port `9090` by default.

## Endpoints
- Base URL: `http://localhost:9090`

- Get products (paginated):
  - `GET /api/products`
  - Example: `http://localhost:9090/api/products?page=0&size=10&sortBy=id`

- Trigger server-side CSV import (uses `src/main/resources/TestExampleFile.csv`):
  - `POST /admin/import-csv`
  - Example (PowerShell):
    ```powershell
    Invoke-WebRequest -Method Post -Uri "http://localhost:9090/admin/import-csv" -UseBasicParsing
    ```

- (Optional) Upload CSV from client: If you prefer to upload a CSV file rather than using the server-side file, you can POST the file to an upload endpoint. Ask the developer to enable `POST /admin/upload-csv` for multipart upload.

## CSV file
- The sample CSV is at `src/main/resources/TestExampleFile.csv`.
- The import will create categories if they don't exist and skip existing products (by product code).

## Configuration
- `src/main/resources/application.yml` (or `application.properties`) contains DB and JPA settings.
- Default DB connection (when using docker-compose) is `jdbc:postgresql://localhost:5432/test` with username `root`, password `1234` (adjust as needed).

## Troubleshooting
- If application fails on startup with `Failed to determine a suitable driver class`:
  - Ensure `org.postgresql:postgresql` is in `pom.xml` (it is included by default).
  - Make sure `application.yml` or `application.properties` contains `spring.datasource.url`.

- If you see `FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"`:
  - Start the JVM with `-Duser.timezone=UTC` or `-Duser.timezone=Asia/Kolkata`, or we already normalize timezone in main.

- If CSV import fails, check the server console logs where you started the app for stack traces.

## Running tests
- To run tests:
```powershell
./mvnw test
```

