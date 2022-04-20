package com.hti.hiinternet.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hti.hiinternet.R
import com.hti.hiinternet.base.BaseFrgment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ApplicationGuideFragment : BaseFrgment() {

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        fun newIntance() = ApplicationGuideFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_guide, container, false)
    }

    override fun showLoadingView() {

    }

    override fun showNormalView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}