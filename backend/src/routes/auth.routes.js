const express = require('express');
const router = express.Router();
const { body } = require('express-validator');
const { asyncHandler } = require('../middlewares/errorHandler');
const { registerValidation, loginValidation } = require('../validators/auth.validators');
const authController = require('../controllers/auth.controller');

/**
 * @swagger
 * /auth/teacher/register:
 *   post:
 *     tags: [Auth]
 *     summary: Register a new teacher
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required: [name, email, password]
 *             properties:
 *               name:
 *                 type: string
 *               email:
 *                 type: string
 *               password:
 *                 type: string
 *     responses:
 *       201:
 *         description: Teacher registered successfully
 */
router.post('/teacher/register', registerValidation, asyncHandler(authController.registerTeacher));

/**
 * @swagger
 * /auth/teacher/login:
 *   post:
 *     tags: [Auth]
 *     summary: Login as teacher
 */
router.post('/teacher/login', loginValidation, asyncHandler(authController.loginTeacher));

/**
 * @swagger
 * /auth/student/register:
 *   post:
 *     tags: [Auth]
 *     summary: Register a new student
 */
router.post('/student/register', registerValidation, asyncHandler(authController.registerStudent));

/**
 * @swagger
 * /auth/student/login:
 *   post:
 *     tags: [Auth]
 *     summary: Login as student
 */
router.post('/student/login', loginValidation, asyncHandler(authController.loginStudent));

/**
 * @swagger
 * /auth/refresh-token:
 *   post:
 *     tags: [Auth]
 *     summary: Refresh access token
 */
router.post('/refresh-token', asyncHandler(authController.refreshToken));

/**
 * @swagger
 * /auth/logout:
 *   post:
 *     tags: [Auth]
 *     summary: Logout user
 */
router.post('/logout', asyncHandler(authController.logout));

module.exports = router;
