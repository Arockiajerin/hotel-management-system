# Hotel Management System

A comprehensive hotel management system built with Angular and Spring Boot.

## Features
- Category Management
- Product Management  
- Bill Management
- User Authentication
- Admin Dashboard

## Tech Stack
- **Frontend:** Angular, Angular Material, TypeScript
- **Backend:** Spring Boot, Java
- **Database:** MySQL
- **Authentication:** JWT

## Prerequisites
Make sure you have the following installed on your system:
- Node.js (version 14 or higher)
- Angular CLI (version 12 or higher)
- Java JDK (version 8 or 11)
- Maven (version 3.6 or higher)
- MySQL (version 5.7 or higher)

## Frontend Setup (Angular)

1. **Clone the repository**
   ```bash
   git clone https://github.com/Arockiajerin/hotel-management-system.git
   cd hotel-management-system/frontend
##application.properties:

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_management
spring.datasource.username=****
spring.datasource.password=****

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080

# JWT Configuration
jwt.secret=mySecretKey
jwt.expiration=86400000




