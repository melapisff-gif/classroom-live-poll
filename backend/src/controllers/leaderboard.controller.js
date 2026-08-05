const { prisma } = require('../config/database');

const getClassroomLeaderboard = async (req, res) => {
  const { classroomId } = req.params;

  const leaderboard = await prisma.leaderboard.groupBy({
    by: ['studentId'],
    where: { poll: { classroomId } },
    _sum: { score: true },
    _avg: { responseTime: true },
    orderBy: { _sum: { score: 'desc' } },
  });

  const result = await Promise.all(
    leaderboard.map(async (entry, index) => {
      const student = await prisma.student.findUnique({
        where: { id: entry.studentId },
        select: { id: true, name: true },
      });

      return {
        rank: index + 1,
        studentId: entry.studentId,
        studentName: student.name,
        totalScore: entry._sum.score,
        avgResponseTime: Math.round(entry._avg.responseTime),
      };
    })
  );

  res.json(result);
};

const getPollLeaderboard = async (req, res) => {
  const { pollId } = req.params;

  const leaderboard = await prisma.leaderboard.findMany({
    where: { pollId },
    include: { student: { select: { id: true, name: true } } },
    orderBy: [{ score: 'desc' }, { responseTime: 'asc' }],
  });

  const result = leaderboard.map((entry, index) => ({
    rank: index + 1,
    studentId: entry.studentId,
    studentName: entry.student.name,
    score: entry.score,
    responseTime: entry.responseTime,
  }));

  res.json(result);
};

module.exports = { getClassroomLeaderboard, getPollLeaderboard };
