package com.juanmacapuano.appmapeo.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.juanmacapuano.appmapeo.room.MapeoDao
import com.juanmacapuano.appmapeo.room.MapeoEntity
import com.juanmacapuano.appmapeo.room.ProjectDao
import com.juanmacapuano.appmapeo.room.ProjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class AppRepository(application: Application) {

    private var projectDao: ProjectDao
    private var mapeoDao : MapeoDao
    private val applicationScope = CoroutineScope(SupervisorJob())

    companion object {
        @Volatile
        private var INSTANCE : AppRepository? = null

        fun getInstance(application: Application) : AppRepository {
            return INSTANCE ?: AppRepository(application)
        }
    }

    init {
        val databaseApp : DatabaseApp = DatabaseApp.getDatabase(application.applicationContext, applicationScope)
        projectDao = databaseApp.projectoDao()
        mapeoDao = databaseApp.mapeoDao()
    }

    //region Projects Room

    suspend fun insertProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.insert(project)
    }

    suspend fun updateProject(projectEntity: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.update(projectEntity)
    }


    fun getAllProject() : LiveData<List<ProjectEntity>> {
        return projectDao.getAllProjects()
    }

    //endregion

    //region Mapeo Room

    suspend fun insertMapeo(mapeoEntity: MapeoEntity) = withContext(Dispatchers.IO) {
        mapeoDao.insert(mapeoEntity)
    }

    suspend fun updateMapeo(mapeoEntity: MapeoEntity) = withContext(Dispatchers.IO) {
        mapeoDao.update(mapeoEntity)
    }

    fun getAllMapeos(id : Long) : LiveData<List<MapeoEntity>> {
        return mapeoDao.getAllMapeos(id)
    }
    //endregion


}