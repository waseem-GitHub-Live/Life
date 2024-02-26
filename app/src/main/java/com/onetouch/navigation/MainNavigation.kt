package com.onetouch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onetouch.screens.ChatScreen
import com.onetouch.screens.ExoPlayerView
import com.onetouch.screens.HomeScreen
import com.onetouch.screens.StartScreen
//import com.onetouch.screens.StoryScreen

@Preview
@Composable
fun MainNavigation() {

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = START_SCREEN){
        composable(START_SCREEN){
            StartScreen(navHostController)
        }
        composable(HOME_SCREEN){
            HomeScreen(navHostController)
        }
        composable(CHAT_SCREEN){
            ChatScreen(navHostController)
        }
        composable(VIDEO_SCREEN){
            ExoPlayerView( "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",fullScreen = true)
        }
        composable(STORY_SCREEN){
//            StoryScreen( navHostController)
        }
    }

}

const val START_SCREEN  = "Start screen"
const val HOME_SCREEN = "Home screen"
const val CHAT_SCREEN = "Char screen"
const val VIDEO_SCREEN = "video screen"
const val STORY_SCREEN = "video screen"

//@Preview
//@Composable
//fun MainNavigationPreview() {
//    // Create a NavController instance using rememberNavController
//    val navHostController = rememberNavController()
//
//    // Call your MainNavigation composable passing the NavController
//    MainNavigation( navHostController,  START_SCREEN)
//}