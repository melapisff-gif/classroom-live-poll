const express = require('express');
const router = express.Router();
const { authenticateToken, requireTeacher, requireStudent } = require('../middlewares/auth');
const { asyncHandler } = require('../middlewares/errorHandler');
const { classroomValidation, joinClassroomValidation } = require('../validators/auth.validators');
const classroomController = require('../controllers/classroom.controller');

/**
 * @swagger
 * /classrooms:
 *   post:
 *     tags: [Classrooms]
 *     summary: Create a classroom (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.post('/', authenticateToken, requireTeacher, classroomValidation, asyncHandler(classroomController.createClassroom));

/**
 * @swagger
 * /classrooms/teacher:
 *   get:
 *     tags: [Classrooms]
 *     summary: Get all classrooms for teacher
 *     security:
 *       - bearerAuth: []
 */
router.get('/teacher', authenticateToken, requireTeacher, asyncHandler(classroomController.getTeacherClassrooms));

/**
 * @swagger
 * /classrooms/{id}:
 *   get:
 *     tags: [Classrooms]
 *     summary: Get classroom by ID
 *     security:
 *       - bearerAuth: []
 */
router.get('/:id', authenticateToken, asyncHandler(classroomController.getClassroomById));

/**
 * @swagger
 * /classrooms/{id}:
 *   put:
 *     tags: [Classrooms]
 *     summary: Update classroom (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.put('/:id', authenticateToken, requireTeacher, asyncHandler(classroomController.updateClassroom));

/**
 * @swagger
 * /classrooms/{id}:
 *   delete:
 *     tags: [Classrooms]
 *     summary: Delete classroom (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.delete('/:id', authenticateToken, requireTeacher, asyncHandler(classroomController.deleteClassroom));

/**
 * @swagger
 * /classrooms/join:
 *   post:
 *     tags: [Classrooms]
 *     summary: Join classroom (Student)
 *     security:
 *       - bearerAuth: []
 */
router.post('/join', authenticateToken, requireStudent, joinClassroomValidation, asyncHandler(classroomController.joinClassroom));

/**
 * @swagger
 * /classrooms/student:
 *   get:
 *     tags: [Classrooms]
 *     summary: Get all classrooms for student
 *     security:
 *       - bearerAuth: []
 */
router.get('/student', authenticateToken, requireStudent, asyncHandler(classroomController.getStudentClassrooms));

/**
 * @swagger
 * /classrooms/{id}/leave:
 *   delete:
 *     tags: [Classrooms]
 *     summary: Leave classroom (Student)
 *     security:
 *       - bearerAuth: []
 */
router.delete('/:id/leave', authenticateToken, requireStudent, asyncHandler(classroomController.leaveClassroom));

/**
 * @swagger
 * /classrooms/{classroomId}/students/{studentId}:
 *   delete:
 *     tags: [Classrooms]
 *     summary: Remove student from classroom (Teacher)
 *     security:
 *       - bearerAuth: []
 */
router.delete('/:classroomId/students/:studentId', authenticateToken, requireTeacher, asyncHandler(classroomController.removeStudent));

module.exports = router;
