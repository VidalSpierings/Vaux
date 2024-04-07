package com.example.barcodescannerdemo.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.barcodescannerdemo.composables.VauxComposable
import com.example.barcodescannerdemo.ui.theme.AppTheme

class VauxView : ComponentActivity() {

    // (Please be aware of the fact that it is a convention to start Composable Object functions with a capital letter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            AppTheme {VauxComposable().Composable()}
        }

        /*

        setContent {

            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {

                    Button(onClick = {
                        val options = ScanOptions()
                        options.setPrompt("Scan de barcode sukkel")
                        options.setBeepEnabled(false)
                        options.setCaptureActivity(MyCaptureActivity::class.java)
                        barLauncher.launch(options)
                    }){

                        Text("Scan")

                    }



                }
            }
        }

        */

    }

@Composable
fun Greeting(name: String, componentActivity: ComponentActivity) {

GreetingPreview()

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        //Greeting("Android")
    }

}
}