# ToDo Application

This is a simple ToDo application built using Spring Boot, integrated with PostgreSQL, and enhanced with additional services and monitoring tools.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK)**: Make sure you have JDK 17 or higher installed. You can download it from [here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
- **Docker**: Docker is required to run the PostgreSQL database and other services in containers. Follow the instructions in the official Docker documentation to install Docker on your machine:
  - [Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/)
  - [Docker Desktop for Mac](https://docs.docker.com/desktop/install/mac-install/)
  - [Docker Engine for Linux](https://docs.docker.com/engine/install/)

## Build and Run the Application

### Running the Application

To run the application, you need to start the `task-service`. The main class to run the application is `ToDoApplication`. This will start the Spring Boot application, and Docker will automatically start the PostgreSQL container along with the other services.

### Additional Services

- **Admin Service**: [Spring Boot Admin](https://docs.spring-boot-admin.com/current/) for monitoring the `task-service`, accessible on port 8083.
- **Keycloak**: For OAuth2 authentication, running on port 8082. Keycloak provides several scopes such as `task_edit`, `task_view`, and others for metrics collection and monitoring. In order to apply all the required settings for stable operation of the application go to the address line http://localhost:8082 log in to master account select "create realm" and paste the file which is located on the path `configs/keycloak/realm`. For more details, refer to the [Keycloak Documentation](https://www.keycloak.org/documentation.html).
- **VictoriaMetrics**: For metrics and monitoring, running on port 8428. Configuration files are located in the `configs/victoria-metrics/` folder. Learn more from the [VictoriaMetrics Documentation](https://docs.victoriametrics.com/).
- **Grafana**: For visualization, running on port 3000. Data files are stored in the `data/grafana` folder. For more information, check the [Grafana Documentation](https://grafana.com/docs/grafana/latest/).

### Accessing the Application

Once the application is running, you can access it at [http://localhost:8081](http://localhost:8081). The default port is set to 8081 in the `application.yml` file.

### API Documentation

The application includes Swagger UI for API documentation. You can access it at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html).

## Testing

The application uses an in-memory H2 database for testing. You can run the tests using the following command:

```sh
./mvnw test