package com.fatherofapps.androidbase.ui.home

import android.graphics.Bitmap
import com.fatherofapps.androidbase.base.viewmodel.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel() {

    var showTranslatedLayout = false
    var mSelectedImage: Bitmap? = null

    var mImageMaxWidth: Int = 0

    // Max height (portrait mode)
    var mImageMaxHeight: Int = 0

    /**
     * Number of results to show in the UI.
     */
    val RESULTS_TO_SHOW = 3

    var currentFrom = ""
    var currentTo = ""

    var isFristTime = true

    override fun fetchData() {
        super.fetchData()
    }

}