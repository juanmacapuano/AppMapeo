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
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class ProjectListViewModel(application: Application) : AndroidViewModel(application), Observable {

    private val getAllProjects : LiveData<List<ProjectEntity>>
    private val repository : AppRepository
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private lateinit var projectToUpdateOrDelete: ProjectEntity
    private var isUpdateOrDelete = false

    @Bindable
    val et_item_project_title = MutableLiveData<String>()

    @Bindable
    val et_item_project_date = MutableLiveData<String>()

    @Bindable
    val et_item_project_location = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        val projectDao = DatabaseApp.getDatabase(
            application, scope
        ).projectoDao()
        repository = AppRepository(application)
        getAllProjects = repository.getAllProject()
        saveOrUpdateButtonText.value = "Crear Proyecto"

    }

    suspend fun insertOrUpdateProject(): Long {
        val projectEntity = ProjectEntity().apply {
            name = et_item_project_title.value!!
            date = et_item_project_date.value!!
            location = et_item_project_location.value!!
            delete = 0
        }
        return if (isUpdateOrDelete) {
            isUpdateOrDelete = false
            repository.updateProject(projectEntity).toLong()
        } else {
            et_item_project_title.value = ""
            et_item_project_date.value = ""
            et_item_project_location.value = ""
            repository.insertProject(projectEntity)
        }
    }

    fun checkEmptyFields() : Boolean {
        return if (et_item_project_title.value == null) {
            statusMessage.value = Event("Ingrese un nombre")
            false
        } else if (et_item_project_date.value == null) {
            statusMessage.value = Event("Ingrese una fecha")
            false
        } else if (et_item_project_location.value == null) {
            statusMessage.value = Event("Ingrese una ubicación")
            false
        } else {
            true
        }
    }

    fun getAllProject() : LiveData<List<ProjectEntity>> {
        return getAllProjects
    }

    fun initUpdate(projectEntity: ProjectEntity) {
        et_item_project_location.value = projectEntity.location
        et_item_project_title.value = projectEntity.name
        et_item_project_date.value = projectEntity.date
        projectToUpdateOrDelete = projectEntity
        isUpdateOrDelete = true
        saveOrUpdateButtonText.value = "Actualizar Proyecto"

    }

    fun setEmptyFields() {
        et_item_project_title.value = null
        et_item_project_date.value = null
        et_item_project_location.value = null
        saveOrUpdateButtonText.value = "Crear Proyecto"
        isUpdateOrDelete = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {    }

}