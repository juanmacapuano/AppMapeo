package com.juanmacapuano.appmapeo.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.ItemListProjectBinding
import com.juanmacapuano.appmapeo.room.ProjectEntity

class ProjectAdapter(private val clickListener:(ProjectEntity)->Unit) : RecyclerView.Adapter<ProjectAdapter.ItemViewHolder>() {

    private var listProject = ArrayList<ProjectEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding : ItemListProjectBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_project, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectAdapter.ItemViewHolder, position: Int) {
        holder.bind(listProject[position], clickListener)
    }

    fun setProjectList(projectList : List<ProjectEntity>) {
        listProject.clear()
        listProject.addAll(projectList)
    }

    override fun getItemCount(): Int {
        return listProject.size
    }

    class ItemViewHolder(val binding: ItemListProjectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(projectEntity: ProjectEntity, clickListener: (ProjectEntity) -> Unit) {
            binding.tvCvTitle.text = projectEntity.name
            binding.tvCvDate.text = projectEntity.date
            binding.tvCvLocation.text = projectEntity.location
            binding.layoutItemList.setOnClickListener {
                clickListener(projectEntity)
            }

        }
    }
}