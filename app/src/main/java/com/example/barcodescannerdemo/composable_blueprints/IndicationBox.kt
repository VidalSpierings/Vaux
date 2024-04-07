package com.example.barcodescannerdemo.composable_blueprints

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.barcodescannerdemo.ui.theme.Blue40

interface IndicationBox {

    @Composable
    fun IndicationBoxComposable(content: @Composable () -> Unit, maxWidth: Int = 1000, minWidth: Int = 300, height: Int = 150){
        Box(
            modifier = Modifier
                .height(height.dp)
                .widthIn(maxWidth.dp, minWidth.dp)
                .border(2.dp, Blue40, shape = RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.Center
        ){

            content()

        }

    }

    /*

    - Box that shows info that can be either shown, or shown and edited, depending on the submitted 'content'

    */

}