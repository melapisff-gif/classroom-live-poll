const jwt = require('jsonwebtoken');
const { prisma } = require('../config/database');

const generateTokens = async (userId, role) => {
  const accessToken = jwt.sign({ id: userId, role }, process.env.JWT_SECRET, {
    expiresIn: process.env.JWT_EXPIRES_IN || '15m',
  });

  const refreshToken = jwt.sign({ id: userId, role }, process.env.JWT_REFRESH_SECRET, {
    expiresIn: process.env.JWT_REFRESH_EXPIRES_IN || '7d',
  });

  const tokenData = {
    token: refreshToken,
    userType: role,
    expiresAt: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000),
  };

  if (role === 'TEACHER') {
    tokenData.teacherId = userId;
  } else {
    tokenData.studentId = userId;
  }

  await prisma.refreshToken.create({ data: tokenData });

  return { accessToken, refreshToken };
};

const verifyRefreshToken = async (token) => {
  const decoded = jwt.verify(token, process.env.JWT_REFRESH_SECRET);
  const storedToken = await prisma.refreshToken.findUnique({
    where: { token },
  });

  if (!storedToken) {
    throw new Error('Invalid refresh token');
  }

  if (new Date() > storedToken.expiresAt) {
    await prisma.refreshToken.delete({ where: { token } });
    throw new Error('Refresh token expired');
  }

  return decoded;
};

const revokeRefreshToken = async (token) => {
  await prisma.refreshToken.deleteMany({ where: { token } });
};

module.exports = { generateTokens, verifyRefreshToken, revokeRefreshToken };
