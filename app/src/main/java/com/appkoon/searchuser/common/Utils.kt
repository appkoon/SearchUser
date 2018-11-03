package com.appkoon.searchuser.common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

fun getResizedHeight(imageWidth: Int, imageHeight:Int, displayWidth: Int) : Int {
    val rate = (displayWidth.toFloat() / imageWidth.toFloat())
    val resizeHeight = (imageHeight.toFloat() * rate)
     return  resizeHeight.toInt()
}

@SuppressLint("SimpleDateFormat")
fun changeDateFormat(dateString: String) : String {
    val oldFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val date = oldFormat.parse(dateString)
    val newFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분 ss초")
    return newFormat.format(date)
}