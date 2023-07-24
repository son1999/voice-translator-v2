package com.fatherofapps.androidbase.ui.multi_lang

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentMultiLangBinding
import com.fatherofapps.androidbase.ui.adapters.ItemMultiLangAdapter
import com.fatherofapps.androidbase.ui.dialog_rating.DialogRatingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultiLangFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentMultiLangBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentMultiLangBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner


        //show rating dialog
        DialogRatingFragment().show(childFragmentManager, "show rating fragment")

        //navigation
        dataBinding.btnChooseLang.setOnClickListener {

            navigateToPage(R.id.introFragment)
        }

        //ui
        dataBinding.tvTitile.setTypeface(Typeface.DEFAULT_BOLD)

        //bind rcv langs list
        val rcvLangs = dataBinding.rcvLangs
        rcvLangs.adapter = ItemMultiLangAdapter(
            ItemMultiLangAdapter.dummyData,
            requireContext(),
            0,
        ){ position, item ->

        }
        rcvLangs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return dataBinding.root


    }
}