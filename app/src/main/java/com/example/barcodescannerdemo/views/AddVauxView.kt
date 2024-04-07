package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.barcodescannerdemo.composables.AddOrEditVauxComposable
import com.example.barcodescannerdemo.ui.theme.AppTheme

class AddVauxView: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            AppTheme {
                AddOrEditVauxComposable().Composable(
                currentPlusCode = "",
                currentVaux = "",
                currentLatitude = "",
                currentLongitude = "",
                currentNumPlateaus = "",
                currentIsAttached = false
            )
            }

        }

    }

}