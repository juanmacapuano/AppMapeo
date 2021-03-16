package com.juanmacapuano.appmapeo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.appmapeo.databinding.FragmentItemProjectBinding

class ProjectItemFragment : Fragment() {

    companion object {
       // fun newInstance(): ProjectItemFragment = ProjectItemFragment()
        const val TAG = "ProjectItemFragment"
        private const val KEY_PROJECT_ID = "project_id"
    }

    lateinit var binding: FragmentItemProjectBinding
    lateinit var viewModel: ProjectListViewModel
    var newRowId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel: ProjectListViewModel by activityViewModels()
        viewModel = ViewModelProvider(requireActivity()).get(ProjectListViewModel::class.java)

        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            itemFragment = this@ProjectItemFragment
        }

        binding.executePendingBindings();
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.returnInsertIdItemProject.observe(viewLifecycleOwner, Observer {
            newRowId = viewModel.returnInsertIdItemProject.value!!
            if (newRowId > 0){
                Toast.makeText(context, R.string.item_project_save_message_ok, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_itemProjectFragment_to_listProjectFragment)
                newRowId = -1
            }else {
                Toast.makeText(context, "ID: $newRowId" , Toast.LENGTH_SHORT).show()
            }
        })
    }

    /** Creates product fragment for specific product ID  */
    fun forProject(projectId: Long): ProjectItemFragment {
        val fragment: ProjectItemFragment = ProjectItemFragment()
        val args = Bundle()
        args.putLong(KEY_PROJECT_ID, projectId)
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.item_toolbar_search).isVisible = false
        menu.findItem(R.id.item_toolbar_edit).isVisible = false
        menu.findItem(R.id.item_toolbar_confirm).isVisible = true
        //super.onCreateOptionsMenu(menu, inflater)
    }

    fun insertProject() {
        Log.e("insertProject", newRowId.toString())
        viewModel.insertProject()

    }
}