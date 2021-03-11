package com.juanmacapuano.appmapeo.projects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanmacapuano.appmapeo.ProjectListViewModel
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentProjectListBinding
import com.juanmacapuano.appmapeo.room.ProjectEntity

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ProjectListFragment : Fragment() {

    lateinit var binding : FragmentProjectListBinding
    lateinit var projectListViewModel: ProjectListViewModel
    lateinit var madapter: ProjectAdapter


    companion object {
        const val TAG = "ProjectListFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)
        madapter = ProjectAdapter({selectedItem:ProjectEntity->listItemClicked(selectedItem)})

        //ViewModel
        projectListViewModel = ViewModelProvider(this).get(ProjectListViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner  //this

        //RecycleView
        binding.rvListProjects.apply {
            adapter = madapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.faAddProject.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.itemProjectFragment)

        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun listItemClicked(selectedItem: ProjectEntity) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (requireActivity() as ProjectsListActivity).show(selectedItem!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suscribeUI()
    }

    fun suscribeUI() {
        projectListViewModel.getAllProject().observe(this, Observer {
            Log.i("TAG", it.toString())
            madapter.setProjectList(it)
            madapter.notifyDataSetChanged()
            binding.executePendingBindings()
        })
    }


}