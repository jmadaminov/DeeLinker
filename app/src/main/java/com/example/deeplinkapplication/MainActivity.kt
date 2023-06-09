@file:OptIn(ExperimentalStdlibApi::class)

package com.example.deeplinkapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.deeplinkapplication.databinding.ActivityBottomNavBinding
import com.example.deeplinkapplication.deeplink.DeeManual
import com.example.deeplinkapplication.deeplink.RootSegments
import com.example.deeplinkapplication.deeplink.constructDeeplinkedNodes
import com.example.deeplinkapplication.deeplink.deeplinkInto
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav)
        navView.setupWithNavController(navController)

        intent.data?.let { deeplinkUri ->
            startDeeLinking(deeplinkUri)
        }

        testLinks()
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { deeplinkUri ->
            startDeeLinking(deeplinkUri)
        }
    }

    private fun startDeeLinking(data: Uri) {
        constructDeeplinkedNodes(
            data,
            DeeManual("https://myorders/all-orders") {
                startActivity(Intent(this@MainActivity, OrdersActivity::class.java))
            },
            DeeManual(
                matcher = { url ->
                    "https://uzum.uz/myorders/.*".toRegex().matches(url)
                },
                onMatch = { matchedUrl ->
                    startActivity(Intent(this@MainActivity, OrderActivity::class.java).apply {
                        putExtra(OrderActivity.EXTRA_ORDER_ID, matchedUrl.substringAfterLast("/"))
                    })
                })
        )?.let { deeplinkStartNode ->


            when (RootSegments.values().firstOrNull { it.id == deeplinkStartNode.segment.id }) {
                RootSegments.HOME -> {
                    //DO NOTHING YOU ARE ALREADY HERE
                }

                RootSegments.DASHBOARD -> {
                    navController.deeplinkInto(R.id.navigation_dashboard, deeplinkStartNode.next)
                }

                RootSegments.CABINET -> {
                    navController.deeplinkInto(R.id.navigation_cabinet, deeplinkStartNode.next)
                }

                null -> {
                    println("Unknown deeplink start node (Consuming in MainActivity): ${deeplinkStartNode.segment}")
                }
            }


        }
        intent.data = null




    }

}

