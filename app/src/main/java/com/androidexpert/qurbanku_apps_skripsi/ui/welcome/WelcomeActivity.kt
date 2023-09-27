package com.androidexpert.qurbanku_apps_skripsi.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.androidexpert.qurbanku_apps_skripsi.R
import com.androidexpert.qurbanku_apps_skripsi.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private var isPanitia: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menu1.setImageDrawable(resources.getDrawable(R.drawable.ic_circle_full_24))
        onAction()
    }

    private fun onAction() {
        binding.btnNext.setOnClickListener {
            binding.apply {
                Handler(Looper.getMainLooper()).postDelayed({
                    menu1.setImageDrawable(resources.getDrawable(R.drawable.ic_circle_24))
                    menu2.setImageDrawable(resources.getDrawable(R.drawable.ic_circle_full_24))
                }, 300)
            }
            this.startAnimation()
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_actor, R.layout.spinner_actor_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_actor_item)
            // Apply the adapter to the spinner
            binding.btnSpinner.adapter = adapter
        }
        binding.btnSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    //set action
                    if (position != 0) isPanitia = false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    isPanitia = true
                }
            }

    }

    private fun startAnimation() {
        binding.apply {
            val tvQuote1 =
                ObjectAnimator.ofFloat(tvWelcomeQuote1, View.ALPHA, 0f).setDuration(100)
            val tvQuote2 =
                ObjectAnimator.ofFloat(tvWelcomeQuote2, View.ALPHA, 1f).setDuration(100)
            val spinner = ObjectAnimator.ofFloat(layoutSpinner, View.ALPHA, 1f).setDuration(100)
            AnimatorSet().apply {
                playSequentially(tvQuote1, tvQuote2, spinner)
                startDelay = 100
            }.start()
        }
    }

}