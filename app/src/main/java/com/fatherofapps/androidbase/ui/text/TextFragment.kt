package com.fatherofapps.androidbase.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.ap_translator.models.DbConstants
import com.example.ap_translator.models.SingletonLanguageTrans
import com.example.speaktotext.models.SpinnerItem
import com.example.speaktotext.models.readDataFromJsonFile
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.adapters.CustomSpinnerAdapter
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentHomeBinding
import com.fatherofapps.androidbase.databinding.FragmentIntroBinding
import com.fatherofapps.androidbase.databinding.FragmentTextBinding
import com.fatherofapps.androidbase.ui.adapters.CarouselAdapter
import com.fatherofapps.androidbase.ui.adapters.ItemLanguageTransAdapter
import com.fatherofapps.androidbase.vision.GraphicOverlay
import com.fatherofapps.androidbase.vision.TextGraphic
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date
import java.util.Locale
import java.util.Objects

@AndroidEntryPoint
class TextFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentTextBinding

    private var fromLang: String = ""
    private var toLang: String = ""
    private var dto:String = ""
    private var spinnerItems: List<SpinnerItem> = emptyList()
    private lateinit var spinnerFromLang: Spinner
    private lateinit var spinnerToLang: Spinner
    private lateinit var idTVInput: EditText
    private lateinit var idTVOutput: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentTextBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner

        return dataBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idTVInput = view.findViewById<EditText>(R.id.idTVInput)
        idTVOutput = view.findViewById<EditText>(R.id.idTVOutput)
        var btnClearEdt = view.findViewById<ImageView>(R.id.btnClearEdt)
        spinnerFromLang = view.findViewById<Spinner>(R.id.formLang)
        spinnerToLang = view.findViewById<Spinner>(R.id.toLang)
        var tapToTranButton = view.findViewById<Button>(R.id.tapToTranButton)
        var copyButton = view.findViewById<ImageView>(R.id.copyButton)
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var changeButtonLang = view.findViewById<ImageView>(R.id.changeButtonLang)

        spinnerItems = readDataFromJsonFile(requireContext())
        // Gán adapter cho Spinner
        val adapter = CustomSpinnerAdapter(requireContext(), spinnerItems)
        spinnerFromLang.adapter = adapter
        spinnerToLang.adapter = adapter

        spinnerFromLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Xử lý sự kiện khi một mục được chọn
                val selectedSpinnerItem = spinnerItems[position]
                fromLang = selectedSpinnerItem.code
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        // click icon x clean text render voice
        btnClearEdt.setOnClickListener{
            idTVInput.setText("")
            idTVOutput.setText("")
        }

        changeButtonLang.setOnClickListener {
            switchLanguages()
        }

        // click icon copy handle
        copyButton.setOnClickListener() {
            var textCopy = idTVInput.getText().toString().trim()

            if (textCopy.isNotEmpty()) {
                // Create a new ClipData with the text
                val clipData = ClipData.newPlainText("text", textCopy)

                // Set the ClipData to the clipboard
                clipboardManager.setPrimaryClip(clipData)

                // Show a message or perform any other actions after copying
                showToast("Text copied to clipboard!")
            } else {
                showToast("Nothing to copy.")
            }
        }

        spinnerToLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Xử lý sự kiện khi một mục được chọn
                val selectedSpinnerItem = spinnerItems[position]
                toLang = selectedSpinnerItem.code
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        tapToTranButton.setOnClickListener {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(fromLang.toString())
                .setTargetLanguage(toLang.toString())
                .build()
            val fromToLangTranslator = Translation.getClient(options)

            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            fromToLangTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    fromToLangTranslator.translate(idTVInput.text.toString())
                        .addOnSuccessListener {translatedText ->
                            Log.i("Son", translatedText.toString())
                            idTVOutput.setText(translatedText.toString())
                            hideKeyboard(tapToTranButton)
                        }
                }
                .addOnFailureListener {exception ->
                    Log.i("Viet", exception.toString())
                }

        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun switchLanguages() {
        // Swap the "fromLang" and "toLang" variables
        val tempLang = fromLang
        fromLang = toLang
        toLang = tempLang

        // Update the selected items in the spinners
        val fromLangPosition = spinnerItems.indexOfFirst { it.code == fromLang }
        val toLangPosition = spinnerItems.indexOfFirst { it.code == toLang }
        spinnerFromLang.setSelection(fromLangPosition)
        spinnerToLang.setSelection(toLangPosition)

        // Perform translation with the new languages
        performTranslation()
    }

    private fun performTranslation() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(fromLang)
            .setTargetLanguage(toLang)
            .build()
        val fromToLangTranslator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        fromToLangTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                fromToLangTranslator.translate(idTVInput.text.toString())
                    .addOnSuccessListener { translatedText ->
                        Log.i("Son", translatedText.toString())
                        idTVOutput.setText(translatedText.toString())
                    }
            }
            .addOnFailureListener { exception ->
                Log.i("Viet", exception.toString())
            }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}