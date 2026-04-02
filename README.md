# 🎬 MovieTalk - Movie Reviews & Recommendations Platform

A full-stack REST API application for movie discovery, reviews, ratings, and personalized recommendations built with **Spring Boot**, **PostgreSQL**, **Redis**, and **Angular**.

![Java](https://img.shields.io/badge/Java-21-blue?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-green?style=flat-square)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=flat-square)
![Redis](https://img.shields.io/badge/Redis-7-red?style=flat-square)
![Angular](https://img.shields.io/badge/Angular-Latest-red?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-Supported-blue?style=flat-square)

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Installation](#-installation)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [API Endpoints](#-api-endpoints)
- [Docker Deployment](#-docker-deployment)
- [Configuration](#-configuration)
- [Security](#-security)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

### User Management
- ✅ User registration & authentication with JWT
- ✅ Email-based login
- ✅ Role-based access control (ADMIN, VIEWER)
- ✅ User profile management
- ✅ Password management

### Movie Features
- ✅ Browse movies by categories
- ✅ Advanced movie search (title, director, actors)
- ✅ Movie details with images and trailers
- ✅ Rating system (1-10)
- ✅ User reviews/comments on movies
- ✅ Favorite movies management

### Recommendations
- ✅ Personalized movie recommendations based on user favorites & ratings
- ✅ Trending movies
- ✅ Top-rated movies
- ✅ New releases
- ✅ Featured movies (high rating + popularity)

### Collections
- ✅ Create custom movie lists
- ✅ Add/remove movies from lists
- ✅ Public/private list sharing

### Performance
- ✅ Redis caching for frequently accessed data
- ✅ Graceful fallback to in-memory cache
- ✅ Optimized database queries

---

## 🛠️ Technology Stack

### Backend
- **Framework:** Spring Boot 4.0.0
- **Language:** Java 21
- **Build Tool:** Maven 3.9.6
- **ORM:** Hibernate 7.1.8.Final / JPA
- **Authentication:** JWT (JSON Web Tokens)
- **Caching:** Redis 7 + Spring Data Redis

### Database
- **Primary:** PostgreSQL 17
- **Persistence:** Hibernate DDL auto-creation

### Frontend
- **Framework:** Angular (TypeScript)
- **Web Server:** Nginx (production)

### DevOps/Infrastructure
- **Containerization:** Docker & Docker Compose
- **API Documentation:** OpenAPI 3.0 / Swagger

### Testing
- **Unit Tests:** JUnit 5 + Mockito
- **Test Coverage:** FavoriteService, CommentService

---

## 📦 Installation

### Prerequisites
- Java 21+
- Maven 3.9.6+
- PostgreSQL 17+
- Docker & Docker Compose (optional)
- Node.js 22+ (for frontend development)

### Local Setup (Without Docker)

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/MovieTalk.git
   cd MovieTalk/MT
   ```

2. **Configure PostgreSQL**
   ```bash
   # Create database
   createdb movieweb
   
   # Update application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/movieweb
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Configure Redis (Optional)**
   ```bash
   # Start Redis
   redis-server
   
   # Check application.properties
   spring.redis.host=localhost
   spring.redis.port=6379
   ```

4. **Build & Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access API**
   ```
   Backend: http://localhost:3839
   Swagger UI: http://localhost:3839/swagger-ui.html
   ```

---

### Docker Setup (Recommended)

1. **Build and Run Everything**
   ```bash
   docker compose up --build
   ```

2. **Services Start**
   - **Backend API:** http://localhost:3839
   - **PostgreSQL:** localhost:5432 (internal only)
   - **Redis:** localhost:6379 (internal only)

3. **Stop Services**
   ```bash
   docker compose down
   ```

---

## 📁 Project Structure

```
MovieTalk/MT
├── src/main/java/com/MovieTalk/MT/
│   ├── config/                 # Configuration classes (Security, Redis, OpenAPI)
│   ├── controller/             # REST API endpoints
│   │   ├── AdminControllers/   # ADMIN-only endpoints
│   │   └── PublicControllers/  # Public endpoints
│   ├── dto/                    # Data Transfer Objects
│   ├── entity/                 # JPA Entities
│   │   └── enums/              # Enumerations (Role)
│   ├── exception/              # Exception handlers
│   │   ├── globalExceptionHandler/
│   │   └── securityException/
│   ├── repository/             # JPA Repositories
│   ├── security/               # JWT & Authentication
│   ├── service/                # Business logic
│   │   └── impl/               # Service implementations
│   ├── utils/                  # Utility classes
│   └── MtApplication.java      # Main entry point
├── src/main/resources/
│   ├── application.properties  # Configuration
│   └── static/                 # Static files
├── src/test/java/             # Unit tests
├── Dockerfile                 # Multi-stage Docker build
├── docker-compose.yml         # Docker Compose configuration
├── pom.xml                    # Maven dependencies
└── README.md                  # This file
```

---

## 🗄️ Database Schema

### Core Entities

```
┌─────────────────────────────────────────┐
│            USER (users)                 │
├─────────────────────────────────────────┤
│ id (PK)      │ Long                     │
│ name         │ String                   │
│ email (UK)   │ String                   │
│ password     │ String (BCrypt)          │
│ role         │ Role (ADMIN/VIEWER)      │
│ active       │ Boolean                  │
└─────────────────────────────────────────┘
         │
         ├──→ FAVORITE (favorites) ←──┐
         ├──→ RATING (ratings) ←───┐  │
         ├──→ COMMENT (comments) ←─┼──┤
         └──→ USERLIST (lists)      │  │
                                     │  │
┌─────────────────────────────────────────┐
│          MOVIE (movies)                 │
├─────────────────────────────────────────┤
│ id (PK)      │ Long                     │
│ title        │ String                   │
│ synopsis     │ Text                     │
│ releaseYear  │ Integer                  │
│ director     │ String                   │
│ actors       │ Text                     │
│ rating       │ Double                   │
│ imagePath    │ String                   │
│ trailerUrl   │ String                   │
│ category_id  │ FK → CATEGORY            │
└─────────────────────────────────────────┘
         │
         ├──→ FAVORITE (favorites)
         ├──→ RATING (ratings)
         ├──→ COMMENT (comments)
         └──→ LISTMOVIE (list_movies)

┌─────────────────────────────────────────┐
│        CATEGORY (categories)            │
├─────────────────────────────────────────┤
│ id (PK)      │ Long                     │
│ name (UK)    │ String                   │
│ imagePath    │ String                   │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│    USERLIST (lists)                     │
├─────────────────────────────────────────┤
│ id (PK)      │ Long                     │
│ title        │ String                   │
│ isPublic     │ Boolean                  │
│ user_id (FK) │ Long → USER              │
└─────────────────────────────────────────┘
         │
         └──→ LISTMOVIE (list_movies)
```

**Total Tables:** 8
- Users, Movies, Categories, Favorites, Ratings, Comments, UserLists, ListMovies

---

## 🔌 API Endpoints

### Authentication (`/auth`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/auth/login` | User login | ❌ |
| POST | `/auth/register` | User registration | ❌ |

### Public Movies (`/movies`, `/categories`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/movies` | List all movies (cached) | ❌ |
| GET | `/movies/{id}` | Movie details | ❌ |
| GET | `/movies/featured` | Featured movies | ❌ |
| GET | `/movies/trending` | Trending movies | ❌ |
| GET | `/movies/new-releases` | New releases | ❌ |
| GET | `/movies/top-rated` | Top-rated movies | ❌ |
| GET | `/movies/recommendations` | Personalized recommendations | ✅ |
| GET | `/movies/by-category/{id}` | Movies by category | ❌ |
| GET | `/movies/search?query={q}` | Search movies | ❌ |
| GET | `/categories` | All categories (cached) | ❌ |

### User Operations (`/user`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/user/profile` | User profile | ✅ VIEWER |
| PUT | `/user/profile/username` | Update username | ✅ VIEWER |
| PUT | `/user/profile/password` | Update password | ✅ VIEWER |
| GET | `/user/favorites` | User favorites | ✅ VIEWER |
| POST | `/user/favorites/{userId}/{movieId}` | Add favorite | ✅ VIEWER |
| DELETE | `/user/favorites/{userId}/{movieId}` | Remove favorite | ✅ VIEWER |
| POST | `/ratings` | Rate movie | ✅ Authenticated |
| GET | `/ratings/movie/{movieId}` | Movie ratings | ✅ |
| POST | `/comments` | Add comment | ✅ Authenticated |
| GET | `/comments/movie/{movieId}` | Movie comments | ✅ |

### Admin Operations (`/admin`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/admin/users` | All users | ✅ ADMIN |
| GET | `/admin/users/{id}` | User details | ✅ ADMIN |
| PATCH | `/admin/users/{id}/active` | Toggle user status | ✅ ADMIN |
| POST | `/admin/movies` | Create movie | ✅ ADMIN |
| GET | `/admin/movies` | Admin movie list (cached) | ✅ ADMIN |
| PUT | `/admin/movies/{id}` | Update movie | ✅ ADMIN |
| DELETE | `/admin/movies/{id}` | Delete movie | ✅ ADMIN |

---

## 🐳 Docker Deployment

### Multi-Stage Build Process

**Stage 1: Frontend Builder**
- Node 22 Alpine
- Builds Angular app
- Optimized production build

**Stage 2: Backend Builder**
- Maven + JDK 21
- Compiles Spring Boot
- Embeds frontend static files

**Stage 3: Runtime**
- Eclipse Temurin JRE 21
- Single JAR artifact
- Health checks enabled

### Docker Compose Services

| Service | Image | Port | Purpose |
|---------|-------|------|---------|
| **postgres** | postgres:17-alpine | 5432 (internal) | Database |
| **redis** | redis:7-alpine | 6379 (internal) | Caching |
| **app** | mt-app | 3839 | Spring Boot API |

### Quick Start

```bash
# Build and run
docker compose up --build

# Run in background
docker compose up -d

# View logs
docker compose logs -f

# Stop all
docker compose down

# Clean everything
docker compose down -v
```

---

## ⚙️ Configuration

### Application Properties

```properties
# Server
server.port=3839

# Database
spring.datasource.url=jdbc:postgresql://postgres:5432/movieweb
spring.datasource.username=hamza
spring.datasource.password=1111
spring.jpa.hibernate.ddl-auto=create-drop

# Redis Caching
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
spring.cache.redis.time-to-live=600000

# JWT
jwt.secret=your-secret-key
jwt.expirationMs=86400000

# File Upload
app.upload.dir=uploads/
app.upload.max-size=10MB
```

### Environment Variables (Docker)

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/movieweb
SPRING_DATASOURCE_USERNAME=hamza
SPRING_DATASOURCE_PASSWORD=1111
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
```

---

## 🔒 Security

### Authentication
- **JWT (JSON Web Tokens)** for stateless authentication
- **BCrypt** password hashing
- **CORS** configured for frontend communication

### Authorization
- **Role-Based Access Control (RBAC)**
  - `ADMIN`: Full system access
  - `VIEWER`: User features access
- **Method-Level Security** with `@Secured` annotations
- **URL-Level Security** via SecurityConfig

### Endpoints Protection
```
/admin/**        → ADMIN only
/user/**         → VIEWER only
/auth/**         → Public
/movies/**       → Public (with caching)
/categories/**   → Public (with caching)
```

---

## 🧪 Testing

### Unit Tests Included

**FavoriteService** (11 tests)
- Add favorite (success, errors, duplicates)
- Delete favorite (success, not found)
- Get favorites by user

**CommentService** (12 tests)
- Add comment
- Update comment
- Delete comment
- Find by movie ID
- Find by user ID

### Run Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=FavoriteServiceTest

# With coverage
mvn clean test jacoco:report
```

---

## 📊 Caching Strategy

### Cached Endpoints
- `GET /admin/movies` → `adminMovies` cache
- `GET /movies` → `publicMovies` cache
- `GET /categories` → `categories` cache

### Cache Configuration
- **TTL:** 10 minutes (600,000 ms)
- **Type:** Redis (with in-memory fallback)
- **Behavior:** Automatic fallback to ConcurrentMapCacheManager if Redis unavailable

