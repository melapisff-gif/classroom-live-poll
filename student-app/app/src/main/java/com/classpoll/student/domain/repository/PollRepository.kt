package com.classpoll.student.domain.repository

import com.classpoll.student.domain.model.Poll
import com.classpoll.student.utils.Resource

interface PollRepository {
    suspend fun getPollById(id: String): Resource<Poll>
    suspend fun submitAnswer(pollId: String, selectedIndices: List<Int>, responseTime: Int): Resource<Unit>
}
