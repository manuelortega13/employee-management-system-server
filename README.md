# Employee Management System — Server

Spring Boot 4.0.3 backend for the Employee Management System. Java 21, Gradle, PostgreSQL, Flyway migrations.

## Prerequisites

- **Java 21** (JDK)
- **PostgreSQL** running locally (or reachable from your machine)
- **Docker** (optional, only if running via Docker Compose)

## Setup

1. Create a PostgreSQL database named `employee_management` (or update the URL in your `.env`).
2. Copy the example environment file and fill in your credentials:

   ```bash
   cp .env.example .env
   ```

   Required variables:

   ```
   EMPLOYEE_MANAGEMENT_DB_DRIVER_CLASS_NAME=org.postgresql.Driver
   EMPLOYEE_MANAGEMENT_DB_URL=jdbc:postgresql://localhost:5432/employee_management
   EMPLOYEE_MANAGEMENT_DB_USER_NAME=postgres
   EMPLOYEE_MANAGEMENT_DB_PASSWORD=your_password
   ```

3. Run Flyway migrations:

   ```bash
   ./gradlew flywayMigrate
   # or
   make migrate
   ```

## Running the Server

### Locally (Gradle)

```bash
./gradlew bootRun
# or
make start
```

The server starts on `http://localhost:8080`.

### Docker Compose

```bash
make docker-up        # build + start
make docker-logs      # tail logs
make docker-down      # stop
```

## Common Commands

| Task           | Command                  |
| -------------- | ------------------------ |
| Run            | `./gradlew bootRun`      |
| Build          | `./gradlew build`        |
| Test           | `./gradlew test`         |
| Clean          | `./gradlew clean`        |
| Migrate DB     | `./gradlew flywayMigrate`|
| Clean build    | `./gradlew clean build`  |

Run a single test:

```bash
./gradlew test --tests "com.ethan.employee_system.SomeTestClass.testMethod"
```

## Tech Stack

- Spring Boot 4.0.3 (Web MVC, Security, Validation, Data JPA)
- PostgreSQL + Flyway
- Lombok
- JUnit 5
- Gradle (wrapper included)

## Project Structure

- Base package: `com.ethan.employee_system`
- Entry point: `EmployeeSystemApplication`
- Migrations: `src/main/resources/db/migration`
