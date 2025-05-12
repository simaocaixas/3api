# Spring Boot Tree RESTful API - A Sample App


## Overview

A fully functional sample project written in **Java**, demonstrating how to create a **Spring Boot backend** following **REST principles** and utilizing several modern technologies. This application provides a simple **Tree CRUD** (Create, Read, Update, Delete) service and integrates multiple tools and frameworks such as:


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




