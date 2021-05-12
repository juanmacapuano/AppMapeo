package com.juanmacapuano.appmapeo

import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ajts.androidmads.library.SQLiteToExcel
import com.ajts.androidmads.library.SQLiteToExcel.ExportListener

const val SHARED = "spinnerItemType"

open class BaseActivity : AppCompatActivity() {

    val TABLE_NAME: String = "project_table"
    val COLUMN_ID: String = BaseColumns._ID
    val COLUMN_PROJECT_NAME: String = "name"
    val COLUMN_PROJECT_LOCATION: String = "location"
    val COLUMN_PROJECT_DATE: String = "date"
    val COLUMN_PROJECT_DELETE: String = "delete"
    var spinnerItemsType : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
       // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        createStringVal();
    }

    private fun createStringVal() {

        spinnerItemsType.add(getString(R.string.value_spinner_1))
        spinnerItemsType.add(getString(R.string.value_spinner_2))
        spinnerItemsType.add(getString(R.string.value_spinner_3))
        spinnerItemsType.add(getString(R.string.value_spinner_4))

        val size : Int = spinnerItemsType.size
        val sharedPreferences = getSharedPreferences(SHARED, Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            spinnerItemsType.size.let { putInt(R.string.key_spinner.toString() + "_size", it) }
            for (i in 0 until spinnerItemsType.size)
                putString(R.string.key_spinner.toString() + "_" + i, spinnerItemsType[i])
            commit()
        }
    }

    open fun showHomeUpIcon(title: String?) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = title
        }
    }

    open fun exportDbTo() {
        val sqLiteToExcel = SQLiteToExcel(applicationContext, "mapeo_database")
        sqLiteToExcel.exportAllTables("mapeo_database.xls", object : SQLiteToExcel.ExportListener {
            override fun onStart() {
            }

            override fun onCompleted(filePath: String) {
                Toast.makeText(applicationContext, R.string.bdExport, Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Exception) {
                Toast.makeText(applicationContext, R.string.bdExportError, Toast.LENGTH_SHORT).show()
            }
        })
    }

}