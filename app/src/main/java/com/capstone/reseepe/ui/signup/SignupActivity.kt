package com.capstone.reseepe.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.ActivitySignupBinding
import com.capstone.reseepe.ui.login.LoginActivity
import com.capstone.reseepe.ui.main.MainActivity
import com.capstone.reseepe.util.ViewModelFactory
import java.util.Calendar


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {

        binding.birthdateEditText.setOnClickListener{

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(

                this,
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

        binding.buttonSignup.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val birthday = binding.birthdateEditText.text.toString()
            val password =  binding.passwordEditText.text.toString()
            val confPassword = binding.confirmPasswordEditText.text.toString()

            if (password != confPassword) {
                showCustomDialog(
                    title = "Oops! Unable to complete registration.",
                    message = "Please make sure your passwords input is correct",
                    buttonText = "Try Again"
                ) {
                    finish()
                }

            } else {
                signupViewModel.register(name, email, birthday, password)
            }

            signupViewModel.registerResponse.observe(this) {
                if (it.error == true) {
                    showCustomDialog(
                        title = "Oops! Unable to complete registration.",
                        message = "Please check your details and try again",
                        buttonText = "Try Again"
                    ) {
                        finish()
                    }

                } else {
                    showCustomDialog(
                        title = "Great news! You've successfully registered your account",
                        message = "Login to continue using Reseepe",
                        buttonText = "Login Now"
                    ) {
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }

                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showCustomDialog(title: String, message: String, buttonText: String, onClickAction: () -> Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirmation_auth, null)

        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.tv_message)
        val dialogButton = dialogView.findViewById<Button>(R.id.conf_btn)

        dialogTitle.text = title
        dialogMessage.text = message
        dialogButton.text = buttonText
        dialogButton.setOnClickListener {
            onClickAction()
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogButton.setOnClickListener {
            onClickAction()
            dialog.dismiss()
        }

        dialog.show()
    }

}