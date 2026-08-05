# Classroom Live Poll System - Architecture

## System Overview

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Teacher App    │     │  Backend Server │     │  Student App    │
│  (Android)      │◄───►│  (Node.js)      │◄───►│  (Android)      │
│  Kotlin/Compose │     │  Express/Socket │     │  Kotlin/Compose │
└─────────────────┘     └────────┬────────┘     └─────────────────┘
                                 │
                                 ▼
                        ┌─────────────────┐
                        │   PostgreSQL    │
                        │   Database      │
                        └─────────────────┘
```

## Three Independent Projects

1. **Teacher Android App** (`com.classpoll.teacher`)
2. **Student Android App** (`com.classpoll.student`)
3. **Backend Server** (Node.js + Express + PostgreSQL)

## Technology Stack

### Teacher & Student Apps
- Kotlin + Jetpack Compose
- Material 3 Design
- MVVM Architecture
- Clean Architecture (Data/Domain/Presentation layers)
- Hilt (Dependency Injection)
- Navigation Compose
- Retrofit + OkHttp (Networking)
- Socket.IO Client (Real-time)
- Room Database (Local cache)
- DataStore Preferences
- Kotlin Coroutines + StateFlow

### Backend
- Node.js + Express.js
- Socket.IO (Real-time)
- PostgreSQL + Prisma ORM
- JWT Authentication + Refresh Tokens
- Bcrypt (Password hashing)
- Docker + Docker Compose
- Swagger (API docs)
- Winston (Logging)
- Helmet, CORS, Rate Limiter

## Navigation Flow

### Teacher App
```
Splash → Login/Register → Dashboard → Create/Manage Classroom
                                   → Create Poll → Live Poll → Results
                                   → Leaderboard
                                   → Analytics & Reports
                                   → Profile & Settings
```

### Student App
```
Splash → Login/Register → Dashboard → Join Classroom
                                   → Receive Live Poll → Submit Answer
                                   → View Results → Leaderboard
                                   → Profile & Settings
```

## Real-time Flow
```
1. Teacher starts poll → Socket emits 'poll_started'
2. Backend broadcasts → Students receive 'poll_received'
3. Students submit → Socket emits 'student_answered'
4. Backend updates → Teacher receives live response counts
5. Teacher stops poll → Socket emits 'poll_stopped'
6. Teacher selects correct answer → Socket emits 'correct_answer_selected'
7. Teacher publishes → Socket emits 'results_published'
8. Leaderboard auto-updates → Socket emits 'leaderboard_updated'
```
