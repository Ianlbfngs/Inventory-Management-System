# Inventory Management System

An inventory management system developed with a microservices architecture using Spring, React, and Docker.

## Overview

This project simulates an inventory management system with features for administrators such as product management, stock movement handling, user authentication, and more. Each functionality is encapsulated within independently deployable microservices, promoting scalability and maintainability.

## Table of Contents
- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Key Features](#key-features)
- [Installation and Usage](#installation-and-usage)
- [Backend Structure](#backend-structure)
- [Future Improvements](#future-improvements)

## Technologies Used

| Backend          | Frontend        | Infrastructure           | Others                  |
|------------------|-----------------|--------------------------|-------------------------|
| Java 21          | React           | Docker & Docker Compose  | RESTful APIs            |
| Spring Boot      | Vite + SWC      | Spring Cloud Gateway     | MySQL Workbench         |
| Spring Data JPA  | React Router    | Eureka Service Registry  | IntelliJ IDEA, VSCode   |
| Spring Security  | Axios           | Nginx                    | Maven                   |
| MySQL            |                 |                          | Postman                 |
|                  |                 |                          | Git & GitHub            |

## Key Features

- **User Authentication**  
  Secure login system using JWT tokens.

- **Microservices Architecture**  
  Each domain (user, storage, product, stock, movements) is encapsulated in an independent Spring Boot service.

- **API Gateway with Spring Cloud Gateway**  
  Centralized routing and filtering for all backend services.

- **Service Discovery with Eureka**  
  Dynamic service registration and discovery to facilitate communication and scalability.

- **CRUD Operations**  
  Full Create, Read, Update, and Delete functionality for all entities.

- **Preloaded Databases**  
  MySQL databases are automatically initialized using SQL scripts via Docker.

- **Health Check Endpoints**  
  Actuator-based endpoints to monitor the health of each service.

- **Docker Configuration**  
  Run the entire system with a single command: `docker-compose up --build`.

- **Modern Frontend Stack**  
  Frontend built in React using Vite + SWC for fast development and optimized builds.

## Installation and Usage

### Prerequisites

- Docker and Docker Compose installed  
- Git (Optional)

### Option 1: Clone with Git and Run with Docker
```bash
git clone https://github.com/Ianlbfngs/Inventory-Management-System.git
cd Inventory-Management-System
docker-compose up --build
```
### Option 2: Download the proyect from GitHub and deploy with Docker 
- Go to: https://github.com/Ianlbfngs/Inventory-Management-System.git
- Click on "CODE" and "Download ZIP"
- Extract the .zip
- Open a terminal in the extracted folder and launch:
```bash
docker-compose up --build
```
### Access
#### Frontend
1. Go from a browser to: http://localhost:3000
2. Log in as administrator:<br>
    <u>User</u>: admin <br>
    <u>Password</u>: admin
#### API Gateway
http://localhost:8080

#### Eureka Dashboard:
1. Go from a browser to: http://localhost:8761


## Backend Structure
```
/backend/eureka-server --> Eureka service
/backend/api-gateway --> API Gateway
/backend/user-service --> Authentication and user credentials service
/backend/product-service --> Product management service
/backend/storage-service --> Storage management service
/backend/stock-service --> Stock management service
/backend/movements-service --> Movement service
/frontend/frontend-app --> Folder containing the frontend
/db-scripts --> Folder with SQL scripts to create the databases 
```
## Future Improvements
- **Advanced filters for lists**  
  Add dynamic and multi-criteria filters to improve data navigation and search.

- **User management improvements**  
  Support for creating and managing multiple user roles with specific permissions.

- **Swagger integration**  
  Add interactive API documentation using Swagger UI to facilitate testing and development.

- **Kafka integration**  
  Implement asynchronous communication between microservices using Kafka for better scalability and decoupling.


 
