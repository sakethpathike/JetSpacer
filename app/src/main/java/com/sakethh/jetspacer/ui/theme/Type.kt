package com.sakethh.jetspacer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sakethh.jetspacer.R

val font = FontFamily(
    Font(R.font.regular, weight = FontWeight.Normal),
    Font(R.font.light, weight = FontWeight.Light),
    Font(R.font.description, weight = FontWeight.Medium),
    Font(R.font.semibold, weight = FontWeight.SemiBold),
    Font(R.font.bold, weight = FontWeight.Bold)
)

val Typography =
    Typography(  // for font towards the text style(heading, description, title and whatever!!)
        headlineLarge = TextStyle(
            fontFamily = font,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ), // title
         headlineMedium =  TextStyle(
            fontFamily = font,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ) /*description and "lyrics" label*/
    )