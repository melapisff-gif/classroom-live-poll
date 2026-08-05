# Backend Server Setup Guide

## Local Development

### Prerequisites
- Node.js 18+ installed
- Supabase account (for PostgreSQL database)

### 1. Install Dependencies
```bash
cd backend
npm install
```

### 2. Configure Environment Variables
Copy `.env.example` to `.env` and update:
```bash
cp .env.example .env
```

Edit `.env` file:
```env
# Server
PORT=3000
NODE_ENV=development

# Database - Supabase
DATABASE_URL="postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT.supabase.co:5432/postgres"

# JWT Secrets (generate your own)
JWT_SECRET=your_random_secret_key_1
JWT_REFRESH_SECRET=your_random_secret_key_2
JWT_EXPIRES_IN=15m
JWT_REFRESH_EXPIRES_IN=7d

# CORS
CORS_ORIGIN=http://localhost:3000
```

### 3. Run Database Migration
```bash
npx prisma migrate dev --name init
```

### 4. Seed Database
```bash
npm run db:seed
```

### 5. Start Server
```bash
npm run dev
```

Server runs at: `http://localhost:3000`
API Docs: `http://localhost:3000/api-docs`

---

## Deploy to Render

### Step 1: Create GitHub Repository
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/classroom-live-poll.git
git push -u origin main
```

### Step 2: Create Web Service on Render
1. Go to https://render.com
2. Click **New +** → **Web Service**
3. Connect your GitHub repository
4. Fill settings:

| Field | Value |
|-------|-------|
| Name | `classroom-poll-backend` |
| Runtime | `Node` |
| Build Command | `npm ci && npx prisma generate` |
| Start Command | `node src/server.js` |
| Plan | `Free` |

### Step 3: Add Environment Variables
Go to **Environment** tab and add:

| Key | Value |
|-----|-------|
| `DATABASE_URL` | `postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT.supabase.co:5432/postgres` |
| `JWT_SECRET` | (any random string) |
| `JWT_REFRESH_SECRET` | (any random string) |
| `JWT_EXPIRES_IN` | `15m` |
| `JWT_REFRESH_EXPIRES_IN` | `7d` |
| `CORS_ORIGIN` | `*` |
| `NODE_ENV` | `production` |

### Step 4: Deploy
- Click **Save**
- Render will auto-build and deploy
- Your URL: `https://YOUR_SERVICE_NAME.onrender.com`

---

## Update Teacher App

After deployment, update `NetworkModule.kt`:

```kotlin
private const val BASE_URL = "https://YOUR_SERVICE_NAME.onrender.com/api/v1/"
```

---

## Demo Credentials

| Role | Email | Password |
|------|-------|----------|
| Teacher | teacher@demo.com | password123 |
| Student | student1@demo.com | password123 |
| Join Code | DEMO1234 | - |

---

## API Endpoints

### Auth
- `POST /api/v1/auth/teacher/register`
- `POST /api/v1/auth/teacher/login`
- `POST /api/v1/auth/student/register`
- `POST /api/v1/auth/student/login`
- `POST /api/v1/auth/refresh-token`
- `POST /api/v1/auth/logout`

### Classrooms
- `POST /api/v1/classrooms`
- `GET /api/v1/classrooms/teacher`
- `GET /api/v1/classrooms/:id`
- `PUT /api/v1/classrooms/:id`
- `DELETE /api/v1/classrooms/:id`
- `DELETE /api/v1/classrooms/:classroomId/students/:studentId`

### Polls
- `POST /api/v1/polls`
- `GET /api/v1/polls/classroom/:classroomId`
- `GET /api/v1/polls/:id`
- `PUT /api/v1/polls/:id`
- `DELETE /api/v1/polls/:id`
- `POST /api/v1/polls/:id/start`
- `POST /api/v1/polls/:id/pause`
- `POST /api/v1/polls/:id/resume`
- `POST /api/v1/polls/:id/stop`
- `POST /api/v1/polls/:id/correct-answer`
- `POST /api/v1/polls/:id/publish`
- `POST /api/v1/polls/:id/answer`

### Leaderboard
- `GET /api/v1/leaderboard/classroom/:classroomId`
- `GET /api/v1/leaderboard/poll/:pollId`

### Analytics
- `GET /api/v1/analytics/classroom/:classroomId`
- `GET /api/v1/analytics/poll/:pollId`

---

## Troubleshooting

### Migration Error
```bash
npx prisma migrate reset
npx prisma migrate dev --name init
```

### Database Connection Issue
- Check Supabase is running
- Verify password (URL encode special characters: `@` → `%40`)
- Check IP whitelist in Supabase settings

### Server Not Starting
```bash
# Check Node version
node --version

# Clear cache
rm -rf node_modules
npm install
```
