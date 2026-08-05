const express = require('express');
const router = express.Router();
const { authenticateToken, requireTeacher } = require('../middlewares/auth');
const { asyncHandler } = require('../middlewares/errorHandler');
const reportController = require('../controllers/report.controller');

/**
 * @swagger
 * /reports/classroom/{classroomId}:
 *   get:
 *     tags: [Reports]
 *     summary: Get classroom reports
 *     security:
 *       - bearerAuth: []
 */
router.get('/classroom/:classroomId', authenticateToken, requireTeacher, asyncHandler(reportController.getClassroomReports));

/**
 * @swagger
 * /reports/{id}/download:
 *   get:
 *     tags: [Reports]
 *     summary: Download report
 *     security:
 *       - bearerAuth: []
 */
router.get('/:id/download', authenticateToken, requireTeacher, asyncHandler(reportController.downloadReport));

module.exports = router;
