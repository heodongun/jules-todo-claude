# Board Application

A full-stack CRUD board application built with Next.js, Ktor, Kotlin Coroutines, and MySQL.

## Tech Stack

### Backend
- **Ktor** - Kotlin web framework
- **Kotlin Coroutines** - Asynchronous programming
- **Exposed** - SQL library for Kotlin
- **MySQL** - Database
- **HikariCP** - Connection pooling

### Frontend
- **Next.js 14** - React framework
- **TypeScript** - Type safety
- **SWR** - Data fetching
- **Axios** - HTTP client

## Features

- ✅ Create posts
- ✅ Read posts with pagination
- ✅ Update posts
- ✅ Delete posts
- ✅ Comprehensive test coverage
- ✅ Docker Compose setup
- ✅ MySQL database

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Node.js 20+ (for local frontend development)
- JDK 17+ (for local backend development)

### Running with Docker Compose

```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f
```

Access:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- MySQL: localhost:3306

### Local Development

#### Backend
```bash
cd backend
./gradlew run
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

### Running Tests

#### Backend Tests
```bash
cd backend
./gradlew test
```

#### Frontend Tests
```bash
cd frontend
npm test
```

## API Endpoints

- `GET /posts?page=1&pageSize=10` - Get all posts with pagination
- `GET /posts/{id}` - Get a specific post
- `POST /posts` - Create a new post
- `PUT /posts/{id}` - Update a post
- `DELETE /posts/{id}` - Delete a post
- `GET /health` - Health check

## Project Structure

```
.
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/board/
│   │   │   │   ├── database/
│   │   │   │   ├── models/
│   │   │   │   ├── plugins/
│   │   │   │   ├── routes/
│   │   │   │   └── services/
│   │   │   └── resources/
│   │   └── test/
│   └── build.gradle.kts
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   ├── components/
│   │   ├── lib/
│   │   └── types/
│   └── package.json
└── docker-compose.yml
```

## Environment Variables

### Backend
- `DATABASE_URL` - JDBC connection string
- `DATABASE_USER` - Database username
- `DATABASE_PASSWORD` - Database password

### Frontend
- `NEXT_PUBLIC_API_URL` - Backend API URL

## Development Workflow

This project follows Git Flow methodology. See issues for planned features and improvements.

## License

MIT
