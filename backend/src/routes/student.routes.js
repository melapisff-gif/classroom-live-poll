const express = require('express');
const router = express.Router();
const { authenticateToken, requireStudent } = require('../middlewares/auth');
const { asyncHandler } = require('../middlewares/errorHandler');
const studentController = require('../controllers/student.controller');

/**
 * @swagger
 * /students/profile:
 *   get:
 *     tags: [Students]
 *     summary: Get student profile
 *     security:
 *       - bearerAuth: []
 */
router.get('/profile', authenticateToken, requireStudent, asyncHandler(studentController.getProfile));

/**
 * @swagger
 * /students/profile:
 *   put:
 *     tags: [Students]
 *     summary: Update student profile
 *     security:
 *       - bearerAuth: []
 */
router.put('/profile', authenticateToken, requireStudent, asyncHandler(studentController.updateProfile));

module.exports = router;
