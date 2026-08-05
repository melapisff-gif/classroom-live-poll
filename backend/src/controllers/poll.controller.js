const { prisma } = require('../config/database');

const createPoll = async (req, res) => {
  const { classroomId, question, pollType, options, timer } = req.body;

  const poll = await prisma.poll.create({
    data: {
      question,
      pollType,
      timer,
      classroomId,
      teacherId: req.user.id,
      options: { create: options.map((opt, idx) => ({ content: opt.content, index: idx })) },
    },
    include: { options: true },
  });

  res.status(201).json(poll);
};

const getClassroomPolls = async (req, res) => {
  const polls = await prisma.poll.findMany({
    where: { classroomId: req.params.classroomId },
    include: { _count: { select: { responses: true } } },
    orderBy: { createdAt: 'desc' },
  });

  res.json(polls);
};

const getPollById = async (req, res) => {
  const poll = await prisma.poll.findUnique({
    where: { id: req.params.id },
    include: {
      options: true,
      responses: {
        include: { student: { select: { id: true, name: true } } },
      },
    },
  });

  if (!poll) {
    throw Object.assign(new Error('Poll not found'), { status: 404 });
  }

  res.json(poll);
};

const updatePoll = async (req, res) => {
  const { question, options } = req.body;

  await prisma.pollOption.deleteMany({ where: { pollId: req.params.id } });

  const poll = await prisma.poll.update({
    where: { id: req.params.id },
    data: {
      question,
      options: { create: options.map((opt, idx) => ({ content: opt.content, index: idx })) },
    },
    include: { options: true },
  });

  res.json(poll);
};

const deletePoll = async (req, res) => {
  await prisma.poll.delete({ where: { id: req.params.id } });
  res.json({ message: 'Poll deleted successfully' });
};

const startPoll = async (req, res) => {
  const poll = await prisma.poll.update({
    where: { id: req.params.id },
    data: { status: 'ACTIVE' },
    include: { options: true },
  });

  const io = req.app.get('io');
  io.to(`classroom:${poll.classroomId}`).emit('poll_started', {
    pollId: poll.id,
    question: poll.question,
    pollType: poll.pollType,
    options: poll.options,
    timer: poll.timer,
  });

  res.json({ message: 'Poll started' });
};

const pausePoll = async (req, res) => {
  const poll = await prisma.poll.update({
    where: { id: req.params.id },
    data: { status: 'PAUSED' },
  });

  const io = req.app.get('io');
  io.to(`classroom:${poll.classroomId}`).emit('poll_paused', { pollId: poll.id });

  res.json({ message: 'Poll paused' });
};

const resumePoll = async (req, res) => {
  const poll = await prisma.poll.update({
    where: { id: req.params.id },
    data: { status: 'ACTIVE' },
  });

  const io = req.app.get('io');
  io.to(`classroom:${poll.classroomId}`).emit('poll_resumed', { pollId: poll.id });

  res.json({ message: 'Poll resumed' });
};

const stopPoll = async (req, res) => {
  const poll = await prisma.poll.update({
    where: { id: req.params.id },
    data: { status: 'STOPPED' },
  });

  const io = req.app.get('io');
  io.to(`classroom:${poll.classroomId}`).emit('poll_stopped', { pollId: poll.id });

  res.json({ message: 'Poll stopped' });
};

const setCorrectAnswer = async (req, res) => {
  const { correctOptionIndex } = req.body;

  const poll = await prisma.poll.update({
    where: { id: req.params.id },
    data: { correctOptionIndex },
  });

  const io = req.app.get('io');
  io.to(`classroom:${poll.classroomId}`).emit('correct_answer_selected', {
    pollId: poll.id,
    correctOptionIndex,
  });

  res.json({ message: 'Correct answer set' });
};

const publishResults = async (req, res) => {
  const poll = await prisma.poll.update({
    where: { id: req.params.id },
    data: { status: 'COMPLETED' },
    include: {
      responses: {
        include: { student: { select: { id: true, name: true } } },
      },
    },
  });

  const results = poll.responses.map((response) => ({
    studentId: response.studentId,
    studentName: response.student.name,
    selectedIndices: response.selectedIndices,
    isCorrect: response.selectedIndices.includes(poll.correctOptionIndex),
    score: response.selectedIndices.includes(poll.correctOptionIndex) ? 100 : 0,
    responseTime: response.responseTime,
  }));

  const io = req.app.get('io');
  io.to(`classroom:${poll.classroomId}`).emit('results_published', {
    pollId: poll.id,
    correctOptionIndex: poll.correctOptionIndex,
    results,
  });

  res.json({ message: 'Results published' });
};

const submitAnswer = async (req, res) => {
  const { selectedIndices, responseTime } = req.body;
  const pollId = req.params.id;

  const existingResponse = await prisma.pollResponse.findUnique({
    where: { pollId_studentId: { pollId, studentId: req.user.id } },
  });

  if (existingResponse) {
    throw Object.assign(new Error('Already answered this poll'), { status: 409 });
  }

  const response = await prisma.pollResponse.create({
    data: { pollId, studentId: req.user.id, selectedIndices, responseTime },
  });

  const poll = await prisma.poll.findUnique({ where: { id: pollId } });

  const io = req.app.get('io');
  io.to(`classroom:${poll.classroomId}`).emit('student_answered', {
    pollId,
    studentId: req.user.id,
    selectedIndices,
    responseTime,
  });

  const totalResponses = await prisma.pollResponse.count({ where: { pollId } });
  const members = await prisma.classroomMember.count({ where: { classroomId: poll.classroomId } });

  io.to(`classroom:${poll.classroomId}`).emit('response_count_updated', {
    pollId,
    totalResponses,
    totalStudents: members,
  });

  res.status(201).json({ message: 'Answer submitted' });
};

module.exports = {
  createPoll,
  getClassroomPolls,
  getPollById,
  updatePoll,
  deletePoll,
  startPoll,
  pausePoll,
  resumePoll,
  stopPoll,
  setCorrectAnswer,
  publishResults,
  submitAnswer,
};
