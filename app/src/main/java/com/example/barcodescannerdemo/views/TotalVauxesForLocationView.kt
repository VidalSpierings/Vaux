package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.barcodescannerdemo.composables.AmountsComposable
import com.example.barcodescannerdemo.ui.theme.AppTheme

class TotalVauxesForLocationView: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            AppTheme {
                AmountsComposable().Composable(
                    descriptionText = "The total number of Vauxes for this location is:",
            )
            }

        }

    }

}