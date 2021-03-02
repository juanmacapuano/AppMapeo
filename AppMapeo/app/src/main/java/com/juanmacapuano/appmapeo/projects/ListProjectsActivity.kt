package com.juanmacapuano.appmapeo.projects

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.juanmacapuano.appmapeo.BaseActivity
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.ActivityListProjectsBinding

class ListProjectsActivity : BaseActivity() {

    lateinit var binding: ActivityListProjectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}