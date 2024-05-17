package com.example.minitest2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.minitest2.ui.theme.MiniTest2Theme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniTest2Theme(darkTheme = false) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(Screen.Home.route){
            HomeScreen(navController)
        }
        composable(Screen.ViewCategory.route){
            ViewCategoryScreen(navController)
        }
        composable(Screen.UpdateProduct.route + "/{productId}/{productName}/{productType}/{productPrice}/{productImageLink}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("productName") { type = NavType.StringType },
                navArgument("productType") { type = NavType.StringType },
                navArgument("productPrice") { type = NavType.StringType },
                navArgument("productImageLink") { type = NavType.StringType }
            )) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val productName = backStackEntry.arguments?.getString("productName")
            val productType = backStackEntry.arguments?.getString("productType")
            val productPrice = backStackEntry.arguments?.getString("productPrice")
            val productImageLink = URLDecoder.decode(backStackEntry.arguments?.getString("productImageLink"), StandardCharsets.UTF_8.toString())
            Log.d("NavigationArgs", "productId: $productId, productName: $productName, productType: $productType, productPrice: $productPrice, productImageLink: $productImageLink")
            UpdateProductScreen(navController , productId, productName, productType, productPrice, productImageLink)
        }
    }
}
