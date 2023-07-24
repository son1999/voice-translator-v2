package com.fatherofapps.androidbase.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ap_translator.models.SingletonLanguageTrans
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentLanguageTransBinding
import com.fatherofapps.androidbase.ui.adapters.ItemLanguageTransAdapter

class LanguageTransFragment: BaseFragment() {

    private lateinit var binding: FragmentLanguageTransBinding
    private lateinit var btnCancel: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
    binding = FragmentLanguageTransBinding.inflate(layoutInflater)

        bindLanguageToTransRcv()

        val btnCancel = binding.langToTransBtnBack

        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    fun bindLanguageToTransRcv(){
        var rcvReading: RecyclerView? = null

        val typeLang = SingletonLanguageTrans.getInstance().languageType

        rcvReading = binding.langToTransRcvLang
        rcvReading.adapter = ItemLanguageTransAdapter(
                ItemLanguageTransAdapter.dummyData,
                requireContext(),
                if (typeLang == "from") {
                SingletonLanguageTrans.getInstance().langTransFromPositionSeleced
            } else {
                SingletonLanguageTrans.getInstance().langTransToPositionSeleced
            },
        ) { position, item ->
            //call back
            if (typeLang == "from") {
                SingletonLanguageTrans.getInstance().langTransFrom = item
                SingletonLanguageTrans.getInstance().langTransFromPositionSeleced = position
            } else {
                SingletonLanguageTrans.getInstance().langTransTo = item
                SingletonLanguageTrans.getInstance().langTransToPositionSeleced = position
            }

            findNavController().popBackStack()
        }

        val layout = LinearLayoutManager (
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        rcvReading.layoutManager = layout
    }

}