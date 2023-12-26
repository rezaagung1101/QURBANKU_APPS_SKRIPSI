package com.androidexpert.qurbanku_apps_skripsi.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityWelcomeBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.auth.login.LoginActivity
import com.androidexpert.qurbanku_apps_skripsi.utils.Constanta

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menu1.setImageDrawable(resources.getDrawable(R.drawable.ic_circle_full_24))
        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnNext.setOnClickListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    menu1.setImageDrawable(resources.getDrawable(R.drawable.ic_circle_24))
                    menu2.setImageDrawable(resources.getDrawable(R.drawable.ic_circle_full_24))
                }, 300)
                startAnimation()
                btnNext.isEnabled = false
                btnNext.setBackgroundColor(resources.getColor(R.color.disabled_background))
                btnNext.setTextColor(resources.getColor(R.color.disabled_text))
            }

            cardPanitia.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                intent.putExtra(Constanta.isPanitia, true)
                startActivity(intent)
                finish()
            }

            cardJemaah.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                intent.putExtra(Constanta.isPanitia, false)
                startActivity(intent)
                finish()
            }

        }
    }

    fun startAnimation() {
        binding.apply {
            val tvQuote1 = ObjectAnimator.ofFloat(tvWelcomeQuote1, View.ALPHA, 0f).setDuration(300)
            tvQuote1.interpolator = AccelerateDecelerateInterpolator()

            val tvQuote2 = ObjectAnimator.ofFloat(tvWelcomeQuote2, View.ALPHA, 1f).setDuration(300)
            tvQuote2.interpolator = AccelerateDecelerateInterpolator()

            val tvRoleTitle = ObjectAnimator.ofFloat(tvChooseRole, View.ALPHA, 1f).setDuration(100)
            tvRoleTitle.interpolator = AccelerateDecelerateInterpolator()

            val ivBanner = ObjectAnimator.ofFloat(ivBanner, View.ALPHA, 0f).setDuration(300)
            ivBanner.interpolator = AccelerateDecelerateInterpolator()

            val cardContainer = ObjectAnimator.ofFloat(cardContainer, View.ALPHA, 1f).setDuration(300)
            cardContainer.interpolator = AccelerateDecelerateInterpolator()

            AnimatorSet().apply {
                playTogether(ivBanner, cardContainer, tvRoleTitle, tvQuote1, tvQuote2)
                startDelay = 100
            }.start()
        }
    }

    override fun onBackPressed() {
        finish()
    }

}