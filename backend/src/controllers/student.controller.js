const { prisma } = require('../config/database');

const getProfile = async (req, res) => {
  const student = await prisma.student.findUnique({
    where: { id: req.user.id },
    select: { id: true, name: true, email: true, avatar: true, rollNumber: true },
  });
  res.json(student);
};

const updateProfile = async (req, res) => {
  const { name, rollNumber } = req.body;
  const student = await prisma.student.update({
    where: { id: req.user.id },
    data: { name, rollNumber },
    select: { id: true, name: true, email: true, rollNumber: true },
  });
  res.json(student);
};

module.exports = { getProfile, updateProfile };
