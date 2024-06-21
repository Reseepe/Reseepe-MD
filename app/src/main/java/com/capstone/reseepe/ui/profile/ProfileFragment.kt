package com.capstone.reseepe.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.capstone.reseepe.R
import com.capstone.reseepe.data.pref.UserModel
import com.capstone.reseepe.databinding.FragmentProfileBinding
import com.capstone.reseepe.ui.main.MainViewModel
import com.capstone.reseepe.util.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel by viewModels<ProfileViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        // Show loading indicator
//        binding.progressBar.visibility = View.VISIBLE
//        binding.profileContainer.visibility = View.GONE

        profileViewModel.userProfile.observe(viewLifecycleOwner, Observer { profileResponse ->
//            // Hide loading indicator
//            binding.progressBar.visibility = View.GONE
//            binding.profileContainer.visibility = View.VISIBLE

            profileResponse?.let {
                binding.tvUserName.text = it.name
                binding.tvUserEmail.text = it.email
            }
        })

        profileViewModel.fetchProfile()

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog(profileViewModel)
        }

        binding.btnResetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_resetPasswordFragment)
        }

        binding.ivEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_editProfileFragment)
        }

        binding.btnHelp.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_helpSupportFragment)
        }

        binding.btnAboutApp.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_aboutAppFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun showLogoutConfirmationDialog(profileViewModel: ProfileViewModel) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout_confirmation, null)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<View>(R.id.btnLogout).setOnClickListener {
            profileViewModel.logout()
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}