const { prisma } = require('../config/database');

const getClassroomAnalytics = async (req, res) => {
  const { classroomId } = req.params;

  const members = await prisma.classroomMember.count({ where: { classroomId } });
  const polls = await prisma.poll.findMany({
    where: { classroomId },
    include: { responses: true },
  });

  const totalResponses = polls.reduce((sum, poll) => sum + poll.responses.length, 0);
  const correctResponses = polls.reduce(
    (sum, poll) =>
      sum + poll.responses.filter((r) => r.selectedIndices.includes(poll.correctOptionIndex)).length,
    0
  );

  const avgResponseTime =
    totalResponses > 0
      ? polls.reduce(
          (sum, poll) =>
            sum + poll.responses.reduce((s, r) => s + r.responseTime, 0) / (poll.responses.length || 1),
          0
        ) / (polls.length || 1)
      : 0;

  const optionWise = {};
  polls.forEach((poll) => {
    poll.responses.forEach((r) => {
      r.selectedIndices.forEach((idx) => {
        optionWise[idx] = (optionWise[idx] || 0) + 1;
      });
    });
  });

  let mostDifficult = null;
  let mostEasy = null;

  polls.forEach((poll) => {
    if (poll.responses.length === 0) return;
    const correct = poll.responses.filter((r) => r.selectedIndices.includes(poll.correctOptionIndex)).length;
    const percentage = (correct / poll.responses.length) * 100;

    if (!mostDifficult || percentage < mostDifficult.correctPercentage) {
      mostDifficult = { id: poll.id, question: poll.question, correctPercentage: percentage };
    }
    if (!mostEasy || percentage > mostEasy.correctPercentage) {
      mostEasy = { id: poll.id, question: poll.question, correctPercentage: percentage };
    }
  });

  res.json({
    totalStudents: members,
    correctPercentage: totalResponses > 0 ? (correctResponses / totalResponses) * 100 : 0,
    wrongPercentage: totalResponses > 0 ? ((totalResponses - correctResponses) / totalResponses) * 100 : 0,
    avgResponseTime: Math.round(avgResponseTime),
    optionWiseResponses: Object.entries(optionWise).map(([index, count]) => ({
      optionIndex: parseInt(index),
      count,
    })),
    mostDifficultQuestion: mostDifficult,
    mostEasyQuestion: mostEasy,
  });
};

const getPollAnalytics = async (req, res) => {
  const { pollId } = req.params;

  const poll = await prisma.poll.findUnique({
    where: { id: pollId },
    include: { responses: true },
  });

  const totalResponses = poll.responses.length;
  const correctCount = poll.responses.filter((r) => r.selectedIndices.includes(poll.correctOptionIndex)).length;
  const avgResponseTime =
    totalResponses > 0
      ? poll.responses.reduce((sum, r) => sum + r.responseTime, 0) / totalResponses
      : 0;

  const optionWise = {};
  poll.responses.forEach((r) => {
    r.selectedIndices.forEach((idx) => {
      optionWise[idx] = (optionWise[idx] || 0) + 1;
    });
  });

  res.json({
    totalResponses,
    correctCount,
    wrongCount: totalResponses - correctCount,
    avgResponseTime: Math.round(avgResponseTime),
    optionWiseResponses: Object.entries(optionWise).map(([index, count]) => ({
      optionIndex: parseInt(index),
      count,
    })),
  });
};

module.exports = { getClassroomAnalytics, getPollAnalytics };
