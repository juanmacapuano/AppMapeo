package com.juanmacapuano.appmapeo.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.juanmacapuano.appmapeo.room.MapeoDao
import com.juanmacapuano.appmapeo.room.MapeoEntity
import com.juanmacapuano.appmapeo.room.ProjectDao
import com.juanmacapuano.appmapeo.room.ProjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ProjectEntity::class, MapeoEntity::class],
        version = 4,
        exportSchema = false
)
abstract class DatabaseApp : RoomDatabase(){

    abstract fun projectoDao() : ProjectDao

    abstract fun mapeoDao() : MapeoDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseApp? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DatabaseApp {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseApp::class.java,
                    "mapeo_database"
                )

                    .fallbackToDestructiveMigration()
                    .addCallback(MapeoDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class MapeoDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                INSTANCE?.let {
                    scope.launch(Dispatchers.IO) {
                    }
                }
            }
        }

    }
}