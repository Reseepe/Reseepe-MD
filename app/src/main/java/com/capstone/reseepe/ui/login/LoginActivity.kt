package com.capstone.reseepe.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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
import com.capstone.reseepe.data.pref.UserModel
import com.capstone.reseepe.databinding.ActivityLoginBinding
import com.capstone.reseepe.ui.main.MainActivity
import com.capstone.reseepe.ui.signup.SignupActivity
import com.capstone.reseepe.util.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        loginViewModel.isLoading.observe(this) {
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

        binding.buttonLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginViewModel.login(email, password)
        }

        loginViewModel.loginResult.observe(this) {
            if (it.error == true) {
                showCustomDialog(
                    title = "Oops! Unable to log in.",
                    message = "Something went wrong with your login. Please try again.",
                    buttonText = "Try Again"
                ) {}
            } else {
                showCustomDialog(
                    title = "All set! You're logged in.",
                    message = "Continue to explore Reseepe",
                    buttonText = "Continue"
                ) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.tvSignup2.setOnClickListener {
                startActivity(Intent(this, SignupActivity::class.java))
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