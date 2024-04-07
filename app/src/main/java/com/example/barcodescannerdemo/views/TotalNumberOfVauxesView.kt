package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.barcodescannerdemo.composables.AmountsComposable
import com.example.barcodescannerdemo.models.TotalNumberOfVauxes
import com.example.barcodescannerdemo.ui.theme.AppTheme

class TotalNumberOfVauxesView: ComponentActivity() {

    private lateinit var model: TotalNumberOfVauxes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = TotalNumberOfVauxes(baseContext)

        setContent{

            AppTheme {AmountsComposable().Composable(amount = model.getTotalNumberOfVauxes().toString())}

        }

    }

    private fun getTotalNumberOfVauxes(): Int{

        return model.getTotalNumberOfVauxes()

    }

}