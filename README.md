# movie-mate-user-service

## Overview

The **User Service** handles authentication, registration, and user profile management for the MovieMate platform.  
It exposes a REST API for all user-related operations and features secure JWT-based authentication.

---

## ✨ Features

- **User Authentication**: Secure login and token-based access.
- **User Registration**: Easy user sign-up and account creation.
- **Profile Management**: Users can update their personal details.
- **Token Validation API**: Fast endpoint for checking JWT validity.

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot** (Security, Data JPA)
- **PostgreSQL** (user data storage)
- **Redis** (caching)
- **JWT** authentication
- **Maven** (build & dependency management)

---

## ⚙️ Environment Variables

Environment variables are defined in `.env.example`.

- Copy `.env.example` to `.env`
- Fill in the required values for your local or production setup

## 🐳 Docker

### **Build image locally:**

You can build the Docker image directly with Maven and [Spring Boot Buildpacks](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/#build-image):

```bash
mvn spring-boot:build-image \
    -DskipTests \
    -Dspring-boot.build-image.image=mm-user-service:latest
