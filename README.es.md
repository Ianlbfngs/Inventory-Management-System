# Inventory Management System

Un sistema de gestión de inventario desarrollado con una arquitectura de microservicios utilizando Spring, React y Docker.
## Descripcion general

Este proyecto simula un sistema de gestión de inventario con funcionalidades para administradores como la gestión de productos, realización de movimientos de stock, autenticación de usuarios y más. Cada funcionalidad está encapsulada dentro de microservicios desplegables de forma independiente, lo que promueve la escalabilidad y el mantenimiento del sistema.

## Tabla de contenidos
- [Descripción general](#descripcion-general)
- [Tecnologías usadas](#tecnologias-usadas)
- [Características principales](#caracteristicas-principales)
- [Instalación y uso](#instalacion-y-uso)
- [Estructura del backend](#estructura-del-backend)
- [Futuras mejoras](#futuras-mejoras)


## Tecnologias Usadas
| Backend          | Frontend        | Infrastructure           | Others                  |
|------------------|-----------------|--------------------------|-------------------------|
| Java 21          | React           | Docker & Docker Compose  | RESTful APIs            |
| Spring Boot      | Vite + SWC      | Spring Cloud Gateway     | MySQL Workbench         |
| Spring Data JPA  | React Router    | Eureka Service Registry  | IntelliJ IDEA, VSCode   |
| Spring Security  | Axios           | Nginx                    | Maven                   |
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
git clone https://github.com/Ianlbfngs/Sistema-Bancario.git
cd sistema-bancario
docker-compose up --build
```
### Opcion 2: Descargar el proyecto de GitHub y ejecutar con Docker 
- Ir a: https://github.com/Ianlbfngs/Sistema-Bancario.git
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


 