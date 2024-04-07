package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.barcodescannerdemo.composables.EditPalletComposable
import com.example.barcodescannerdemo.ui.theme.AppTheme

class EditPalletView: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            AppTheme {EditPalletComposable().Composable()}

        }

    }

}