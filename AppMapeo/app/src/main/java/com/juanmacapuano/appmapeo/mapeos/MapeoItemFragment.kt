package com.juanmacapuano.appmapeo.mapeos

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.juanmacapuano.appmapeo.model.ProjectListViewModel
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentItemMapeoBinding
import com.juanmacapuano.appmapeo.room.MapeoEntity
import kotlinx.coroutines.launch


const val PHOTO_EDITOR_REQUEST_CODE = 231

class MapeoItemFragment : Fragment() {


    lateinit var binding : FragmentItemMapeoBinding
    lateinit var mapeoEntity: MapeoEntity
    private val sharedViewModel: ProjectListViewModel by activityViewModels()
    private val isUpdate = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_mapeo, container, false)
        setHasOptionsMenu(true)
        mapeoEntity = MapeoEntity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            itemFragmentmapeo = this@MapeoItemFragment
        }

        if (isUpdate) {
            binding.viewModel?.initUpdateMapeo(mapeoEntity)
        }

        binding.executePendingBindings()

        sharedViewModel.message.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        if (sharedViewModel.isUpdateOrDeleteMapeo) {
            setEditableFields(false)
            binding.btnItemMapeoAdd.isGone = true
            binding.btnItemMapeoTakePhoto.isGone = false
        } else {
            binding.btnItemMapeoTakePhoto.isGone = true
            setEditableFields(true)
            sharedViewModel.initInsertMapeo()
        }

        setToolbarConfigurations()

        fillSpinner()

    }

    private fun fillSpinner() {

        val spinnerFindingType = binding.etMapeoFindingTypeSp

        val sharedPreferences = activity?.getSharedPreferences("spinnerItemType", Context.MODE_PRIVATE)
        val size : Int = sharedPreferences?.getInt(R.string.key_spinner.toString() + "_size", 0) ?: return
        val arrayListPref : ArrayList<String> = arrayListOf()
        var item : String?
        for (i in 0 until size) {
            item = sharedPreferences.getString(R.string.key_spinner.toString() + "_" + i, null)
            if (item != null) {
                arrayListPref.add(item)
            }
        }

        val arrayAdapter =  ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_item,
            arrayListPref.toList() as MutableList<*>
        )

        spinnerFindingType.setAdapter(arrayAdapter)
        spinnerFindingType.threshold = 1
    }

    private fun setToolbarConfigurations() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.item_mapeo_item_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setEditableFields(state: Boolean) {
        binding.etItemMapeoNumber.isEnabled = state
        binding.etMapeoGridNumber.isEnabled = state
        binding.etMapeoNumberStationTotal.isEnabled = state
        binding.etMapeoObservations.isEnabled = state
        binding.etMapeoStratigraphicUnit.isEnabled = state
        binding.etMapeoFindingTypeSp.isEnabled = state
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
        binding.btnItemMapeoAdd.isGone = false
        binding.btnItemMapeoTakePhoto.isGone = true
        setEditableFields(true)
    }

    fun insertOrUpdateMapeo() = lifecycleScope.launch {
        var newRowId = -1
        var existNumberMapeo = -1
        val checkEmptyFields = sharedViewModel.checkEmptyFieldsMapeo()
        if (checkEmptyFields) {
            if (!sharedViewModel.isUpdateOrDeleteMapeo) {
                existNumberMapeo = sharedViewModel.checkExistNumberMapeo()
            }
            if (existNumberMapeo <= 0) {
                newRowId = sharedViewModel.insertOrUpdateMapeo().toInt()
                if (newRowId > 0) {
                    sharedViewModel.idMapeo = newRowId.toLong()
                    if (sharedViewModel.isUpdateOrDeleteMapeo) {
                        Toast.makeText(context, R.string.item_mapeo_update_message_ok, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast
                            .makeText(context, R.string.item_mapeo_save_message_ok, Toast.LENGTH_SHORT)
                            .show()
                    }
                    findNavController().navigate(R.id.action_mapeoItemFragment_to_mapeoListFragment)
                } else Toast
                    .makeText(context, R.string.item_mapeo_save_message_error, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, R.string.item_mapeo_update_message_exist_number, Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun launchPhotoEditor() {
        findNavController().navigate(R.id.action_mapeoItemFragment_to_photoEdit)
    }
}