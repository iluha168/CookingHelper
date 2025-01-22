package com.iluha168.sigmaweight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iluha168.sigmaweight.ui.screen.MainScreen
import com.iluha168.sigmaweight.ui.screen.WeightScreen
import com.iluha168.sigmaweight.ui.theme.SigmaWeightTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private lateinit var nav: NavHostController

    @Serializable
    private object RouteMainScreen

    @Serializable
    private object RouteWeightScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            nav = rememberNavController()
            SigmaWeightTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavHost(nav, startDestination = RouteMainScreen, builder = ::makeNavGraph)
                    }
                }
            }
        }
    }

    private fun makeNavGraph(builder: NavGraphBuilder) {
        builder.composable<RouteMainScreen> {
            MainScreen { nav.navigate(RouteWeightScreen) }
        }
        builder.composable<RouteWeightScreen> {
            WeightScreen()
        }
    }
}