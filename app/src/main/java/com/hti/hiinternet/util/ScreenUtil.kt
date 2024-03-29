package com.hti.hiinternet.util

import android.content.Context
import android.util.DisplayMetrics

object ScreenUtil {
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.getResources()
            .getDisplayMetrics().densityDpi  / DisplayMetrics.DENSITY_DEFAULT).toFloat()
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.getResources()

            .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
    }

}