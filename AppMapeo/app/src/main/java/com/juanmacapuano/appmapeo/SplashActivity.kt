package com.juanmacapuano.appmapeo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.juanmacapuano.appmapeo.databinding.ActivitySplashBinding
import android.view.animation.AnimationUtils
import com.juanmacapuano.appmapeo.projects.ProjectsListActivity

const val SPLASH_TIME_OUT : Long = 3500

class SplashActivity : BaseActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animationUp = AnimationUtils.loadAnimation(this, R.anim.scroll_up)
        val animationDown = AnimationUtils.loadAnimation(this, R.anim.scroll_down)

        binding.apply {
            tvSplashTitle.startAnimation(animationUp)
            tvSplashDevName.startAnimation(animationDown)
            tvSplashVersion.startAnimation(animationUp)
            ivSplashLogo.startAnimation((animationDown))
        }

        Handler().postDelayed({
            startActivity(Intent(this, ProjectsListActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)

    }
}