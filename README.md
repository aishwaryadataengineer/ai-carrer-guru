# AI Career Guru

AI Career Guru is a Spring Boot application designed for user authentication and profile management. It utilizes Spring Data JPA, Spring Security, and MySQL for backend services while providing endpoints for user signup and login. A React frontend can easily be integrated to provide a modern user interface for these services.

## Features

- **User Signup:** Create new user accounts with details such as username, email, password, first name, last name, phone, title, location, and LinkedIn URL.
- **User Login:** Authenticate existing users via a dedicated login endpoint.
- **Spring Security:** Uses HTTP Basic authentication with plans for future OAuth integration.
- **Spring Data JPA:** Automatically manages schema creation and updates based on JPA entity mappings.
- **Externalized Configuration:** Supports environment-based configuration for database connection properties.
- **Optional React Frontend:** Example React components can be used to implement the signup and login interfaces.

## Prerequisites

- **Java:** JDK 21 or later
- **Maven:** Build tool for dependency management and compilation
- **MySQL Server:** Running and accessible (the application connects to a MySQL database)
- **IntelliJ IDEA:** (or your preferred IDE) configured with annotation processing for Lombok
- **Node.js & React (Optional):** For developing and running the React frontend
