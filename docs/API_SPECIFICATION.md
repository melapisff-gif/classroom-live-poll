# API Specification

## Base URL
```
Development: http://localhost:3000/api/v1
Production: https://your-domain.com/api/v1
```

## Authentication
All protected routes require:
```
Authorization: Bearer <access_token>
```

---

## Auth Endpoints

### POST /auth/teacher/register
```json
Request: { "name": "string", "email": "string", "password": "string", "phone": "string?" }
Response: { "teacher": { "id", "name", "email" }, "accessToken", "refreshToken" }
```

### POST /auth/teacher/login
```json
Request: { "email": "string", "password": "string" }
Response: { "teacher": { "id", "name", "email" }, "accessToken", "refreshToken" }
```

### POST /auth/student/register
```json
Request: { "name": "string", "email": "string", "password": "string", "rollNumber": "string?" }
Response: { "student": { "id", "name", "email" }, "accessToken", "refreshToken" }
```

### POST /auth/student/login
```json
Request: { "email": "string", "password": "string" }
Response: { "student": { "id", "name", "email" }, "accessToken", "refreshToken" }
```

### POST /auth/refresh-token
```json
Request: { "refreshToken": "string" }
Response: { "accessToken", "refreshToken" }
```

### POST /auth/logout
```json
Request: { "refreshToken": "string" }
Response: { "message": "Logged out successfully" }
```

---

## Teacher Endpoints

### GET /teachers/profile
```json
Response: { "id", "name", "email", "phone", "avatar", "school", "subject" }
```

### PUT /teachers/profile
```json
Request: { "name": "string?", "phone": "string?", "school": "string?", "subject": "string?" }
Response: { "id", "name", "email", "phone", "school", "subject" }
```

---

## Student Endpoints

### GET /students/profile
```json
Response: { "id", "name", "email", "rollNumber", "avatar" }
```

### PUT /students/profile
```json
Request: { "name": "string?", "rollNumber": "string?" }
Response: { "id", "name", "email", "rollNumber" }
```

---

## Classroom Endpoints

### POST /classrooms (Teacher)
```json
Request: { "name": "string", "description": "string?" }
Response: { "id", "name", "description", "joinCode", "createdAt" }
```

### GET /classrooms/teacher (Teacher)
```json
Response: [{ "id", "name", "joinCode", "studentCount", "status", "createdAt" }]
```

### GET /classrooms/:id (Teacher)
```json
Response: { "id", "name", "joinCode", "students": [{ "id", "name", "email", "joinedAt" }], "pollCount" }
```

### PUT /classrooms/:id (Teacher)
```json
Request: { "name": "string?", "description": "string?" }
Response: { "id", "name", "description", "joinCode" }
```

### DELETE /classrooms/:id (Teacher)
```json
Response: { "message": "Classroom deleted successfully" }
```

### POST /classrooms/join (Student)
```json
Request: { "joinCode": "string" }
Response: { "id", "name", "description", "teacherName", "joinedAt" }
```

### GET /classrooms/student (Student)
```json
Response: [{ "id", "name", "teacherName", "joinedAt" }]
```

### DELETE /classrooms/:id/leave (Student)
```json
Response: { "message": "Left classroom successfully" }
```

### DELETE /classrooms/:classroomId/students/:studentId (Teacher)
```json
Response: { "message": "Student removed successfully" }
```

---

## Poll Endpoints

### POST /polls (Teacher)
```json
Request: {
  "classroomId": "string",
  "question": "string",
  "pollType": "SINGLE_CHOICE|MULTIPLE_CHOICE|TRUE_FALSE|YES_NO|INTEGER",
  "options": [{ "content": "string", "index": 0 }],
  "timer": 30
}
Response: { "id", "question", "pollType", "options", "timer", "status" }
```

### GET /polls/classroom/:classroomId (Teacher)
```json
Response: [{ "id", "question", "pollType", "status", "createdAt", "responseCount" }]
```

### GET /polls/:id (Teacher)
```json
Response: { "id", "question", "pollType", "options", "timer", "status", "responses" }
```

### PUT /polls/:id (Teacher)
```json
Request: { "question": "string?", "options": [{ "content": "string", "index": 0 }] }
Response: { "id", "question", "pollType", "options", "timer", "status" }
```

### DELETE /polls/:id (Teacher)
```json
Response: { "message": "Poll deleted successfully" }
```

### POST /polls/:id/start (Teacher)
```json
Response: { "message": "Poll started" }
```
Socket: Emits `poll_started` to classroom

### POST /polls/:id/pause (Teacher)
```json
Response: { "message": "Poll paused" }
```
Socket: Emits `poll_paused` to classroom

### POST /polls/:id/resume (Teacher)
```json
Response: { "message": "Poll resumed" }
```
Socket: Emits `poll_resumed` to classroom

### POST /polls/:id/stop (Teacher)
```json
Response: { "message": "Poll stopped" }
```
Socket: Emits `poll_stopped` to classroom

### POST /polls/:id/correct-answer (Teacher)
```json
Request: { "correctOptionIndex": 0 }
Response: { "message": "Correct answer set" }
```
Socket: Emits `correct_answer_selected` to classroom

### POST /polls/:id/publish (Teacher)
```json
Response: { "message": "Results published" }
```
Socket: Emits `results_published` to classroom

### POST /polls/:id/answer (Student)
```json
Request: { "selectedIndices": [0], "responseTime": 15 }
Response: { "message": "Answer submitted" }
```

---

## Leaderboard Endpoints

### GET /leaderboard/classroom/:classroomId
```json
Response: [{ "rank", "studentId", "studentName", "totalScore", "avgResponseTime" }]
```

### GET /leaderboard/poll/:pollId
```json
Response: [{ "rank", "studentId", "studentName", "score", "responseTime" }]
```

---

## Analytics Endpoints

### GET /analytics/classroom/:classroomId
```json
Response: {
  "totalStudents": 30,
  "studentsOnline": 25,
  "correctPercentage": 65.5,
  "wrongPercentage": 34.5,
  "avgResponseTime": 12.5,
  "optionWiseResponses": [{ "optionIndex": 0, "count": 15 }],
  "mostDifficultQuestion": { "id", "question", "correctPercentage" },
  "mostEasyQuestion": { "id", "question", "correctPercentage" }
}
```

### GET /analytics/poll/:pollId
```json
Response: {
  "totalResponses": 25,
  "correctCount": 18,
  "wrongCount": 7,
  "avgResponseTime": 11.2,
  "optionWiseResponses": [{ "optionIndex": 0, "count": 18 }]
}
```

---

## Report Endpoints

### GET /reports/classroom/:classroomId
```json
Response: [{ "id", "pollId", "question", "totalStudents", "correctPercentage", "generatedAt" }]
```

### GET /reports/:id/download?format=pdf|excel|csv
```json
Response: File download
```

---

## Error Response Format
```json
{
  "error": {
    "message": "Error description",
    "code": "ERROR_CODE"
  }
}
```

## Status Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 409: Conflict
- 500: Internal Server Error
