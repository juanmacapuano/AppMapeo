package com.juanmacapuano.appmapeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.juanmacapuano.appmapeo.databinding.ItemProjectFragmentBinding

class ProjectItemFragment : Fragment() {

    companion object {
        fun newInstance() : ProjectItemFragment = ProjectItemFragment()
        const val TAG = "ProjectItemFragment"
        private const val KEY_PROJECT_ID = "project_id"
    }

    lateinit var binding: ItemProjectFragmentBinding
    lateinit var viewModel: ProjectListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_project_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ProjectListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    /** Creates product fragment for specific product ID  */
    fun forProject(projectId: Long): ProjectItemFragment {
        val fragment : ProjectItemFragment = ProjectItemFragment()
        val args = Bundle()
        args.putLong(KEY_PROJECT_ID, projectId)
        fragment.setArguments(args)
        return fragment
    }
}