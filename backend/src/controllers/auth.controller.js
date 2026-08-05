const { validationResult } = require('express-validator');
const authService = require('../services/auth.service');

const registerTeacher = async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ error: { message: errors.array()[0].msg, code: 'VALIDATION_ERROR' } });
  }

  const { name, email, password } = req.body;
  const result = await authService.registerTeacher(name, email, password);
  res.status(201).json(result);
};

const loginTeacher = async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ error: { message: errors.array()[0].msg, code: 'VALIDATION_ERROR' } });
  }

  const { email, password } = req.body;
  const result = await authService.loginTeacher(email, password);
  res.json(result);
};

const registerStudent = async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ error: { message: errors.array()[0].msg, code: 'VALIDATION_ERROR' } });
  }

  const { name, email, password } = req.body;
  const result = await authService.registerStudent(name, email, password);
  res.status(201).json(result);
};

const loginStudent = async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ error: { message: errors.array()[0].msg, code: 'VALIDATION_ERROR' } });
  }

  const { email, password } = req.body;
  const result = await authService.loginStudent(email, password);
  res.json(result);
};

const refreshToken = async (req, res) => {
  const { refreshToken: token } = req.body;
  const result = await authService.refreshToken(token);
  res.json(result);
};

const logout = async (req, res) => {
  const { refreshToken: token } = req.body;
  await authService.logout(token);
  res.json({ message: 'Logged out successfully' });
};

module.exports = {
  registerTeacher,
  loginTeacher,
  registerStudent,
  loginStudent,
  refreshToken,
  logout,
};
