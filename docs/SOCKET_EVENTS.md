# Socket.IO Events

## Connection

### Teacher Connection
```javascript
Event: 'teacher_connected'
Payload: { teacherId: string, classroomId: string }
```

### Student Connection
```javascript
Event: 'student_connected'
Payload: { studentId: string, classroomId: string }
```

### Disconnect
```javascript
Event: 'teacher_disconnected'
Payload: { teacherId: string, classroomId: string }

Event: 'student_disconnected'
Payload: { studentId: string, classroomId: string }
```

---

## Poll Events

### Teacher Creates Poll
```javascript
Event: 'teacher_created_poll'
Payload: {
  pollId: string,
  question: string,
  pollType: string,
  options: [{ index: number, content: string }],
  timer: number
}
```

### Teacher Starts Poll
```javascript
Event: 'poll_started'
Payload: {
  pollId: string,
  question: string,
  pollType: string,
  options: [{ index: number, content: string }],
  timer: number
}
```

### Student Receives Poll
```javascript
Event: 'poll_received'
Payload: {
  pollId: string,
  question: string,
  pollType: string,
  options: [{ index: number, content: string }],
  timer: number
}
```

### Student Submits Answer
```javascript
Event: 'student_answered'
Payload: {
  pollId: string,
  studentId: string,
  selectedIndices: number[],
  responseTime: number
}
```

### Timer Updates
```javascript
Event: 'poll_timer_updated'
Payload: { pollId: string, remainingTime: number }
```

### Teacher Pauses Poll
```javascript
Event: 'poll_paused'
Payload: { pollId: string }
```

### Teacher Resumes Poll
```javascript
Event: 'poll_resumed'
Payload: { pollId: string }
```

### Teacher Stops Poll
```javascript
Event: 'poll_stopped'
Payload: { pollId: string }
```

### Teacher Selects Correct Answer
```javascript
Event: 'correct_answer_selected'
Payload: { pollId: string, correctOptionIndex: number }
```

### Teacher Publishes Results
```javascript
Event: 'results_published'
Payload: {
  pollId: string,
  correctOptionIndex: number,
  results: [{
    studentId: string,
    studentName: string,
    selectedIndices: number[],
    isCorrect: boolean,
    score: number,
    responseTime: number
  }]
}
```

### Leaderboard Updates
```javascript
Event: 'leaderboard_updated'
Payload: {
  classroomId: string,
  leaderboard: [{
    rank: number,
    studentId: string,
    studentName: string,
    totalScore: number,
    avgResponseTime: number
  }]
}
```

---

## Live Response Updates (Teacher Only)

### Response Count Update
```javascript
Event: 'response_count_updated'
Payload: {
  pollId: string,
  totalResponses: number,
  totalStudents: number,
  optionCounts: [{ optionIndex: number, count: number }]
}
```

---

## Error Events

### Socket Error
```javascript
Event: 'error'
Payload: { message: string, code: string }
```

---

## Room Management

Each classroom creates a Socket.IO room:
- Room name: `classroom:{classroomId}`
- Teachers and Students join the room on connection
- Events are broadcast to the room
