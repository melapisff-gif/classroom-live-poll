const jwt = require('jsonwebtoken');
const { prisma } = require('../config/database');

const authenticateToken = async (req, res, next) => {
  try {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
      return res.status(401).json({
        error: { message: 'Access token required', code: 'TOKEN_REQUIRED' },
      });
    }

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    let user = null;
    if (decoded.role === 'TEACHER') {
      user = await prisma.teacher.findUnique({ where: { id: decoded.id } });
    } else if (decoded.role === 'STUDENT') {
      user = await prisma.student.findUnique({ where: { id: decoded.id } });
    }

    if (!user) {
      return res.status(401).json({
        error: { message: 'User not found', code: 'USER_NOT_FOUND' },
      });
    }

    req.user = { ...user, role: decoded.role };
    next();
  } catch (error) {
    if (error.name === 'JsonWebTokenError') {
      return res.status(401).json({
        error: { message: 'Invalid token', code: 'INVALID_TOKEN' },
      });
    }
    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({
        error: { message: 'Token expired', code: 'TOKEN_EXPIRED' },
      });
    }
    next(error);
  }
};

const requireTeacher = (req, res, next) => {
  if (req.user.role !== 'TEACHER') {
    return res.status(403).json({
      error: { message: 'Teacher access required', code: 'TEACHER_REQUIRED' },
    });
  }
  next();
};

const requireStudent = (req, res, next) => {
  if (req.user.role !== 'STUDENT') {
    return res.status(403).json({
      error: { message: 'Student access required', code: 'STUDENT_REQUIRED' },
    });
  }
  next();
};

module.exports = { authenticateToken, requireTeacher, requireStudent };
