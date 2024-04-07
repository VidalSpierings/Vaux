package com.example.barcodescannerdemo.composable_blueprints

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType

interface GeodeticTextField {

    @Composable
    fun GeodeticTextFieldComposable(label: String, onTextFieldChange: (String) -> Unit, text: String = ""){
        TextField(

            value = text,
            onValueChange = onTextFieldChange,
            label = {

                Text(label)

            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            leadingIcon = {

                Icon(
                    painter = rememberVectorPainter(image = Icons.Outlined.Place),
                    contentDescription = ""
                )

            }
        )
    }

    // TextField (EditText) that can work with either a latitude or a longitude

}