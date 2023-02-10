package ru.khozyainov.splashun.utils

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
