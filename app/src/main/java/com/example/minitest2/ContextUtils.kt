package com.example.minitest2

import android.app.Activity
import android.app.Application
import android.content.Context

object ContextUtils {
    fun isContextValid(context: Context?): Boolean {
        return when (context) {
            is Activity -> !context.isFinishing
            is Application -> true
            else -> false
        }
    }
}