const { prisma } = require('../config/database');

const getProfile = async (req, res) => {
  const teacher = await prisma.teacher.findUnique({
    where: { id: req.user.id },
    select: { id: true, name: true, email: true, phone: true, avatar: true, school: true, subject: true },
  });
  res.json(teacher);
};

const updateProfile = async (req, res) => {
  const { name, phone, school, subject } = req.body;
  const teacher = await prisma.teacher.update({
    where: { id: req.user.id },
    data: { name, phone, school, subject },
    select: { id: true, name: true, email: true, phone: true, school: true, subject: true },
  });
  res.json(teacher);
};

module.exports = { getProfile, updateProfile };
