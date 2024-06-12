package com.aloysius.voicehealthguide.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.aloysius.dicoding.data.pref.UserModel
import com.aloysius.dicoding.view.customview.EmailEditText
import com.aloysius.dicoding.view.customview.PasswordEditText
import com.aloysius.voicehealthguide.MainActivity
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.databinding.ActivityLoginBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword


        setupView()
        setupAction()
        setupObserver()
        playAnimation()
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
        binding.loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (validateInputText()) {
                showLoading(true)
                viewModel.login(email, password)
            } else {
                binding.loginButton.isEnabled = false
            }
        }

        binding.edLoginEmail.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })
        binding.edLoginPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            validateInputText()
        })

        validateInputText()
    }

    private fun setupObserver() {
        viewModel.loginResponse.observe(this) { response ->
            response?.let {
                showLoading(false)
                val token = it.data?.accessToken ?: ""
                saveSessionAndProceed(UserModel(token, true, 0L))
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                showLoading(false)
                showErrorDialog(it)
            }
        }
    }

    private fun validateInputText(): Boolean {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8

        binding.loginButton.isEnabled = isEmailValid && isPasswordValid
        return isEmailValid && isPasswordValid
    }

    private fun saveSessionAndProceed(userModel: UserModel) {
        viewModel.saveSession(userModel)
        showLoginSuccessDialog()
    }

    private fun showLoginSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(context.getString(R.string.login_succes_dialog))
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK", null)
            create()
            show()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailEditTextLayout,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}
