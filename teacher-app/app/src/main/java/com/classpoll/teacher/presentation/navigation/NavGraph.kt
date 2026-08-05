package com.classpoll.teacher.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.classpoll.teacher.presentation.analytics.AnalyticsScreen
import com.classpoll.teacher.presentation.auth.login.LoginScreen
import com.classpoll.teacher.presentation.auth.register.RegisterScreen
import com.classpoll.teacher.presentation.classroom.create.CreateClassroomScreen
import com.classpoll.teacher.presentation.classroom.detail.ClassroomDetailScreen
import com.classpoll.teacher.presentation.classroom.edit.EditClassroomScreen
import com.classpoll.teacher.presentation.classroom.joincode.JoinCodeScreen
import com.classpoll.teacher.presentation.dashboard.DashboardScreen
import com.classpoll.teacher.presentation.leaderboard.LeaderboardScreen
import com.classpoll.teacher.presentation.poll.create.CreatePollScreen
import com.classpoll.teacher.presentation.poll.edit.EditPollScreen
import com.classpoll.teacher.presentation.poll.live.LivePollScreen

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
                onCreateClassroom = { navController.navigate("create_classroom") },
                onClassroomClick = { classroomId ->
                    navController.navigate("classroom_detail/$classroomId")
                }
            )
        }

        composable("create_classroom") {
            CreateClassroomScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("classroom_detail/{classroomId}") { backStackEntry ->
            val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
            ClassroomDetailScreen(
                classroomId = classroomId,
                onCreatePoll = { navController.navigate("create_poll/$classroomId") },
                onLivePoll = { pollId ->
                    navController.navigate("live_poll/$classroomId/$pollId")
                },
                onEditPoll = { pollId ->
                    navController.navigate("edit_poll/$pollId")
                },
                onEditClassroom = {
                    navController.navigate("edit_classroom/$classroomId")
                },
                onLeaderboard = { navController.navigate("leaderboard/$classroomId") },
                onAnalytics = { navController.navigate("analytics/$classroomId") },
                onJoinCode = { navController.navigate("join_code/$classroomId") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("create_poll/{classroomId}") { backStackEntry ->
            val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
            CreatePollScreen(
                classroomId = classroomId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("live_poll/{classroomId}/{pollId}") { backStackEntry ->
            val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            LivePollScreen(
                classroomId = classroomId,
                pollId = pollId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("join_code/{classroomId}") { backStackEntry ->
            val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
            JoinCodeScreen(
                classroomId = classroomId,
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

        composable("analytics/{classroomId}") { backStackEntry ->
            val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
            AnalyticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("edit_classroom/{classroomId}") { backStackEntry ->
            EditClassroomScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("edit_poll/{pollId}") { backStackEntry ->
            EditPollScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
