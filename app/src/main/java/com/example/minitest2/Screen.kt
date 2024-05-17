package com.example.minitest2

sealed class Screen(
    val route : String
) {
    object Home : Screen(
        route = "home"
    )
    object ViewCategory : Screen(
        route = "viewCategory"
    )
    object UpdateProduct : Screen(
        route = "updateProduct"
    )
}