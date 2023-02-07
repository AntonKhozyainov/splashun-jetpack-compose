package ru.khozyainov.splashun.utils

import android.content.Context
import android.util.DisplayMetrics

//Переводим пиксели в dp
fun Int.fromPixelsToDp(context: Context) : Int =
    context.resources.displayMetrics.densityDpi * DisplayMetrics.DENSITY_DEFAULT / this
