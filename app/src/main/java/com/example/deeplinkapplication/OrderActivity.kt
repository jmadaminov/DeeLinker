package com.example.deeplinkapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deeplinkapplication.databinding.ActivityOrderBinding
import dev.jmadaminov.deelinker.DeeNode

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_ORDER_ID)?.let { orderId ->
            binding.tvSelectedOrder.text = orderId
        }

       DeeNode.getMetaData(EXTRA_ORDER_ID)?.let { orderId ->
            binding.tvSelectedOrder.text = orderId
        }

    }

    companion object {
        const val EXTRA_ORDER_ID = "order_id"
    }

}
