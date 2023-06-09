package com.example.deeplinkapplication.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.deeplinkapplication.OrderActivity
import com.example.deeplinkapplication.OrdersActivity
import com.example.deeplinkapplication.databinding.FragmentCabinetBinding
import com.example.deeplinkapplication.deeplink.CabinetSegments
import com.example.deeplinkapplication.deeplink.consumeDlNodeInFragAs
import com.example.deeplinkapplication.deeplink.deeplinkInto

class CabinetFragment : Fragment() {

    private var _binding: FragmentCabinetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cabinetViewModel =
            ViewModelProvider(this)[CabinetViewModel::class.java]

        _binding = FragmentCabinetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.btnOrders.setOnClickListener {
            startActivity(Intent(requireActivity(), OrdersActivity::class.java))
        }
        val textView: TextView = binding.textNotifications
        cabinetViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        consumeDlNodeInFragAs<CabinetSegments> { segment, nextDlNode ->
            when (segment) {
                CabinetSegments.ORDERS -> {
                    deeplinkInto<OrdersActivity>(nextDlNode.next) {
                        putExtra(OrderActivity.EXTRA_ORDER_ID, nextDlNode.getIdParam())
                    }
                }
                CabinetSegments.PROFILE -> {

                }
                null -> {}
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
