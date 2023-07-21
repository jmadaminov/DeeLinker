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
import dev.jmadaminov.deelinker.DeeNode
import dev.jmadaminov.deelinker.LinkHandler
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
                customHandlers = listOf(
                    LinkHandler(
                        predicate = { "domain://myorders/all-orders" == it.toString() },
                        onMatch = {
                            startActivity(Intent(this@MainActivity, OrdersActivity::class.java))
                        }),
                    LinkHandler(
                        predicate = {
                            "domain://domain.uz/myorders/.*".toRegex().matches(it.toString())
                        },
                        onMatch = { uri ->
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    OrderActivity::class.java
                                ).apply {
                                    putExtra(
                                        OrderActivity.EXTRA_ORDER_ID,
                                        uri.toString().substringAfterLast("/")
                                    )
                                })
                        })
                )

                segmentAsMetaDataHandlers = listOf(ORDER_ID_KEY to ::isOrderId)
                ignoreSegmentKeys = listOf("uz", "ru", "en")
            },
            onSuccess = { node ->
                when (node) {
                    MainDirections.HOME -> {
                        //DO NOTHING YOU ARE ALREADY HERE
                    }

                    MainDirections.DASHBOARD -> {
                        navController.deeLinkInto(R.id.navigation_dashboard, node.nextNode)
                    }

                    MainDirections.CABINET -> {
                        navController.deeLinkInto(R.id.navigation_cabinet, node.nextNode)
                        if (node.nextNode == null) {
                            node.getMetaData(ORDER_ID_KEY)?.let { orderId ->
                                startActivity(
                                    Intent(this@MainActivity, OrderActivity::class.java).apply {
                                        putExtra(OrderActivity.EXTRA_ORDER_ID, orderId)
                                    })
                            }
                        }
                    }
                }
            }
        )
        intent.data = null
    }

    private fun isOrderId(pathEntry: String): Boolean {
        return pathEntry.toLongOrNull() != null
    }

    companion object {
        private val ORDER_ID_KEY: String = "order_id"
    }
}


fun NavController.deeLinkInto(@IdRes destinationId: Int, node: DeeNode?) {
    navigate(
        destinationId,
        Bundle().apply { putSerializable(DeeNode.NODE_KEY, node) })
}
