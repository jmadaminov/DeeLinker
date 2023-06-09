package com.example.deeplinkapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deeplinkapplication.databinding.ActivityOrderBinding
import com.example.deeplinkapplication.deeplink.consumeDeeNodeAs

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_ORDER_ID)?.let { orderId ->
            binding.tvSelectedOrder.text = orderId
        }

//        consumeDeeNodeAs<Any>()?.let { dlNode ->
//            when (dlNode) {
//                else -> {
//                    print("WARNING! UNKNOWN DEEPLINK NODE! CAN'T GO FURTHER!")
//                }
//            }
//
//        }
    }

    companion object {
        const val EXTRA_ORDER_ID = "order_id"
    }

}