package com.juanmacapuano.appmapeo.mapeos

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanmacapuano.appmapeo.viewModel.ProjectListViewModel
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentMapeoListBinding
import com.juanmacapuano.appmapeo.room.MapeoEntity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

class MapeoListFragment : Fragment() {

    lateinit var binding: FragmentMapeoListBinding
    lateinit var madapter: MapeoAdapter
    private val sharedViewModel: ProjectListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mapeo_list,
            container,
            false
        )

        madapter = MapeoAdapter({ selectedItem: MapeoEntity -> listItemClicked(selectedItem) })

        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvListMapeos.apply {
            adapter = madapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.faAddMapeo.setOnClickListener(View.OnClickListener {
            sharedViewModel.initInsertMapeo()
            findNavController().navigate(R.id.action_mapeoListFragment_to_mapeoItemFragment)
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun listItemClicked(selectedItem: MapeoEntity) {
        sharedViewModel.initUpdateMapeo(selectedItem)
        findNavController().navigate(R.id.action_mapeoListFragment_to_mapeoItemFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suscribeUI()
        setToolbarConfigurations()
    }

    private fun suscribeUI() {
        sharedViewModel.getAllMapeos().observe(viewLifecycleOwner, Observer {
            madapter.setMapeoList(it)
            madapter.notifyDataSetChanged()
            binding.executePendingBindings()
        })
    }

    fun setToolbarConfigurations() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.item_mapeo_list_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu){
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.item_toolbar_search).isVisible = false
        menu.findItem(R.id.item_toolbar_edit).isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
    }
}