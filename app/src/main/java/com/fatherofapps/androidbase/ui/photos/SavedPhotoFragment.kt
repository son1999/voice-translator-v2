package com.fatherofapps.androidbase.ui.photos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ap_translator.models.DbConstants
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentSavedPhotoBinding
import com.fatherofapps.androidbase.ui.adapters.SavedPhotoAdapter
import com.tsuryo.swipeablerv.SwipeLeftRightCallback.Listener
import java.io.File


class SavedPhotoFragment : BaseFragment() {

    private lateinit var binding: FragmentSavedPhotoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSavedPhotoBinding.inflate(layoutInflater)
        val layout = LinearLayoutManager (
                context,
                LinearLayoutManager.VERTICAL,
                false
        )
        binding.recycleView.layoutManager = layout
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        bindRecycleView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT < 30) {
            if (!checkBefore30()) {
                requestBefore30();
            } else {
                // User granted file permission, Access your file
                bindRecycleView()
            }
        } else if (Build.VERSION.SDK_INT >= 30) {
            check30AndAfter();
        } else {
            // User already has file access permission
            bindRecycleView()
        }

    }

    private fun bindRecycleView() {
        val listFile = loadData().toMutableList()
        binding.apply {
            recycleView.adapter = SavedPhotoAdapter(listFile, requireContext()) { index, file ->
                val bundle = Bundle()
                bundle.putString("file", file.absolutePath)
                findNavController().navigate(R.id.photoViewFragment, bundle)
            }

            recycleView.setListener(object: Listener {
                override fun onSwipedLeft(position: Int) {
                    val item = listFile[position]
                    item.delete()
                    listFile.removeAt(position)
                    (recycleView.adapter as? SavedPhotoAdapter)?.update(listFile)
                }

                override fun onSwipedRight(position: Int) {

                }

            })

        }

    }

    private fun loadData() : List<File> {
        val YOUR_FOLDER_NAME = DbConstants.getAppLable(requireContext())
        val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), YOUR_FOLDER_NAME
        )
        // Create a storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(YOUR_FOLDER_NAME, "Failed to create directory")
            }
        }
        val files = mediaStorageDir.listFiles()
        files.sortByDescending {
            it.name
        }
        return files?.asList() ?: listOf()

    }

    private fun checkBefore30(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestBefore30() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), "Storage permission required. Please allow this permission", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission for storage access successful!
                // Read your files now
                bindRecycleView()
            }
            else {
                // Allow permission for storage access!
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun check30AndAfter() {
        if (!Environment.isExternalStorageManager()) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", requireContext().packageName))
                startActivityForResult(intent, 200)
            }
            catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 200)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (30 >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Permission for storage access successful!
                    // Read your files now
                }
                else {
                    // Allow permission for storage access!
                }
            }
        }
    }


}