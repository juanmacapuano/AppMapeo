package com.juanmacapuano.appmapeo.projects

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.juanmacapuano.appmapeo.BaseActivity
import com.juanmacapuano.appmapeo.ProjectItemFragment
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.ActivityProjectsListBinding
import com.juanmacapuano.appmapeo.room.ProjectEntity
import androidx.appcompat.widget.Toolbar

class ProjectsListActivity : BaseActivity() {

    lateinit var binding: ActivityProjectsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar();

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            val fragment = ProjectListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, ProjectListFragment.TAG).commit()
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        showHomeUpIcon(getString(R.string.item_project_title_toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        return super.onOptionsItemSelected(item)
    }

    fun show(projectEntity: ProjectEntity) {
        val projectItemFragment: ProjectItemFragment =
            ProjectItemFragment().forProject(projectEntity.id)
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("project")
            .replace(
                R.id.fragment_container,
                projectItemFragment, null
            ).commit()
    }
}