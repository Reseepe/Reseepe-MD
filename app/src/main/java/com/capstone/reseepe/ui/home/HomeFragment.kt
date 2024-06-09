package com.capstone.reseepe.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.FragmentHomeBinding
import com.capstone.reseepe.ui.adapter.CarouselAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi ViewPager2 dan Adapter
        val viewPager: ViewPager2 = binding.viewPager
        val images = listOf(
            R.drawable.exp_img,
            R.drawable.exp_img,
            R.drawable.exp_img,
            )
        val adapter = CarouselAdapter(images)
        viewPager.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
