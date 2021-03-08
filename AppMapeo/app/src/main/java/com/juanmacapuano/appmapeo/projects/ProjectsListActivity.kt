package com.juanmacapuano.appmapeo.projects

import android.os.Bundle
import com.juanmacapuano.appmapeo.BaseActivity
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.ActivityProjectsListBinding

class ProjectsListActivity : BaseActivity() {

    lateinit var binding: ActivityProjectsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            val fragment = ProjectListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, ProjectListFragment.TAG).commit()
        }
    }
}