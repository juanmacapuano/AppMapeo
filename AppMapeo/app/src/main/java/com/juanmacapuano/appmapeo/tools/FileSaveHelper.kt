package com.juanmacapuano.appmapeo.tools

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.*
import com.juanmacapuano.appmapeo.mapeos.PhotoEditFragment
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FileSaveHelper(contentResolver: ContentResolver?) : LifecycleObserver {
    var mContentResolver: ContentResolver? = null
    var executor: ExecutorService? = null
    var fileCreatedResult: MutableLiveData<FileMeta>? = null
    var resultListener: OnFileCreateResult? = null
    private val observer: Observer<FileMeta> =
        Observer<FileMeta> { fileMeta: FileMeta ->
            resultListener?.onFileCreateResult(
                fileMeta.isCreated,
                fileMeta.filePath,
                fileMeta.error,
                fileMeta.uri
            )
        }

    constructor(fragment: PhotoEditFragment, contentResolver: ContentResolver?) : this(contentResolver) {
        addObserver(fragment)
    }

    init {
        mContentResolver = contentResolver
        executor = Executors.newSingleThreadExecutor()
        fileCreatedResult = MutableLiveData<FileMeta>()
    }

    private fun addObserver(lifecycleOwner: LifecycleOwner) {
        fileCreatedResult!!.observe(lifecycleOwner, observer)
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() {
        executor?.shutdownNow()
    }

    fun isSdkHigherThan28(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }


    /**
     * The effects of this method are
     * 1- insert new Image File data in MediaStore.Images column
     * 2- create File on Disk.
     *
     * @param fileNameToSave fileName
     * @param listener       result listener
     */

     fun createFile(fileNameToSave: String?, listener: OnFileCreateResult?) {
        resultListener = listener
        executor!!.submit {
            val cursor: Cursor? = null
            val filePath: String
            try {
                val newImageDetails = ContentValues()
                val imageCollection = buildUriCollection(newImageDetails)
                val editedImageUri =
                    getEditedImageUri(fileNameToSave!!, newImageDetails, imageCollection)
                filePath = getFilePath(cursor, editedImageUri)
                var filePathString = filePath
                filePathString = filePath.replace(".jpg", fileNameToSave)
                filePathString = "$filePathString.jpg"
                updateResult(true, filePath, null, editedImageUri, newImageDetails)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                updateResult(false, null, ex.message, null, null)
            } finally {
                cursor?.close()
            }
        }
    }

    private fun getFilePath(_cursor: Cursor?, editedImageUri: Uri): String {
        var cursor: Cursor? = _cursor
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = mContentResolver!!.query(editedImageUri, proj, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    @Throws(IOException::class)
    fun getEditedImageUri(
        fileNameToSave: String,
        newImageDetails: ContentValues,
        imageCollection: Uri
    ): Uri {
        newImageDetails.put(MediaStore.Images.Media.DISPLAY_NAME, fileNameToSave)
        val editedImageUri = mContentResolver!!.insert(imageCollection, newImageDetails)
        val outputStream = mContentResolver!!.openOutputStream(editedImageUri!!)
        outputStream!!.close()
        return editedImageUri
    }

    @SuppressLint("InlinedApi")
    fun buildUriCollection(newImageDetails: ContentValues): Uri {
        val imageCollection: Uri
        if (isSdkHigherThan28()) {
            imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
            newImageDetails.put(MediaStore.Images.Media.IS_PENDING, 1)
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        return imageCollection
    }

    fun notifyThatFileIsNowPubliclyAvailable(contentResolver: ContentResolver) {
        if (isSdkHigherThan28()) {
            executor!!.submit {
                val value: FileMeta? = fileCreatedResult!!.value
                if (value != null) {
                    value.imageDetails?.clear()
                    value.imageDetails?.put(MediaStore.Images.Media.IS_PENDING, 0)
                    value.uri?.let { contentResolver.update(it, value.imageDetails, null, null) }
                }
            }
        }
    }

    class FileMeta(
        var isCreated: Boolean, var filePath: String?,
        var uri: Uri?, var error: String?,
        var imageDetails: ContentValues?
    )

    interface OnFileCreateResult {
        /**
         * @param created  whether file creation is success or failure
         * @param filePath filepath on disk. null in case of failure
         * @param error    in case file creation is failed . it would represent the cause
         * @param uri      Uri to the newly created file. null in case of failure
         */
        fun onFileCreateResult(created: Boolean, filePath: String?, error: String?, uri: Uri?)
    }

    open fun updateResult(
        result: Boolean,
        filePath: String?,
        error: String?,
        uri: Uri?,
        newImageDetails: ContentValues?
    ) {
        fileCreatedResult!!.postValue(FileMeta(result, filePath, uri, error, newImageDetails))
    }
}