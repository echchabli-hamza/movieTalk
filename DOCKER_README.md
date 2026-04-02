# Docker Setup Guide

This project includes Docker configuration for containerized deployment.

## Prerequisites
- Docker installed (20.10+)
- Docker Compose installed (2.0+)

## Quick Start

### Option 1: Using Docker Compose (Recommended)
This will start the entire stack with PostgreSQL, Redis, and the application:

```bash
docker-compose up --build
```

The application will be available at: `http://localhost:3839`

### Option 2: Using Docker Manually

#### 1. Build the Docker image:
```bash
docker build -t mt-app:latest .
```

#### 2. Run with dependencies:
First, start PostgreSQL:
```bash
docker run -d \
  --name postgres_mt \
  -e POSTGRES_DB=movieweb \
  -e POSTGRES_USER=hamza \
  -e POSTGRES_PASSWORD=1111 \
  -p 5432:5432 \
  postgres:17-alpine
```

Start Redis:
```bash
docker run -d \
  --name redis_mt \
  -p 6379:6379 \
  redis:7-alpine
```

Run the application:
```bash
docker run -d \
  --name mt-app \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres_mt:5432/movieweb \
  -e SPRING_DATASOURCE_USERNAME=hamza \
  -e SPRING_DATASOURCE_PASSWORD=1111 \
  -e SPRING_REDIS_HOST=redis_mt \
  -e SPRING_REDIS_PORT=6379 \
  -p 3839:3839 \
  --link postgres_mt \
  --link redis_mt \
  mt-app:latest
```

## Dockerfile Overview

The Dockerfile uses a **multi-stage build** approach:

1. **Builder Stage**: 
   - Uses `maven:3.9.6-eclipse-temurin-21`
   - Compiles the application with `mvn clean install`
   - Creates the JAR file (~100MB)

2. **Runtime Stage**:
   - Uses `eclipse-temurin:21-jre` (slim JRE image)
   - Copies only the built JAR
   - Final image size: ~350MB

## Key Features

- ✅ Multi-stage build for minimal final image
- ✅ Health check configured
- ✅ Environment variables for configuration
- ✅ Volume mounts for uploads
- ✅ Service health checks in docker-compose
- ✅ Automatic service dependency management

## Common Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Rebuild image
docker-compose up -d --build

# Run specific service
docker-compose up -d postgres

# Scale a service
docker-compose up -d --scale app=3
```

## Environment Configuration

You can override environment variables in `docker-compose.yml` or create a `.env` file:

```bash
cat > .env << EOF
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/movieweb
SPRING_DATASOURCE_USERNAME=hamza
SPRING_DATASOURCE_PASSWORD=1111
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
EOF
```

## Troubleshooting

### Application won't start
```bash
# Check logs
docker-compose logs app

# Verify database connection
docker-compose exec app curl -f http://localhost:3839/movies
```

### Port already in use
```bash
# Change port in docker-compose.yml
# Or kill existing process
lsof -i :3839 | grep LISTEN | awk '{print $2}' | xargs kill -9
```

### Clear everything and start fresh
```bash
docker-compose down -v
docker system prune -a
docker-compose up --build
```

## Build Process

The Dockerfile builds the project with:
```bash
mvn clean install -DskipTests
```

This generates `target/MT-0.0.1-SNAPSHOT.jar` which is then copied to the final image.

## Performance Tips

1. Use `.dockerignore` to exclude unnecessary files
2. Multi-stage build reduces final image size
3. JRE-only image faster than full JDK
4. Leverage Docker layer caching
