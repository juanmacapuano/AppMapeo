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
        const val TAG = "ProjectItemFragment"
        const val KEY_PROJECT_ID = "project_id"
    }
    lateinit var binding: ItemProjectFragmentBinding

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
}