const express = require('express');
const router = express.Router();
const { authenticateToken, requireTeacher } = require('../middlewares/auth');
const { asyncHandler } = require('../middlewares/errorHandler');
const teacherController = require('../controllers/teacher.controller');

/**
 * @swagger
 * /teachers/profile:
 *   get:
 *     tags: [Teachers]
 *     summary: Get teacher profile
 *     security:
 *       - bearerAuth: []
 */
router.get('/profile', authenticateToken, requireTeacher, asyncHandler(teacherController.getProfile));

/**
 * @swagger
 * /teachers/profile:
 *   put:
 *     tags: [Teachers]
 *     summary: Update teacher profile
 *     security:
 *       - bearerAuth: []
 */
router.put('/profile', authenticateToken, requireTeacher, asyncHandler(teacherController.updateProfile));

module.exports = router;
