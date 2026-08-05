const { prisma } = require('../config/database');
const { v4: uuidv4 } = require('uuid');

const generateJoinCode = () => {
  return uuidv4().substring(0, 8).toUpperCase();
};

const createClassroom = async (req, res) => {
  const { name, description } = req.body;
  const joinCode = generateJoinCode();

  const classroom = await prisma.classroom.create({
    data: { name, description, joinCode, teacherId: req.user.id },
    include: { _count: { select: { members: true } } },
  });

  res.status(201).json(classroom);
};

const getTeacherClassrooms = async (req, res) => {
  const classrooms = await prisma.classroom.findMany({
    where: { teacherId: req.user.id },
    include: { _count: { select: { members: true, polls: true } } },
    orderBy: { createdAt: 'desc' },
  });

  res.json(classrooms);
};

const getClassroomById = async (req, res) => {
  const classroom = await prisma.classroom.findUnique({
    where: { id: req.params.id },
    include: {
      members: {
        include: { student: { select: { id: true, name: true, email: true } } },
      },
      _count: { select: { polls: true } },
    },
  });

  if (!classroom) {
    throw Object.assign(new Error('Classroom not found'), { status: 404 });
  }

  res.json(classroom);
};

const updateClassroom = async (req, res) => {
  const { name, description } = req.body;
  const classroom = await prisma.classroom.update({
    where: { id: req.params.id },
    data: { name, description },
  });

  res.json(classroom);
};

const deleteClassroom = async (req, res) => {
  await prisma.classroom.delete({ where: { id: req.params.id } });
  res.json({ message: 'Classroom deleted successfully' });
};

const joinClassroom = async (req, res) => {
  const { joinCode } = req.body;

  const classroom = await prisma.classroom.findUnique({
    where: { joinCode },
    include: { teacher: { select: { id: true, name: true } } },
  });

  if (!classroom) {
    throw Object.assign(new Error('Invalid join code'), { status: 404 });
  }

  const existingMember = await prisma.classroomMember.findUnique({
    where: { classroomId_studentId: { classroomId: classroom.id, studentId: req.user.id } },
  });

  if (existingMember) {
    throw Object.assign(new Error('Already joined this classroom'), { status: 409 });
  }

  await prisma.classroomMember.create({
    data: { classroomId: classroom.id, studentId: req.user.id },
  });

  res.json({
    id: classroom.id,
    name: classroom.name,
    description: classroom.description,
    teacherName: classroom.teacher.name,
    joinedAt: new Date(),
  });
};

const getStudentClassrooms = async (req, res) => {
  const memberships = await prisma.classroomMember.findMany({
    where: { studentId: req.user.id },
    include: {
      classroom: {
        include: { teacher: { select: { id: true, name: true } } },
      },
    },
  });

  const classrooms = memberships.map((m) => ({
    id: m.classroom.id,
    name: m.classroom.name,
    teacherName: m.classroom.teacher.name,
    joinedAt: m.joinedAt,
  }));

  res.json(classrooms);
};

const leaveClassroom = async (req, res) => {
  await prisma.classroomMember.deleteMany({
    where: { classroomId: req.params.id, studentId: req.user.id },
  });

  res.json({ message: 'Left classroom successfully' });
};

const removeStudent = async (req, res) => {
  const { classroomId, studentId } = req.params;

  await prisma.classroomMember.deleteMany({
    where: { classroomId, studentId },
  });

  res.json({ message: 'Student removed successfully' });
};

module.exports = {
  createClassroom,
  getTeacherClassrooms,
  getClassroomById,
  updateClassroom,
  deleteClassroom,
  joinClassroom,
  getStudentClassrooms,
  leaveClassroom,
  removeStudent,
};
