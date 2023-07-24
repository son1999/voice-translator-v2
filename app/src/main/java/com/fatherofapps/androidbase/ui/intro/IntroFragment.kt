package com.fatherofapps.androidbase.ui.intro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentIntroBinding
import com.fatherofapps.androidbase.databinding.FragmentMultiLangBinding
import com.fatherofapps.androidbase.ui.adapters.CarouselAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : BaseFragment() {
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var dataBinding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentIntroBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner

        val carouselItems = listOf(
            R.drawable.intro1,
            R.drawable.intro2,
            R.drawable.intro3,
            R.drawable.intro4
            // Add more image resources or URLs here
        )

        carouselAdapter = CarouselAdapter(carouselItems)
        var dotsIndicator = dataBinding.dotsIndicator
        var viewPager = dataBinding.viewPager
        var btnStart = dataBinding.buttonStartIntro

        viewPager.adapter = carouselAdapter
        dotsIndicator.setViewPager2(viewPager)

        btnStart.setOnClickListener {
            navigateToPage(R.id.permissionFragment)
        }

        return dataBinding.root
    }
}