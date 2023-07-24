package com.fatherofapps.androidbase.ui.dialog_rating

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.databinding.FragmentDialogRatingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogRatingFragment : DialogFragment() {

    private lateinit var dataBinding: FragmentDialogRatingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_round_corner);
        dataBinding = FragmentDialogRatingBinding.inflate(inflater)

        val imgReact = dataBinding.imgReaction
        val tvTitle = dataBinding.tvTitle

        val btn1Star = dataBinding.btn1star
        val btn2Star = dataBinding.btn2star
        val btn3Star = dataBinding.btn3star
        val btn4Star = dataBinding.btn4star
        val btn5Star = dataBinding.btn5star

        val btnRate = dataBinding.btnRate
        val btnExit = dataBinding.btnExit

        btn1Star.setImageResource(R.drawable.star_not_rate)
        btn2Star.setImageResource(R.drawable.star_not_rate)
        btn3Star.setImageResource(R.drawable.star_not_rate)
        btn4Star.setImageResource(R.drawable.star_not_rate)
        btn5Star.setImageResource(R.drawable.star_not_rate)

        imgReact.setImageResource(R.drawable.react_start)
        tvTitle.text = "Do you like the app?"
        tvTitle.setTypeface(tvTitle.typeface, Typeface.BOLD)

        btnRate.setOnClickListener {
            dismiss()
        }

        btnExit.setOnClickListener {
            dismiss()
        }

        btn1Star.setOnClickListener {
            btn1Star.setImageResource(R.drawable.star_rated)
            btn2Star.setImageResource(R.drawable.star_not_rate)
            btn3Star.setImageResource(R.drawable.star_not_rate)
            btn4Star.setImageResource(R.drawable.star_not_rate)
            btn5Star.setImageResource(R.drawable.star_not_rate)
            imgReact.setImageResource(R.drawable.react_1star)
            tvTitle.text = "Oh, no!"
        }
        btn2Star.setOnClickListener {
            btn1Star.setImageResource(R.drawable.star_rated)
            btn2Star.setImageResource(R.drawable.star_rated)
            btn3Star.setImageResource(R.drawable.star_not_rate)
            btn4Star.setImageResource(R.drawable.star_not_rate)
            btn5Star.setImageResource(R.drawable.star_not_rate)
            imgReact.setImageResource(R.drawable.react_2star)
            tvTitle.text = "Oh, no!"
        }
        btn3Star.setOnClickListener {
            btn1Star.setImageResource(R.drawable.star_rated)
            btn2Star.setImageResource(R.drawable.star_rated)
            btn3Star.setImageResource(R.drawable.star_rated)
            btn4Star.setImageResource(R.drawable.star_not_rate)
            btn5Star.setImageResource(R.drawable.star_not_rate)
            imgReact.setImageResource(R.drawable.react_3star)
            tvTitle.text = "Oh, no!"
        }
        btn4Star.setOnClickListener {
            btn1Star.setImageResource(R.drawable.star_rated)
            btn2Star.setImageResource(R.drawable.star_rated)
            btn3Star.setImageResource(R.drawable.star_rated)
            btn4Star.setImageResource(R.drawable.star_rated)
            btn5Star.setImageResource(R.drawable.star_not_rate)
            imgReact.setImageResource(R.drawable.react_4star)
            tvTitle.text = "We love you too!"
        }
        btn5Star.setOnClickListener {
            btn1Star.setImageResource(R.drawable.star_rated)
            btn2Star.setImageResource(R.drawable.star_rated)
            btn3Star.setImageResource(R.drawable.star_rated)
            btn4Star.setImageResource(R.drawable.star_rated)
            btn5Star.setImageResource(R.drawable.star_rated)
            imgReact.setImageResource(R.drawable.react_5star)
            tvTitle.text = "We love you too!"
        }

        return dataBinding.root
    }



    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}