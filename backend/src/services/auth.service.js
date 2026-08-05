const bcrypt = require('bcryptjs');
const { prisma } = require('../config/database');
const { generateTokens, verifyRefreshToken, revokeRefreshToken } = require('../utils/tokens');

const registerTeacher = async (name, email, password) => {
  const existingTeacher = await prisma.teacher.findUnique({ where: { email } });
  if (existingTeacher) {
    throw Object.assign(new Error('Email already registered'), { status: 409, code: 'EMAIL_EXISTS' });
  }

  const hashedPassword = await bcrypt.hash(password, 12);
  const teacher = await prisma.teacher.create({
    data: { name, email, password: hashedPassword },
    select: { id: true, name: true, email: true },
  });

  const tokens = await generateTokens(teacher.id, 'TEACHER');
  return { teacher, ...tokens };
};

const loginTeacher = async (email, password) => {
  const teacher = await prisma.teacher.findUnique({ where: { email } });
  if (!teacher) {
    throw Object.assign(new Error('Invalid email or password'), { status: 401, code: 'INVALID_CREDENTIALS' });
  }

  const isValidPassword = await bcrypt.compare(password, teacher.password);
  if (!isValidPassword) {
    throw Object.assign(new Error('Invalid email or password'), { status: 401, code: 'INVALID_CREDENTIALS' });
  }

  const tokens = await generateTokens(teacher.id, 'TEACHER');
  return {
    teacher: { id: teacher.id, name: teacher.name, email: teacher.email },
    ...tokens,
  };
};

const registerStudent = async (name, email, password) => {
  const existingStudent = await prisma.student.findUnique({ where: { email } });
  if (existingStudent) {
    throw Object.assign(new Error('Email already registered'), { status: 409, code: 'EMAIL_EXISTS' });
  }

  const hashedPassword = await bcrypt.hash(password, 12);
  const student = await prisma.student.create({
    data: { name, email, password: hashedPassword },
    select: { id: true, name: true, email: true },
  });

  const tokens = await generateTokens(student.id, 'STUDENT');
  return { student, ...tokens };
};

const loginStudent = async (email, password) => {
  const student = await prisma.student.findUnique({ where: { email } });
  if (!student) {
    throw Object.assign(new Error('Invalid email or password'), { status: 401, code: 'INVALID_CREDENTIALS' });
  }

  const isValidPassword = await bcrypt.compare(password, student.password);
  if (!isValidPassword) {
    throw Object.assign(new Error('Invalid email or password'), { status: 401, code: 'INVALID_CREDENTIALS' });
  }

  const tokens = await generateTokens(student.id, 'STUDENT');
  return {
    student: { id: student.id, name: student.name, email: student.email },
    ...tokens,
  };
};

const refreshToken = async (token) => {
  const decoded = await verifyRefreshToken(token);
  const newTokens = await generateTokens(decoded.id, decoded.role);
  await revokeRefreshToken(token);
  return newTokens;
};

const logout = async (token) => {
  await revokeRefreshToken(token);
};

module.exports = {
  registerTeacher,
  loginTeacher,
  registerStudent,
  loginStudent,
  refreshToken,
  logout,
};
