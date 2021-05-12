package com.juanmacapuano.appmapeo.mapeos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.tools.ToolType
import java.util.*

class EditingToolsAdapter(onItemSelected: OnItemSelected) : RecyclerView.Adapter<EditingToolsAdapter.ViewHolder>(){


    private val mToolList: MutableList<ToolModel> = ArrayList()
    private var mOnItemSelected: OnItemSelected? = null

    init {
        mOnItemSelected = onItemSelected
        mToolList.add(ToolModel("Camera", R.drawable.ic_camera, ToolType.CAMERA))
        mToolList.add(ToolModel("Gallery", R.drawable.ic_gallery, ToolType.GALLERY))
        mToolList.add(ToolModel("Redo", R.drawable.ic_redo, ToolType.REDO))
        mToolList.add(ToolModel("Undo", R.drawable.ic_undo, ToolType.UNDO))
        mToolList.add(ToolModel("Brush", R.drawable.ic_brush_new, ToolType.BRUSH))
        mToolList.add(ToolModel("Eraser", R.drawable.ic_eraser_new, ToolType.ERASER))
        mToolList.add(ToolModel("Text", R.drawable.ic_text_new, ToolType.TEXT))

    }


    interface OnItemSelected {
        fun onToolSelected(toolType: ToolType?)
    }

    internal class ToolModel(val mToolName: String, val mToolIcon: Int, toolType: ToolType) {
        val mToolType: ToolType

        init {
            mToolType = toolType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_editing_tools, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditingToolsAdapter.ViewHolder, position: Int) {
        val item = mToolList[position]
        holder.txtTool.text = item.mToolName
        holder.imgToolIcon.setImageResource(item.mToolIcon)
    }

    override fun getItemCount(): Int {
        return mToolList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgToolIcon: ImageView
        var txtTool: TextView

        init {
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon)
            txtTool = itemView.findViewById(R.id.txtTool)
            itemView.setOnClickListener {
                mOnItemSelected?.onToolSelected(
                    mToolList.get(
                        layoutPosition
                    ).mToolType
                )
            }
        }
    }


}