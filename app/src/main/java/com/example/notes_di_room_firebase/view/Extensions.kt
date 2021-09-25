package com.example.notes_di_room_firebase.view

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.model.Color
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMM.yy HH:mm"

fun Date.format(): String = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(this)

fun Color.getColorInt(context: Context): Int =
    ContextCompat.getColor(context, getColorRes())

fun Color.getColorRes(): Int = when(this) {
    Color.WHITE -> R.color.color_white
    Color.BLUE -> R.color.color_blue
    Color.GREEN -> R.color.color_green
    Color.PINK -> R.color.color_pink
    Color.RED -> R.color.color_red
    Color.VIOLET -> R.color.color_violet
    Color.YELLOW -> R.color.color_yellow
}

fun sortArray(list: List<Int?>?): List<Int> =
    list?.filterNotNull()
        ?.distinct()
        ?.sorted()
        ?.reversed()
        ?: listOf()



