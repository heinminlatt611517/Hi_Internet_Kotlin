package com.hti.hiinternet.util.ui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridLayoutManager

    : GridLayoutManager {


    @JvmOverloads
    constructor(
        context: Context?,
        spacount: Int
    ) : super(context, spacount)

    private val horizontalSpace get() = width - paddingStart - paddingEnd
    private val verticalSpace get() = height - paddingTop - paddingBottom
    private val ratio = 0.86f // change to 0.7f for 70%
    private fun scaledLayoutParams(layoutParams: RecyclerView.LayoutParams) =
        layoutParams.apply {
            when (orientation) {
                LinearLayoutManager.HORIZONTAL -> width = (horizontalSpace * ratio).toInt()
                LinearLayoutManager.VERTICAL -> height = (verticalSpace * ratio).toInt()
            }
        }


}