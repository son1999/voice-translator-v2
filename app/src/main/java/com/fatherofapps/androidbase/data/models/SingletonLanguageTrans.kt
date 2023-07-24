package com.example.ap_translator.models

import android.content.Context
import com.fatherofapps.androidbase.R
import com.google.mlkit.nl.translate.Translator

public class SingletonLanguageTrans private constructor() {
    init {
        // define in constructor
// Create an English-German translator:
    }

    var languageType = ""
    var translator: Translator? = null

    var langTransFrom: LanguageTrans = LanguageTrans(name = "English", code = "en", avatar = R.drawable.color_unitedkingdom)
    var langTransFromPositionSeleced = 6
    var langTransTo: LanguageTrans = LanguageTrans(name = "French", code = "fr", avatar = R.drawable.color_france)
    var langTransToPositionSeleced = 0

    private object Holder { val INSTANCE = SingletonLanguageTrans() }

    companion object {
        @JvmStatic
        fun getInstance(): SingletonLanguageTrans{
            return Holder.INSTANCE
        }
    }
}

public class DbConstants {
    companion object {
        val APP_NAME = "Camera Translator"

        fun getAppLable(context: Context): String {
            return  context.getString(R.string.app_name)
        }

    }
}