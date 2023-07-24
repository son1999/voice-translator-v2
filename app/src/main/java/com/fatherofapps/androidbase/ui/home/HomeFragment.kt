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
class HomeFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentHomeBinding

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var codeFlag: String
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var outputTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentHomeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner

        return dataBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputTV = view.findViewById<TextView>(R.id.idTVOutput)
        var micIV = view.findViewById<ImageView>(R.id.idIVMic)
        var btnClearEdt = view.findViewById<ImageView>(R.id.btnClearEdt)
        var spinner = view.findViewById<Spinner>(R.id.idChooseLang)
        var buttonTaptoSpeak = view.findViewById<Button>(R.id.tapToSpeakButton)
        var textViewHelp = view.findViewById<TextView>(R.id.textViewHelp)
        var copyButton = view.findViewById<ImageView>(R.id.copyButton)
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        textViewHelp.setTextColor(getResources().getColor(R.color.text_base));

        var spinnerItems: List<SpinnerItem>

        // Đọc dữ liệu từ tập tin JSON và gán cho spinnerItems
        spinnerItems = readDataFromJsonFile(requireContext())
        // Gán adapter cho Spinner
        val adapter = CustomSpinnerAdapter(requireContext(), spinnerItems)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Xử lý sự kiện khi một mục được chọn
                val selectedSpinnerItem = spinnerItems[position]
                codeFlag = selectedSpinnerItem.code
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // click icon x clean text render voice
        btnClearEdt.setOnClickListener() {
            outputTV.text = "";
        }

        // click icon copy handle
        copyButton.setOnClickListener() {
            var textCopy = outputTV.getText().toString().trim()

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

        // listener for mic image view.
        micIV.setOnClickListener {
            // on below line we are calling speech recognizer intent.
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on below line we are passing language model
            // and model free form in our intent
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            // on below line we are passing our
            // language as a default language.
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
//                Locale.getDefault()
                codeFlag.toString()
            )

            // on below line we are specifying a prompt
            // message as speak to text on below line.
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            // on below line we are specifying a try catch block.
            // in this block we are calling a start activity
            // for result method and passing our result code.
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // todo
            }
        }

        buttonTaptoSpeak.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
//                Locale.getDefault()
                codeFlag.toString()
            )
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // todo
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == Activity.RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                outputTV.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
        }
    }
}