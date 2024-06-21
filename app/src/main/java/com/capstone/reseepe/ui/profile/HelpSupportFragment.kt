package com.capstone.reseepe.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.capstone.reseepe.databinding.FragmentHelpSupportBinding

class HelpSupportFragment : Fragment() {

    private var _binding: FragmentHelpSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpSupportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.faq1.setOnClickListener {
            toggleAnswerVisibility(binding.answer1)
        }

        binding.faq2.setOnClickListener {
            toggleAnswerVisibility(binding.answer2)
        }

        binding.faq3.setOnClickListener {
            toggleAnswerVisibility(binding.answer3)
        }

        return root
    }

    private fun toggleAnswerVisibility(answerView: View) {
        if (answerView.visibility == View.VISIBLE) {
            answerView.visibility = View.GONE
        } else {
            answerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
