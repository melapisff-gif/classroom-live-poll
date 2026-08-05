const { body } = require('express-validator');

const registerValidation = [
  body('name').trim().notEmpty().withMessage('Name is required'),
  body('email').isEmail().normalizeEmail().withMessage('Valid email is required'),
  body('password').isLength({ min: 6 }).withMessage('Password must be at least 6 characters'),
];

const loginValidation = [
  body('email').isEmail().normalizeEmail().withMessage('Valid email is required'),
  body('password').notEmpty().withMessage('Password is required'),
];

const classroomValidation = [
  body('name').trim().notEmpty().withMessage('Classroom name is required'),
  body('description').optional().trim(),
];

const joinClassroomValidation = [
  body('joinCode').trim().notEmpty().withMessage('Join code is required'),
];

const pollValidation = [
  body('classroomId').trim().notEmpty().withMessage('Classroom ID is required'),
  body('question').trim().notEmpty().withMessage('Question is required'),
  body('pollType').isIn(['SINGLE_CHOICE', 'MULTIPLE_CHOICE', 'TRUE_FALSE', 'YES_NO', 'INTEGER']),
  body('options').isArray({ min: 2, max: 6 }).withMessage('2-6 options required'),
  body('timer').isInt({ min: 0 }).withMessage('Timer must be a positive integer'),
];

const answerValidation = [
  body('selectedIndices').isArray({ min: 1 }).withMessage('At least one option must be selected'),
  body('responseTime').isInt({ min: 0 }).withMessage('Response time is required'),
];

module.exports = {
  registerValidation,
  loginValidation,
  classroomValidation,
  joinClassroomValidation,
  pollValidation,
  answerValidation,
};
