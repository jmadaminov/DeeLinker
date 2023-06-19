@file:OptIn(ExperimentalStdlibApi::class)

package com.example.deeplinkapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.deeplinkapplication.databinding.ActivityOrdersBinding
import com.example.deeplinkapplication.deeplink.DeeNode
import com.example.deeplinkapplication.deeplink.OrdersDirections
import com.example.deeplinkapplication.deeplink.consumeDeeNodeAs
import com.example.deeplinkapplication.deeplink.deeLinkInto
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrdersActivity : AppCompatActivity() {

    private val pageTitles = listOf("Active", "All")
    private lateinit var binding: ActivityOrdersBinding

    lateinit var pager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPagerWithTabs()


        consumeDeeNodeAs<OrdersDirections>(
            onParamId = { idParam, dlNode ->
                deeLinkInto<OrderActivity>(dlNode) {
                    putExtra(DeeNode.PARAM_ID, idParam)
                }
            },
            onQuery = { query, dlNode ->
                binding.tvQuery.text = query
            },
            consumeBlock = { direction, dlNode ->
                when (direction) {
                    OrdersDirections.ACTIVE -> {
                        pager.setCurrentItem(0, true)
                    }

                    OrdersDirections.ALL -> {
                        pager.setCurrentItem(1, true)
                    }
                }
            })
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