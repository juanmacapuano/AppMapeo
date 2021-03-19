package com.juanmacapuano.appmapeo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MapeoDao {

    @Insert
    suspend fun insert(mapeoEntity: MapeoEntity) : Long

    @Update
    suspend fun update(mapeoEntity: MapeoEntity) : Int

    @Query("SELECT * FROM $TABLE_NAME_MAPEO WHERE $COLUMN_ID_MAPEO = :id")
    fun getAllMapeos(id : Long) : LiveData<List<MapeoEntity>>
}