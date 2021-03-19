package com.juanmacapuano.appmapeo

import android.app.Application
import com.juanmacapuano.appmapeo.repository.AppRepository
import com.juanmacapuano.appmapeo.repository.DatabaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MapeoAplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { DatabaseApp.getDatabase(this, applicationScope) }
    val repository by lazy { AppRepository(this) }
}