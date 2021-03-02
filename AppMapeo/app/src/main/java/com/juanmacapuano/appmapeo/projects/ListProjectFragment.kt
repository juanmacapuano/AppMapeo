package com.juanmacapuano.appmapeo.projects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentListProjectBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ListProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListProjectFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentListProjectBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_project, container, false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = ListProjectFragment()
    }
}