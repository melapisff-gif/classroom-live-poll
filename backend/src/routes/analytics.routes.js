const express = require('express');
const router = express.Router();
const { authenticateToken } = require('../middlewares/auth');
const { asyncHandler } = require('../middlewares/errorHandler');
const analyticsController = require('../controllers/analytics.controller');

/**
 * @swagger
 * /analytics/classroom/{classroomId}:
 *   get:
 *     tags: [Analytics]
 *     summary: Get classroom analytics
 *     security:
 *       - bearerAuth: []
 */
router.get('/classroom/:classroomId', authenticateToken, asyncHandler(analyticsController.getClassroomAnalytics));

/**
 * @swagger
 * /analytics/poll/{pollId}:
 *   get:
 *     tags: [Analytics]
 *     summary: Get poll analytics
 *     security:
 *       - bearerAuth: []
 */
router.get('/poll/:pollId', authenticateToken, asyncHandler(analyticsController.getPollAnalytics));

module.exports = router;
