# ToDo Application

This is a simple ToDo application that allows users to manage their tasks. The application is built using Spring Boot and integrates with a PostgreSQL database.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK)**: Make sure you have JDK 17 or higher installed. You can download it from [here](https://www.oracle.com/java/technologies/downloads/#java17).
- **Docker**: Docker is required to run the PostgreSQL database in a container. Follow the instructions in the official Docker documentation to install Docker on your machine:
    - [Docker Desktop for Windows](https://docs.docker.com/desktop/windows/install/)
    - [Docker Desktop for Mac](https://docs.docker.com/desktop/mac/install/)
    - [Docker Engine for Linux](https://docs.docker.com/engine/install/)

## Build and Run the Application

## Running the Application

The main class to run the application is `ToDoApplication`. You can start the application by running the `main` method in this class. This will start the Spring Boot application, and Docker will automatically start the PostgreSQL container.

## Accessing the Application

Once the application is running, you can access it at [http://localhost:8081](http://localhost:8081). The default port is set to `8081` in the `application.yml` file.

## API Documentation

The application includes Swagger UI for API documentation. You can access it at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) once the application is up and running.

## Testing

The application uses an in-memory H2 database for testing. You can run the tests using the following command:

```bash
./mvnw test
