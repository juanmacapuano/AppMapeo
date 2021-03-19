package com.juanmacapuano.appmapeo.mapeos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanmacapuano.appmapeo.ProjectListViewModel
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

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun listItemClicked(selectedItem: MapeoEntity) {
        findNavController().navigate(R.id.action_itemProjectFragment_to_mapeoListFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suscribeUI()
    }

    private fun suscribeUI() {
        sharedViewModel.getAllMapeos().observe(viewLifecycleOwner, Observer {
            madapter.setMapeoList(it)
            madapter.notifyDataSetChanged()
            binding.executePendingBindings()
        })
    }
}