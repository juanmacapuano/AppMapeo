package com.juanmacapuano.appmapeo.viewModel

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.juanmacapuano.appmapeo.repository.AppRepository
import com.juanmacapuano.appmapeo.room.MapeoEntity
import com.juanmacapuano.appmapeo.room.ProjectEntity
import com.juanmacapuano.appmapeo.tools.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class ProjectListViewModel(application: Application) : AndroidViewModel(application), Observable {

    private lateinit var getAllMapeos : LiveData<List<MapeoEntity>>
    private val repository : AppRepository
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private lateinit var projectToUpdateOrDelete: ProjectEntity
    private lateinit var mapeoToUpdateOrDelete: MapeoEntity
    var isUpdateOrDeleteProject = false
    var isUpdateOrDeleteMapeo = false
    var idProject : Long = -1
    var idMapeo : Long = -1

    @Bindable
    val et_item_project_title = MutableLiveData<String>()

    @Bindable
    val et_item_project_date = MutableLiveData<String>()

    @Bindable
    val et_item_project_location = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val et_item_mapeo_number = MutableLiveData<String>()

    @Bindable
    val et_mapeo_findingType_sp = MutableLiveData<String>()

    @Bindable
    val et_mapeo_gridNumber = MutableLiveData<String>()

    @Bindable
    val et_mapeo_stratigraphicUnit = MutableLiveData<String>()

    @Bindable
    val et_mapeo_numberStationTotal = MutableLiveData<String>()

    @Bindable
    val et_mapeo_observations = MutableLiveData<String>()

    @Bindable
    val btn_item_mapeo_add = MutableLiveData<String>()

    private val _statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = _statusMessage

    private val _btn_item_project_add = MutableLiveData<Boolean>()
    val btn_item_project_add: LiveData<Boolean>
        get() = _btn_item_project_add

    init {
        repository = AppRepository(application)
        saveOrUpdateButtonText.value = "Crear Proyecto"
        btn_item_mapeo_add.value = "Crear Mapeo"
    }

    suspend fun insertOrUpdateProject(): Long {

        return if (isUpdateOrDeleteProject) {
            isUpdateOrDeleteProject = false
           // _btn_item_project_add.value = false
            projectToUpdateOrDelete.name = et_item_project_title.value!!
            projectToUpdateOrDelete.date = et_item_project_date.value!!
            projectToUpdateOrDelete.location = et_item_project_location.value!!
            repository.updateProject(projectToUpdateOrDelete).toLong()
        } else {
           // _btn_item_project_add.value = true
            val name = et_item_project_title.value!!
            val date = et_item_project_date.value!!
            val location = et_item_project_location.value!!
            et_item_project_title.value = ""
            et_item_project_date.value = ""
            et_item_project_location.value = ""
            repository.insertProject(ProjectEntity(0, name, location, date, 0))
        }
    }

    suspend fun insertOrUpdateMapeo(): Long {

        return if (isUpdateOrDeleteMapeo) {
            isUpdateOrDeleteMapeo = false
            mapeoToUpdateOrDelete.number = et_item_mapeo_number.value!!
            mapeoToUpdateOrDelete.grid = et_mapeo_gridNumber.value!!
            mapeoToUpdateOrDelete.stationTotalNumber = et_mapeo_numberStationTotal.value!!
            if (et_mapeo_findingType_sp.value != null)
                mapeoToUpdateOrDelete.type = et_mapeo_findingType_sp.value!!
            mapeoToUpdateOrDelete.stratigraphicUnit = et_mapeo_stratigraphicUnit.value!!

            repository.updateMapeo(mapeoToUpdateOrDelete).toLong()

        } else {
            val number = et_item_mapeo_number.value!!
            val grid = et_mapeo_gridNumber.value!!
            var stationNumber = ""
            if (et_mapeo_numberStationTotal.value != null)
                stationNumber = et_mapeo_numberStationTotal.value!!
            var typeFinding = ""
            if (et_mapeo_findingType_sp.value != null)
                typeFinding = et_mapeo_findingType_sp.value!!
            val stratigraphic = et_mapeo_stratigraphicUnit.value!!
            var observation = ""
            if (et_mapeo_observations.value != null) {
                observation = et_mapeo_observations.value!!
            }

            val timeStamp : String = SimpleDateFormat("dd/MM/yyyy").format(Date())

            et_item_mapeo_number.value = ""
            et_mapeo_gridNumber.value = ""
            et_mapeo_numberStationTotal.value = ""
            et_mapeo_findingType_sp.value = ""
            et_mapeo_stratigraphicUnit.value = ""
            et_mapeo_observations.value = ""

            repository.insertMapeo(MapeoEntity(0,number, timeStamp, grid, typeFinding, observation, stationNumber, stratigraphic,idProject))
        }
    }

    fun checkEmptyFieldsProject() : Boolean {
        return if (et_item_project_title.value == null) {
            _statusMessage.value = Event("Ingrese un nombre")
            false
        } else if (et_item_project_date.value == null) {
            _statusMessage.value = Event("Ingrese una fecha")
            false
        } else if (et_item_project_location.value == null) {
            _statusMessage.value = Event("Ingrese una ubicación")
            false
        } else {
            true
        }
    }

    fun checkEmptyFieldsMapeo() : Boolean {
        return if (et_item_mapeo_number.value == null) {
            _statusMessage.value = Event("Ingrese un número")
            false
        } else if (et_mapeo_gridNumber.value == null) {
            _statusMessage.value = Event("Ingrese una cuadrícula")
            false
        } else if (et_mapeo_stratigraphicUnit.value == null) {
            _statusMessage.value = Event("Ingrese una Unidad Estratigráfica")
            false
        } else {
            true
        }
    }

    fun getAllProject() : LiveData<List<ProjectEntity>> {
        return repository.getAllProject()
    }

    fun getAllMapeos() : LiveData<List<MapeoEntity>> {
        return repository.getAllMapeos(projectToUpdateOrDelete.id)
    }

    fun initUpdateProject(projectEntity: ProjectEntity) {
        et_item_project_location.value = projectEntity.location
        et_item_project_title.value = projectEntity.name
        et_item_project_date.value = projectEntity.date
        idProject = projectEntity.id
        projectToUpdateOrDelete = projectEntity
        isUpdateOrDeleteProject = true
       // _btn_item_project_add.value = false
        saveOrUpdateButtonText.value = "Actualizar Proyecto"
    }

    fun initInsertProject() {
        et_item_project_location.value = null
        et_item_project_title.value = null
        et_item_project_date.value = null
        isUpdateOrDeleteProject = false
      //  _btn_item_project_add.value = true
        saveOrUpdateButtonText.value = "Crear Proyecto"
    }

    fun initUpdateMapeo(mapeoEntity: MapeoEntity) {
        idMapeo = mapeoEntity.id
        et_item_mapeo_number.value = mapeoEntity.number
        et_mapeo_gridNumber.value = mapeoEntity.grid
        et_mapeo_findingType_sp.value = mapeoEntity.type
        et_mapeo_numberStationTotal.value = mapeoEntity.stationTotalNumber
        et_mapeo_stratigraphicUnit.value = mapeoEntity.stratigraphicUnit
        et_mapeo_observations.value = mapeoEntity.observation
        mapeoToUpdateOrDelete = mapeoEntity
        isUpdateOrDeleteMapeo = true
        btn_item_mapeo_add.value = "Actualizar Mapeo"
    }

    fun initInsertMapeo() {
        et_item_mapeo_number.value = null
        et_mapeo_gridNumber.value = null
        et_mapeo_numberStationTotal.value = null
        et_mapeo_findingType_sp.value = null
        et_mapeo_stratigraphicUnit.value = null
        et_mapeo_observations.value = null
        isUpdateOrDeleteMapeo = false
        btn_item_mapeo_add.value = "Crear Mapeo"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {    }

    fun initMapeoFragmentList() {
        getAllMapeos = getAllMapeos()
    }

    suspend fun checkExistNumberMapeo() : Int {
       return repository.getMapeoByNumber(idProject, et_item_mapeo_number.value!!)
    }

    fun setDate(dayOfMonth: Int, month: Int, year: Int) {
        val selectedDate = dayOfMonth.toString() + "/" + (month+1) + "/" + year
        et_item_project_date.value = selectedDate
    }

    fun setVisibility(state: Boolean) {
        _btn_item_project_add.value = state
    }

}