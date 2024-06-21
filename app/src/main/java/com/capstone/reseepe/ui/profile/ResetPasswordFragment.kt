package com.capstone.reseepe.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.capstone.reseepe.databinding.FragmentResetPasswordBinding
import com.capstone.reseepe.util.ViewModelFactory

class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileViewModel by viewModels<ProfileViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonSave.setOnClickListener {
            val oldPassword = binding.oldPasswordEditText.text.toString()
            val newPassword = binding.newPasswordEditText.text.toString()
            profileViewModel.resetPassword(oldPassword, newPassword)
        }

        profileViewModel.resetPasswordResult.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                if (!it.error) {
                    Toast.makeText(context, "Password reset successful: ${it.message}", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(context, "Password reset failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
