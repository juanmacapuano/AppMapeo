package com.juanmacapuano.appmapeo

import android.app.Application
import androidx.lifecycle.*
import com.juanmacapuano.appmapeo.repository.AppRepository
import com.juanmacapuano.appmapeo.repository.DatabaseApp
import com.juanmacapuano.appmapeo.room.ProjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProjectListViewModel (application: Application) : AndroidViewModel(application) {

    private val getAllProjects : LiveData<List<ProjectEntity>>
    private val repository : AppRepository
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val projectDao = DatabaseApp.getDatabase(
                application, scope
        ).projectoDao()
        repository = AppRepository(application)
        getAllProjects = repository.getAllProject()
    }

    fun insertProject(projectEntity: ProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProject(projectEntity)
        }
    }
}