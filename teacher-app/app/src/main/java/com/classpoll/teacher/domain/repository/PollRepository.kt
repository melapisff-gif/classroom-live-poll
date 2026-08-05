package com.classpoll.teacher.domain.repository

import com.classpoll.teacher.domain.model.Poll
import com.classpoll.teacher.domain.model.PollDetail
import com.classpoll.teacher.domain.model.PollSummary
import com.classpoll.teacher.utils.Resource

interface PollRepository {
    suspend fun createPoll(classroomId: String, question: String, pollType: String, options: List<Pair<String, Int>>, timer: Int): Resource<Poll>
    suspend fun getClassroomPolls(classroomId: String): Resource<List<PollSummary>>
    suspend fun getPollById(id: String): Resource<PollDetail>
    suspend fun updatePoll(id: String, question: String?, options: List<Pair<String, Int>>?): Resource<Poll>
    suspend fun deletePoll(id: String): Resource<Unit>
    suspend fun startPoll(id: String): Resource<Unit>
    suspend fun pausePoll(id: String): Resource<Unit>
    suspend fun resumePoll(id: String): Resource<Unit>
    suspend fun stopPoll(id: String): Resource<Unit>
    suspend fun setCorrectAnswer(pollId: String, optionIndex: Int): Resource<Unit>
    suspend fun publishResults(pollId: String): Resource<Unit>
}
