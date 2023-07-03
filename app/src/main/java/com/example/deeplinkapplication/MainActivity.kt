@file:OptIn(ExperimentalStdlibApi::class)

package com.example.deeplinkapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.deeplinkapplication.databinding.ActivityBottomNavBinding
import com.example.deeplinkapplication.deeplink.MainDirections
import com.example.deeplinkapplication.deeplink.hosts
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.jmadaminov.deelinker.DeeManual
import dev.jmadaminov.deelinker.DeeNode
import dev.jmadaminov.deelinker.buildDeeLinker

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
            startDeeLinker(deeplinkUri)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { deeplinkUri ->
            startDeeLinker(deeplinkUri)
        }
    }

    private fun startDeeLinker(data: Uri) {
        buildDeeLinker(
            deeplinkUri = data,
            root = object : DeeNode {
                override var host: String = ""
                override var segment: String = ""
                override var nextNode: DeeNode? = null
                override val possibleDirections = mutableListOf<DeeNode>(*MainDirections.values())

            },
            hosts = hosts,
            DeeManual("uzum://myorders/all-orders") {
                startActivity(Intent(this@MainActivity, OrdersActivity::class.java))
            },
            DeeManual(
                matcher = { url ->
                    "uzum://uzum.uz/myorders/.*".toRegex().matches(url)
                },
                onMatch = { matchedUrl ->
                    startActivity(Intent(this@MainActivity, OrderActivity::class.java).apply {
                        putExtra(OrderActivity.EXTRA_ORDER_ID, matchedUrl.substringAfterLast("/"))
                    })
                })
        )?.let { deeStartNode ->
            when (MainDirections.values().firstOrNull { it.segment == deeStartNode.segment }) {
                MainDirections.HOME -> {
                    //DO NOTHING YOU ARE ALREADY HERE
                }

                MainDirections.DASHBOARD -> {
                    navController.deeLinkInto(
                        R.id.navigation_dashboard,
                        deeStartNode.nextNode
                    )
                }

                MainDirections.CABINET -> {
                    navController.deeLinkInto(R.id.navigation_cabinet, deeStartNode.nextNode)
                    if (deeStartNode.nextNode == null) {
                        deeStartNode.getIdParam()?.let { orderId ->
                            startActivity(
                                Intent(this@MainActivity, OrderActivity::class.java).apply {
                                    putExtra(OrderActivity.EXTRA_ORDER_ID, orderId)
                                })
                        }
                    }
                }

                null -> {
                    println("Unknown deeplink start node (Consuming in MainActivity): ${deeStartNode.segment}")
                }
            }
        }
        intent.data = null
    }

}


fun NavController.deeLinkInto(@IdRes destinationId: Int, node: DeeNode?) {
    navigate(
        destinationId,
        Bundle().apply { putSerializable(DeeNode.NODE_KEY, node) })
}
