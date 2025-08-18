# movie-mate-user-service

## Overview

The User Service handles user authentication, registration, and profile management in MovieMate.
It provides a REST API for user-related operations, including JWT-based authentication.

## Features

- **User Authentication**: Handles user login and token validation.
- **User Registration**: Manages user sign-up and account creation.
- **Profile Management**: Allows users to update their personal information.
- **Token Validation API**: Provides an endpoint for verifying authentication tokens.

## Tech Stack

- Java 17
- Spring Boot (Spring Security, Spring Data JPA)
- PostgreSQL
- Redis (caching)
- JWT Authentication
- Maven

## Environment Variables
This service requires two sets of environment variables:
* Service-specific variables – defined in .env.example.
Copy this file to .env and adjust the values to your environment.
These variables control only the mm-user-service configuration.
* Cluster-wide variables – defined in the root .env. and .env.db (mm-infrastructure)
Make sure this file is available and injected into your deployment (e.g., via Kubernetes Secrets, Docker Compose, or GitHub Actions secrets).

## Setup

### Prerequisites

- Java 17+
- Maven
- Docker (optional, for PostgreSQL)