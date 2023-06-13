package com.example.deeplinkapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.deeplinkapplication.deeplink.buildDeeLinker

class ComposeActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildDeeLinker(Uri.parse("https://uzum.uz/user/order/400/feedback"))
        setContentView(R.layout.activity_main)

        setContent {

        }
    }






}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DeeplinkApplicationTheme {
//        Greeting("Android")
//    }
//}