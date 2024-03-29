package com.hti.hiinternet.util.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PeekRatioLayoutManager : LinearLayoutManager {

    @JvmOverloads
    constructor(
        context: Context?,
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
        reverseLayout: Boolean = false
    ) : super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun generateDefaultLayoutParams() =
        scaledLayoutParams(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?) =
        scaledLayoutParams(super.generateLayoutParams(lp))

    override fun generateLayoutParams(c: Context?, attrs: AttributeSet?) =
        scaledLayoutParams(super.generateLayoutParams(c, attrs))

    private val horizontalSpace get() = width - paddingStart - paddingEnd
    private val verticalSpace get() = height - paddingTop - paddingBottom
    private val ratio = 0.86f // change to 0.7f for 70%
    private fun scaledLayoutParams(layoutParams: RecyclerView.LayoutParams) =
        layoutParams.apply {
            when (orientation) {
                HORIZONTAL -> width = (horizontalSpace * ratio).toInt()
                VERTICAL -> height = (verticalSpace * ratio).toInt()
            }
        }

}