package com.juanmacapuano.appmapeo

import android.os.Bundle
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    val TABLE_NAME: String = "project_table"
    val COLUMN_ID: String = BaseColumns._ID
    val COLUMN_PROJECT_NAME: String = "name"
    val COLUMN_PROJECT_LOCATION: String = "location"
    val COLUMN_PROJECT_DATE: String = "date"
    val COLUMN_PROJECT_DELETE: String = "delete"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createStringVal();
    }

    private fun createStringVal() {


    }
}