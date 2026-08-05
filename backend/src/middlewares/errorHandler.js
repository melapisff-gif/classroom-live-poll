const logger = require('../utils/logger');

const errorHandler = (err, req, res, next) => {
  logger.error(err.message, { stack: err.stack });

  if (err.name === 'ValidationError') {
    return res.status(400).json({
      error: { message: err.message, code: 'VALIDATION_ERROR' },
    });
  }

  if (err.name === 'UnauthorizedError' || err.message === 'Invalid token') {
    return res.status(401).json({
      error: { message: 'Unauthorized', code: 'UNAUTHORIZED' },
    });
  }

  if (err.code === 'P2025') {
    return res.status(404).json({
      error: { message: 'Resource not found', code: 'NOT_FOUND' },
    });
  }

  if (err.code === 'P2002') {
    return res.status(409).json({
      error: { message: 'Resource already exists', code: 'CONFLICT' },
    });
  }

  res.status(err.status || 500).json({
    error: {
      message: err.message || 'Internal server error',
      code: err.code || 'INTERNAL_ERROR',
    },
  });
};

const asyncHandler = (fn) => (req, res, next) => {
  Promise.resolve(fn(req, res, next)).catch(next);
};

module.exports = { errorHandler, asyncHandler };
