package com.example.barcodescannerdemo.composable_blueprints

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

interface StandardTextField {

    @Composable
    fun StandardTextFieldComposable(label: String, onTextFieldChange: (String) -> Unit, text: String = ""){
        TextField(

            value = text,
            onValueChange = onTextFieldChange,
            label = {

                Text(label)

            }
        )
    }

    // regular TextField (EditText) for showing and editing info

}