package com.capstone.reseepe.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.capstone.reseepe.databinding.FragmentEditProfileBinding
import com.capstone.reseepe.util.ViewModelFactory
import java.util.Calendar

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val profileViewModel by viewModels<ProfileViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        setupAction(profileViewModel)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return root
    }

    private fun setupAction(profileViewModel: ProfileViewModel) {
        binding.birthdateEditText.setOnClickListener{

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(

                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->


                    val dat = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
                    binding.birthdateEditText.setText(dat)
                },

                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        binding.buttonSave.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val birthday = binding.birthdateEditText.text.toString()
            profileViewModel.editProfile(name, email, birthday)
        }

        profileViewModel.editProfileResult.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                if (!it.error) {
                    Toast.makeText(context, "Edit profile successful: ${it.message}", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(context, "Edit profile failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
