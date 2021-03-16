package com.juanmacapuano.appmapeo

import android.content.Intent.getIntent
import android.content.Intent.parseUri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.appmapeo.databinding.FragmentItemProjectBinding
import com.juanmacapuano.appmapeo.room.ProjectEntity
import kotlinx.coroutines.launch

class ProjectItemFragment : Fragment() {

    companion object {
       // fun newInstance(): ProjectItemFragment = ProjectItemFragment()
        const val TAG = "ProjectItemFragment"
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_NAME = "project_name"
        private const val KEY_PROJECT_DATE = "project_date"
        private const val KEY_PROJECT_LOCATION = "project_location"
        lateinit var projectEntity : ProjectEntity
    }

    var binding: FragmentItemProjectBinding? = null
    lateinit var viewModel: ProjectListViewModel
    var newRowId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg : Bundle
        projectEntity = ProjectEntity()
        var extras: Bundle? = activity?.intent?.extras
        extras = this.arguments
        if (extras != null) {
            val projectId : Long = extras.getLong(KEY_PROJECT_ID)
            val projectName : String = extras.getString(KEY_PROJECT_NAME)!!
            val projectDate : String = extras.getString(KEY_PROJECT_DATE)!!
            val projectLocation : String = extras.getString(KEY_PROJECT_LOCATION)!!
            projectEntity.name = projectName
            projectEntity.date = projectDate
            projectEntity.location = projectLocation
        }
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
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel: ProjectListViewModel by activityViewModels()
        viewModel = ViewModelProvider(requireActivity()).get(ProjectListViewModel::class.java)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            itemFragment = this@ProjectItemFragment
        }
        binding?.viewModel?.initUpdate(projectEntity)
        binding?.executePendingBindings();
    }

    /** Creates product fragment for specific product ID  */
    fun forProject(projectEntity: ProjectEntity): ProjectItemFragment {
        val fragment: ProjectItemFragment = ProjectItemFragment()
        val args = Bundle()
        args.putLong(KEY_PROJECT_ID, projectEntity.id)
        args.putString(KEY_PROJECT_DATE, projectEntity.date)
        args.putString(KEY_PROJECT_NAME, projectEntity.name)
        args.putString(KEY_PROJECT_LOCATION, projectEntity.location)
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.item_toolbar_search).isVisible = false
        menu.findItem(R.id.item_toolbar_edit).isVisible = false
        menu.findItem(R.id.item_toolbar_confirm).isVisible = true
    }

    fun insertProject() = lifecycleScope.launch {
        val newRowId = viewModel.insertProject()
        if (newRowId > 0) {
            Toast
                .makeText(context, R.string.item_project_save_message_ok, Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_itemProjectFragment_to_listProjectFragment)
        } else Toast
            .makeText(context, "ID: $newRowId", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}