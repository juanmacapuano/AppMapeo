package com.juanmacapuano.appmapeo.projects

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanmacapuano.appmapeo.viewModel.ProjectListViewModel
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentProjectListBinding
import com.juanmacapuano.appmapeo.room.ProjectEntity

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val TAG = "ProjectListFragment"
class ProjectListFragment : Fragment() {

    var binding : FragmentProjectListBinding? = null
    private val sharedViewModel: ProjectListViewModel by activityViewModels()
    lateinit var madapter: ProjectAdapter

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_project_list,
            container,
            false
        )
        madapter = ProjectAdapter({ selectedItem: ProjectEntity -> listItemClicked(selectedItem) })

        binding?.lifecycleOwner = viewLifecycleOwner

        //RecycleView
        binding?.rvListProjects?.apply {
            adapter = madapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding?.faAddProject?.setOnClickListener(View.OnClickListener {
            sharedViewModel.initInsertProject()
            findNavController().navigate(R.id.action_listProjectFragment_to_itemProjectFragment)
        })

        requireActivity().invalidateOptionsMenu()

        // Inflate the layout for this fragment
        return binding?.root
    }

    private fun listItemClicked(selectedItem: ProjectEntity) {
        sharedViewModel.initUpdateProject(selectedItem)
        findNavController().navigate(R.id.action_listProjectFragment_to_itemProjectFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suscribeUI()
        setToolbarConfigurations()
    }

    fun suscribeUI() {
        sharedViewModel.getAllProject().observe(viewLifecycleOwner, Observer {
            madapter.setProjectList(it)
            madapter.notifyDataSetChanged()
            binding?.executePendingBindings()
        })
    }

    fun setToolbarConfigurations() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.item_project_list_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onPrepareOptionsMenu(menu: Menu){
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.item_toolbar_edit)
        item.isVisible = false
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*when (item.itemId) {
            R.id.item_toolbar_edit -> insertProjectConfirm()
            android.R.id.home, R.id.cancelarAlta -> alertDialogBuilder()
            else -> {
            }
        }*/


        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}