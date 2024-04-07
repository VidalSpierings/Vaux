package com.example.barcodescannerdemo.composable_blueprints

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

interface BottomButtons {

    @Composable
    fun BottomButtonsComposable(context: Context, editIntent: Intent, addIntent: Intent, isSelected: Boolean, deleteOnClick: () -> Unit){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            val context = LocalContext.current

            Button(

                onClick = {
                    if (isSelected) {
                        context.startActivity(editIntent)
                    } else {
                        Toast.makeText(context, "can't edit the current selection", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {

                Text("Edit")

            }

            Button(

                onClick = {context.startActivity(addIntent)}) {

                Text("Add new")

            }

            Button(onClick = deleteOnClick) {

                Text("Delete")

            }

        }
    }

    @Composable
    fun ConfirmButtonComposable(onClickFunctionality: () -> Unit){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){

            Button(

                onClick = onClickFunctionality
            ) {

                Text("Confirm")

            }

        }
    }

    // three bottom buttons that are present in most screens (an option to add, edit and delete a certain object)

}