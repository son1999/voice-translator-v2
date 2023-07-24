package com.fatherofapps.androidbase.ui.photos

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentPhotoViewBinding

class PhotoViewFragment : BaseFragment() {

    private lateinit var binding: FragmentPhotoViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPhotoViewBinding.inflate(layoutInflater)
        bindPhoto()
        return binding.root
    }

    private fun bindPhoto() {
        val file = arguments?.getString("file")
        val bitmap = BitmapFactory.decodeFile(file)
        binding.photoView.setImageBitmap(bitmap)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}