package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.barcodescannerdemo.composables.AddPlateauComposable
import com.example.barcodescannerdemo.ui.theme.AppTheme

class AddPlateauView: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            AppTheme {AddPlateauComposable().Composable()}

        }

    }

}