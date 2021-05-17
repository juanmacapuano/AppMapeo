package com.juanmacapuano.appmapeo.mapeos

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.juanmacapuano.appmapeo.R
import com.juanmacapuano.appmapeo.databinding.FragmentPhotoEditBinding
import com.juanmacapuano.appmapeo.viewModel.ProjectListViewModel
import com.juanmacapuano.appmapeo.tools.FileSaveHelper
import com.juanmacapuano.appmapeo.tools.ToolType
import iamutkarshtiwari.github.io.ananas.editimage.fragment.TextEditorDialogFragment
import iamutkarshtiwari.github.io.ananas.editimage.interfaces.OnTextEditorListener
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import java.io.IOException

const val CAMERA_REQUEST = 52
const val PICK_REQUEST = 53

class PhotoEditFragment : Fragment(), EditingToolsAdapter.OnItemSelected, OnPhotoEditorListener,
    View.OnClickListener {

    lateinit var binding: FragmentPhotoEditBinding
    lateinit var photoEditor: PhotoEditor
    lateinit var adapterEditingTools: EditingToolsAdapter
    private val TAG = PhotoEditFragment::class.java.simpleName
    lateinit var mPhotoEditorView: PhotoEditorView
    private val sharedViewModel: ProjectListViewModel by activityViewModels()
    lateinit var mSaveFileHelper: FileSaveHelper
    lateinit var imageUri: Uri

    @VisibleForTesting
    var mSaveImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_edit, container, false)

        mPhotoEditorView = binding.photoEditorView

        val mTextRobotoTf = context?.let { ResourcesCompat.getFont(it, R.font.roboto_medium) }

        adapterEditingTools = EditingToolsAdapter(this)
        binding.rvConstraintTools.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterEditingTools
        }


        photoEditor = PhotoEditor.Builder(requireContext(), mPhotoEditorView)
            .setPinchTextScalable(true)
            .setDefaultTextTypeface(mTextRobotoTf)
            .setDefaultEmojiTypeface(mTextRobotoTf)
            .build()

        photoEditor.setOnPhotoEditorListener(this)

        initClickListener()

        mSaveFileHelper = FileSaveHelper(this, contentResolver = activity?.contentResolver)

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.item_toolbar_search).isVisible = false
        menu.findItem(R.id.item_toolbar_edit).isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
    }

    private fun initClickListener() {
        binding.imgUndo.setOnClickListener(this)
        binding.imgRedo.setOnClickListener(this)
        binding.imgClose.setOnClickListener(this)
        binding.imgGallery.setOnClickListener(this)
        binding.imgCamera.setOnClickListener(this)
        binding.imgSave.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgUndo -> photoEditor.undo()
            R.id.imgRedo -> photoEditor.redo()
            R.id.imgClose -> onBackPressed()
            R.id.imgGallery -> gotToGallery()
            R.id.imgCamera -> takePhoto()
            R.id.imgSave -> saveImage()
        }
    }

    private fun gotToGallery() {
        var intent = Intent()
        intent.apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_REQUEST)
    }

    private fun takePhoto() {
        val mContentResolver: ContentResolver? = activity?.contentResolver
        val values = ContentValues()
        val idMapeo = sharedViewModel.et_item_mapeo_number.value.toString()
        val idProject = sharedViewModel.idProject
        val fileName = "Project:" + idProject + "_Mapeo:" + idMapeo

        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.Images.Media.DESCRIPTION, "EditPhoto description")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)

        imageUri = mContentResolver!!.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, CAMERA_REQUEST)
    }



    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE),
                1
            )
            return
        } else {
            //permission already granted
            takePhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    photoEditor.clearAllViews()
                    val uri: Uri? = data?.data
                    val thumbnail =
                        MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)

                    mPhotoEditorView.source.setImageBitmap(thumbnail)
                }
                PICK_REQUEST -> {
                    try {
                        photoEditor.clearAllViews()
                        val uri: Uri? = data?.data
                        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            uri
                        )
                        mPhotoEditorView.source.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

        }

    }

    private fun onBackPressed() {
        if (!photoEditor.isCacheEmpty) {
            showSaveDialog()
        } else {
            requireActivity().onBackPressed()
        }
    }

    private fun showSaveDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.msg_save_image))
        builder.setPositiveButton(
            "Grabar"
        ) { dialog, which -> saveImage() }
        builder.setNegativeButton(
            "Cancelar"
        ) { dialog, which -> dialog.dismiss() }
        builder.setNeutralButton(
            "Descartar"
        ) { dialog, which -> findNavController().navigate(R.id.action_photoEdit_to_mapeoItemFragment) }
        builder.create().show()
    }

    private fun saveImage() {
        val idMapeo = sharedViewModel.idMapeo
        val idProject = sharedViewModel.idProject
        val fileName = "Project:" + idProject + "_Mapeo:" + idMapeo
        val hasStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (hasStoragePermission || isSdkHigherThan28()) {

            mSaveFileHelper.createFile(fileName, object : FileSaveHelper.OnFileCreateResult {
                @SuppressLint("MissingPermission")
                override fun onFileCreateResult(
                    created: Boolean,
                    filePath: String?,
                    error: String?,
                    uri: Uri?
                ) {
                    if (created) {
                        val saveSettings = SaveSettings.Builder()
                            .setClearViewsEnabled(true)
                            .setTransparencyEnabled(true)
                            .build()
                        if (filePath != null) {
                            photoEditor.saveAsFile(filePath, saveSettings, object : OnSaveListener {
                                override fun onSuccess(imagePath: String) {
                                    activity?.contentResolver?.let {
                                        mSaveFileHelper.notifyThatFileIsNowPubliclyAvailable(
                                            it
                                        )
                                    }
                                    showSnackbar("Image Saved Successfully")
                                    mSaveImageUri = uri
                                    mPhotoEditorView.source.setImageURI(mSaveImageUri)
                                }

                                override fun onFailure(exception: Exception) {
                                    showSnackbar("Failed to save Image")
                                }
                            })
                        }
                    } else {
                        if (error != null) {
                            showSnackbar(error)
                        }
                    }
                }
            })
        } else {
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)
        }

    }


    private fun isSdkHigherThan28(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    override fun onToolSelected(toolType: ToolType?) {
        when (toolType) {
            ToolType.BRUSH -> {
                photoEditor.setBrushDrawingMode(true)
                photoEditor.brushColor = resources.getColor(R.color.design_default_color_primary)
                binding.txtCurrentTool.text = "BRUSH"
            }
            ToolType.TEXT -> {
                val textEditorDialogFragment =
                    TextEditorDialogFragment.show(context as AppCompatActivity)
                textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode ->
                    val styleBuilder = TextStyleBuilder()
                    styleBuilder.withTextColor(colorCode)
                    photoEditor.addText(inputText, styleBuilder)
                    binding.txtCurrentTool.text = "TEXT"
                }
            }
            ToolType.ERASER -> {
                photoEditor.brushEraser()
                binding.txtCurrentTool.text = "Eraser Mode"
            }
            ToolType.REDO -> {
                photoEditor.redo()
                binding.txtCurrentTool.text = "Redo"
            }
            ToolType.UNDO -> {
                photoEditor.undo()
                binding.txtCurrentTool.text = "Undo"
            }
            ToolType.GALLERY -> {
                gotToGallery()
                binding.txtCurrentTool.text = "Gallery"
            }
            ToolType.CAMERA -> {
                checkPermission()
                binding.txtCurrentTool.text = "Camera"
            }
            else -> binding.txtCurrentTool.text = "Nothing"
        }
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        val textEditorDialogFragment = TextEditorDialogFragment.show(
            context as AppCompatActivity,
            text!!, colorCode
        )
        textEditorDialogFragment.setOnTextEditorListener(OnTextEditorListener { inputText, colorCode ->
            val styleBuilder = TextStyleBuilder()
            styleBuilder.withTextColor(colorCode)
            if (rootView != null) {
                photoEditor.editText(rootView, inputText, styleBuilder)
            }
            binding.txtCurrentTool.text = "TEXT"
        })
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(
            TAG,
            "onAddViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(
            TAG,
            "onRemoveViewListener() called with: viewType: [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStartVireChangeListener() called with: viewType: [$viewType]")
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType: [$viewType]")
    }

    private fun showSnackbar(message: String) {
        val view: View? = activity?.findViewById(android.R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


}