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
import com.example.deeplinkapplication.deeplink.myHosts
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.jmadaminov.deelinker.DeeMatcher
import dev.jmadaminov.deelinker.DeeNode
import dev.jmadaminov.deelinker.buildDeeLinker
import dev.jmadaminov.deelinker.deeConfig

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
        buildDeeLinker<MainDirections>(
            deeplinkUri = data,
            config = deeConfig {
                hosts = myHosts
                deeMatchers = listOf(
                    DeeMatcher("domain://myorders/all-orders") {
                        startActivity(Intent(this@MainActivity, OrdersActivity::class.java))
                    },
                    DeeMatcher(
                        matcher = { url ->
                            "domain://domain.uz/myorders/.*".toRegex().matches(url)
                        },
                        onMatch = { matchedUrl ->
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    OrderActivity::class.java
                                ).apply {
                                    putExtra(
                                        OrderActivity.EXTRA_ORDER_ID,
                                        matchedUrl.substringAfterLast("/")
                                    )
                                })
                        })
                )
                ignoreSegmentKeys = listOf("uz", "ru", "en")
            }
        )?.let { deeStartNode ->
            when (deeStartNode) {
                MainDirections.HOME -> {
                    //DO NOTHING YOU ARE ALREADY HERE
                }

                MainDirections.DASHBOARD -> {
                    navController.deeLinkInto(R.id.navigation_dashboard, deeStartNode.nextNode)
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
