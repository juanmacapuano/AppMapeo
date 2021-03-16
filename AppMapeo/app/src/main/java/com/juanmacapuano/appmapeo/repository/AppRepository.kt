package com.juanmacapuano.appmapeo.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.juanmacapuano.appmapeo.ProjectItemFragment.Companion.projectEntity
import com.juanmacapuano.appmapeo.room.ProjectDao
import com.juanmacapuano.appmapeo.room.ProjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class AppRepository(application: Application) {

    private lateinit var projectDao: ProjectDao
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
    }

    suspend fun insertProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.insert(project)
    }


    fun getAllProject() : LiveData<List<ProjectEntity>> {
        return projectDao.getAllProjects()
    }


}