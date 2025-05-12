# Spring Boot Tree RESTful API - A Sample App


## Overview

This is a fully functional sample project written in Java that samples a simple Spring Boot backend adhering to REST principles. The application provides a Tree CRUD service, allowing authenticated users to create, read, update, and delete tree data. User authentication is handled through JWT tokens using HTTPS-only cookies for added security.

The project utilizes PostgreSQL for data storage, and pgAdmin4 for database management both are containerized with Docker for easy deployment and scalability. Swagger is integrated to provide interactive API documentation, while Mockito is used for unit testing **TO-DO**.

Verify API documentation in http://localhost:8080/swagger-ui/index.html when running.

## Setup and Running localy

Ensure that you have the following installed:

- **Docker**: [Download and install Docker](https://www.docker.com/get-started).
- **Java 23**: [Download and install Java](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html).
- **Maven**: [Install Apache Maven](https://maven.apache.org/install.html).

### Step 1: Clone the Repository

First, clone the repository to your local machine:

```bash
git clone git@github.com:simaocaixas/3api.git
cd 3api
```

### Step 2: Build and run Docker conteiners

The project includes a docker-compose.yml file that automates the setup of PostgreSQL, pgAdmin4, in a Docker container.
Run docker and run the following command: 
```bash
docker-compose up
```

PostgreSQL running on port 5432 
Pgadmin running on port 5050

**Pgadmin credentials** 
Email: admin@example.com Password: adminpass

### Step 3: Start and build the Maven project

In the project foldier, run the following command:
```bash
mvn spring-boot:run
```
This will start the Spring Boot app, running it by default in port 8080.




