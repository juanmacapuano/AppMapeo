package com.juanmacapuano.appmapeo

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
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
    val et_item_project_title = MutableLiveData<String>()

    @Bindable
    val et_item_project_date = MutableLiveData<String>()

    @Bindable
    val et_item_project_location = MutableLiveData<String>()

    val returnInsertIdItemProject = MutableLiveData<Long>()

    init {
        val projectDao = DatabaseApp.getDatabase(
                application, scope
        ).projectoDao()
        repository = AppRepository(application)
        getAllProjects = repository.getAllProject()

    }

    fun insertProject(){
        var projectEntity = ProjectEntity()
        projectEntity.name = et_item_project_title.value!!
        projectEntity.date = et_item_project_date.value!!
        projectEntity.location = et_item_project_location.value!!
        projectEntity.delete = 0

        viewModelScope.launch(Dispatchers.IO) {
            Log.e("corroutine", "entro")
            returnInsertIdItemProject.postValue(repository.insertProject(projectEntity))
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