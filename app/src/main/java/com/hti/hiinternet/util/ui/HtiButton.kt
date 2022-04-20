package com.hti.hiinternet.util.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class HtiButton : MaterialButton
{
    constructor(context: Context?) : super(context!!){
        setTypeFace(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs){
        setTypeFace(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ){
        setTypeFace(context)
    }
    fun setTypeFace(context: Context?) {
       val typeface=Typeface.createFromAsset(context?.assets,"fonts/roboto_light.ttf")
        setTypeface(typeface)
    }
}
