package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.barcodescannerdemo.composables.EditPlateauComposable
import com.example.barcodescannerdemo.composables.PalletsComposable
import com.example.barcodescannerdemo.ui.theme.AppTheme

class PalletView: ComponentActivity() {

    override fun onStart() {
        super.onStart()



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            AppTheme {PalletsComposable().Composable()}

        }

    }

}