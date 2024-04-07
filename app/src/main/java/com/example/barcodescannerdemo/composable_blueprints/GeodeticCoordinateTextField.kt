package com.example.barcodescannerdemo.composable_blueprints

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

interface GeodeticCoordinateTextField: IndicationBox, GeodeticTextField {

    @Composable
    fun GeodeticCoordinateTextFieldComposable(
        coordinateTypeString: String,
        text: String,
        onTextFieldChange: (String) -> Unit){
        Text(coordinateTypeString)

        IndicationBoxComposable(
            // Komt hierboven een error?
            // Gelieve IndicationBox te verwijderen als implementerende interface ->
            // clean project ->
            // opnieuw toevoegen als implementerende interface ->
            // clean project. Dit zou het probleem moeten verhelpen
            content = {
                GeodeticTextFieldComposable(
                    label = coordinateTypeString,
                    onTextFieldChange = onTextFieldChange,
                    text = text,
                )
                      },
            400,
            125,
            75

        )
    }

    // TextField (EditText) that can work with either a latitude or a longitude

}