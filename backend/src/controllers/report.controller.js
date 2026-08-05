const { prisma } = require('../config/database');

const getClassroomReports = async (req, res) => {
  const { classroomId } = req.params;

  const polls = await prisma.poll.findMany({
    where: { classroomId, status: 'COMPLETED' },
    include: { responses: true },
    orderBy: { createdAt: 'desc' },
  });

  const reports = polls.map((poll) => {
    const totalStudents = poll.responses.length;
    const correctCount = poll.responses.filter((r) =>
      r.selectedIndices.includes(poll.correctOptionIndex)
    ).length;

    return {
      id: poll.id,
      pollId: poll.id,
      question: poll.question,
      totalStudents,
      correctPercentage: totalStudents > 0 ? (correctCount / totalStudents) * 100 : 0,
      generatedAt: poll.updatedAt,
    };
  });

  res.json(reports);
};

const downloadReport = async (req, res) => {
  const { id } = req.params;
  const { format } = req.query;

  const poll = await prisma.poll.findUnique({
    where: { id },
    include: {
      responses: {
        include: { student: { select: { id: true, name: true, email: true } } },
      },
      options: true,
      classroom: { select: { name: true } },
    },
  });

  if (!poll) {
    throw Object.assign(new Error('Poll not found'), { status: 404 });
  }

  const reportData = {
    classroom: poll.classroom.name,
    question: poll.question,
    pollType: poll.pollType,
    options: poll.options.map((o) => o.content),
    correctAnswer: poll.options.find((o) => o.index === poll.correctOptionIndex)?.content,
    totalResponses: poll.responses.length,
    responses: poll.responses.map((r) => ({
      studentName: r.student.name,
      studentEmail: r.student.email,
      selectedOptions: r.selectedIndices.map((i) => poll.options.find((o) => o.index === i)?.content),
      isCorrect: r.selectedIndices.includes(poll.correctOptionIndex),
      responseTime: r.responseTime,
    })),
  };

  if (format === 'csv') {
    const csv = [
      'Student Name,Email,Selected Options,Correct,Response Time',
      ...reportData.responses.map(
        (r) => `${r.studentName},${r.studentEmail},"${r.selectedOptions.join('; ')}",${r.isCorrect},${r.responseTime}s`
      ),
    ].join('\n');

    res.setHeader('Content-Type', 'text/csv');
    res.setHeader('Content-Disposition', `attachment; filename=report-${id}.csv`);
    return res.send(csv);
  }

  res.json(reportData);
};

module.exports = { getClassroomReports, downloadReport };
