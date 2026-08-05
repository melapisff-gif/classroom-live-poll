const express = require('express');
const router = express.Router();
const { authenticateToken } = require('../middlewares/auth');
const { asyncHandler } = require('../middlewares/errorHandler');
const leaderboardController = require('../controllers/leaderboard.controller');

/**
 * @swagger
 * /leaderboard/classroom/{classroomId}:
 *   get:
 *     tags: [Leaderboard]
 *     summary: Get classroom leaderboard
 *     security:
 *       - bearerAuth: []
 */
router.get('/classroom/:classroomId', authenticateToken, asyncHandler(leaderboardController.getClassroomLeaderboard));

/**
 * @swagger
 * /leaderboard/poll/{pollId}:
 *   get:
 *     tags: [Leaderboard]
 *     summary: Get poll leaderboard
 *     security:
 *       - bearerAuth: []
 */
router.get('/poll/:pollId', authenticateToken, asyncHandler(leaderboardController.getPollLeaderboard));

module.exports = router;
