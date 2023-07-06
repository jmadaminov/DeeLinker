package com.example.deeplinkapplication.ui.cabinet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.deeplinkapplication.OrdersActivity
import com.example.deeplinkapplication.databinding.FragmentCabinetBinding
import com.example.deeplinkapplication.deeplink.CabinetDirections
import dev.jmadaminov.deelinker.consumeDeeNodeInFragAs
import dev.jmadaminov.deelinker.deeLinkInto

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


        consumeDeeNodeInFragAs<CabinetDirections> { segment, deeNode ->
            when (segment) {
                CabinetDirections.ORDERS -> {
                    deeLinkInto<OrdersActivity>(deeNode)
                }

                CabinetDirections.PROFILE -> {

                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
