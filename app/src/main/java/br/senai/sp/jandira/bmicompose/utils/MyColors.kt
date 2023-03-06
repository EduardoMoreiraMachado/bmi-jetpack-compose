package br.senai.sp.jandira.bmicompose.utils

import androidx.compose.ui.graphics.Color

fun getColor(bmi: Double): Color {
    return if(bmi < 18.5)
        Color.Red
    else if (bmi >= 18.5 && bmi < 25)
        Color.Green
    else if (bmi >= 25 && bmi < 30)
        Color(255, 152, 0, 255)
    else
        Color.Red
}