package com.classpoll.teacher.data.remote

import android.util.Log
import com.classpoll.teacher.data.local.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(
    private val tokenManager: TokenManager
) {
    private var socket: Socket? = null

    fun connect(classroomId: String, onStudentAnswered: (Map<Int, Int>) -> Unit) {
        try {
            val token = runBlocking { tokenManager.accessToken.first() }
            val userId = runBlocking { tokenManager.userId.first() }

            val options = IO.Options.builder()
                .setAuth(mapOf("token" to token))
                .build()

            socket = IO.socket(URI.create("http://10.0.2.2:3000"), options)

            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("SocketManager", "Connected to server")
                val data = JSONObject().apply {
                    put("classroomId", classroomId)
                    put("userId", userId)
                    put("role", "TEACHER")
                }
                socket?.emit("join_classroom", data)
            }

            socket?.on("response_count_updated") { args ->
                try {
                    val data = args[0] as? JSONObject
                    val responseCount = data?.optJSONObject("responseCount")
                    val countMap = mutableMapOf<Int, Int>()
                    responseCount?.keys()?.forEach { key ->
                        countMap[key.toInt()] = responseCount.getInt(key)
                    }
                    onStudentAnswered(countMap)
                } catch (e: Exception) {
                    Log.e("SocketManager", "Error parsing response count", e)
                }
            }

            socket?.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketManager", "Disconnected from server")
            }

            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("SocketManager", "Connection error: ${args[0]}")
            }

            socket?.connect()
        } catch (e: Exception) {
            Log.e("SocketManager", "Error connecting to socket", e)
        }
    }

    fun disconnect(classroomId: String) {
        try {
            val userId = runBlocking { tokenManager.userId.first() }
            val data = JSONObject().apply {
                put("classroomId", classroomId)
                put("userId", userId)
                put("role", "TEACHER")
            }
            socket?.emit("leave_classroom", data)
            socket?.disconnect()
            socket?.off()
            socket = null
        } catch (e: Exception) {
            Log.e("SocketManager", "Error disconnecting socket", e)
        }
    }
}
