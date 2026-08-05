const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcryptjs');

const prisma = new PrismaClient();

const seed = async () => {
  try {
    console.log('Seeding database...');

    // Create demo teacher
    const teacherPassword = await bcrypt.hash('password123', 12);
    const teacher = await prisma.teacher.upsert({
      where: { email: 'teacher@demo.com' },
      update: {},
      create: {
        name: 'Demo Teacher',
        email: 'teacher@demo.com',
        password: teacherPassword,
        school: 'Demo School',
        subject: 'General',
      },
    });
    console.log('Teacher created:', teacher.email);

    // Create demo students
    const studentPassword = await bcrypt.hash('password123', 12);
    const students = [];
    for (let i = 1; i <= 5; i++) {
      const student = await prisma.student.upsert({
        where: { email: `student${i}@demo.com` },
        update: {},
        create: {
          name: `Student ${i}`,
          email: `student${i}@demo.com`,
          password: studentPassword,
          rollNumber: `ROLL${String(i).padStart(3, '0')}`,
        },
      });
      students.push(student);
    }
    console.log('Students created:', students.length);

    // Create demo classroom
    const classroom = await prisma.classroom.upsert({
      where: { joinCode: 'DEMO1234' },
      update: {},
      create: {
        name: 'Demo Classroom',
        description: 'A demo classroom for testing',
        joinCode: 'DEMO1234',
        teacherId: teacher.id,
      },
    });
    console.log('Classroom created:', classroom.joinCode);

    // Add students to classroom
    for (const student of students) {
      await prisma.classroomMember.upsert({
        where: { classroomId_studentId: { classroomId: classroom.id, studentId: student.id } },
        update: {},
        create: { classroomId: classroom.id, studentId: student.id },
      });
    }
    console.log('Students added to classroom');

    // Create demo poll
    const poll = await prisma.poll.create({
      data: {
        question: 'What is the capital of France?',
        pollType: 'SINGLE_CHOICE',
        timer: 30,
        status: 'COMPLETED',
        correctOptionIndex: 0,
        classroomId: classroom.id,
        teacherId: teacher.id,
        options: {
          create: [
            { content: 'Paris', index: 0 },
            { content: 'London', index: 1 },
            { content: 'Berlin', index: 2 },
            { content: 'Madrid', index: 3 },
          ],
        },
      },
    });
    console.log('Poll created:', poll.question);

    console.log('Database seeded successfully!');
    console.log('\nDemo Credentials:');
    console.log('Teacher: teacher@demo.com / password123');
    console.log('Student: student1@demo.com / password123');
    console.log('Join Code: DEMO1234');
  } catch (error) {
    console.error('Seeding error:', error);
  } finally {
    await prisma.$disconnect();
  }
};

seed();
