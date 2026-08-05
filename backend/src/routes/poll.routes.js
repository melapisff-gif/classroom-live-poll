const express = require('express');
const router = express.Router();
const { authenticateToken, requireTeacher, requireStudent } = require('../middlewares/auth');
const { asyncHandler } = require('../middlewares/errorHandler');
const { pollValidation, answerValidation } = require('../validators/auth.validators');
const pollController = require('../controllers/poll.controller');

/**
 * @swagger
 * /polls:
 *   post:
 *     tags: [Polls]
 *     summary: Create a poll (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/', authenticateToken, requireTeacher, pollValidation, asyncHandler(pollController.createPoll));

/**
 * @swagger
 * /polls/classroom/{classroomId}:
 *   get:
 *     tags: [Polls]
 *     summary: Get polls for classroom (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.get('/classroom/:classroomId', authenticateToken, requireTeacher, asyncHandler(pollController.getClassroomPolls));

/**
 * @swagger
 * /polls/{id}:
 *   get:
 *     tags: [Polls]
 *     summary: Get poll by ID
 *     security:
 *       - bearerAuth: []
 */
router.get('/:id', authenticateToken, asyncHandler(pollController.getPollById));

/**
 * @swagger
 * /polls/{id}:
 *   put:
 *     tags: [Polls]
 *     summary: Update poll (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.put('/:id', authenticateToken, requireTeacher, asyncHandler(pollController.updatePoll));

/**
 * @swagger
 * /polls/{id}:
 *   delete:
 *     tags: [Polls]
 *     summary: Delete poll (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.delete('/:id', authenticateToken, requireTeacher, asyncHandler(pollController.deletePoll));

/**
 * @swagger
 * /polls/{id}/start:
 *   post:
 *     tags: [Polls]
 *     summary: Start poll (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/:id/start', authenticateToken, requireTeacher, asyncHandler(pollController.startPoll));

/**
 * @swagger
 * /polls/{id}/pause:
 *   post:
 *     tags: [Polls]
 *     summary: Pause poll (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/:id/pause', authenticateToken, requireTeacher, asyncHandler(pollController.pausePoll));

/**
 * @swagger
 * /polls/{id}/resume:
 *   post:
 *     tags: [Polls]
 *     summary: Resume poll (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/:id/resume', authenticateToken, requireTeacher, asyncHandler(pollController.resumePoll));

/**
 * @swagger
 * /polls/{id}/stop:
 *   post:
 *     tags: [Polls]
 *     summary: Stop poll (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/:id/stop', authenticateToken, requireTeacher, asyncHandler(pollController.stopPoll));

/**
 * @swagger
 * /polls/{id}/correct-answer:
 *   post:
 *     tags: [Polls]
 *     summary: Select correct answer (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/:id/correct-answer', authenticateToken, requireTeacher, asyncHandler(pollController.setCorrectAnswer));

/**
 * @swagger
 * /polls/{id}/publish:
 *   post:
 *     tags: [Polls]
 *     summary: Publish results (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/:id/publish', authenticateToken, requireTeacher, asyncHandler(pollController.publishResults));

/**
 * @swagger
 * /polls/{id}/answer:
 *   post:
 *     tags: [Polls]
 *     summary: Submit answer (Student)
 *     security:
 *       - bearerAuth: []
 */
router.post('/:id/answer', authenticateToken, requireStudent, answerValidation, asyncHandler(pollController.submitAnswer));

module.exports = router;
