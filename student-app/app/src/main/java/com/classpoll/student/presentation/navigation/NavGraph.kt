package com.classpoll.student.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.classpoll.student.presentation.auth.login.LoginScreen
import com.classpoll.student.presentation.auth.register.RegisterScreen
import com.classpoll.student.presentation.dashboard.DashboardScreen
import com.classpoll.student.presentation.classroom.join.JoinClassroomScreen
import com.classpoll.student.presentation.poll.active.ActivePollScreen
import com.classpoll.student.presentation.poll.result.PollResultScreen
import com.classpoll.student.presentation.leaderboard.LeaderboardScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onJoinClassroom = { navController.navigate("join_classroom") },
                onClassroomClick = { classroomId ->
                    navController.navigate("classroom_detail/$classroomId")
                }
            )
        }

        composable("join_classroom") {
            JoinClassroomScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("classroom_detail/{classroomId}") { backStackEntry ->
            val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
            com.classpoll.student.presentation.classroom.detail.ClassroomDetailScreen(
                classroomId = classroomId,
                onLeaderboard = { navController.navigate("leaderboard/$classroomId") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("active_poll/{pollId}") { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            ActivePollScreen(
                pollId = pollId,
                onNavigateToResult = { navController.navigate("poll_result/$pollId") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("poll_result/{pollId}") { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            PollResultScreen(
                pollId = pollId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("leaderboard/{classroomId}") { backStackEntry ->
            val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
            LeaderboardScreen(
                classroomId = classroomId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
