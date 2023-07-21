@file:OptIn(ExperimentalStdlibApi::class)

package com.example.deeplinkapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.deeplinkapplication.OrderActivity.Companion.EXTRA_ORDER_ID
import com.example.deeplinkapplication.databinding.ActivityOrdersBinding
import com.example.deeplinkapplication.deeplink.OrdersDirections
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.jmadaminov.deelinker.DeeNode
import dev.jmadaminov.deelinker.consumeDeeNodeAs

class OrdersActivity : AppCompatActivity() {

    private val pageTitles = listOf("Active", "All")
    private lateinit var binding: ActivityOrdersBinding

    lateinit var pager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPagerWithTabs()

        DeeNode.getMetaData(EXTRA_ORDER_ID)?.let {
            startActivity(
                Intent(this, OrderActivity::class.java).apply {
                    putExtra(EXTRA_ORDER_ID, it)
                })
        }

        consumeDeeNodeAs<OrdersDirections> { dlNode ->
            when (dlNode) {
                OrdersDirections.ACTIVE -> {
                    pager.setCurrentItem(0, true)
                }

                OrdersDirections.ALL -> {
                    pager.setCurrentItem(1, true)
                }
            }
        }
    }

    private fun setupViewPagerWithTabs() {
        binding.viewpager.adapter = DemoViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = pageTitles[position]
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    pager.setCurrentItem(tab?.position ?: 0, true)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })
        pager = binding.viewpager
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }
}
