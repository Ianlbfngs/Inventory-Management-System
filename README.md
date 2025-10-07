# Inventory Management System

Un sistema de gestión de inventario desarrollado con una arquitectura de microservicios utilizando Spring Boot para el backend, React para el frontend y Docker para el despliegue.
## Descripcion general

Este proyecto simula un sistema de gestión de inventario con funcionalidades para administradores como la gestión de productos, realización de movimientos de stock, autenticación de usuarios y más. Cada funcionalidad está encapsulada dentro de microservicios desplegables de forma independiente, lo que promueve la escalabilidad y el mantenimiento del sistema.

## Tabla de contenidos
- [Descripción general](#descripcion-general)
- [Tecnologías usadas](#tecnologias-usadas)
- [Características principales](#características-principales)
- [Instalación y uso](#instalacion-y-uso)
- [Estructura del backend](#estructura-del-backend)
- [Futuras mejoras](#futuras-mejoras)


## Tecnologias Usadas
| Backend          | Frontend        | Infrastructure           | Otros                  |
|------------------|-----------------|--------------------------|-------------------------|
| Java 21          | React           | Docker & Docker Compose  | RESTful APIs            |
| Spring Boot      | Vite + SWC      | Spring Cloud Gateway     | MySQL Workbench         |
| Spring Data JPA  | React Router    | Eureka Service Registry  | IntelliJ IDEA + VSCode  |
| Spring Security  | Axios           |                          | Maven                   |
| MySQL            |                 |                          | Postman                 |
|                  |                 |                          | Git & GitHub            |

## Características principales
- **Autenticación de usuarios**  
  Sistema de inicio de sesión seguro usando tokens JWT.

- **Arquitectura de microservicios**  
  Cada dominio (usuario, almacenamiento, producto, stock, movimientos) está encapsulado en un servicio Spring Boot independiente.

- **API Gateway con Spring Cloud Gateway**  
  Enrutamiento centralizado y filtrado para todos los servicios backend.

- **Descubrimiento de servicios con Eureka**  
  Registro y descubrimiento dinámico de servicios para facilitar la comunicación y la escalabilidad.

- **Operaciones CRUD**  
  Funcionalidad completa de Crear, Leer, Actualizar y Eliminar para todas las entidades.

- **Bases de datos pre-cargadas**  
  Las bases de datos MySQL se inicializan automáticamente usando scripts SQL vía Docker.

- **Endpoints para chequeo de salud**  
  Endpoints basados en Actuator para monitorear la salud de cada servicio.

- **Configuración con Docker**  
  Ejecuta todo el sistema con un solo comando `docker-compose up --build`.

- **Stack moderno para frontend**  
  Frontend en React con Vite + SWC para un desarrollo rápido y compilaciones optimizadas.

## Instalacion y uso
### Requisitos previos
- Docker y Docker Compose instalados
- Git (Opcional)
### Opcion 1: Clonar con Git y Ejecutar con Docker
```bash
git clone https://github.com/Ianlbfngs/Inventory-Management-System.git
cd Inventory-Management-System
docker-compose up --build
```
### Opcion 2: Descargar el proyecto de GitHub y ejecutar con Docker 
- Ir a: https://github.com/Ianlbfngs/Inventory-Management-System.git
- Hacer click en "CODE" y "Download ZIP"
- Extraer el .zip
- Abrir un terminal en la carpeta extraida y ejecutar:
```bash
docker-compose up --build
```
### Acceso
#### Frontend
1. Ir desde un navegador a: http://localhost:3000
2. Iniciar sesion como administrador:<br>
    <u>Usuario</u>: admin <br>
    <u>Contraseña</u>: admin
#### API Gateway
http://localhost:8080

#### Eureka Dashboard:
1. Ir desde un navegador a: http://localhost:8761




## Estructura del backend
```
/backend/eureka-server            --> Eureka service 
/backend/api-gateway              --> API Gateway  
/backend/user-service             --> Authentication and user credentials service 
/backend/product-service          --> Product management service 
/backend/storage-service          --> Storage management service 
/backend/stock-service            --> Stock management service  
/backend/movements-service        --> Movement service
/frontend/frontend-app            --> Carpeta con el frontend
/db-scripts                       --> Carpeta con scripts SQL para crear las bases de datos   
```
## Futuras mejoras
- **Filtros avanzados para las listas**
    Agregar filtros dinámicos y multi-criterio para mejorar la navegación y búsqueda de datos.

- **Mejoras en la gestion de usuarios**  
    Soporte para crear y manejar múltiples roles de usuario con permisos específicos.

- **Integracion de Swagger**  
    Agregar documentación interactiva de la API usando Swagger UI para facilitar pruebas y desarrollo.

- **Integracion de Kafka**  
    Implementar comunicación asíncrona entre microservicios usando Kafka para mayor escalabilidad y desacople.



Ingles
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


 
