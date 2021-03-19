package com.juanmacapuano.appmapeo.room

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_MAPEO: String = "mapeo_table"
const val COLUMN_ID_MAPEO: String = BaseColumns._ID
const val COLUMN_MAPEO_NUMBER: String = "number"
const val COLUMN_MAPEO_DATE: String = "date"
const val COLUMN_MAPEO_TRIDI: String = "tridi"
const val COLUMN_MAPEO_TYPE: String = "type"
const val COLUMN_MAPEO_OBS: String = "observations"
const val COLUMN_MAPEO_GRID: String = "grid"

const val COLUMN_MAPEO_DELETE: String = "delete"

@Entity(tableName = TABLE_NAME_MAPEO)
data class MapeoEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID_MAPEO)
    var id : Long,
    @ColumnInfo(name = COLUMN_MAPEO_NUMBER)
    var number : String,
    @ColumnInfo(name = COLUMN_MAPEO_DATE)
    var date : String,
    @ColumnInfo(name = COLUMN_MAPEO_GRID)
    var grid : String,
    @ColumnInfo(name = COLUMN_MAPEO_TYPE)
    var type : String,
    @ColumnInfo(name = COLUMN_MAPEO_OBS)
    var observation : String,
    @ColumnInfo(name = COLUMN_MAPEO_TRIDI)
    var tridi : String
    )
{
    constructor() : this (0, "", "", "", "", "", "")
}