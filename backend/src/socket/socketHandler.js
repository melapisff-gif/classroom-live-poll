const logger = require('../utils/logger');

const initializeSocket = (io) => {
  io.on('connection', (socket) => {
    logger.info(`Socket connected: ${socket.id}`);

    socket.on('join_classroom', ({ classroomId, userId, role }) => {
      socket.join(`classroom:${classroomId}`);
      socket.data = { classroomId, userId, role };

      if (role === 'TEACHER') {
        io.to(`classroom:${classroomId}`).emit('teacher_connected', { teacherId: userId, classroomId });
      } else {
        io.to(`classroom:${classroomId}`).emit('student_connected', { studentId: userId, classroomId });
      }

      logger.info(`${role} ${userId} joined classroom ${classroomId}`);
    });

    socket.on('leave_classroom', ({ classroomId, userId, role }) => {
      socket.leave(`classroom:${classroomId}`);

      if (role === 'TEACHER') {
        io.to(`classroom:${classroomId}`).emit('teacher_disconnected', { teacherId: userId, classroomId });
      } else {
        io.to(`classroom:${classroomId}`).emit('student_disconnected', { studentId: userId, classroomId });
      }

      logger.info(`${role} ${userId} left classroom ${classroomId}`);
    });

    socket.on('poll_started', (data) => {
      io.to(`classroom:${data.classroomId}`).emit('poll_received', data);
    });

    socket.on('student_answered', (data) => {
      io.to(`classroom:${data.classroomId}`).emit('response_count_updated', data);
    });

    socket.on('timer_update', (data) => {
      io.to(`classroom:${data.classroomId}`).emit('poll_timer_updated', data);
    });

    socket.on('disconnect', () => {
      const { classroomId, userId, role } = socket.data || {};
      if (classroomId) {
        if (role === 'TEACHER') {
          io.to(`classroom:${classroomId}`).emit('teacher_disconnected', { teacherId: userId, classroomId });
        } else {
          io.to(`classroom:${classroomId}`).emit('student_disconnected', { studentId: userId, classroomId });
        }
      }
      logger.info(`Socket disconnected: ${socket.id}`);
    });
  });
};

module.exports = { initializeSocket };
