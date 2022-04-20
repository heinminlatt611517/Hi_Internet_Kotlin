package com.hti.hiinternet.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hti.hiinternet.R
import kotlinx.android.synthetic.main.fragment_image.*


private const val ARG_IMAGE_PATH = "IMAGE_PATH"


class ImageFragment : Fragment() {

    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getString(ARG_IMAGE_PATH)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(imagePath)
            .into(ivPromotoinImage)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_PATH, param1)

                }
            }
    }
}