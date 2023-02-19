package ru.khozyainov.splashun.utils

import android.app.ActivityManager
import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics

//Переводим пиксели в dp
fun Int.fromPixelsToDp(context: Context) : Int =
    context.resources.displayMetrics.densityDpi * DisplayMetrics.DENSITY_DEFAULT / this

fun String.underLine(): String {
    val spanStr = SpannableString(this)
    spanStr.setSpan(UnderlineSpan(), 0, spanStr.length, 0)
    return spanStr.toString()
}

fun Long.getLikeCountString(): String = when {
    this >= 1000000 -> "${
        this.toString().substring(0, this.toString().length - 6)
    }kk"
    this >= 1000 -> "${
        this.toString().substring(0, this.toString().length - 3)
    }k"
    else -> this.toString()
}

fun Context.isAppInForeground(): Boolean {

    val application = this.applicationContext
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningProcessList = activityManager.runningAppProcesses

    if (runningProcessList != null) {
        val myApp = runningProcessList.find { it.processName == application.packageName }
        ActivityManager.getMyMemoryState(myApp)
        return myApp?.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    }

    return false
}
