package com.juanmacapuano.appmapeo.projects

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.juanmacapuano.appmapeo.BaseActivity
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.ActivityProjectsListBinding
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ajts.androidmads.library.SQLiteToExcel
import com.juanmacapuano.appmapeo.AboutActivity

class ProjectsListActivity : BaseActivity() {

    lateinit var binding: ActivityProjectsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar();
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return navController.navigateUp()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        showHomeUpIcon(getString(R.string.item_project_list_toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.acerca_de -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.exportarDb -> {
                exportDbTo()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showHomeUpIcon(title: String?) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.title = title
        }
    }



}