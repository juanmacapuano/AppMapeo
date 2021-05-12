package com.juanmacapuano.appmapeo.projects

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.appmapeo.tools.DatePickerFragment
import com.juanmacapuano.appmapeo.model.ProjectListViewModel
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentItemProjectBinding
import com.juanmacapuano.appmapeo.room.ProjectEntity
import kotlinx.coroutines.launch

class ProjectItemFragment : Fragment() {

    companion object {
        const val TAG = "ProjectItemFragment"
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_NAME = "project_name"
        private const val KEY_PROJECT_DATE = "project_date"
        private const val KEY_PROJECT_LOCATION = "project_location"

        lateinit var projectEntity : ProjectEntity
    }

    var binding: FragmentItemProjectBinding? = null
    private val sharedViewModel: ProjectListViewModel by activityViewModels()
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectEntity = ProjectEntity()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_item_project,
            container,
            false
        )

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            itemFragment = this@ProjectItemFragment
        }
        if (isUpdate) {
            binding?.viewModel?.initUpdateProject(projectEntity)
        }
        binding?.executePendingBindings();

        sharedViewModel.message.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        if (sharedViewModel.isUpdateOrDeleteProject) {
            setEditableFields(false)
            //binding?.btnItemProjectAdd?.isGone = true
        } else {
            setEditableFields(true)
            sharedViewModel.initInsertProject()
        }

        setToolbarConfigurations()
    }

    private fun setToolbarConfigurations() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.item_project_item_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setEditableFields(state : Boolean) {
        binding?.etItemProjectTitle?.isEnabled = state
        binding?.etItemProjectDate?.isEnabled = state
        binding?.etItemProjectLocation?.isEnabled = state
        sharedViewModel.setVisibility(state)
    }

    override fun onPrepareOptionsMenu(menu: Menu){
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.item_toolbar_search).isVisible = false
        menu.findItem(R.id.item_toolbar_edit).isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_toolbar_edit -> showBtnAddOrUpdate()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBtnAddOrUpdate() {

        //binding?.btnItemProjectAdd?.isGone = false
        setEditableFields(true)
    }

    fun insertOrUpdateProject() = lifecycleScope.launch {
        var newRowId = -1
        val checkEmptyFields = sharedViewModel.checkEmptyFieldsProject()
        if (checkEmptyFields) {
            newRowId = sharedViewModel.insertOrUpdateProject().toInt()
            if (newRowId > 0) {
                Toast
                    .makeText(context, R.string.item_project_save_message_ok, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.listProjectFragment)
            } else Toast
                .makeText(context, "ID: $newRowId", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun goToMapeoFragmentList() {
        findNavController().navigate(R.id.action_itemProjectFragment_to_mapeoListFragment)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun showDatePickerDialog() {
        val newFragmentDialogDate = DatePickerFragment()
        activity?.supportFragmentManager?.let { newFragmentDialogDate.show(it, "datePicker") }
    }
}