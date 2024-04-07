package com.example.barcodescannerdemo.composable_blueprints

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

interface StandardText {

    @Composable
    fun StandardTextComposable(text: String, fontSize: Int = 40){
        Text(
            text = text,
            fontSize = fontSize.sp
        )
    }

    // regular Text (TextView) for showing text

}