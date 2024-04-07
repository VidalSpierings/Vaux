package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.barcodescannerdemo.composables.EditPlateauComposable
import com.example.barcodescannerdemo.ui.theme.AppTheme

class EditPlateauView: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            AppTheme {
                EditPlateauComposable().Composable(
                currentPlateau = "",
                currentVaux = "",
                currentPlateauHeight = ""
                )
            }

        }

    }

}