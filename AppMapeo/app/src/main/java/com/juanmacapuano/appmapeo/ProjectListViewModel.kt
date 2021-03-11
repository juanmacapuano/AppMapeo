package com.juanmacapuano.appmapeo

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.juanmacapuano.appmapeo.repository.AppRepository
import com.juanmacapuano.appmapeo.repository.DatabaseApp
import com.juanmacapuano.appmapeo.room.ProjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProjectListViewModel (application: Application) : AndroidViewModel(application), Observable {

    private val getAllProjects : LiveData<List<ProjectEntity>>
    private val repository : AppRepository
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    @Bindable
    val tv_item_project_title = MutableLiveData<String>()

    @Bindable
    val tv_item_project_date = MutableLiveData<String>()

    @Bindable
    val tv_item_project_location = MutableLiveData<String>()


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
    fun getAllProject() : LiveData<List<ProjectEntity>> {
        return getAllProjects
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}