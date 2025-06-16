# Inventory Management System

Migration of the monolithic project "Sistema de gestión bancaria" to a microservices architecture using Spring Boot and React

## Table of contents
- [Technologies and concepts used](#Technologies-and-concepts-used)
- [Features](#Features)
- [Installation and Usage](#Installation-and-Usage)
- [Repository structure](#Repository-structure)
- [General service structure](#General-service-structure)


## Technologies and concepts used
- Java 21
- Spring Boot
- Spring Cloud Gateway
- React
- MySQL
- Git and GitHub
- IntelliJ IDEA
- Postman
- Maven
- Docker
- REST APIs
- Microservices

## Features
- Login for clients and administrators
- Lists with multiple filters
- Validations when performing CRUD operations
### Administrator functions
- CRUD operations for clients
- CRUD operations for accounts
### Clients functions
- View transaction history
- Make transfers
- Manage multiple accounts

## Installation and Usage
### Prerequisites
- Docker and Docker Compose installed
### Clone the repository
```bash
git clone https://github.com/Ianlbfngs/Sistema-Bancario.git
cd sistema-bancario
```
### Run with Docker
```bash
docker-compose up --build
```
### Access
1. Open a browser and go to: http://localhost:3000
2. Log in as administrator <br>
    <u>User</u>: admin <br>
    <u>Password</u>: admin

## Repository structure
```
/api-gateway         --> Folder containing the service gateway  
/auth-service        --> Folder containing the login credentials service  
/register-service    --> Folder for client registration service  
/clients-service     --> Folder for client service  
/account-service     --> Folder for account service  
/movements-service   --> Folder for transaction service  
/frontend            --> Folder containing the frontend (made with React)  
/db-scripts          --> Folder with SQL scripts to create the databases   
```
## General service structure
```
/service/src/main/java/com/ib/service/config         --> General configuration for the service  
/service/src/main/java/com/ib/service/controller     --> REST controllers  
/service/src/main/java/com/ib/service/dto            --> Data Transfer Objects  
/service/src/main/java/com/ib/service/entity         --> Entity classes representing database tables  
/service/src/main/java/com/ib/service/mapper         --> Mapping classes between entities and DTOs  
/service/src/main/java/com/ib/service/repository     --> Interfaces for database access  
/service/src/main/java/com/ib/service/response       --> Classes for custom API responses  
/service/src/main/java/com/ib/service/service        --> Interfaces and classes implementing business logic  
```

 
