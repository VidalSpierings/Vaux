package com.example.barcodescannerdemo.composable_blueprints

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

interface GeodeticCoordinateText: IndicationBox, StandardText {

    @Composable
    fun GeodeticCoordinateTextComposable(coordinateTypeString: String, text: String){
        Text(coordinateTypeString)
        IndicationBoxComposable(
            content = {
                StandardTextComposable(text = text, fontSize = 20)
                      },
            400,
            125,
            75
        )
    }

    // Text (TextView) that shows either a latitude or a longitude

}