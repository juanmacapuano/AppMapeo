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

    @Bindable
    val et_item_project_title = MutableLiveData<String>()

    @Bindable
    val et_item_project_date = MutableLiveData<String>()

    @Bindable
    val et_item_project_location = MutableLiveData<String>()

    init {
        val projectDao = DatabaseApp.getDatabase(
            application, scope
        ).projectoDao()
        repository = AppRepository(application)
        getAllProjects = repository.getAllProject()

    }

    /*fun insertProyecto(): Long {
        var projectEntity = ProjectEntity()
        projectEntity.name = et_item_project_title.value!!
        projectEntity.date = et_item_project_date.value!!
        projectEntity.location = et_item_project_location.value!!
        projectEntity.delete = 0
        et_item_project_title.value = ""
        et_item_project_date.value = ""
        et_item_project_location.value = ""

        val callable: Callable<Long> = Callable { repository.insertProjecto(projectEntity) }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        try {
            return future.get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return -1
    }*/

    suspend fun insertProject(): Long {
        val projectEntity = ProjectEntity().apply {
            name = et_item_project_title.value!!
            date = et_item_project_date.value!!
            location = et_item_project_location.value!!
            delete = 0
        }
        return repository.insertProject(projectEntity)
    }

    fun getAllProject() : LiveData<List<ProjectEntity>> {
        return getAllProjects
    }

    fun initUpdate(projectEntity: ProjectEntity) {
        et_item_project_location.value = projectEntity.location
        et_item_project_title.value = projectEntity.name
        et_item_project_date.value = projectEntity.date
    }

    fun setEmptyFields() {
        et_item_project_location.value = ""
        et_item_project_title.value = ""
        et_item_project_date.value = ""
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}