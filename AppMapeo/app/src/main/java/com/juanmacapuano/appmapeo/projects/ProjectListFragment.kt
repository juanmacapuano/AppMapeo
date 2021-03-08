package com.juanmacapuano.appmapeo.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanmacapuano.appmapeo.MapeoAplication
import com.juanmacapuano.appmapeo.ProjectListViewModel
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentProjectListBinding
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.appmapeo.ProjectItemFragment

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ProjectListFragment : Fragment() {

    lateinit var binding : FragmentProjectListBinding
    lateinit var projectListViewModel: ProjectListViewModel


    companion object {
        const val TAG = "ProjectListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_project_list, container, false
        )
        binding.rvListProjects.apply {
            adapter = ProjectListAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }

        //ViewModel
        projectListViewModel = ViewModelProvider(this).get(ProjectListViewModel::class.java)

        binding.faAddProject.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.itemProjectFragment)

        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}