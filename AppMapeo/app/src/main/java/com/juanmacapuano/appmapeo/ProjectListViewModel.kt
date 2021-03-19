package com.juanmacapuano.appmapeo

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.juanmacapuano.appmapeo.repository.AppRepository
import com.juanmacapuano.appmapeo.repository.DatabaseApp
import com.juanmacapuano.appmapeo.room.MapeoEntity
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
    private lateinit var getAllMapeos : LiveData<List<MapeoEntity>>
    private val repository : AppRepository
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private lateinit var projectToUpdateOrDelete: ProjectEntity
    var isUpdateOrDelete = false
    private val idProject : Long = -1

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
            application, scope).projectoDao()
        val mapeoDao = DatabaseApp.getDatabase(
            application, scope).mapeoDao()
        repository = AppRepository(application)
        getAllProjects = repository.getAllProject()
        saveOrUpdateButtonText.value = "Crear Proyecto"
    }

    suspend fun insertOrUpdateProject(): Long {

        return if (isUpdateOrDelete) {
            isUpdateOrDelete = false
            projectToUpdateOrDelete.name = et_item_project_title.value!!
            projectToUpdateOrDelete.date = et_item_project_date.value!!
            projectToUpdateOrDelete.location = et_item_project_location.value!!
            repository.updateProject(projectToUpdateOrDelete).toLong()
        } else {
            val name = et_item_project_title.value!!
            val date = et_item_project_date.value!!
            val location = et_item_project_location.value!!
            et_item_project_title.value = ""
            et_item_project_date.value = ""
            et_item_project_location.value = ""
            repository.insertProject(ProjectEntity(0, name, location, date, 0))
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

    fun getAllMapeos() : LiveData<List<MapeoEntity>> {
        return repository.getAllMapeos(projectToUpdateOrDelete.id)
    }

    fun initUpdate(projectEntity: ProjectEntity) {
        et_item_project_location.value = projectEntity.location
        et_item_project_title.value = projectEntity.name
        et_item_project_date.value = projectEntity.date
        projectToUpdateOrDelete = projectEntity
        isUpdateOrDelete = true
        saveOrUpdateButtonText.value = "Actualizar Proyecto"

    }

    fun initInsert() {
        et_item_project_location.value = null
        et_item_project_title.value = null
        et_item_project_date.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Crear Proyecto"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {    }

    fun initMapeoFragmentList() {
        getAllMapeos = getAllMapeos()
    }

}