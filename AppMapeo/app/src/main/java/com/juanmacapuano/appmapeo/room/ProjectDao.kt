package com.juanmacapuano.appmapeo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ProjectDao {

    @Insert
    suspend fun insert(project: ProjectEntity) : Long

    @Update
    suspend fun update(project: ProjectEntity) : Int

    @Query("SELECT * FROM $TABLE_NAME_PROJECT ORDER BY $COLUMN_ID DESC")
    fun getAllProjects() : LiveData<List<ProjectEntity>>



}