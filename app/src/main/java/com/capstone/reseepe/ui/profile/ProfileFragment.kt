package com.capstone.reseepe.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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

        profileViewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            if (userModel.isLogin) {
                profileViewModel.fetchProfile(userModel.token)
            } else {
                // Handle case where user is not logged in
            }
        })

        profileViewModel.profile.observe(viewLifecycleOwner, Observer { user ->
            binding.tvUserName.text = user.name
            binding.tvUserEmail.text =user.email
        })

        profileViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            // Handle error message
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })


        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog(profileViewModel)
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