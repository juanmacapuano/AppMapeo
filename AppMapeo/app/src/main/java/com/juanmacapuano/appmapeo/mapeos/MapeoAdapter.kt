package com.juanmacapuano.appmapeo.mapeos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.ItemListMapeoBinding
import com.juanmacapuano.appmapeo.room.MapeoEntity

class MapeoAdapter(private val clickListener:(MapeoEntity)->Unit) : RecyclerView.Adapter<MapeoAdapter.ItemMapeoViewHolder>() {

    private var listMapeo = ArrayList<MapeoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapeoAdapter.ItemMapeoViewHolder {
        val binding : ItemListMapeoBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_mapeo, parent, false)

        return ItemMapeoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapeoAdapter.ItemMapeoViewHolder, position: Int) {
        holder.bind(listMapeo[position], clickListener)
    }

    override fun getItemCount(): Int {
        return listMapeo.size
    }

    fun setMapeoList(mapeoList : List<MapeoEntity>) {
        listMapeo.clear()
        listMapeo.addAll(mapeoList)
    }

    class ItemMapeoViewHolder(private val binding: ItemListMapeoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mapeoEntity: MapeoEntity, clickListener: (MapeoEntity) -> Unit) {
            binding.tvCvNumMapeo.text = mapeoEntity.number
            binding.tvCvDateMapeo.text = mapeoEntity.date
            binding.tvCvTridiMapeo.text = mapeoEntity.tridi
            binding.layoutItemListMapeo.setOnClickListener {
                clickListener(mapeoEntity)
            }
        }
    }
}