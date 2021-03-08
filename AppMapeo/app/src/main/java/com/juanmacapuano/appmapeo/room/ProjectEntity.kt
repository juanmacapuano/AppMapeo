package com.juanmacapuano.appmapeo.room

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_PROJECT: String = "project_table"
const val COLUMN_ID: String = BaseColumns._ID
const val COLUMN_PROJECT_NAME: String = "name"
const val COLUMN_PROJECT_LOCATION: String = "location"
const val COLUMN_PROJECT_DATE: String = "date"
const val COLUMN_PROJECT_DELETE: String = "delete"

@Entity(tableName = TABLE_NAME_PROJECT)
data class ProjectEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id : Long,
    @ColumnInfo(name = COLUMN_PROJECT_NAME)
    val name : String,
    @ColumnInfo(name = COLUMN_PROJECT_LOCATION)
    val location : String,
    @ColumnInfo(name = COLUMN_PROJECT_DATE)
    val date : String,
    @ColumnInfo(name = COLUMN_PROJECT_DELETE)
    val delete : Int
)