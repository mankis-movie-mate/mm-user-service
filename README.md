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

The User Service configuration is controlled using the following environment variables:

```bash
MOVIE_MATE_USER_SERVICE_PORT= # optional (default 7030)
MOVIE_MATE_USER_SERVICE_CONTEXT_PATH= # optional (default /api/v1)
MOVIE_MATE_DISCOVERY_SERVER_URL=

# POSTGRE 
MOVIE_MATE_POSTGRE_URL=
MOVIE_MATE_POSTGRE_USERNAME=
MOVIE_MATE_POSTGRE_PASSWORD=

# REDIS
MOVIE_MATE_REDIS_HOST=
MOVIE_MATE_REDIS_PORT=
MOVIE_MATE_REDIS_PASSWORD=
MOVIE_MATE_USER_SERVICE_DATABASE_INDEX=  # default is 0
MOVIE_MATE_USER_SERVICE_REDIS_TTL= # default is -1

# JWT
MOVIE_MATE_JWT_SECRET_KEY=
MOVIE_MATE_JWT_ACCESS_EXPIRATION_TIME=
MOVIE_MATE_JWT_REFRESH_EXPIRATION_TIME=

# ROOT USER
MOVIE_MATE_USER_SERVICE_ROOT_USERNAME=
MOVIE_MATE_USER_SERVICE_ROOT_EMAIL=
MOVIE_MATE_USER_SERVICE_ROOT_PASSWORD=
```

## Setup

### Prerequisites

- Java 17+
- Maven
- Docker (optional, for PostgreSQL)