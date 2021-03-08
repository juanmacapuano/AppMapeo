package com.juanmacapuano.appmapeo.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.room.ProjectEntity

class ProjectListAdapter : ListAdapter<ProjectEntity, ProjectListAdapter.ItemViewHolder>(ItemProjectComparator) {

    private var listProject = emptyList<ProjectEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_project, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProjectListAdapter.ItemViewHolder, position: Int) {
        val currentItem =  listProject[position]
        holder.tv_cv_title.text = currentItem.name
        holder.tv_cv_description.text = currentItem.date
        holder.tv_cv_descriptionDown.text = currentItem.location
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tv_cv_title: TextView
        lateinit var tv_cv_description: TextView
        lateinit var tv_cv_descriptionDown: TextView

        init {

            tv_cv_title = itemView.findViewById(R.id.tv_cv_title)
            tv_cv_description = itemView.findViewById(R.id.tv_cv_description)
            tv_cv_descriptionDown = itemView.findViewById(R.id.tv_cv_descriptionDown)

            itemView.setOnClickListener {
                var position: Int = getAdapterPosition()
                val context = itemView.context
                it.findNavController().navigate(R.id.action_listProjectFragment_to_itemProjectFragment)
            }
        }
    }

    companion object {
        private val ItemProjectComparator = object : DiffUtil.ItemCallback<ProjectEntity>() {
            override fun areItemsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}