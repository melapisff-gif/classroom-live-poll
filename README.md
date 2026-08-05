# Classroom Live Poll System

A complete production-ready Classroom Live Poll System similar to Kahoot, Quizizz, and Mentimeter, customized for coaching institutes, schools, and colleges.

## Project Structure

```
classroom-live-poll/
├── teacher-app/          # Teacher Android App (Kotlin + Jetpack Compose)
├── student-app/          # Student Android App (Kotlin + Jetpack Compose)
├── backend/              # Backend Server (Node.js + Express + PostgreSQL)
├── database/             # Database scripts
├── docker/               # Docker configuration
└── docs/                 # Documentation
```

## Tech Stack

### Teacher & Student Apps
- Kotlin + Jetpack Compose
- Material 3 Design
- MVVM Architecture
- Clean Architecture
- Hilt (Dependency Injection)
- Retrofit + OkHttp (Networking)
- Socket.IO Client (Real-time)
- Room Database
- DataStore Preferences

### Backend
- Node.js + Express.js
- Socket.IO (Real-time)
- PostgreSQL + Prisma ORM
- JWT Authentication
- Docker + Docker Compose

## Getting Started

### Backend Setup

1. Navigate to backend folder:
```bash
cd backend
```

2. Install dependencies:
```bash
npm install
```

3. Set up environment variables:
```bash
cp .env.example .env
```

4. Run database migrations:
```bash
npx prisma migrate dev
```

5. Seed the database:
```bash
npm run db:seed
```

6. Start the server:
```bash
npm run dev
```

### Docker Setup

1. Navigate to docker folder:
```bash
cd docker
```

2. Start all services:
```bash
docker-compose up -d
```

### Android Apps Setup

1. Open `teacher-app` or `student-app` in Android Studio
2. Sync Gradle files
3. Run on emulator or device

## Demo Credentials

- **Teacher:** teacher@demo.com / password123
- **Student:** student1@demo.com / password123
- **Join Code:** DEMO1234

## Features

### Teacher Features
- Create/Edit/Delete Classrooms
- Generate Join Codes
- Create/Edit/Delete Polls
- Start/Pause/Resume/Stop Polls
- View Live Responses
- Select Correct Answers
- Publish Results
- View Leaderboard
- View Analytics

### Student Features
- Join Classrooms
- Receive Live Polls
- Submit Answers
- View Results
- View Leaderboard
- View Performance

## API Documentation

Access Swagger API documentation at: `http://localhost:3000/api-docs`

## Real-time Events

The system uses Socket.IO for real-time communication:
- Teacher joins classroom
- Student joins classroom
- Poll starts/pauses/resumes/stops
- Student submits answer
- Results published
- Leaderboard updates
